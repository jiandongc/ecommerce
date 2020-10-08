package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppingcart.domain.Voucher;
import shoppingcart.repository.VoucherRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
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

    @Override
    public void addNewCustomerWelcomeVoucher(UUID customerUid) {

        final List<Voucher> vouchers = voucherRepository.findByCustomerUid(customerUid);
        for (Voucher voucher : vouchers) {
            if (voucher.getCode().startsWith("WELCOME20_")) {
                return;
            }
        }

        final String voucherCode = "WELCOME20_" + randomString();
        Voucher voucher = Voucher.builder()
                .type(Voucher.Type.PERCENTAGE)
                .code(voucherCode)
                .name("Welcome to Noodle Monster!")
                .maxUses(1)
                .maxUsesUser(1)
                .discountAmount(BigDecimal.valueOf(20))
                .startDate(LocalDate.now())
                .customerUid(customerUid)
                .build();
        voucherRepository.save(voucher);
    }

    private String randomString() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        final String date = formatter.format(today);

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return date + generatedString.toUpperCase();
    }

}
