package shoppingcart.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.ShoppingCartMapper;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;

@Repository
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    private static final String INSERT_SQL = "insert into shopping_cart (cart_uid, customer_id) values (?, ?)";
    private static final String SELECT_BY_UUID_SQL = "select * from shopping_cart where cart_uid = ?";
    private static final String UPDATE_CUSTOMER_ID = "update shopping_cart set customer_id = ? where cart_uid = ?";

    private final JdbcTemplate jdbcTemplate;
    private final ShoppingCartMapper shoppingCartMapper;

    public ShoppingCartRepositoryImpl(JdbcTemplate jdbcTemplate, ShoppingCartMapper shoppingCartMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.shoppingCartMapper = shoppingCartMapper;
    }

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
    public Optional<ShoppingCart> findByUUID(UUID uuid) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_UUID_SQL, shoppingCartMapper, uuid));
        } catch (Exception e) {
            return empty();
        }
    }

    @Override
    public int updateCustomerId(UUID cartUid, Long customerId) {
        return jdbcTemplate.update(UPDATE_CUSTOMER_ID, customerId, cartUid);
    }
}
