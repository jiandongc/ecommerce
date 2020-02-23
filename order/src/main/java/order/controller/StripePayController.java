package order.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import order.domain.Order;
import order.domain.OrderAddress;
import order.domain.OrderStatus;
import order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping("/orders/stripe")
public class StripePayController {

    @Autowired
    private OrderService orderService;

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

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                orderService.addOrderStatus(orderNumber,
                        OrderStatus.builder()
                                .status("PAYMENT SUCCEEDED")
                                .description("Amount received: " + String.valueOf(paymentIntent.getAmountReceived()))
                                .build()
                );

                break;
            case "payment_intent.payment_failed":
                orderService.addOrderStatus(orderNumber,
                        OrderStatus.builder()
                                .status("PAYMENT FAILED")
                                .description(format("Amount failed to capture: %s. \"%s\"", paymentIntent.getAmount(),
                                        paymentIntent.getLastPaymentError() != null ? paymentIntent.getLastPaymentError().getMessage(): ""))
                                .build()
                );

                break;
            default:
                // Unexpected event type
                response.setStatus(400);
                return;
        }

        response.setStatus(200);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = GET, value = "/{orderNumber}/{shoppingCartUid}", produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> downloadClientSecret(@PathVariable String orderNumber, @PathVariable String shoppingCartUid) throws Exception {
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
                .putMetadata("shopping_cart_uid", shoppingCartUid)
                .putMetadata("order_number", orderNumber);

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
