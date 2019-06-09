package shoppingcart.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import shoppingcart.domain.DeliveryOption;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DeliveryOptionMapper implements RowMapper<DeliveryOption> {

    @Override
    public DeliveryOption mapRow(ResultSet rs, int i) throws SQLException {
        return DeliveryOption.builder()
                .method(rs.getString("method"))
                .charge(rs.getDouble("charge"))
                .minDaysRequired(rs.getInt("min_days_required"))
                .maxDaysRequired(rs.getInt("max_days_required"))
                .cartId(rs.getLong("shopping_cart_id"))
                .creationTime(rs.getDate("creation_time"))
                .lastUpdateTime(rs.getDate("last_update_time"))
                .build();
    }
}
