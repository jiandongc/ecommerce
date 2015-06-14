package authserver;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class UserRowMapper implements RowMapper{

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		final String email = rs.getString("EMAIL");
		final String password = rs.getString("PASSWORD");
		return new User(email, password, AuthorityUtils.NO_AUTHORITIES);
	}

}
