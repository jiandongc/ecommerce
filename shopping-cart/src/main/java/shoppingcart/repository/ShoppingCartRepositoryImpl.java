package shoppingcart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.Address;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.AddressMapper;
import shoppingcart.mapper.ShoppingCartMapper;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;

@Repository
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    private static final String INSERT_SQL = "INSERT INTO shopping_cart (cart_uid, customer_id) VALUES (?, ?)";
    private static final String SELECT_BY_UUID_SQL = "SELECT * FROM shopping_cart WHERE cart_uid = ?";
    private static final String SELECT_BY_CUSTOMER_ID_SQL = "SELECT * FROM shopping_cart WHERE customer_id = ?";
    private static final String UPDATE_CUSTOMER_ID = "UPDATE shopping_cart SET customer_id = ? WHERE cart_uid = ?";
    private static final String DELETE_BY_UUID_SQL = "DELETE FROM shopping_cart where cart_uid = ?";
    private static final String INSERT_ADDRESS_SQL = "INSERT INTO address " +
            "(address_type, title, first_name, last_name, mobile, address_line_1, address_line_2, address_line_3, city, country, post_code, shopping_cart_id) " +
            "VALUES (:address_type, :title, :first_name, :last_name, :mobile, :address_line_1, :address_line_2, :address_line_3, :city, :country, :post_code, :shopping_cart_id) " +
            "ON CONFLICT ON CONSTRAINT address_type_constraint " +
            "DO UPDATE SET title=EXCLUDED.title, first_name=EXCLUDED.first_name, last_name=EXCLUDED.last_name, mobile=EXCLUDED.mobile, address_line_1=EXCLUDED.address_line_1, " +
            "address_line_2=EXCLUDED.address_line_2, address_line_3=EXCLUDED.address_line_3, city=EXCLUDED.city, country=EXCLUDED.country, post_code=EXCLUDED.post_code ";
    private static final String SELECT_ADDRESS_SQL = "SELECT * FROM address WHERE shopping_cart_id=? AND address_type=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressMapper addressMapper;

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
    public List<ShoppingCart> findByCustomerId(Long customerId) {
        return jdbcTemplate.query(SELECT_BY_CUSTOMER_ID_SQL, shoppingCartMapper, customerId);
    }

    @Override
    public int updateCustomerId(UUID cartUid, Long customerId) {
        return jdbcTemplate.update(UPDATE_CUSTOMER_ID, customerId, cartUid);
    }

    @Override
    public int delete(UUID cartUid) {
        return jdbcTemplate.update(DELETE_BY_UUID_SQL, cartUid);
    }

    @Override
    public void addAddress(long cartId, Address address) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("address_type", address.getAddressType(), Types.VARCHAR);
        namedParameters.addValue("title", address.getTitle(), Types.VARCHAR);
        namedParameters.addValue("first_name", address.getFirstName(), Types.VARCHAR);
        namedParameters.addValue("last_name", address.getLastName(), Types.VARCHAR);
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
}
