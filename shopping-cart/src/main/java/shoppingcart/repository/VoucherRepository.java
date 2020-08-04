package shoppingcart.repository;

import shoppingcart.domain.Voucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {

    void save(Voucher voucher);
    List<Voucher> findByCustomerUid(UUID customerUid);
    Optional<Voucher> findByVoucherCode(String voucherCode);
    Integer findNumberOfUses(String code);
}
