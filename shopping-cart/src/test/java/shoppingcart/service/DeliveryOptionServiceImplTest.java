package shoppingcart.service;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Mockito;
import shoppingcart.domain.DeliveryOptionOffer;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.repository.DeliveryOptionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

public class DeliveryOptionServiceImplTest {

    private DeliveryOptionRepository deliveryOptionRepository = Mockito.mock(DeliveryOptionRepository.class);
    private ShoppingCartService shoppingCartService = Mockito.mock(ShoppingCartService.class);
    private DeliveryOptionService deliveryOptionService = new DeliveryOptionServiceImpl(deliveryOptionRepository, shoppingCartService);

    @Test
    public void shouldReturnBothDeliveryOptionIfItemSubtotalEqualsToMinSpend(){
        // Given
        UUID cartUid = UUID.randomUUID();
        ShoppingCart shoppingCart = Mockito.mock(ShoppingCart.class);
        Mockito.when(shoppingCart.getItemSubTotal()).thenReturn(BigDecimal.valueOf(39.99D));
        Mockito.when(shoppingCartService.getShoppingCartByUid(cartUid)).thenReturn(Optional.of(shoppingCart));
        DeliveryOptionOffer standardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .build();
        DeliveryOptionOffer freeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .build();
        Mockito.when(deliveryOptionRepository.findDeliveryOptionOffersByCountryCode("UK", LocalDate.now())).thenReturn(Arrays.asList(standardDelivery, freeDelivery));

        // When
        List<DeliveryOptionOffer> actual = deliveryOptionService.getDeliveryOptionOffers(cartUid, "UK");

        // Then
        assertThat(actual.size(), CoreMatchers.is(2));
    }

    @Test
    public void shouldReturnBothDeliveryOptionIfItemSubtotalIsGreaterThanMinSpend(){
        // Given
        UUID cartUid = UUID.randomUUID();
        ShoppingCart shoppingCart = Mockito.mock(ShoppingCart.class);
        Mockito.when(shoppingCart.getItemSubTotal()).thenReturn(BigDecimal.valueOf(40.00D));
        Mockito.when(shoppingCartService.getShoppingCartByUid(cartUid)).thenReturn(Optional.of(shoppingCart));
        DeliveryOptionOffer standardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .build();
        DeliveryOptionOffer freeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .build();
        Mockito.when(deliveryOptionRepository.findDeliveryOptionOffersByCountryCode("UK", LocalDate.now())).thenReturn(Arrays.asList(standardDelivery, freeDelivery));

        // When
        List<DeliveryOptionOffer> actual = deliveryOptionService.getDeliveryOptionOffers(cartUid, "UK");

        // Then
        assertThat(actual.size(), CoreMatchers.is(2));
    }

    @Test
    public void shouldOnlyReturnStandardDeliveryOptionIfItemSubtotalIsGreaterThanMinSpend(){
        // Given
        UUID cartUid = UUID.randomUUID();
        ShoppingCart shoppingCart = Mockito.mock(ShoppingCart.class);
        Mockito.when(shoppingCart.getItemSubTotal()).thenReturn(BigDecimal.valueOf(39.98D));
        Mockito.when(shoppingCartService.getShoppingCartByUid(cartUid)).thenReturn(Optional.of(shoppingCart));
        DeliveryOptionOffer standardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .build();
        DeliveryOptionOffer freeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .build();
        Mockito.when(deliveryOptionRepository.findDeliveryOptionOffersByCountryCode("UK", LocalDate.now())).thenReturn(Arrays.asList(standardDelivery, freeDelivery));

        // When
        List<DeliveryOptionOffer> actual = deliveryOptionService.getDeliveryOptionOffers(cartUid, "UK");

        // Then
        assertThat(actual.size(), CoreMatchers.is(1));
        assertThat(actual.get(0).getMethod(), CoreMatchers.is("Standard Delivery"));
    }

}