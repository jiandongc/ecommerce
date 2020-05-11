package authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private static final String SQL = "select * from customer where email = ?";
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    public Optional<ApplicationUser> findCustomerByEmail(String email) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL, new Object[]{email}, userRowMapper));
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

}
