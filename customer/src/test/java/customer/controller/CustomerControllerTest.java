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
		String customerJson = "{\"name\":\"jiandong\",\"email\":\"jiandong.c@gmail.com\",\"password\":\"1234qwer\"}";
		
		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.POST, payload, Customer.class);
		
		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("jiandong"));
		assertThat(response.getBody().getEmail(), is("jiandong.c@gmail.com"));
		assertThat(response.getBody().getPassword(), is(nullValue()));

		assertThat(customerRepository.findByEmail("jiandong.c@gmail.com").getPassword(), is("1234qwer"));

	}

	@Test
	public void shouldRejectRequestIfGuestTokenIsNotAvailable(){
		String customerJson = "{\"name\":\"jiandong\",\"email\":\"jiandong.c@gmail.com\",\"password\":\"1234qwer\"}";

		// When
		final HttpEntity<String> payload = new HttpEntity<String>(customerJson, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.POST, payload, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
	}
	
	@Test
	public void shouldGetCustomerById(){
		// Given
		this.setUserToken();
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");
		customerRepository.save(customer);
				
		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response =  rest.exchange(BASE_URL + "/" + customer.getId(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Name"));
		assertThat(response.getBody().getEmail(), is("Email"));
		assertThat(response.getBody().getPassword(), is(nullValue()));
	}

	@Test
	public void shouldRejectGetCustomerByIdRequestWithGuestToken(){
		// Given
		this.setGuestToken();
		Customer customer = new Customer();
		customer.setName("Name");
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
		customer.setEmail("Email");
		customer.setPassword("Password");
		customerRepository.save(customer);
				
		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "?email=" + customer.getEmail(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Name"));
		assertThat(response.getBody().getEmail(), is("Email"));
		assertThat(response.getBody().getPassword(), is(nullValue()));
	}

	@Test
	public void shouldRejectGetCustomerByEmailRequestWithGuestToken(){
		// Given
		this.setGuestToken();
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");
		customerRepository.save(customer);

		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "?email=" + customer.getEmail(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
	}

}
