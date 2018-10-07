package customer.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import customer.domain.Customer;

public class CustomerRepositoryTest extends AbstractRepositoryTest{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void shouldSaveAndFindCustomer(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");
		
		// When
		customerRepository.save(customer);
		
		// Then
		Customer savedCustomer = customerRepository.findOne(customer.getId());
		assertThat(savedCustomer, is(customer));
	}
	
	@Test
	public void shouldFindCustomerByEmail(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");
		
		// When
		customerRepository.save(customer);
		
		// Then
		Customer savedCustomer = customerRepository.findByEmail("Email");
		assertThat(savedCustomer, is(customer));
	}
}
