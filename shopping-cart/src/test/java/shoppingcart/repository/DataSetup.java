package shoppingcart.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import shoppingcart.domain.Voucher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static shoppingcart.domain.Voucher.Type.CUSTOMER_SIGN_UP_VOUCHER;

public class DataSetup extends AbstractRepositoryTest {

    @Autowired
    private VoucherRepository voucherRepository;

    @Test
    @Rollback(false)
    public void addVouchers() {

        Voucher voucherOne = Voucher.builder()
                .type(CUSTOMER_SIGN_UP_VOUCHER)
                .code("code-1")
                .name("name-1")
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
                .type(CUSTOMER_SIGN_UP_VOUCHER)
                .code("code-2")
                .name("name-2")
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
                .type(CUSTOMER_SIGN_UP_VOUCHER)
                .code("code-3")
                .name("name-3")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.TEN)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(2))
                .customerUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build();

        voucherRepository.save(voucherThree);
    }
}
