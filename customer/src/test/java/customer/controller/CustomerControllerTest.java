package customer.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import customer.domain.Customer;

public class CustomerControllerTest extends AbstractControllerTest{

	private final String BASE_URL = "http://localhost:8081/customers";
	private final TestRestTemplate rest = new TestRestTemplate();

	@Test
	public void shouldSaveCustomer(){
		// Given
		this.setGuestToken();
		String customerJson = "{" +
				"\"name\":\"jiandong\"," +
				"\"title\":\"Mr\"," +
				"\"email\":\"jiandong.c@gmail.com\"," +
				"\"mobile\":\"07736473343\"," +
				"\"password\":\"1234qwer\"" +
				"}";
		
		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.POST, payload, Customer.class);
		
		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("jiandong"));
		assertThat(response.getBody().getTitle(), is("Mr"));
		assertThat(response.getBody().getEmail(), is("jiandong.c@gmail.com"));
		assertThat(response.getBody().getMobile(), is("07736473343"));
		assertThat(response.getBody().getPassword(), is(nullValue()));

		assertThat(customerRepository.findByEmail("jiandong.c@gmail.com").getPassword(), is("1234qwer"));
	}

	@Test
	public void shouldRejectRequestIfGuestTokenIsNotAvailable(){
		String customerJson = "{" +
			"\"name\":\"jiandong\"," +
			"\"title\":\"Mr\"," +
			"\"email\":\"jiandong.c@gmail.com\"," +
			"\"mobile\":\"07736473343\"," +
			"\"password\":\"1234qwer\"" +
			"}";

		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.POST, payload, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
	}

	@Test
	public void shouldUpdateCustomer(){
		// Given
		this.setUserToken();
		Customer customer = new Customer();
		customer.setName("mark");
		customer.setTitle(null);
		customer.setMobile("07736473343");
		customer.setEmail("mark@gmail.com");
		customer.setPassword("Password");
		customerRepository.save(customer);

		String customerJson = "{" +
				"\"id\":\"" + customer.getId() + "\"," +
				"\"name\":\"jiandong\"," +
				"\"title\":\"Mr\"," +
				"\"email\":\"jiandong.c@gmail.com\"," +
				"\"mobile\":\"07736473346\"" +
				"}";

		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.PUT, payload, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));

		final Customer updatedCustomer = customerRepository.findOne(customer.getId());
		assertThat(updatedCustomer.getName(), is("jiandong"));
		assertThat(updatedCustomer.getTitle(), is("Mr"));
		assertThat(updatedCustomer.getEmail(), is("jiandong.c@gmail.com"));
		assertThat(updatedCustomer.getMobile(), is("07736473346"));
		assertThat(updatedCustomer.getPassword(), is("Password"));
	}

	@Test
	public void shouldNotUpdateEmailToOneThatHasAlreadyBeenUsed(){
		// Given
		this.setUserToken();
		Customer mark = new Customer();
		mark.setName("mark");
		mark.setTitle(null);
		mark.setMobile("07736473343");
		mark.setEmail("mark@gmail.com");
		mark.setPassword("Password");
		customerRepository.save(mark);

		Customer lee = new Customer();
		lee.setName("mark");
		lee.setTitle(null);
		lee.setMobile("07736473343");
		lee.setEmail("lee@gmail.com");
		lee.setPassword("Password");
		customerRepository.save(lee);

		String customerJson = "{" +
				"\"id\":\"" + mark.getId() + "\"," +
				"\"name\":\"jiandong\"," +
				"\"title\":\"Mr\"," +
				"\"email\":\"lee@gmail.com\"," +
				"\"mobile\":\"07736473346\"" +
				"}";

		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.PUT, payload, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
	}

	@Test
	public void shouldRejectUpdateCustomerRequestWithGuestToken(){
		// Given
		this.setGuestToken();
		String customerJson = "{" +
				"\"id\":\"1\"," +
				"\"name\":\"jiandong\"," +
				"\"title\":\"Mr\"," +
				"\"email\":\"lee@gmail.com\"," +
				"\"mobile\":\"07736473346\"" +
				"}";

		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.PUT, payload, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
	}

	@Test
	public void shouldGetCustomerById(){
		// Given
		this.setUserToken();
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setTitle("Mr");
		customer.setMobile("07736473343");
		customer.setEmail("Email");
		customer.setPassword("Password");
		customerRepository.save(customer);
				
		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response =  rest.exchange(BASE_URL + "/" + customer.getId(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Name"));
		assertThat(response.getBody().getTitle(), is("Mr"));
		assertThat(response.getBody().getEmail(), is("Email"));
		assertThat(response.getBody().getMobile(), is("07736473343"));
		assertThat(response.getBody().getPassword(), is(nullValue()));
	}

	@Test
	public void shouldRejectGetCustomerByIdRequestWithGuestToken(){
		// Given
		this.setGuestToken();
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setTitle("Mr");
		customer.setMobile("07736473343");
		customer.setEmail("Email");
		customer.setPassword("Password");
		customerRepository.save(customer);

		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response =  rest.exchange(BASE_URL + "/" + customer.getId(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
	}
	
	@Test
	public void shouldGetCustomerByEmail(){
		// Given
		this.setUserToken();
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setTitle("Mr");
		customer.setMobile("07736473343");
		customer.setEmail("Email");
		customer.setPassword("Password");
		customerRepository.save(customer);
				
		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "?email=" + customer.getEmail(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Name"));
		assertThat(response.getBody().getTitle(), is("Mr"));
		assertThat(response.getBody().getEmail(), is("Email"));
		assertThat(response.getBody().getMobile(), is("07736473343"));
		assertThat(response.getBody().getPassword(), is(nullValue()));
	}

	@Test
	public void shouldRejectGetCustomerByEmailRequestWithGuestToken(){
		// Given
		this.setGuestToken();
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setTitle("Mr");
		customer.setMobile("07736473343");
		customer.setEmail("Email");
		customer.setPassword("Password");
		customerRepository.save(customer);

		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "?email=" + customer.getEmail(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
	}

	@Test
	public void shouldReturnConflictStatusCodeIfEmailAlreadyExists(){
		// Given
		Customer customer = new Customer();
		customer.setName("chen");
		customer.setEmail("jiandong.c@gmail.com");
		customer.setPassword("1234asdf");
		customerRepository.save(customer);

		this.setGuestToken();
		String customerJson = "{\"name\":\"jiandong\",\"email\":\"jiandong.c@gmail.com\",\"password\":\"1234qwer\"}";

		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.POST, payload, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
		assertThat(response.getBody(), is(nullValue()));
		assertThat(customerRepository.findByEmail("jiandong.c@gmail.com").getPassword(), is("1234asdf"));
	}
}
