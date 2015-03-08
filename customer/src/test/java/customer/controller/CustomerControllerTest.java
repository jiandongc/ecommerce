package customer.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import customer.domain.Customer;
import customer.repository.CustomerRepository;

public class CustomerControllerTest extends AbstractControllerTest{
	@Autowired
	private CustomerRepository customerRepository;

	private final String BASE_URL = "http://localhost:8081/customers/";
	private RestTemplate rest = new TestRestTemplate();
	
	@After
	public void before(){
		customerRepository.deleteAll();
	}
	
	@Test
	public void shouldSaveCustomer(){
		// Given
		Customer customer = new Customer("Name", "Email", "Password");
		
		// When
		final ResponseEntity<Customer> repsonse = rest.postForEntity(BASE_URL, customer, Customer.class);
		
		// Then
		assertThat(repsonse.getStatusCode(), is(HttpStatus.OK));
		assertThat(repsonse.getBody().getName(), is("Name"));
		assertThat(repsonse.getBody().getEmail(), is("Email"));
		assertThat(repsonse.getBody().getPassword(), is("Password"));
	}
	
}
