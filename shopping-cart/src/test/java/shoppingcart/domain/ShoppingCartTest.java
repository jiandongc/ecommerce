package shoppingcart.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static shoppingcart.domain.Voucher.Type.MONETARY;
import static shoppingcart.domain.Voucher.Type.PERCENTAGE;

public class ShoppingCartTest {

    @Test
    public void shouldCalculateItemsSubTotal() {
        // Given
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(1.6))
                .quantity(3)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCart.addItem(cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(11.9))
                .quantity(2)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCart.addItem(cartItem2);

        // When
        BigDecimal itemSubTotal = shoppingCart.getItemSubTotal();

        // Then
        assertThat(itemSubTotal, is(BigDecimal.valueOf(28.6).setScale(2)));
    }

    @Test
    public void shouldCalculateItemsVat() {
        // Given
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(1.6))
                .quantity(3)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(11.9))
                .quantity(2)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem2);

        // When
        BigDecimal vat = shoppingCart.getItemsVat();

        // Then
        assertThat(vat, is(BigDecimal.valueOf(4.77).setScale(2)));
    }

    @Test
    public void shouldCalculateItemsVatWithMixedRate() {
        // Given
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(1.6))
                .quantity(3)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(11.9))
                .quantity(2)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .vatRate(0)
                .build();
        shoppingCart.addItem(cartItem2);

        // When
        BigDecimal vat = shoppingCart.getItemsVat();

        // Then
        assertThat(vat, is(BigDecimal.valueOf(0.80).setScale(2)));
    }

    @Test
    public void shouldCalculatePostageVat() {
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final DeliveryOption deliveryOption = DeliveryOption.builder().charge(6D).vatRate(20).build();
        shoppingCart.setDeliveryOption(deliveryOption);

        assertThat(shoppingCart.getPostageVat(), is(BigDecimal.valueOf(1D).setScale(2)));
        assertThat(shoppingCart.getPostageBeforeVat(), is(BigDecimal.valueOf(5D).setScale(2)));
        assertThat(shoppingCart.getPostage(), is(BigDecimal.valueOf(6D).setScale(2)));
    }

    @Test
    public void shouldReturnZeroWhenDeliveryOptionIsNotSet() {
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();

        assertThat(shoppingCart.getPostageVat(), is(BigDecimal.ZERO.setScale(2)));
        assertThat(shoppingCart.getPostageBeforeVat(), is(BigDecimal.ZERO.setScale(2)));
        assertThat(shoppingCart.getPostage(), is(BigDecimal.ZERO.setScale(2)));
    }

    @Test
    public void shouldCalculateMonetaryDiscountVat(){
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(14))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(0)
                .build();
        shoppingCart.addItem(cartItem);
        final DeliveryOption deliveryOption = DeliveryOption.builder().charge(6D).vatRate(20).build();
        shoppingCart.setDeliveryOption(deliveryOption);
        final Promotion promotion = Promotion.builder().voucherType(MONETARY).discountAmount(BigDecimal.valueOf(5D)).build();
        shoppingCart.setPromotion(promotion);

        assertThat(shoppingCart.getDiscount(), is(BigDecimal.valueOf(5).setScale(2)));
        assertThat(shoppingCart.getDiscountVat(), is(BigDecimal.valueOf(0.24).setScale(2)));
        assertThat(shoppingCart.getDiscountBeforeVat(), is(BigDecimal.valueOf(4.76).setScale(2)));
        assertThat(shoppingCart.getOrderTotal(), is(BigDecimal.valueOf(15).setScale(2)));
    }

    @Test
    public void shouldCalculatePercentageDiscountVat(){
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(14))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(0)
                .build();
        shoppingCart.addItem(cartItem);
        final DeliveryOption deliveryOption = DeliveryOption.builder().charge(6D).vatRate(20).build();
        shoppingCart.setDeliveryOption(deliveryOption);
        final Promotion promotion = Promotion.builder().voucherType(PERCENTAGE).discountAmount(BigDecimal.valueOf(5D)).build();
        shoppingCart.setPromotion(promotion);

        assertThat(shoppingCart.getDiscount(), is(BigDecimal.valueOf(1).setScale(2)));
        assertThat(shoppingCart.getDiscountVat(), is(BigDecimal.valueOf(0.05).setScale(2)));
        assertThat(shoppingCart.getDiscountBeforeVat(), is(BigDecimal.valueOf(0.95).setScale(2)));
        assertThat(shoppingCart.getOrderTotal(), is(BigDecimal.valueOf(19).setScale(2)));
    }

    @Test
    public void vatOnItemPlusVatOnPostagePlusDiscount(){
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(22.4))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem);
        final DeliveryOption deliveryOption = DeliveryOption.builder().charge(3.99D).vatRate(20).build();
        shoppingCart.setDeliveryOption(deliveryOption);
        final Promotion promotion = Promotion.builder().voucherType(PERCENTAGE).discountAmount(BigDecimal.valueOf(20D)).build();
        shoppingCart.setPromotion(promotion);

        assertThat(shoppingCart.getItemsBeforeVat(), is(BigDecimal.valueOf(18.67).setScale(2)));
        assertThat(shoppingCart.getItemsVat(), is(BigDecimal.valueOf(3.73).setScale(2)));
        assertThat(shoppingCart.getPostageBeforeVat(), is(BigDecimal.valueOf(3.33).setScale(2)));
        assertThat(shoppingCart.getPostageVat(), is(BigDecimal.valueOf(0.66).setScale(2)));
        assertThat(shoppingCart.getDiscountBeforeVat(), is(BigDecimal.valueOf(4.4).setScale(2)));
        assertThat(shoppingCart.getDiscountVat(), is(BigDecimal.valueOf(0.88).setScale(2)));
        assertThat(shoppingCart.getTotalBeforeVat(), is(BigDecimal.valueOf(17.6).setScale(2)));
        assertThat(shoppingCart.getVatTotal(), is(BigDecimal.valueOf(3.51).setScale(2)));
        assertThat(shoppingCart.getOrderTotal(), is(BigDecimal.valueOf(21.11).setScale(2)));
    }

    @Test
    public void noVatOnItemPlusVatOnPostagePlusDiscount(){
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(22.4))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(0)
                .build();
        shoppingCart.addItem(cartItem);
        final DeliveryOption deliveryOption = DeliveryOption.builder().charge(3.99D).vatRate(20).build();
        shoppingCart.setDeliveryOption(deliveryOption);
        final Promotion promotion = Promotion.builder().voucherType(PERCENTAGE).discountAmount(BigDecimal.valueOf(20D)).build();
        shoppingCart.setPromotion(promotion);

        assertThat(shoppingCart.getItemsBeforeVat(), is(BigDecimal.valueOf(22.4).setScale(2)));
        assertThat(shoppingCart.getItemsVat(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getPostageBeforeVat(), is(BigDecimal.valueOf(3.33).setScale(2)));
        assertThat(shoppingCart.getPostageVat(), is(BigDecimal.valueOf(0.66).setScale(2)));
        assertThat(shoppingCart.getDiscountBeforeVat(), is(BigDecimal.valueOf(5.13).setScale(2)));
        assertThat(shoppingCart.getDiscountVat(), is(BigDecimal.valueOf(0.15).setScale(2)));
        assertThat(shoppingCart.getTotalBeforeVat(), is(BigDecimal.valueOf(20.60).setScale(2)));
        assertThat(shoppingCart.getVatTotal(), is(BigDecimal.valueOf(0.51).setScale(2)));
        assertThat(shoppingCart.getOrderTotal(), is(BigDecimal.valueOf(21.11).setScale(2)));
    }

    @Test
    public void vatOnItemPlusFreePostagePlusDiscount(){
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(22.4))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem);
        final DeliveryOption deliveryOption = DeliveryOption.builder().charge(0D).vatRate(20).build();
        shoppingCart.setDeliveryOption(deliveryOption);
        final Promotion promotion = Promotion.builder().voucherType(PERCENTAGE).discountAmount(BigDecimal.valueOf(20D)).build();
        shoppingCart.setPromotion(promotion);

        assertThat(shoppingCart.getItemsBeforeVat(), is(BigDecimal.valueOf(18.67).setScale(2)));
        assertThat(shoppingCart.getItemsVat(), is(BigDecimal.valueOf(3.73).setScale(2)));
        assertThat(shoppingCart.getPostageBeforeVat(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getPostageVat(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getDiscountBeforeVat(), is(BigDecimal.valueOf(3.73).setScale(2)));
        assertThat(shoppingCart.getDiscountVat(), is(BigDecimal.valueOf(0.75).setScale(2)));
        assertThat(shoppingCart.getTotalBeforeVat(), is(BigDecimal.valueOf(14.94).setScale(2)));
        assertThat(shoppingCart.getVatTotal(), is(BigDecimal.valueOf(2.98).setScale(2)));
        assertThat(shoppingCart.getOrderTotal(), is(BigDecimal.valueOf(17.92).setScale(2)));
    }

    @Test
    public void noVatOnItemPlusFreePostagePlusDiscount(){
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(22.4))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(0)
                .build();
        shoppingCart.addItem(cartItem);
        final DeliveryOption deliveryOption = DeliveryOption.builder().charge(0D).vatRate(20).build();
        shoppingCart.setDeliveryOption(deliveryOption);
        final Promotion promotion = Promotion.builder().voucherType(PERCENTAGE).discountAmount(BigDecimal.valueOf(20D)).build();
        shoppingCart.setPromotion(promotion);

        assertThat(shoppingCart.getItemsBeforeVat(), is(BigDecimal.valueOf(22.4).setScale(2)));
        assertThat(shoppingCart.getItemsVat(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getPostageBeforeVat(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getPostageVat(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getDiscountBeforeVat(), is(BigDecimal.valueOf(4.48).setScale(2)));
        assertThat(shoppingCart.getDiscountVat(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getTotalBeforeVat(), is(BigDecimal.valueOf(17.92).setScale(2)));
        assertThat(shoppingCart.getVatTotal(), is(BigDecimal.valueOf(0).setScale(2)));
        assertThat(shoppingCart.getOrderTotal(), is(BigDecimal.valueOf(17.92).setScale(2)));
    }
}