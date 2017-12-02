package shoppingcart.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import shoppingcart.domain.ShoppingCart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ShoppingCartMapper implements RowMapper<ShoppingCart>{

    @Override
    public ShoppingCart mapRow(ResultSet rs, int i) throws SQLException {
        ShoppingCart.ShoppingCartBuilder builder = ShoppingCart.builder();
        builder.id(rs.getLong("id"));
        builder.cartUid(UUID.fromString(rs.getString("cart_uid")));
        builder.customerId(rs.getObject("customer_id") != null ? rs.getLong("customer_id") : null);
        builder.creationTime(rs.getDate("creation_time"));
        return builder.build();
    }
}
