package shoppingcart.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.service.ShoppingCartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping("/carts/stripe")
public class StripePayController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Value("${stripe.endpoint.secret}")
    private String endpointSecret;

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
        final String shoppingCartUid = paymentIntent.getMetadata().get("shopping_cart_uid");

        // Handle the event
        if (event.getType().equalsIgnoreCase("payment_intent.succeeded")) {
            Optional<ShoppingCart> shoppingCartOptional = shoppingCartService.getShoppingCartByUid(UUID.fromString(shoppingCartUid));
            shoppingCartOptional.ifPresent(shoppingCart -> shoppingCartService.deactivateShoppingCart(shoppingCart));
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
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
