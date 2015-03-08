package customer.service;

import static org.mockito.Mockito.mock;

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
}
