package shoppingcart.service;

import shoppingcart.domain.Voucher;

import java.util.List;
import java.util.UUID;

public interface VoucherService {

    List<Voucher> findByCustomerUid(UUID customerUid);
}