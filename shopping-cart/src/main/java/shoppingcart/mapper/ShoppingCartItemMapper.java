package shoppingcart.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import shoppingcart.domain.ShoppingCartItem;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShoppingCartItemMapper implements RowMapper<ShoppingCartItem> {

    @Override
    public ShoppingCartItem mapRow(ResultSet rs, int i) throws SQLException {
        return ShoppingCartItem.builder()
                .id(rs.getLong("id"))
                .cartId(rs.getLong("shopping_cart_id"))
                .sku(rs.getString("sku"))
                .name(rs.getString("product_name"))
                .price(rs.getBigDecimal("unit_price"))
                .quantity(rs.getInt("quantity"))
                .imageUrl(rs.getString("image_url"))
                .creationTime(rs.getDate("creation_time"))
                .lastUpdateTime(rs.getDate("last_update_time"))
                .build();
    }
}
