package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingcart.data.CustomerData;
import shoppingcart.data.VoucherData;
import shoppingcart.domain.Voucher;
import shoppingcart.mapper.VoucherDataMapper;
import shoppingcart.service.ShoppingCartEmailService;
import shoppingcart.service.VoucherService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/carts/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherDataMapper voucherDataMapper;

    @Autowired
    private ShoppingCartEmailService emailService;

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = GET)
    public List<VoucherData> getVouchers(@RequestParam(value = "customerId") String customerId,
                                         @RequestParam(value = "status", required = false) String status) {
        return voucherService.findByCustomerUid(UUID.fromString(customerId), status)
                .stream().map(voucherDataMapper::map)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "welcome", method = POST)
    public ResponseEntity addNewCustomerWelcomeVoucher(@RequestBody CustomerData customerData) {
        Optional<Voucher> voucherOptional = voucherService.addNewCustomerWelcomeVoucher(UUID.fromString(customerData.getId()));
        voucherOptional.ifPresent(voucher -> emailService.sendWelcomeEmail(
                customerData.getEmail(),
                voucher.getDiscountAmount().toBigInteger().toString(),
                voucher.getCode()));
        return new ResponseEntity<>(OK);
    }

}
