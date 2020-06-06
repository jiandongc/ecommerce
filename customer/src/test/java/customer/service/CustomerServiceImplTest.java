package customer.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import customer.domain.Address;
import customer.security.HashService;
import org.junit.Test;

import customer.domain.Customer;
import customer.repository.CustomerRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private HashService hashService;

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
		customer.setCustomerUid(UUID.randomUUID());

		Customer theCustomer = Mockito.mock(Customer.class);
		when(customerRepository.findByCustomerUid(any(UUID.class))).thenReturn(theCustomer);

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
		when(customerRepository.findByCustomerUid(any(UUID.class))).thenReturn(theCustomer);

		// When & Then
		customerService.update(customer);
	}
	
	@Test
	public void shouldFindCustomerById(){
		// Given 
		final UUID uuid = UUID.randomUUID();
		// When
		customerService.findByUid(uuid);
		// Then
		verify(customerRepository).findByCustomerUid(uuid);
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
	public void shouldFindAddressesByCustomerUid(){
		// When
		UUID uuid = UUID.randomUUID();
		customerService.findAddressesByCustomerUid(uuid);
		// Then
		verify(customerRepository).findByCustomerUid(uuid);
	}

	@Test
	public void shouldFindAddressByUid(){
		// When
		UUID customerUid = UUID.randomUUID();
		UUID addressUid = UUID.randomUUID();
		customerService.findAddressByUid(customerUid, addressUid);
		// Then
		verify(customerRepository).findByCustomerUid(customerUid);
	}

	@Test
	public void shouldAddNewAddressAndModifyDefaultPropertyAccordingly(){
		// Given
		Customer customer = new Customer();
		Address existingAddress = new Address();
		existingAddress.setDefaultAddress(true);
		customer.addAddress(existingAddress);
		Address newAddress = new Address();
		newAddress.setDefaultAddress(true);
		UUID uuid = UUID.randomUUID();
		when(customerRepository.findByCustomerUid(uuid)).thenReturn(customer);

		// When
		customerService.addAddress(uuid, newAddress);

		// Then
		assertThat(customer.getAddresses().get(0).isDefaultAddress(), is(false));
		assertThat(customer.getAddresses().get(1).isDefaultAddress(), is(true));
	}

	@Test
	public void shouldRemoveAddress(){
		// When
		UUID customerUid = UUID.randomUUID();
		UUID addressUid = UUID.randomUUID();
		Customer customer = Customer.builder().customerUid(customerUid).build();
		customer.addAddress(Address.builder().addressUid(addressUid).build());
		when(customerRepository.findByCustomerUid(any(UUID.class))).thenReturn(customer);
		customerService.removeAddress(customerUid, addressUid);
		// Then
		verify(customerRepository).findByCustomerUid(customerUid);
		assertThat(customer.getAddresses().size(), is(0));
	}

}
