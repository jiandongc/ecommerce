package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ValidationResult;
import shoppingcart.domain.Voucher;
import shoppingcart.repository.VoucherRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class VoucherValidationService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    public ValidationResult validate(UUID cartUid, String voucherCode) {

        Optional<Voucher> voucherOptional = voucherRepository.findByVoucherCode(voucherCode);

        if (!voucherOptional.isPresent()) {
            return ValidationResult.invalid(String.format("Invalid code: %s", voucherCode));
        }

        final Voucher voucher = voucherOptional.get();

        if (voucher.isExpired()) {
            return ValidationResult.invalid(String.format("Voucher %s has expired. Valid until: %s.", voucherCode, voucher.getEndDate()));
        }

        if (!voucher.isActive()) {
            return ValidationResult.invalid(String.format("Voucher %s is inactive. Valid from %s to %s.", voucherCode, voucher.getStartDate(), voucher.getEndDate()));
        }

        Integer numberOfUses = voucherRepository.findNumberOfUses(voucherCode);

        if (numberOfUses >= voucher.getMaxUses()) {
            return ValidationResult.invalid(String.format("Voucher %s exceeded maximum uses.", voucherCode));
        }

        Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);

        if (!cartOptional.isPresent()) {
            return ValidationResult.invalid(String.format("Invalid shopping cart: %s", cartUid));
        }

        ShoppingCart shoppingCart = cartOptional.get();

        if (voucher.getMinSpend() != null && voucher.getMinSpend().compareTo(shoppingCart.getItemSubTotal()) > 0) {
            return ValidationResult.invalid(String.format("Minimum spend of this voucher is £%s.", voucher.getMinSpend()));
        }


        return ValidationResult.valid();
    }

}
