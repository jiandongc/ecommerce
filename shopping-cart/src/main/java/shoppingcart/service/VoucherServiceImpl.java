package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppingcart.domain.Voucher;
import shoppingcart.repository.VoucherRepository;

import java.util.List;
import java.util.UUID;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public List<Voucher> findByCustomerUid(UUID customerUid) {
        return voucherRepository.findByCustomerUid(customerUid);
    }
}
