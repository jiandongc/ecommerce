package order.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import email.data.OrderConfirmationData;
import email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import order.data.StripeMetaData;
import order.domain.Order;
import order.domain.OrderAddress;
import order.domain.OrderStatus;
import order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping("/orders/stripe")
public class StripePayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Value("${stripe.endpoint.secret}")
    private String endpointSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    @RequestMapping(method = POST, value = "/webhook/payment_intents")
    public void processPaymentEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final Optional<Event> eventOptional = constructEvent(request);

        if (!eventOptional.isPresent()) {
            response.setStatus(400);
            return;
        }

        final Event event = eventOptional.get();
        final Optional<StripeObject> stripeObjectOptional = constructStripeObject(eventOptional.get());

        if (!stripeObjectOptional.isPresent()) {
            response.setStatus(400);
            return;
        }

        final StripeObject stripeObject = stripeObjectOptional.get();
        final PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
        final String orderNumber = paymentIntent.getMetadata().get("order_number");
        final String name = paymentIntent.getMetadata().get("name");
        final String siteName = paymentIntent.getMetadata().get("siteName");
        final String registrationPage = paymentIntent.getMetadata().get("registrationPage");
        final String homePage = paymentIntent.getMetadata().get("homePage");

        // Handle the event
        if (event.getType().equalsIgnoreCase("payment_intent.succeeded")) {
            Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
            if (orderOptional.isPresent()) {
                orderService.addOrderStatus(orderNumber,
                        OrderStatus.builder()
                                .status("PAYMENT SUCCEEDED")
                                .description("Amount received: " + String.valueOf(paymentIntent.getAmountReceived()))
                                .build()
                );

                Order order = orderOptional.get();
                OrderAddress shippingAddress = order.getShippingAddress().get();
                List<OrderConfirmationData.OrderItemData> orderItems = order.getOrderItems().stream()
                        .map(item -> OrderConfirmationData.OrderItemData.builder()
                                .code(item.getCode())
                                .description(item.getDescription())
                                .imageUrl(item.getImageUrl())
                                .name(item.getName())
                                .quantity(String.valueOf(item.getQuantity()))
                                .price(item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString())
                                .subTotal(item.getSubTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString())
                                .sku(item.getSku())
                                .build())
                        .collect(Collectors.toList());
                OrderConfirmationData orderConfirmationData = OrderConfirmationData.builder()
                        .sendTo(Arrays.asList(order.getEmail()))
                        .customerName(name)
                        .orderNumber(order.getOrderNumber())
                        .orderEta(order.getEta())
                        .orderDeliveryMethod(order.getDeliveryMethod())
                        .orderItems(orderItems)
                        .shippingAddress(
                                OrderConfirmationData.AddressData.builder()
                                        .title(shippingAddress.getTitle())
                                        .name(shippingAddress.getName())
                                        .addressLine1(shippingAddress.getAddressLine1())
                                        .addressLine2(shippingAddress.getAddressLine2())
                                        .city(shippingAddress.getCity())
                                        .country(shippingAddress.getCountry())
                                        .build()
                        )
                        .guest(order.getCustomerUid() == null)
                        .siteName(siteName)
                        .homePage(homePage)
                        .registrationPage(registrationPage)
                        .build();

                emailService.sendMessage(orderConfirmationData);
            } else {
                response.setStatus(400);
                return;
            }
        } else if (event.getType().equalsIgnoreCase("payment_intent.payment_failed")) {
            orderService.addOrderStatus(orderNumber,
                    OrderStatus.builder()
                            .status("PAYMENT FAILED")
                            .description(format("Amount failed to capture: %s. \"%s\"", paymentIntent.getAmount(),
                                    paymentIntent.getLastPaymentError() != null ? paymentIntent.getLastPaymentError().getMessage() : ""))
                            .build()
            );
        } else {
            // Unexpected event type
            response.setStatus(400);
            return;
        }

        response.setStatus(200);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = POST, value = "/{orderNumber}", produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> downloadClientSecret(@PathVariable String orderNumber, @RequestBody StripeMetaData stripeMetaData) throws Exception {
        Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
        if (!orderOptional.isPresent()) {
            log.error("Order Number  {} not found", orderNumber);
            return new ResponseEntity<>(NOT_FOUND);
        }

        final Order order = orderOptional.get();

        if (order.getOrderTotal().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Order {} is not chargeable. Order total: {}", order.getOrderNumber(), order.getOrderTotal());
            return new ResponseEntity<>(BAD_REQUEST);
        }

        PaymentIntentCreateParams.Builder paymentIntentCreateParamsBuilder = new PaymentIntentCreateParams.Builder()
                .setCurrency("gbp")
                .setAmount(new Long(order.getOrderTotal().setScale(2, BigDecimal.ROUND_HALF_UP).movePointRight(2).toPlainString()))
                .putMetadata("shopping_cart_uid", stripeMetaData.getShoppingCartId())
                .putMetadata("order_number", orderNumber)
                .putMetadata("name", !"false".equalsIgnoreCase(stripeMetaData.getUserName()) ? stripeMetaData.getUserName() : order.getShippingAddress().get().getName())
                .putMetadata("siteName", stripeMetaData.getSiteName())
                .putMetadata("homePage", stripeMetaData.getHomePage())
                .putMetadata("registrationPage", stripeMetaData.getRegistrationPage());

        final Optional<OrderAddress> shippingAddressOptional = order.getShippingAddress();
        if (shippingAddressOptional.isPresent()) {
            final OrderAddress shippingAddress = shippingAddressOptional.get();
            paymentIntentCreateParamsBuilder.setShipping(
                    new PaymentIntentCreateParams.Shipping.Builder()
                            .setName(shippingAddress.getName())
                            .setPhone(shippingAddress.getMobile())
                            .setAddress(
                                    new PaymentIntentCreateParams.Shipping.Address.Builder()
                                            .setLine1(shippingAddress.getAddressLine1())
                                            .setLine2(shippingAddress.getAddressLine2())
                                            .setCity(shippingAddress.getCity())
                                            .setCountry(shippingAddress.getCountry())
                                            .setPostalCode(shippingAddress.getPostcode())
                                            .build()
                            ).build()
            );
        }

        final PaymentIntent intent = PaymentIntent.create(paymentIntentCreateParamsBuilder.build());
        return new ResponseEntity<>(intent.getClientSecret(), OK);
    }

    private Optional<Event> constructEvent(HttpServletRequest request) throws IOException {
        String payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String sigHeader = request.getHeader("Stripe-Signature");
        try {
            return Optional.of(Webhook.constructEvent(payload, sigHeader, endpointSecret));
        } catch (SignatureVerificationException e) {
            log.error("Stripe signature verification failed");
            return Optional.empty();
        } catch (Exception e) {
            log.error("Exception occurred reading the request", e);
            return Optional.empty();
        }
    }

    private Optional<StripeObject> constructStripeObject(Event event) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (dataObjectDeserializer.getObject().isPresent()) {
            return Optional.of(dataObjectDeserializer.getObject().get());
        } else {
            log.error("Deserialization failed, probably due to an API version mismatch");
            return Optional.empty();
        }
    }

}
