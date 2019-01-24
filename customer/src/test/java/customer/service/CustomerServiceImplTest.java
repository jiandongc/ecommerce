package customer.service;

import static java.lang.Long.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import customer.domain.Address;
import customer.repository.AddressRepository;
import org.junit.Test;

import customer.domain.Customer;
import customer.repository.CustomerRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private CustomerServiceImpl customerService;
	
	@Test
	public void shouldSaveCustomer(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");
		when(customerRepository.findByEmail("Email")).thenReturn(null);
		
		// When
		customerService.save(customer);
		
		// Then
		verify(customerRepository).save(customer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfEmailIsAlreadyUsedWhenSaving(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");
		when(customerRepository.findByEmail("Email")).thenReturn(new Customer());

		// When & Then
		customerService.save(customer);
	}

	@Test
	public void shouldUpdateCustomer(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setTitle("Mr");
		customer.setMobile("Mobile");

		Customer theCustomer = Mockito.mock(Customer.class);
		when(customerRepository.findOne(any(Long.class))).thenReturn(theCustomer);

		// When
		customerService.update(customer);

		// Then
		Mockito.verify(theCustomer).setEmail("Email");
		Mockito.verify(theCustomer).setName("Name");
		Mockito.verify(theCustomer).setTitle("Mr");
		Mockito.verify(theCustomer).setMobile("Mobile");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfEmailIsAlreadyUsedWhenUpdating(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("NewEmail");
		customer.setTitle("Mr");
		customer.setMobile("Mobile");

		Customer theCustomer = Mockito.mock(Customer.class);
		Mockito.when(theCustomer.getEmail()).thenReturn("Email");
		when(customerRepository.findByEmail("NewEmail")).thenReturn(new Customer());
		when(customerRepository.findOne(any(Long.class))).thenReturn(theCustomer);

		// When & Then
		customerService.update(customer);
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

	@Test
	public void shouldFindAddressesByCustomerId(){
		// When
		customerService.findAddressesByCustomerId(1L);
		// Then
		verify(addressRepository).findByCustomerId(1L);
	}

	@Test
	public void shouldAddNewAddressAndModifyDefaultAccordingly(){
		// Given
		Customer customer = new Customer();
		Address existingAddress = new Address();
		existingAddress.setDefaultAddress(true);
		customer.addAddress(existingAddress);
		Address newAddress = new Address();
		newAddress.setDefaultAddress(true);
		when(customerRepository.findOne(1L)).thenReturn(customer);

		// When
		customerService.addAddress(1L, newAddress);

		// Then
		assertThat(customer.getAddresses().get(0).isDefaultAddress(), is(false));
		assertThat(customer.getAddresses().get(1).isDefaultAddress(), is(true));
	}

}
