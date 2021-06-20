package shoppingcart.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import shoppingcart.domain.DeliveryOptionOffer;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DeliveryOptionOfferMapper implements RowMapper<DeliveryOptionOffer> {

    @Override
    public DeliveryOptionOffer mapRow(ResultSet rs, int i) throws SQLException {
        return DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode(rs.getString("country_code"))
                .method(rs.getString("method"))
                .minSpend(rs.getDouble("min_spend"))
                .charge(rs.getDouble("charge"))
                .minDaysRequired(rs.getInt("min_days_required"))
                .maxDaysRequired(rs.getInt("max_days_required"))
                .vatRate(rs.getInt("vat_rate"))
                .startDate(rs.getObject("start_date") != null ? rs.getDate("start_date").toLocalDate() : null)
                .endDate(rs.getObject("end_date") != null ? rs.getDate("end_date").toLocalDate() : null)
                .build();
    }
}
