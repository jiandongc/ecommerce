package shoppingcart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.AddressMapper;
import shoppingcart.mapper.DeliveryOptionMapper;
import shoppingcart.mapper.ShoppingCartMapper;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;

@Repository
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    private static final String INSERT_SQL = "INSERT INTO shopping_cart (cart_uid, customer_id, email) VALUES (?, ?, ?)";
    private static final String SELECT_BY_UUID_SQL = "SELECT * FROM shopping_cart WHERE cart_uid = ?";
    private static final String SELECT_BY_CUSTOMER_ID_SQL = "SELECT * FROM shopping_cart WHERE customer_id = ?";
    private static final String UPDATE_CUSTOMER_ID = "UPDATE shopping_cart SET customer_id = ? WHERE cart_uid = ?";
    private static final String UPDATE_EMAIL = "UPDATE shopping_cart SET email = ? WHERE cart_uid = ?";
    private static final String INSERT_ADDRESS_SQL = "INSERT INTO address " +
            "(address_type, title, name, mobile, address_line_1, address_line_2, address_line_3, city, country, post_code, shopping_cart_id) " +
            "VALUES (:address_type, :title, :name, :mobile, :address_line_1, :address_line_2, :address_line_3, :city, :country, :post_code, :shopping_cart_id) " +
            "ON CONFLICT ON CONSTRAINT address_type_constraint " +
            "DO UPDATE SET title=EXCLUDED.title, name=EXCLUDED.name, mobile=EXCLUDED.mobile, address_line_1=EXCLUDED.address_line_1, " +
            "address_line_2=EXCLUDED.address_line_2, address_line_3=EXCLUDED.address_line_3, city=EXCLUDED.city, country=EXCLUDED.country, post_code=EXCLUDED.post_code ";
    private static final String SELECT_ADDRESS_SQL = "SELECT * FROM address WHERE shopping_cart_id=? AND address_type=?";
    private static final String INSERT_DELIVERY_OPTION_SQL = "INSERT INTO delivery_option " +
            "(method, charge, min_days_required, max_days_required, shopping_cart_id) " +
            "VALUES (:method, :charge, :min_days_required, :max_days_required, :shopping_cart_id) " +
            "ON CONFLICT ON CONSTRAINT delivery_option_constraint " +
            "DO UPDATE SET method=EXCLUDED.method, charge=EXCLUDED.charge, min_days_required=EXCLUDED.min_days_required, max_days_required=EXCLUDED.max_days_required, last_update_time=now()";
    private static final String SELECT_DELIVERY_OPTION_SQL = "SELECT * FROM delivery_option WHERE shopping_cart_id=?";
    private static final String DELETE_SHOPPING_CART_BY_ID_SQL = "DELETE FROM shopping_cart where id = ?";
    private static final String DELETE_ADDRESS_BY_SESSION_ID_SQL = "DELETE FROM address where shopping_cart_id = ?";
    private static final String DELETE_DELIVERY_OPTION_BY_SESSION_ID_SQL = "DELETE FROM delivery_option where shopping_cart_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private DeliveryOptionMapper deliveryOptionMapper;

    @Override
    public UUID create() {
        final UUID uuid = UUID.randomUUID();
        jdbcTemplate.update(INSERT_SQL, uuid, null, null);
        return uuid;
    }

    @Override
    public UUID create(long customerId, String email) {
        final UUID uuid = UUID.randomUUID();
        jdbcTemplate.update(INSERT_SQL, uuid, customerId, email);
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
    public List<ShoppingCart> findByCustomerId(Long customerId) {
        return jdbcTemplate.query(SELECT_BY_CUSTOMER_ID_SQL, shoppingCartMapper, customerId);
    }

    @Override
    public int updateCustomerId(UUID cartUid, Long customerId) {
        return jdbcTemplate.update(UPDATE_CUSTOMER_ID, customerId, cartUid);
    }

    @Override
    public int updateEmail(UUID cartUid, String email) {
        return jdbcTemplate.update(UPDATE_EMAIL, email, cartUid);
    }

    @Override
    public int delete(long cartId) {
        jdbcTemplate.update(DELETE_ADDRESS_BY_SESSION_ID_SQL, cartId);
        jdbcTemplate.update(DELETE_DELIVERY_OPTION_BY_SESSION_ID_SQL, cartId);
        return jdbcTemplate.update(DELETE_SHOPPING_CART_BY_ID_SQL, cartId);
    }

    @Override
    public void addAddress(long cartId, Address address) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("address_type", address.getAddressType(), Types.VARCHAR);
        namedParameters.addValue("title", address.getTitle(), Types.VARCHAR);
        namedParameters.addValue("name", address.getName(), Types.VARCHAR);
        namedParameters.addValue("mobile", address.getMobile(), Types.VARCHAR);
        namedParameters.addValue("address_line_1", address.getAddressLine1(), Types.VARCHAR);
        namedParameters.addValue("address_line_2", address.getAddressLine2(), Types.VARCHAR);
        namedParameters.addValue("address_line_3", address.getAddressLine3(), Types.VARCHAR);
        namedParameters.addValue("city", address.getCity(), Types.VARCHAR);
        namedParameters.addValue("country", address.getCountry(), Types.VARCHAR);
        namedParameters.addValue("post_code", address.getPostcode(), Types.VARCHAR);
        namedParameters.addValue("shopping_cart_id", cartId, Types.INTEGER);
        namedParameterJdbcTemplate.update(INSERT_ADDRESS_SQL, namedParameters);
    }

    @Override
    public Optional<Address> findAddress(long cartId, String addressType) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_ADDRESS_SQL, addressMapper, cartId, addressType));
        } catch (Exception e) {
            return empty();
        }
    }

    @Override
    public void addDeliveryOption(long cartId, DeliveryOption deliveryOption) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("method", deliveryOption.getMethod(), Types.VARCHAR);
        namedParameters.addValue("charge", deliveryOption.getCharge(), Types.DOUBLE);
        namedParameters.addValue("min_days_required", deliveryOption.getMinDaysRequired(), Types.INTEGER);
        namedParameters.addValue("max_days_required", deliveryOption.getMaxDaysRequired(), Types.INTEGER);
        namedParameters.addValue("shopping_cart_id", cartId, Types.INTEGER);
        namedParameterJdbcTemplate.update(INSERT_DELIVERY_OPTION_SQL, namedParameters);
    }

    @Override
    public Optional<DeliveryOption> findDeliveryOption(long cartId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_DELIVERY_OPTION_SQL, deliveryOptionMapper, cartId));
        } catch (Exception e) {
            return empty();
        }
    }
}
