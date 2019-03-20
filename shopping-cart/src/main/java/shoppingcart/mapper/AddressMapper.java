package shoppingcart.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import shoppingcart.domain.Address;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AddressMapper implements RowMapper<Address> {

    @Override
    public Address mapRow(ResultSet rs, int i) throws SQLException {
        final Address address = new Address();
        address.setId(rs.getLong("id"));
        address.setCartId(rs.getLong("shopping_cart_id"));
        address.setAddressType(rs.getString("address_type"));
        address.setTitle(rs.getString("title"));
        address.setFirstName(rs.getString("first_name"));
        address.setLastName(rs.getString("last_name"));
        address.setMobile(rs.getString("mobile"));
        address.setAddressLine1(rs.getString("address_line_1"));
        address.setAddressLine2(rs.getString("address_line_2"));
        address.setAddressLine3(rs.getString("address_line_3"));
        address.setCity(rs.getString("city"));
        address.setCountry(rs.getString("country"));
        address.setPostcode(rs.getString("post_code"));
        return address;
    }
}
