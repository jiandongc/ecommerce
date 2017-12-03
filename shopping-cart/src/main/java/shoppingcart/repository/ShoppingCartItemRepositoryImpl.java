package shoppingcart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.mapper.ShoppingCartItemMapper;

import java.util.List;

@Repository
public class ShoppingCartItemRepositoryImpl implements ShoppingCartItemRepository {

    private static final String INSERT_SQL = "insert into shopping_cart_item " +
            "(shopping_cart_id, sku, product_name, unit_price, image_url) values (?, ?, ?, ?, ?)";
    private static final String SELECT_SQL = "select * from shopping_cart_item where shopping_cart_id = ? order by creation_time asc";

    private final JdbcTemplate jdbcTemplate;
    private final ShoppingCartItemMapper shoppingCartItemMapper;

    @Autowired
    public ShoppingCartItemRepositoryImpl(JdbcTemplate jdbcTemplate, ShoppingCartItemMapper shoppingCartItemMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.shoppingCartItemMapper = shoppingCartItemMapper;
    }

    @Override
    public void save(long cartId, ShoppingCartItem cartItem) {
        jdbcTemplate.update(INSERT_SQL, cartId, cartItem.getSku(), cartItem.getName(), cartItem.getPrice(), cartItem.getImageUrl());
    }

    @Override
    public List<ShoppingCartItem> findByCartId(long cartId) {
        return jdbcTemplate.query(SELECT_SQL, shoppingCartItemMapper, cartId);
    }
}
