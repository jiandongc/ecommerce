package shoppingcart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.mapper.ShoppingCartItemMapper;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@Repository
public class ShoppingCartItemRepositoryImpl implements ShoppingCartItemRepository {

    private static final String SELECT_SQL = "select * from shopping_cart_item where shopping_cart_id = ? order by creation_time asc";
    private static final String SELECT_BY_SKU_SQL = "select * from shopping_cart_item where shopping_cart_id = ? and sku = ?";
    private static final String INSERT_SQL = "insert into shopping_cart_item " +
            "(shopping_cart_id, product_code, sku, product_name, unit_price, image_url, description) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QTY_SQL = "update shopping_cart_item set quantity = ? where shopping_cart_id = ? and sku = ?";
    private static final String DELETE_BY_CART_ID_AND_SKU_SQL = "delete from shopping_cart_item where shopping_cart_id = ? and sku = ?";
    private static final String DELETE_BY_CARA_ID_SQL = "delete from shopping_cart_item where shopping_cart_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShoppingCartItemMapper shoppingCartItemMapper;

    @Override
    public void save(long cartId, ShoppingCartItem cartItem) {
        jdbcTemplate.update(INSERT_SQL,
                cartId,
                cartItem.getCode(),
                cartItem.getSku(),
                cartItem.getName(),
                cartItem.getPrice(),
                cartItem.getImageUrl(),
                cartItem.getDescription());
    }

    @Override
    public void updateQuantity(long cartId, String sku, int quantity) {
        jdbcTemplate.update(UPDATE_QTY_SQL, quantity, cartId, sku);
    }

    @Override
    public List<ShoppingCartItem> findByCartId(long cartId) {
        return jdbcTemplate.query(SELECT_SQL, shoppingCartItemMapper, cartId);
    }

    @Override
    public Optional<ShoppingCartItem> findByCartIdAndSku(long cartId, String sku) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_SKU_SQL, shoppingCartItemMapper, cartId, sku));
        } catch (Exception exc) {
            return empty();
        }
    }

    @Override
    public void delete(long cartId, String sku) {
        jdbcTemplate.update(DELETE_BY_CART_ID_AND_SKU_SQL, cartId, sku);
    }

    @Override
    public int deleteByCartId(long cartId) {
        return jdbcTemplate.update(DELETE_BY_CARA_ID_SQL, cartId);
    }
}
