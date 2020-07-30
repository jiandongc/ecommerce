package shoppingcart.mapper;

import org.springframework.stereotype.Component;
import shoppingcart.data.VoucherData;
import shoppingcart.domain.Voucher;

@Component
public class VoucherDataMapper {

    public VoucherData map(Voucher voucher) {
        return VoucherData.builder()
                .code(voucher.getCode())
                .name(voucher.getName())
                .discountAmount(voucher.getDiscountAmount())
                .startDate(voucher.getStartDate().toString())
                .endDate(voucher.getEndDate() != null ? voucher.getEndDate().toString() : null)
                .creationTime(voucher.getCreationTime().toString())
                .build();
    }
}
