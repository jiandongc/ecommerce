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
        return ShoppingCart.builder()
                .id(rs.getLong("id"))
                .cartUid(UUID.fromString(rs.getString("cart_uid")))
                .email(rs.getString("email"))
                .customerUid(rs.getObject("customer_uid") != null ? UUID.fromString(rs.getString("customer_uid")) : null)
                .creationTime(rs.getDate("creation_time"))
                .active(rs.getBoolean("active"))
                .build();
    }
}
