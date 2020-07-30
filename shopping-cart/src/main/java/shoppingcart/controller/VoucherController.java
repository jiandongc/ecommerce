package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingcart.data.VoucherData;
import shoppingcart.mapper.VoucherDataMapper;
import shoppingcart.service.VoucherService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/carts/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherDataMapper voucherDataMapper;

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = GET)
    public List<VoucherData> getVouchers(@RequestParam(value = "customerId") String customerId,
                                         @RequestParam(value = "status", required = false) String status) {
        return voucherService.findByCustomerUid(UUID.fromString(customerId), status)
                .stream().map(voucherDataMapper::map)
                .collect(Collectors.toList());
    }
}
