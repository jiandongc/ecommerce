package shoppingcart.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import shoppingcart.domain.Voucher;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class VoucherMapper implements RowMapper<Voucher> {

    @Override
    public Voucher mapRow(ResultSet rs, int i) throws SQLException {
        return Voucher.builder()
                .type(Voucher.Type.valueOf(rs.getString("type")))
                .code(rs.getString("code"))
                .name(rs.getString("name"))
                .maxUses(rs.getInt("max_uses"))
                .maxUsesUser(rs.getInt("max_uses_user"))
                .minSpend(rs.getBigDecimal("min_spend").setScale(2, BigDecimal.ROUND_HALF_UP))
                .discountAmount(rs.getBigDecimal("discount_amount").setScale(2, BigDecimal.ROUND_HALF_UP))
                .startDate(rs.getDate("start_date").toLocalDate())
                .endDate(rs.getDate("end_date").toLocalDate())
                .customerUid(UUID.fromString(rs.getString("customer_uid")))
                .creationTime(rs.getTimestamp("creation_time").toLocalDateTime())
                .build();
    }
}
