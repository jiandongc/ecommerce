package shoppingcart.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import shoppingcart.domain.Promotion;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PromotionMapper implements RowMapper<Promotion> {

    @Override
    public Promotion mapRow(ResultSet rs, int i) throws SQLException {
        return Promotion.builder()
                .voucherCode(rs.getString("voucher_code"))
                .discountAmount(rs.getBigDecimal("discount_amount").setScale(2, BigDecimal.ROUND_HALF_UP))
                .vatRate(rs.getInt("vat_rate"))
                .creationTime(rs.getDate("creation_time"))
                .lastUpdateTime(rs.getDate("last_update_time"))
                .build();
    }
}
