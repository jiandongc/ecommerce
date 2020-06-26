package email.data;


import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class OrderConfirmationDataTest {

    @Test
    public void shouldGenerateOrderConfirmationEmailForGuest() throws IOException {
        // Given
        OrderConfirmationData orderConfirmationData = OrderConfirmationData.builder()
                .sendTo(Arrays.asList("abc@gmail.com"))
                .customerName("John")
                .orderNumber("0159-318963-2182")
                .orderEta("21st Jun, 2019")
                .orderDeliveryMethod("Express")
                .shippingAddress(OrderConfirmationData.AddressData.builder().name("John").build())
                .guest(true)
                .siteName("Noodle Monster")
                .homePage("http://cawaii.co.uk/#!/home")
                .registrationPage("http://cawaii.co.uk/#!/register/")
                .build();

        // When
        String text = orderConfirmationData.generateText();
System.out.println(text);
        // Then
        assertThat(text, containsString("Would you like to create an account with us to track your order on Noodle Monster? <a href=\"http://cawaii.co.uk/#!/register/0159-318963-2182\">Click here to create account</a>"));
    }

    @Test
    public void shouldGenerateOrderConfirmationEmailForRegisteredCustomer() throws IOException {
        // Given
        OrderConfirmationData orderConfirmationData = OrderConfirmationData.builder()
                .sendTo(Arrays.asList("abc@gmail.com"))
                .customerName("John")
                .orderNumber("0159-318963-2182")
                .orderEta("21st Jun, 2019")
                .orderDeliveryMethod("Express")
                .guest(false)
                .siteName("Noodle Monster")
                .homePage("http://cawaii.co.uk/#!/home")
                .registrationPage("http://cawaii.co.uk/#!/register/")
                .build();

        // When
        String text = orderConfirmationData.generateText();

        // Then
        assertThat(text, containsString("You can view the status of your order by visiting Your Orders on <a href=\"http://cawaii.co.uk/#!/home\">Noodle Monster</a>"));
    }

}