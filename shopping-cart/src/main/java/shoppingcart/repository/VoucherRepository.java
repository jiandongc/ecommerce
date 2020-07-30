package shoppingcart.repository;

import shoppingcart.domain.Voucher;

import java.util.List;
import java.util.UUID;

public interface VoucherRepository {

    void save(Voucher voucher);
    List<Voucher> findByCustomerUid(UUID customerUid);
    Integer findNumberOfUses(String code);
}
