package shoppingcart.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import shoppingcart.domain.Promotion;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.Voucher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static shoppingcart.domain.Voucher.Type.CUSTOMER_SIGN_UP_VOUCHER;

public class VoucherRepositoryImplTest extends AbstractRepositoryTest {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldFindVoucherByCustomerUid(){
        // Given
        Voucher voucher = Voucher.builder()
                .type(CUSTOMER_SIGN_UP_VOUCHER)
                .code("ABC-12")
                .name("name")
                .maxUses(10)
                .maxUsesUser(1)
                .minSpend(BigDecimal.ONE)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2020, 6, 21))
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(voucher);

        // When
        List<Voucher> actual = voucherRepository.findByCustomerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        // Then
        assertThat(actual.size(), is(1));
        assertThat(actual.get(0).getCode(), is("ABC-12"));
        assertThat(actual.get(0).getName(), is("name"));
        assertThat(actual.get(0).getMaxUses(), is(10));
        assertThat(actual.get(0).getMaxUsesUser(), is(1));
        assertThat(actual.get(0).getMinSpend(), is(BigDecimal.ONE.setScale(2)));
        assertThat(actual.get(0).getDiscountAmount(), is(BigDecimal.TEN.setScale(2)));
        assertThat(actual.get(0).getStartDate(), is(LocalDate.of(2020, 1, 1)));
        assertThat(actual.get(0).getEndDate(), is(LocalDate.of(2020, 6, 21)));
        assertThat(actual.get(0).getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
    }

    @Test
    public void shouldNotLoadSoftDeletedRecord(){
        // Given
        Voucher voucher = Voucher.builder()
                .type(CUSTOMER_SIGN_UP_VOUCHER)
                .code("ABC-12")
                .name("name")
                .maxUses(10)
                .maxUsesUser(1)
                .minSpend(BigDecimal.ONE)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2020, 6, 21))
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(voucher);
        jdbcTemplate.update("update voucher set soft_delete = true");

        // When
        List<Voucher> actual = voucherRepository.findByCustomerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        // Then
        assertThat(actual.size(), is(0));
    }

    @Test
    public void shouldReturnZeroUse(){
        // Given - cart is active
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final Promotion promotion = Promotion.builder()
                .voucherCode("ABC-15")
                .vatRate(20)
                .discountAmount(BigDecimal.ONE)
                .cartId(cart.getId())
                .build();
        shoppingCartRepository.addPromotion(cart.getId(), promotion);

        // When
        Integer actual = voucherRepository.findNumberOfUses("ABC-15");

        // Then
        assertThat(actual, is(0));
    }

    @Test
    public void shouldReturnOneUse(){
        // Given - cart is de-activated
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final Promotion promotion = Promotion.builder()
                .voucherCode("ABC-15")
                .vatRate(20)
                .discountAmount(BigDecimal.ONE)
                .cartId(cart.getId())
                .build();
        shoppingCartRepository.addPromotion(cart.getId(), promotion);
        shoppingCartRepository.deactivateShoppingCart(cart.getId());

        // When
        Integer actual = voucherRepository.findNumberOfUses("ABC-15");

        // Then
        assertThat(actual, is(1));
    }

}