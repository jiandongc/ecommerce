package customer.repository;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import customer.domain.Customer;

public class CustomerRepositoryTest extends AbstractRepositoryTest{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void shouldSaveCustomer(){
		// Given
		Customer customer = new Customer("Name", "Email", "Password");
		
		// When
		customerRepository.save(customer);
		
		// Then
		Customer savedCustomer = customerRepository.findOne(customer.getId());
		MatcherAssert.assertThat(savedCustomer, Matchers.is(customer));
	
	}
}
