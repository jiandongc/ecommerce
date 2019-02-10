package customer.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import customer.domain.Address;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import customer.domain.Customer;

import java.util.List;

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

	@Test
	public void shouldGetAddressesByCustomerId(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");

		Address addressOne = new Address();
		addressOne.setTitle("Mr.");
		addressOne.setName("John");
		addressOne.setAddressLine1("2 Sally Lane");
		addressOne.setCity("Manchester");
		addressOne.setCountry("United Kingdom");
		addressOne.setPostcode("M1 2DD");
		addressOne.setDefaultAddress(true);
		customer.addAddress(addressOne);

		Address addressTwo = new Address();
		addressTwo.setTitle("Mr.");
		addressTwo.setName("John");
		addressTwo.setAddressLine1("17 London Road");
		addressTwo.setCity("London");
		addressTwo.setCountry("United Kingdom");
		addressTwo.setPostcode("BR1 7DE");
		addressTwo.setDefaultAddress(false);
		customer.addAddress(addressTwo);

		Customer savedCustomer = customerRepository.save(customer);

		// When
		this.setUserToken();
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<Address[]> response =  rest.exchange(BASE_URL + "/" + savedCustomer.getId() + "/addresses", HttpMethod.GET, httpEntity, Address[].class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
		assertThat(response.getBody()[0].getName(), is("John"));
		assertThat(response.getBody()[0].getAddressLine1(), is("2 Sally Lane"));
		assertThat(response.getBody()[0].getPostcode(), is("M1 2DD"));
		assertThat(response.getBody()[1].getName(), is("John"));
		assertThat(response.getBody()[1].getAddressLine1(), is("17 London Road"));
		assertThat(response.getBody()[1].getPostcode(), is("BR1 7DE"));
	}

	@Test
	public void shouldAddNewAddress(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");

		Address addressOne = new Address();
		addressOne.setTitle("Mr.");
		addressOne.setName("John");
		addressOne.setAddressLine1("2 Sally Lane");
		addressOne.setCity("Manchester");
		addressOne.setCountry("United Kingdom");
		addressOne.setPostcode("M1 2DD");
		addressOne.setDefaultAddress(true);
		customer.addAddress(addressOne);

		Customer savedCustomer = customerRepository.save(customer);

		// When (add new address as default)
		this.setUserToken();
		String addressJson = "{" +
				"\"title\":\"Mr.\"," +
				"\"name\":\"John\"," +
				"\"mobile\":null," +
				"\"addressLine1\":\"17 London Road\"," +
				"\"addressLine2\":null," +
				"\"addressLine3\":null," +
				"\"city\":\"London\"," +
				"\"country\":\"United Kingdom\"," +
				"\"postcode\":\"BR1 7DE\"," +
				"\"defaultAddress\":true" +
				"}";
		HttpEntity<String> payload = new HttpEntity<String>(addressJson, headers);
		ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getId() + "/addresses", HttpMethod.POST, payload, String.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		List<Address> addresses = addressRepository.findByCustomerId(savedCustomer.getId());
		assertThat(addresses.size(), is(2));
		Address firstAddress = addresses.stream().filter(address -> address.getPostcode().equals("M1 2DD")).findFirst().get();
		assertThat(firstAddress.isDefaultAddress(), is(false));
		Address newAddress = addresses.stream().filter(address -> address.getPostcode().equals("BR1 7DE")).findFirst().get();
		assertThat(newAddress.isDefaultAddress(), is(true));

		// When (add another new address as non default)
		addressJson = "{" +
				"\"title\":\"Mr.\"," +
				"\"name\":\"John\"," +
				"\"mobile\":null," +
				"\"addressLine1\":\"17 London Road\"," +
				"\"addressLine2\":null," +
				"\"addressLine3\":null," +
				"\"city\":\"London\"," +
				"\"country\":\"United Kingdom\"," +
				"\"postcode\":\"SE9 7TT\"," +
				"\"defaultAddress\":false" +
				"}";
		payload = new HttpEntity<String>(addressJson, headers);
		response = rest.exchange(BASE_URL + "/" + savedCustomer.getId() + "/addresses", HttpMethod.POST, payload, String.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		addresses = addressRepository.findByCustomerId(savedCustomer.getId());
		assertThat(addresses.size(), is(3));
		firstAddress = addresses.stream().filter(address -> address.getPostcode().equals("M1 2DD")).findFirst().get();
		assertThat(firstAddress.isDefaultAddress(), is(false));
		Address secondAddress = addresses.stream().filter(address -> address.getPostcode().equals("BR1 7DE")).findFirst().get();
		assertThat(secondAddress.isDefaultAddress(), is(true));
		newAddress = addresses.stream().filter(address -> address.getPostcode().equals("SE9 7TT")).findFirst().get();
		assertThat(newAddress.isDefaultAddress(), is(false));
	}

	@Test
	public void shouldUpdateAddress(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");

		Address addressOne = new Address();
		addressOne.setTitle("Mr.");
		addressOne.setName("John");
		addressOne.setAddressLine1("2 Sally Lane");
		addressOne.setCity("Manchester");
		addressOne.setCountry("United Kingdom");
		addressOne.setPostcode("M1 2DD");
		addressOne.setDefaultAddress(true);
		customer.addAddress(addressOne);

		Address addressTwo = new Address();
		addressTwo.setTitle("Mr.");
		addressTwo.setName("John");
		addressTwo.setAddressLine1("17 London Road");
		addressTwo.setCity("London");
		addressTwo.setCountry("United Kingdom");
		addressTwo.setPostcode("BR1 7DE");
		addressTwo.setDefaultAddress(false);
		customer.addAddress(addressTwo);

		Customer savedCustomer = customerRepository.save(customer);

		// When (update addressTwo)
		this.setUserToken();
		String addressJson = "{" +
				"\"title\":\"Mr.\"," +
				"\"name\":\"John_NEW\"," +
				"\"mobile\":\"00000000\"," +
				"\"addressLine1\":\"17 London Road_NEW\"," +
				"\"addressLine2\":\"17 London Road_NEW\"," +
				"\"addressLine3\":null," +
				"\"city\":\"London\"," +
				"\"country\":\"United Kingdom\"," +
				"\"postcode\":\"BR1 7BB\"," +
				"\"defaultAddress\":true" +
				"}";
		HttpEntity<String> payload = new HttpEntity<String>(addressJson, headers);
		ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getId() + "/addresses/" + addressTwo.getId(), HttpMethod.PUT, payload, String.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		addressOne = addressRepository.findOne(addressOne.getId());
		assertThat(addressOne.isDefaultAddress(), is(false));
		addressTwo = addressRepository.findOne(addressTwo.getId());
		assertThat(addressTwo.getTitle(), is("Mr."));
		assertThat(addressTwo.getName(), is("John_NEW"));
		assertThat(addressTwo.getMobile(), is("00000000"));
		assertThat(addressTwo.getAddressLine1(), is("17 London Road_NEW"));
		assertThat(addressTwo.getAddressLine2(), is("17 London Road_NEW"));
		assertThat(addressTwo.getAddressLine3(), is(nullValue()));
		assertThat(addressTwo.getCity(), is("London"));
		assertThat(addressTwo.getCountry(), is("United Kingdom"));
		assertThat(addressTwo.getPostcode(), is("BR1 7BB"));
		assertThat(addressTwo.isDefaultAddress(), is(true));
	}

	@Test
	public void shouldReceiveErrorCodeIfAddressIsNotFound(){
		// Given
		Customer customer = new Customer();
		customer.setName("Name");
		customer.setEmail("Email");
		customer.setPassword("Password");

		Customer savedCustomer = customerRepository.save(customer);

		// When (update addressTwo)
		this.setUserToken();
		String addressJson = "{" +
				"\"title\":\"Mr.\"," +
				"\"name\":\"John_NEW\"," +
				"\"mobile\":\"00000000\"," +
				"\"addressLine1\":\"17 London Road_NEW\"," +
				"\"addressLine2\":\"17 London Road_NEW\"," +
				"\"addressLine3\":null," +
				"\"city\":\"London\"," +
				"\"country\":\"United Kingdom\"," +
				"\"postcode\":\"BR1 7BB\"," +
				"\"defaultAddress\":true" +
				"}";
		HttpEntity<String> payload = new HttpEntity<String>(addressJson, headers);
		ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getId() + "/addresses/123", HttpMethod.PUT, payload, String.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
	}
}
