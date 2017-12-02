package shoppingcart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.ShoppingCartMapper;

import java.util.UUID;

@Repository
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    private static final String INSERT_SQL = "insert into shopping_cart (cart_uid, customer_id) values (?, ?)";
    private static final String SELECT_BY_UUID_SQL = "select * from shopping_cart where cart_uid = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public UUID create() {
        final UUID uuid = UUID.randomUUID();
        jdbcTemplate.update(INSERT_SQL, uuid, null);
        return uuid;
    }

    @Override
    public UUID create(long customerId) {
        final UUID uuid = UUID.randomUUID();
        jdbcTemplate.update(INSERT_SQL, uuid, customerId);
        return uuid;
    }

    @Override
    public ShoppingCart findByUUID(UUID uuid) {
        return jdbcTemplate.queryForObject(SELECT_BY_UUID_SQL, shoppingCartMapper, uuid);
    }
}
