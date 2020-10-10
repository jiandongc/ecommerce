package shoppingcart.service;

import shoppingcart.domain.Voucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoucherService {

    List<Voucher> findByCustomerUid(UUID customerUid, String status);

    Integer getNumberOfUses(String code);

    Optional<Voucher> addNewCustomerWelcomeVoucher(UUID customerUid);
}
