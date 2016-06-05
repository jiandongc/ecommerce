package authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User findCustomerByEmail(String email) {
		final String SQL = "SELECT * FROM CUSTOMER WHERE EMAIL = ?";

		return (User) jdbcTemplate.queryForObject(SQL, new Object[] { email },
				new UserRowMapper());
	}

}
