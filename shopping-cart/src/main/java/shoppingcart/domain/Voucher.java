package shoppingcart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    private Long id;

    private Voucher.Type type;

    private String code;

    private String name;

    private Integer maxUses;

    private Integer maxUsesUser;

    private BigDecimal minSpend;

    private BigDecimal discountAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime creationTime;

    private UUID customerUid;

    public enum Type {
        CUSTOMER_SIGN_UP_VOUCHER,
        PROMOTION_VOUCHER
    }

    public boolean isActive() {
        final LocalDate today = LocalDate.now();
        return (this.getStartDate() != null && !today.isBefore(this.getStartDate()))
                && (this.getEndDate() == null || !today.isAfter(this.getEndDate()));
    }

    public boolean isExpired() {
        final LocalDate today = LocalDate.now();
        return this.getEndDate() != null && today.isAfter(this.getEndDate());
    }
}
