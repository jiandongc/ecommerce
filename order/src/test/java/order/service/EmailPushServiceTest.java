package order.service;

import email.data.GoogleReviewRequestData;
import email.data.OrderConfirmationData;
import email.data.OrderShippedData;
import email.service.EmailService;
import order.domain.Order;
import order.mapper.GoogleReviewRequestDataMapper;
import order.mapper.OrderConfirmationDataMapper;
import order.mapper.OrderShippedDataMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailPushServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private EmailService emailService;

    @Mock
    private OrderConfirmationDataMapper orderConfirmationDataMapper;

    @Mock
    private OrderShippedDataMapper orderShippedDataMapper;

    @Mock
    private GoogleReviewRequestDataMapper googleReviewRequestDataMapper;

    @InjectMocks
    private EmailPushService emailPushService;

    @Test
    public void shouldPushOrderConfirmationEmail(){
        // Given
        OrderConfirmationData orderConfirmationData = OrderConfirmationData.builder().build();
        when(orderService.findByOrderNumber("123-456-789")).thenReturn(Optional.of(new Order()));
        when(orderConfirmationDataMapper.map(any(Order.class), any(String.class), any(String.class), any(String.class), any(String.class))).thenReturn(orderConfirmationData);

        // When
        emailPushService.push("123-456-789", "order-confirmation");

        // Then
        Mockito.verify(emailService).sendMessage(orderConfirmationData);
    }

    @Test
    public void shouldPushOrderShippedEmail(){
        // Given
        OrderShippedData orderShippedData = OrderShippedData.builder().build();
        when(orderService.findByOrderNumber("123-456-789")).thenReturn(Optional.of(new Order()));
        when(orderShippedDataMapper.map(any(Order.class))).thenReturn(orderShippedData);

        // When
        emailPushService.push("123-456-789", "order-shipped");

        // Then
        Mockito.verify(emailService).sendMessage(orderShippedData);
    }

    @Test
    public void shouldPushGoogleReviewRequestEmail(){
        // Given
        GoogleReviewRequestData googleReviewRequestData = GoogleReviewRequestData.builder().build();
        when(orderService.findByOrderNumber("123-456-789")).thenReturn(Optional.of(new Order()));
        when(googleReviewRequestDataMapper.map(any(Order.class), any(String.class))).thenReturn(googleReviewRequestData);

        // When
        emailPushService.push("123-456-789", "google-review-request");

        // Then
        Mockito.verify(emailService).sendMessage(googleReviewRequestData);
    }

    @Test
    public void shouldPushGoogleReviewRequestEmailWithVoucherCode(){
        // Given
        GoogleReviewRequestData googleReviewRequestData = GoogleReviewRequestData.builder().build();
        when(orderService.findByOrderNumber("123-456-789")).thenReturn(Optional.of(new Order()));
        when(googleReviewRequestDataMapper.map(any(Order.class), eq("voucherCode"))).thenReturn(googleReviewRequestData);

        // When
        emailPushService.pushGoogleReviewRequestMailWithVoucherCode("123-456-789", "voucherCode");

        // Then
        Mockito.verify(emailService).sendMessage(googleReviewRequestData);
    }

    @Test
    public void shouldNotPushAnyEmailIfTypeIsUnknown(){
        // Given
        when(orderService.findByOrderNumber("123-456-789")).thenReturn(Optional.of(new Order()));

        // When
        emailPushService.push("123-456-789", "unknown");

        // Then
        Mockito.verifyZeroInteractions(emailService);
    }

    @Test
    public void shouldNotPushAnyEmailIfOrderNotFound(){
        // Given
        when(orderService.findByOrderNumber("123-456-789")).thenReturn(Optional.empty());

        // When
        emailPushService.push("123-456-789", "order-confirmation");

        // Then
        Mockito.verifyZeroInteractions(emailService);
    }

}