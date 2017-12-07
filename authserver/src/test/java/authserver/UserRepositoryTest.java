package authserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= NONE)
@Transactional
public class UserRepositoryTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserRepository userRepository;

	@Before
	public void setup(){
		jdbcTemplate.update("delete from customer");
	}
	
	@Test
	public void shouldFindCustomerByEmail(){
		// Given
		final String sql = "insert into customer (id, name, email, password) values (nextval('customer_seq'), 'chen', 'chen@gmail.com', '12345')";
		jdbcTemplate.execute(sql);

		// When
		Optional<ApplicationUser> applicationUserOptional = userRepository.findCustomerByEmail("chen@gmail.com");

		// Then
		assertThat(applicationUserOptional.isPresent(), is(true));
		ApplicationUser applicationUser = applicationUserOptional.get();
		assertThat(applicationUser.getUsername(), is("chen@gmail.com"));
		assertThat(applicationUser.getPassword(), is("12345"));
	}

	@Test
	public void shouldReturnEmptyOptionalIfCustomerNotFoundByEmail(){
		// Given & When
		Optional<ApplicationUser> applicationUserOptional = userRepository.findCustomerByEmail("non_found@gmail.com");

		// Then
		assertThat(applicationUserOptional.isPresent(), is(false));
	}
}
