package shoppingcart.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import shoppingcart.domain.Voucher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static shoppingcart.domain.Voucher.Type.MONETARY;
import static shoppingcart.domain.Voucher.Type.PERCENTAGE;

public class DataSetup extends AbstractRepositoryTest {

    @Autowired
    private VoucherRepository voucherRepository;

    @Test
    @Rollback(false)
    public void addVouchers() {

        Voucher voucherOne = Voucher.builder()
                .type(MONETARY)
                .code("code-1")
                .name("name-1")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.ONE)
                .discountAmount(BigDecimal.ONE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .customerUid(UUID.fromString("954a1c32-869e-485a-8213-33195be50244"))
                .build();

        voucherRepository.save(voucherOne);

        Voucher voucherTwo = Voucher.builder()
                .type(MONETARY)
                .code("code-2")
                .name("name-2")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.TEN)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now())
                .customerUid(UUID.fromString("954a1c32-869e-485a-8213-33195be50244"))
                .build();

        voucherRepository.save(voucherTwo);

        Voucher voucherThree = Voucher.builder()
                .type(MONETARY)
                .code("code-3")
                .name("name-3")
                .maxUses(1)
                .maxUsesUser(1)
                .minSpend(BigDecimal.TEN)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(2))
                .customerUid(UUID.fromString("954a1c32-869e-485a-8213-33195be50244"))
                .build();

        voucherRepository.save(voucherThree);

        Voucher voucherFour = Voucher.builder()
                .type(PERCENTAGE)
                .code("code-4")
                .name("name-4")
                .maxUses(1)
                .maxUsesUser(1)
                .discountAmount(BigDecimal.TEN)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .customerUid(UUID.fromString("954a1c32-869e-485a-8213-33195be50244"))
                .build();

        voucherRepository.save(voucherFour);
    }
}
