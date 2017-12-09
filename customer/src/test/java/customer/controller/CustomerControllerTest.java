package customer.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import customer.domain.Customer;
import customer.repository.CustomerRepository;

public class CustomerControllerTest extends AbstractControllerTest{
	
	@Autowired
	private CustomerRepository customerRepository;

	private final String BASE_URL = "http://localhost:8081/customers";
	private final TestRestTemplate rest = new TestRestTemplate();
	private HttpHeaders headers = null;
	
	@Before
	public void before(){
		if (headers == null) {
			headers = new HttpHeaders();
		}
	}

	@After
	public void after(){
		customerRepository.deleteAll();
	}
	
	@Test
	public void shouldSaveCustomer(){
		// Given
		Customer customer = new Customer("Name", "Email", "Password");
		
		// When
		final HttpEntity<Customer> payload = new HttpEntity<Customer>(customer, headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL, HttpMethod.POST, payload, Customer.class);
		
		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Name"));
		assertThat(response.getBody().getEmail(), is("Email"));
		assertThat(response.getBody().getPassword(), is("Password"));
	}
	
	@Test
	public void shouldGetCustomerById(){
		// Given 
		Customer customer = new Customer("Name", "Email", "Password");
		customerRepository.save(customer);
				
		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response =  rest.exchange(BASE_URL + "/" + customer.getId(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is(customer));
	}
	
	@Test
	public void shouldGetCustomerByEmail(){
		// Given 
		Customer customer = new Customer("Name", "Email", "Password");
		customerRepository.save(customer);
				
		// When
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "?email=" + customer.getEmail(), HttpMethod.GET, httpEntity, Customer.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is(customer));
	}
	
}
