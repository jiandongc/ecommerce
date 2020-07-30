package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppingcart.domain.Voucher;
import shoppingcart.repository.VoucherRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public List<Voucher> findByCustomerUid(UUID customerUid, String status) {
        List<Voucher> vouchers = voucherRepository.findByCustomerUid(customerUid);
        if (status == null) {
            return vouchers;
        } else if (status.equalsIgnoreCase("active")) {
            return vouchers.stream()
                    .filter(Voucher::isActive)
                    .filter(voucher -> voucher.getMaxUses() > voucherRepository.findNumberOfUses(voucher.getCode()))
                    .collect(Collectors.toList());
        } else if (status.equalsIgnoreCase("used")) {
            return vouchers.stream()
                    .filter(voucher -> voucherRepository.findNumberOfUses(voucher.getCode()) > 0)
                    .collect(Collectors.toList());
        } else if (status.equalsIgnoreCase("expired")) {
            return vouchers.stream()
                    .filter(Voucher::isExpired)
                    .collect(Collectors.toList());
        } else {
            return vouchers;
        }
    }

    @Override
    public Integer getNumberOfUses(String code) {
        return voucherRepository.findNumberOfUses(code);
    }
}
