package shoppingcart.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import shoppingcart.data.VoucherData;
import shoppingcart.domain.Promotion;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.Voucher;
import shoppingcart.repository.ShoppingCartRepository;
import shoppingcart.repository.VoucherRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static shoppingcart.domain.Voucher.Type.MONETARY;

public class VoucherControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void shouldGetActiveVouchersForCustomer() {
        // Given
        Voucher voucherOne = Voucher.builder()
                .type(MONETARY)
                .code("code-1")
                .name("Thank you for signing up!")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.ONE)
                .discountAmount(BigDecimal.ONE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(voucherOne);

        Voucher voucherTwo = Voucher.builder()
                .type(MONETARY)
                .code("code-2")
                .name("Thank you for signing up!")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.TEN)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now())
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(voucherTwo);

        Voucher voucherThree = Voucher.builder()
                .type(MONETARY)
                .code("code-3")
                .name("Thank you for signing up!")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.TEN)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now())
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(voucherThree);

        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final Promotion promotion = Promotion.builder()
                .voucherCode("code-3")
                .voucherType(MONETARY)
                .vatRate(20)
                .discountAmount(BigDecimal.TEN)
                .cartId(cart.getId())
                .build();
        shoppingCartRepository.addPromotion(cart.getId(), promotion);
        shoppingCartRepository.deactivateShoppingCart(cart.getId());

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<VoucherData[]> response = rest.exchange("/carts/vouchers?customerId=123e4567-e89b-12d3-a456-426614174000&status=active", GET, httpEntity, VoucherData[].class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().length, is(2));
        assertThat(response.getBody()[0].getCode(), is("code-1"));
        assertThat(response.getBody()[0].getType(), is("MONETARY"));
        assertThat(response.getBody()[0].getName(), is("Thank you for signing up!"));
        assertThat(response.getBody()[0].getDiscountAmount(), is(BigDecimal.ONE.setScale(2)));
        assertThat(response.getBody()[0].getStartDate(), is(LocalDate.now().toString()));
        assertThat(response.getBody()[0].getEndDate(), is(LocalDate.now().plusDays(10).toString()));
        assertThat(response.getBody()[1].getCode(), is("code-2"));
        assertThat(response.getBody()[1].getType(), is("MONETARY"));
        assertThat(response.getBody()[1].getName(), is("Thank you for signing up!"));
        assertThat(response.getBody()[1].getDiscountAmount(), is(BigDecimal.TEN.setScale(2)));
        assertThat(response.getBody()[1].getStartDate(), is(LocalDate.now().minusDays(10).toString()));
        assertThat(response.getBody()[1].getEndDate(), is(LocalDate.now().toString()));
    }

    @Test
    public void shouldGetUsedVouchersForCustomer() {
        // Given
        Voucher usedVoucher = Voucher.builder()
                .type(MONETARY)
                .code("code-3")
                .name("name-3")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.TEN)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now())
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(usedVoucher);

        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final Promotion promotion = Promotion.builder()
                .voucherCode("code-3")
                .voucherType(MONETARY)
                .vatRate(20)
                .discountAmount(BigDecimal.TEN)
                .cartId(cart.getId())
                .build();
        shoppingCartRepository.addPromotion(cart.getId(), promotion);
        shoppingCartRepository.deactivateShoppingCart(cart.getId());

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<VoucherData[]> response = rest.exchange("/carts/vouchers?customerId=123e4567-e89b-12d3-a456-426614174000&status=used", GET, httpEntity, VoucherData[].class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().length, is(1));
        assertThat(response.getBody()[0].getCode(), is("code-3"));
        assertThat(response.getBody()[0].getType(), is("MONETARY"));
        assertThat(response.getBody()[0].getName(), is("name-3"));
        assertThat(response.getBody()[0].getDiscountAmount(), is(BigDecimal.TEN.setScale(2)));
        assertThat(response.getBody()[0].getStartDate(), is(LocalDate.now().minusDays(10).toString()));
        assertThat(response.getBody()[0].getEndDate(), is(LocalDate.now().toString()));
    }

    @Test
    public void shouldGetExpiredVouchersForCustomer() {
        // Given
        Voucher usedVoucher = Voucher.builder()
                .type(MONETARY)
                .code("code-3")
                .name("name-3")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.TEN)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(1))
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(usedVoucher);

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<VoucherData[]> response = rest.exchange("/carts/vouchers?customerId=123e4567-e89b-12d3-a456-426614174000&status=expired", GET, httpEntity, VoucherData[].class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().length, is(1));
        assertThat(response.getBody()[0].getCode(), is("code-3"));
        assertThat(response.getBody()[0].getType(), is("MONETARY"));
        assertThat(response.getBody()[0].getName(), is("name-3"));
        assertThat(response.getBody()[0].getDiscountAmount(), is(BigDecimal.TEN.setScale(2)));
        assertThat(response.getBody()[0].getStartDate(), is(LocalDate.now().minusDays(10).toString()));
        assertThat(response.getBody()[0].getEndDate(), is(LocalDate.now().minusDays(1).toString()));
    }

    @Test
    public void shouldAddWelcomeVoucherForNewCustomer(){
        // Given
        this.setUserToken();
        final String customerData = "{\"id\": \"123e4567-e89b-12d3-a456-556642440000\", \"email\": \"jiandong.c@gmail.com\"}";
        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);

        // When
        ResponseEntity<String> response = rest.exchange("/carts/vouchers/welcome", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        List<Voucher> vouchers = voucherRepository.findByCustomerUid(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        assertThat(vouchers.size(), is(1));
        assertThat(vouchers.get(0).getType(), is(Voucher.Type.PERCENTAGE));
        assertThat(vouchers.get(0).getDiscountAmount(), is(BigDecimal.valueOf(20).setScale(2)));
        assertThat(vouchers.get(0).getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));

        // When
        response = rest.exchange("/carts/vouchers/welcome", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        vouchers = voucherRepository.findByCustomerUid(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        assertThat(vouchers.size(), is(1));
        assertThat(vouchers.get(0).getType(), is(Voucher.Type.PERCENTAGE));
        assertThat(vouchers.get(0).getDiscountAmount(), is(BigDecimal.valueOf(20).setScale(2)));
        assertThat(vouchers.get(0).getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
    }

}