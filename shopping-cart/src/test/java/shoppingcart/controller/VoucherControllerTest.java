package shoppingcart.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import shoppingcart.data.VoucherData;
import shoppingcart.domain.Voucher;
import shoppingcart.repository.VoucherRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static shoppingcart.domain.Voucher.Type.CUSTOMER_SIGN_UP_VOUCHER;

public class VoucherControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private VoucherRepository voucherRepository;

    @Test
    public void shouldGetVouchersForCustomer(){
        // Given
        Voucher expiredVoucher = Voucher.builder()
                .type(CUSTOMER_SIGN_UP_VOUCHER)
                .code("code-1")
                .name("name-1")
                .maxUses(10)
                .maxUsesUser(1)
                .minSpend(BigDecimal.ONE)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2020, 6, 21))
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(expiredVoucher);

        Voucher voucher = Voucher.builder()
                .type(CUSTOMER_SIGN_UP_VOUCHER)
                .code("code-2")
                .name("name-2")
                .maxUses(10)
                .maxUsesUser(1)
                .minSpend(BigDecimal.ONE)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2022, 6, 21))
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(voucher);

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<VoucherData[]> response = rest.exchange("/carts/vouchers?customerId=123e4567-e89b-12d3-a456-426614174000", GET, httpEntity, VoucherData[].class);
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().length, is(2));
        assertThat(response.getBody()[0].getCode(), is("code-1"));
        assertThat(response.getBody()[0].getName(), is("name-1"));
        assertThat(response.getBody()[0].getValid(), is(false));
        assertThat(response.getBody()[1].getCode(), is("code-2"));
        assertThat(response.getBody()[1].getName(), is("name-2"));
        assertThat(response.getBody()[1].getValid(), is(true));



    }

}