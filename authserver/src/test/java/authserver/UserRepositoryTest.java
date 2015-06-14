package authserver;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class UserRepositoryTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void shouldFindCustomerByEmail(){
		// Given
		String sql = "INSERT INTO CUSTOMER (ID, NAME, EMAIL, PASSWORD) VALUES (nextval('CUSTOMER_SEQ'), 'CHEN', 'CHEN@GMAIL.COM', '12345')";
		jdbcTemplate.execute(sql);
		
		// When
		User actualUser = userRepository.findCustomerByEmail("CHEN@GMAIL.COM");
		
		// Then
		MatcherAssert.assertThat(actualUser.getUsername(), Matchers.is("CHEN@GMAIL.COM"));
		MatcherAssert.assertThat(actualUser.getPassword(), Matchers.is("12345"));
		
	}
	

	
}
