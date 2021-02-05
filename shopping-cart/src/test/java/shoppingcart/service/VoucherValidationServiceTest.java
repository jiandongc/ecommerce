package shoppingcart.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.domain.ValidationResult;
import shoppingcart.domain.Voucher;
import shoppingcart.repository.ShoppingCartRepository;
import shoppingcart.repository.VoucherRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoucherValidationServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private VoucherValidationService validationService;

    @Test
    public void shouldFailValidationIfVoucherCodeIsNotFound(){
        // Given
        final String voucherCode = "ABC-2020";
        when(voucherRepository.findByVoucherCode(voucherCode)).thenReturn(Optional.empty());

        // When
        ValidationResult actual = validationService.validate(UUID.randomUUID(), voucherCode);

        // Then
        assertThat(actual.isValid(), is(false));
        assertThat(actual.getErrorMsg(), is("Invalid code: ABC-2020"));
    }

    @Test
    public void shouldFailValidationIfVoucherIsExpired(){
        // Given
        final String voucherCode = "ABC-2019";
        when(voucherRepository.findByVoucherCode(voucherCode))
                .thenReturn(Optional.of(Voucher.builder().endDate(LocalDate.of(2019, 12, 31)).build()));

        // When
        ValidationResult actual = validationService.validate(UUID.randomUUID(), voucherCode);

        // Then
        assertThat(actual.isValid(), is(false));
        assertThat(actual.getErrorMsg(), is("Voucher ABC-2019 has expired. Valid until: 2019-12-31."));
    }

    @Test
    public void shouldFailValidationIfVoucherIsInactive(){
        // Given
        final String voucherCode = "ABC-2099";
        when(voucherRepository.findByVoucherCode(voucherCode))
                .thenReturn(Optional.of(Voucher.builder().startDate(LocalDate.of(2099, 1, 1)).endDate(LocalDate.of(2099, 12, 31)).build()));

        // When
        ValidationResult actual = validationService.validate(UUID.randomUUID(), voucherCode);

        // Then
        assertThat(actual.isValid(), is(false));
        assertThat(actual.getErrorMsg(), is("Voucher ABC-2099 is inactive. Valid from 2099-01-01 to 2099-12-31."));
    }

    @Test
    public void shouldFailValidationIfVoucherExceededMaxUses(){
        // Given
        final String voucherCode = "ABC-2099";
        when(voucherRepository.findByVoucherCode(voucherCode))
                .thenReturn(Optional.of(Voucher.builder()
                        .startDate(LocalDate.of(2010, 1, 1))
                        .endDate(LocalDate.of(2099, 12, 31))
                        .maxUses(10)
                        .build()));
        when(voucherRepository.findNumberOfUses(voucherCode)).thenReturn(10);

        // When
        ValidationResult actual = validationService.validate(UUID.randomUUID(), voucherCode);

        // Then
        assertThat(actual.isValid(), is(false));
        assertThat(actual.getErrorMsg(), is("Voucher ABC-2099 exceeded maximum uses."));
    }

    @Test
    public void shouldFailValidationIfShoppingCartDoesNotMeetMinSpendRequirement(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final String voucherCode = "ABC-2099";
        when(voucherRepository.findByVoucherCode(voucherCode))
                .thenReturn(Optional.of(Voucher.builder()
                        .startDate(LocalDate.of(2010, 1, 1))
                        .endDate(LocalDate.of(2099, 12, 31))
                        .maxUses(10)
                        .minSpend(BigDecimal.valueOf(30L).setScale(2))
                        .build()));
        when(voucherRepository.findNumberOfUses(voucherCode)).thenReturn(1);
        ShoppingCart shoppingCart = ShoppingCart.builder().build();
        shoppingCart.addItem(ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(29.99))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build());
        when(shoppingCartService.getShoppingCartByUid(cartUid))
                .thenReturn(Optional.of(shoppingCart));


        // When
        ValidationResult actual = validationService.validate(cartUid, voucherCode);

        // Then
        assertThat(actual.isValid(), is(false));
        assertThat(actual.getErrorMsg(), is("Minimum spend of this voucher is £30.00."));
    }

    @Test
    @Ignore
    public void shouldFailValidationIfVoucherDoesNotBelongToTheCustomer(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final String voucherCode = "ABC-2099";
        when(voucherRepository.findByVoucherCode(voucherCode))
                .thenReturn(Optional.of(Voucher.builder()
                        .customerUid(UUID.randomUUID())
                        .startDate(LocalDate.of(2010, 1, 1))
                        .endDate(LocalDate.of(2099, 12, 31))
                        .maxUses(10)
                        .minSpend(BigDecimal.valueOf(10L).setScale(2))
                        .build()));
        when(voucherRepository.findNumberOfUses(voucherCode)).thenReturn(1);
        ShoppingCart shoppingCart = ShoppingCart.builder().customerUid(UUID.randomUUID()).build();
        shoppingCart.addItem(ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(29.99))
                .quantity(1)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build());
        when(shoppingCartService.getShoppingCartByUid(cartUid))
                .thenReturn(Optional.of(shoppingCart));

        // When
        ValidationResult actual = validationService.validate(cartUid, voucherCode);

        // Then
        assertThat(actual.isValid(), is(false));
        assertThat(actual.getErrorMsg(), is("Not authorized to use voucher: ABC-2099. You might need to sign in first."));
    }
    
    @Test
    public void shouldPassValidation(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final String voucherCode = "ABC-2099";
        when(voucherRepository.findByVoucherCode(voucherCode))
                .thenReturn(Optional.of(Voucher.builder()
                        .startDate(LocalDate.of(2010, 1, 1))
                        .endDate(LocalDate.of(2099, 12, 31))
                        .maxUses(10)
                        .minSpend(BigDecimal.valueOf(30L).setScale(2))
                        .build()));
        when(voucherRepository.findNumberOfUses(voucherCode)).thenReturn(1);
        ShoppingCart shoppingCart = ShoppingCart.builder().build();
        shoppingCart.addItem(ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(29.99))
                .quantity(2)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build());
        when(shoppingCartService.getShoppingCartByUid(cartUid))
                .thenReturn(Optional.of(shoppingCart));


        // When
        ValidationResult actual = validationService.validate(cartUid, voucherCode);

        // Then
        assertThat(actual.isValid(), is(true));
    }
}