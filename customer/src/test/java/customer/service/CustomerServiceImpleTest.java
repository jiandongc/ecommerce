package customer.service;

import static java.lang.Long.valueOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import customer.domain.Customer;
import customer.repository.CustomerRepository;


public class CustomerServiceImpleTest {
	
	private CustomerRepository customerRepository = mock(CustomerRepository.class);
	private CustomerService customerService = new CustomerServiceImpl(customerRepository);
	
	@Test
	public void shouldSaveCustomer(){
		// Given
		Customer customer = new Customer("Name", "Email", "Password");
		
		// When
		customerService.save(customer);
		
		// Then
		Mockito.verify(customerRepository).save(customer);
	}
	
	@Test
	public void shouldFindCustomerById(){
		// Given 
		final long id = valueOf(2);
		// When
		customerService.findById(id);
		// Then
		verify(customerRepository).findOne(id);
	}
	
	@Test
	public void shouldFindCustomerByEmail(){
		// Given 
		final String email = "email";
		// When
		customerService.findByEmail(email);
		// Then
		verify(customerRepository).findByEmail(email);
	}

}
