package authserver;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRowMapper implements RowMapper<ApplicationUser> {

    @Override
    public ApplicationUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        final String email = rs.getString("email");
        final String password = rs.getString("password");
        return new ApplicationUser(email, password);
    }

}
