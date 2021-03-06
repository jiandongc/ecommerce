package customer.controller;

import static customer.domain.Token.Type.PASSWORD_RESET;
import static customer.domain.Product.Type.FAVOURITE;
import static customer.domain.Product.Type.NOTIFY_IN_STOCK;
import static java.time.LocalDate.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.*;

import customer.domain.Address;
import customer.domain.Product;
import customer.domain.Token;
import customer.security.HashService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import customer.domain.Customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CustomerControllerTest extends AbstractControllerTest {

    private final String BASE_URL = "http://localhost:8081/customers";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private HashService hashService;

    @Test
    public void shouldSaveCustomer() {
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
        final HttpEntity<String> payload = new HttpEntity<>(customerJson, headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL, POST, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getName(), is("jiandong"));
        assertThat(response.getBody().getTitle(), is("Mr"));
        assertThat(response.getBody().getEmail(), is("jiandong.c@gmail.com"));
        assertThat(response.getBody().getMobile(), is("07736473343"));
        assertThat(response.getBody().getPassword(), is(nullValue()));

        String hash = customerRepository.findByEmail("jiandong.c@gmail.com").getPassword();
        assertThat(hashService.matches("1234qwer", hash), is(true));
    }

    @Test
    public void shouldRejectRequestIfGuestTokenIsNotAvailable() {
        String customerJson = "{" +
                "\"name\":\"jiandong\"," +
                "\"title\":\"Mr\"," +
                "\"email\":\"jiandong.c@gmail.com\"," +
                "\"mobile\":\"07736473343\"," +
                "\"password\":\"1234qwer\"" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerJson, headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL, POST, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldRejectRequestIfTokenIsExpired() {
        this.setExpiredToken();
        String customerJson = "{" +
                "\"name\":\"jiandong\"," +
                "\"title\":\"Mr\"," +
                "\"email\":\"jiandong.c@gmail.com\"," +
                "\"mobile\":\"07736473343\"," +
                "\"password\":\"1234qwer\"" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerJson, headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL, POST, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldReturnConflictStatusCodeIfEmailAlreadyExists() {
        // Given
        Customer customer = new Customer();
        customer.setName("chen");
        customer.setEmail("jiandong.c@gmail.com");
        customer.setPassword("1234asdf");
        customer.setCustomerUid(UUID.randomUUID());
        customerRepository.save(customer);

        this.setGuestToken();
        String customerJson = "{\"name\":\"jiandong\",\"email\":\"jiandong.c@gmail.com\",\"password\":\"1234qwer\"}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerJson, headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL, POST, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
        assertThat(response.getBody(), is(nullValue()));
        assertThat(customerRepository.findByEmail("jiandong.c@gmail.com").getPassword(), is("1234asdf"));
    }

    @Test
    public void shouldUpdateCustomer() {
        // Given
        this.setUserToken();
        Customer customer = new Customer();
        customer.setName("mark");
        customer.setTitle(null);
        customer.setMobile("07736473343");
        customer.setEmail("mark@gmail.com");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customerRepository.save(customer);

        String customerJson = "{" +
                "\"id\":\"" + customer.getCustomerUid().toString() + "\"," +
                "\"name\":\"jiandong\"," +
                "\"title\":\"Mr\"," +
                "\"email\":\"jiandong.c@gmail.com\"," +
                "\"mobile\":\"07736473346\"" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerJson, headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL, PUT, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        final Customer updatedCustomer = customerRepository.findByCustomerUid(customer.getCustomerUid());
        assertThat(updatedCustomer.getName(), is("jiandong"));
        assertThat(updatedCustomer.getTitle(), is("Mr"));
        assertThat(updatedCustomer.getEmail(), is("jiandong.c@gmail.com"));
        assertThat(updatedCustomer.getMobile(), is("07736473346"));
        assertThat(updatedCustomer.getPassword(), is("Password"));
        assertThat(updatedCustomer.getCustomerUid(), is(customer.getCustomerUid()));
    }

    @Test
    public void shouldNotUpdateEmailToOneThatHasAlreadyBeenUsed() {
        // Given
        this.setUserToken();
        Customer mark = new Customer();
        mark.setName("mark");
        mark.setTitle(null);
        mark.setMobile("07736473343");
        mark.setEmail("mark@gmail.com");
        mark.setPassword("Password");
        mark.setCustomerUid(UUID.randomUUID());
        customerRepository.save(mark);

        Customer lee = new Customer();
        lee.setName("mark");
        lee.setTitle(null);
        lee.setMobile("07736473343");
        lee.setEmail("lee@gmail.com");
        lee.setPassword("Password");
        lee.setCustomerUid(UUID.randomUUID());
        customerRepository.save(lee);

        String customerJson = "{" +
                "\"id\":\"" + mark.getCustomerUid().toString() + "\"," +
                "\"name\":\"jiandong\"," +
                "\"title\":\"Mr\"," +
                "\"email\":\"lee@gmail.com\"," +
                "\"mobile\":\"07736473346\"" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerJson, headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL, PUT, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    public void shouldRejectUpdateCustomerRequestWithGuestToken() {
        // Given
        this.setGuestToken();
        String customerJson = "{" +
                "\"id\":\"" + UUID.randomUUID().toString() + "\"," +
                "\"name\":\"jiandong\"," +
                "\"title\":\"Mr\"," +
                "\"email\":\"lee@gmail.com\"," +
                "\"mobile\":\"07736473346\"" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerJson, headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL, PUT, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldGetCustomerByUid() {
        // Given
        this.setUserToken();
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setTitle("Mr");
        customer.setMobile("07736473343");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customerRepository.save(customer);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "/" + customer.getCustomerUid().toString(), GET, httpEntity, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getName(), is("Name"));
        assertThat(response.getBody().getTitle(), is("Mr"));
        assertThat(response.getBody().getEmail(), is("Email"));
        assertThat(response.getBody().getMobile(), is("07736473343"));
        assertThat(response.getBody().getPassword(), is(nullValue()));
    }

    @Test
    public void shouldRejectGetCustomerByIdRequestWithGuestToken() {
        // Given
        this.setGuestToken();
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setTitle("Mr");
        customer.setMobile("07736473343");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customerRepository.save(customer);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "/" + customer.getCustomerUid().toString(), GET, httpEntity, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldGetCustomerByEmail() {
        // Given
        this.setUserToken();
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setTitle("Mr");
        customer.setMobile("07736473343");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customerRepository.save(customer);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "?email=" + customer.getEmail(), GET, httpEntity, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getName(), is("Name"));
        assertThat(response.getBody().getTitle(), is("Mr"));
        assertThat(response.getBody().getEmail(), is("Email"));
        assertThat(response.getBody().getMobile(), is("07736473343"));
        assertThat(response.getBody().getPassword(), is(nullValue()));
    }

    @Test
    public void shouldRejectGetCustomerByEmailRequestWithGuestToken() {
        // Given
        this.setGuestToken();
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setTitle("Mr");
        customer.setMobile("07736473343");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customerRepository.save(customer);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "?email=" + customer.getEmail(), GET, httpEntity, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldGetAddressesByCustomerUid() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());

        Address addressOne = new Address();
        addressOne.setTitle("Mr.");
        addressOne.setName("John");
        addressOne.setAddressLine1("2 Sally Lane");
        addressOne.setCity("Manchester");
        addressOne.setCountry("United Kingdom");
        addressOne.setPostcode("M1 2DD");
        addressOne.setDefaultAddress(true);
        addressOne.setAddressUid(UUID.randomUUID());
        customer.addAddress(addressOne);

        Address addressTwo = new Address();
        addressTwo.setTitle("Mr.");
        addressTwo.setName("John");
        addressTwo.setAddressLine1("17 London Road");
        addressTwo.setCity("London");
        addressTwo.setCountry("United Kingdom");
        addressTwo.setPostcode("BR1 7DE");
        addressTwo.setDefaultAddress(false);
        addressTwo.setAddressUid(UUID.randomUUID());
        customer.addAddress(addressTwo);

        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setUserToken();
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Address[]> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid().toString() + "/addresses", GET, httpEntity, Address[].class);

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
    public void shouldGetAddressesByAddressUid() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());

        Address address = new Address();
        address.setTitle("Mr.");
        address.setName("John");
        address.setAddressLine1("2 Sally Lane");
        address.setCity("Manchester");
        address.setCountry("United Kingdom");
        address.setPostcode("M1 2DD");
        address.setDefaultAddress(true);
        address.setAddressUid(UUID.randomUUID());
        customer.addAddress(address);

        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setUserToken();
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<Address> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/addresses/" + address.getAddressUid(), GET, httpEntity, Address.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getName(), is("John"));
        assertThat(response.getBody().getAddressLine1(), is("2 Sally Lane"));
        assertThat(response.getBody().getPostcode(), is("M1 2DD"));
    }

    @Test
    public void shouldAddNewAddress() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());

        Address addressOne = new Address();
        addressOne.setTitle("Mr.");
        addressOne.setName("John");
        addressOne.setAddressLine1("2 Sally Lane");
        addressOne.setCity("Manchester");
        addressOne.setCountry("United Kingdom");
        addressOne.setPostcode("M1 2DD");
        addressOne.setDefaultAddress(true);
        addressOne.setAddressUid(UUID.randomUUID());
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
        HttpEntity<String> payload = new HttpEntity<>(addressJson, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/addresses", POST, payload, String.class);

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
        payload = new HttpEntity<>(addressJson, headers);
        response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/addresses", POST, payload, String.class);

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
    public void shouldUpdateAddress() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());

        Address addressOne = new Address();
        addressOne.setTitle("Mr.");
        addressOne.setName("John");
        addressOne.setAddressLine1("2 Sally Lane");
        addressOne.setCity("Manchester");
        addressOne.setCountry("United Kingdom");
        addressOne.setPostcode("M1 2DD");
        addressOne.setDefaultAddress(true);
        addressOne.setAddressUid(UUID.randomUUID());
        customer.addAddress(addressOne);

        Address addressTwo = new Address();
        addressTwo.setTitle("Mr.");
        addressTwo.setName("John");
        addressTwo.setAddressLine1("17 London Road");
        addressTwo.setCity("London");
        addressTwo.setCountry("United Kingdom");
        addressTwo.setPostcode("BR1 7DE");
        addressTwo.setDefaultAddress(false);
        addressTwo.setAddressUid(UUID.randomUUID());
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
        HttpEntity<String> payload = new HttpEntity<>(addressJson, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/addresses/" + addressTwo.getAddressUid(), PUT, payload, String.class);

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
    public void shouldReceiveErrorCodeIfAddressIsNotFound() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());

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
        HttpEntity<String> payload = new HttpEntity<>(addressJson, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/addresses/" + UUID.randomUUID(), PUT, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldRemoveAddress() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());

        Address address = new Address();
        address.setTitle("Mr.");
        address.setName("John");
        address.setAddressLine1("2 Sally Lane");
        address.setCity("Manchester");
        address.setCountry("United Kingdom");
        address.setPostcode("M1 2DD");
        address.setDefaultAddress(true);
        address.setAddressUid(UUID.randomUUID());
        customer.addAddress(address);

        customerRepository.save(customer);

        // When
        this.setUserToken();
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = rest.exchange(BASE_URL + "/" + customer.getCustomerUid() + "/addresses/" + address.getAddressUid(), DELETE, httpEntity, Object.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(addressRepository.findOne(address.getId()), is(nullValue()));
    }

    @Test
    public void shouldReturn404IfTheAddressToRemoveIsNotFound() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customerRepository.save(customer);

        // When
        this.setUserToken();
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = rest.exchange(BASE_URL + "/" + customer.getCustomerUid() + "/addresses/" + UUID.randomUUID(), DELETE, httpEntity, Object.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldUpdateCustomerPassword() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());

        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setUserToken();

        // When
        final HttpEntity<String> payload = new HttpEntity<>("Password321", headers);
        final ResponseEntity<Customer> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/password", PUT, payload, Customer.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        Customer updatedCustomer = customerService.findByEmail("john.smith@gmail.com");
        assertThat(updatedCustomer.getPassword(), is(notNullValue()));
        assertThat(updatedCustomer.getPassword(), is(not("Password123")));
    }

    @Test
    public void shouldAddProduct() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());

        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setUserToken();
        String addressJson = "{" +
                "\"productCode\":\"11L\"," +
                "\"type\":\"FAVOURITE\"" +
                "}";
        HttpEntity<String> payload = new HttpEntity<>(addressJson, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/products", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Product> products = customerService.findProductsByCustomerUid(savedCustomer.getCustomerUid());
        assertThat(products.size(), is(1));
        assertThat(products.get(0).getProductCode(), is("11L"));
        assertThat(products.get(0).getType(), is(FAVOURITE));
        assertThat(products.get(0).getEmail(), is("Email"));
        assertThat(products.get(0).getStartDate(), is(now()));
    }

    @Test
    public void shouldAddTheSameProduct() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customer.addProduct(Product.builder().productCode("11L").type(FAVOURITE).startDate(now().minusDays(10L)).productUid(UUID.randomUUID()).build());

        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setUserToken();
        String addressJson = "{" +
                "\"productCode\":\"11L\"," +
                "\"type\":\"FAVOURITE\"" +
                "}";
        HttpEntity<String> payload = new HttpEntity<>(addressJson, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/products", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Product> products = customerService.findProductsByCustomerUid(savedCustomer.getCustomerUid());
        assertThat(products.size(), is(1));
        assertThat(products.get(0).getProductCode(), is("11L"));
        assertThat(products.get(0).getType(), is(FAVOURITE));
        assertThat(products.get(0).getStartDate(), is(now()));
    }

    @Test
    public void shouldAddSameProductWithEmail() {
        // Given
        Product product = new Product();
        product.setProductCode("11L");
        product.setEmail("lee@gmail.com");
        product.setStartDate(LocalDate.now().minusDays(10));
        product.setType(Product.Type.NOTIFY_IN_STOCK);
        product.setProductUid(UUID.randomUUID());
        productRepository.save(product);

        this.setGuestToken();
        String addressJson = "{" +
                "\"productCode\":\"11L\"," +
                "\"email\":\"lee@gmail.com\"," +
                "\"type\":\"NOTIFY_IN_STOCK\"" +
                "}";
        HttpEntity<String> payload = new HttpEntity<>(addressJson, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/products", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Product> products = productRepository.findAll();
        assertThat(products.size(), is(1));
        assertThat(products.get(0).getProductCode(), is("11L"));
        assertThat(products.get(0).getType(), is(NOTIFY_IN_STOCK));
        assertThat(products.get(0).getEmail(), is("lee@gmail.com"));
        assertThat(products.get(0).getStartDate(), is(now()));
    }

    @Test
    public void shouldAddProductWithEmail() {
        // Given
        this.setGuestToken();
        String addressJson = "{" +
                "\"productCode\":\"11L\"," +
                "\"email\":\"lee@gmail.com\"," +
                "\"type\":\"NOTIFY_IN_STOCK\"" +
                "}";
        HttpEntity<String> payload = new HttpEntity<>(addressJson, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/products", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Product> products = productRepository.findAll();
        assertThat(products.size(), is(1));
        assertThat(products.get(0).getProductCode(), is("11L"));
        assertThat(products.get(0).getType(), is(NOTIFY_IN_STOCK));
        assertThat(products.get(0).getEmail(), is("lee@gmail.com"));
        assertThat(products.get(0).getStartDate(), is(now()));
    }

    @Test
    public void shouldFindProductsByCustomerIdAndType() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customer.addProduct(Product.builder().productCode("10L").type(FAVOURITE).startDate(now().minusDays(9L)).productUid(UUID.randomUUID()).build());
        customer.addProduct(Product.builder().productCode("11L").type(FAVOURITE).startDate(now().minusDays(10L)).endDate(now()).productUid(UUID.randomUUID()).build());
        customer.addProduct(Product.builder().productCode("12L").type(NOTIFY_IN_STOCK).startDate(now().minusDays(10L)).productUid(UUID.randomUUID()).build());

        Customer savedCustomer = customerRepository.save(customer);

        // When & Then
        this.setUserToken();
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Product[]> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/products?type=favourite", GET, httpEntity, Product[].class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().length, is(2));
        assertThat(response.getBody()[0].getId(), is(notNullValue()));
        assertThat(response.getBody()[0].getProductCode(), is("10L"));
        assertThat(response.getBody()[0].getType(), is(FAVOURITE));
        assertThat(response.getBody()[1].getProductCode(), is("11L"));
        assertThat(response.getBody()[1].getType(), is(FAVOURITE));

        // When & Then
        response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/products?type=notify_in_stock", GET, httpEntity, Product[].class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().length, is(1));
        assertThat(response.getBody()[0].getProductCode(), is("12L"));
        assertThat(response.getBody()[0].getType(), is(NOTIFY_IN_STOCK));
    }

    @Test
    public void shouldReturnValidProducts() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customer.addProduct(Product.builder().productCode("10L").type(FAVOURITE).startDate(now().minusDays(9L)).endDate(now().minusDays(1L)).productUid(UUID.randomUUID()).build());
        customer.addProduct(Product.builder().productCode("11L").type(FAVOURITE).startDate(now().minusDays(10L)).productUid(UUID.randomUUID()).build());

        Customer savedCustomer = customerRepository.save(customer);

        // When & Then
        this.setUserToken();
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Product[]> response = rest.exchange(BASE_URL + "/" + savedCustomer.getCustomerUid() + "/products?type=favourite", GET, httpEntity, Product[].class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().length, is(1));
        assertThat(response.getBody()[0].getProductCode(), is("11L"));
        assertThat(response.getBody()[0].getType(), is(FAVOURITE));
    }

    @Test
    public void shouldRemoveProduct() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customer.addProduct(Product.builder().productCode("abc").type(FAVOURITE).startDate(now()).productUid(UUID.randomUUID()).build());
        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setUserToken();
        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = rest.exchange(BASE_URL + "/" + customer.getCustomerUid() + "/products/" + customer.getValidProducts().get(0).getProductUid(), DELETE, httpEntity, Object.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Product> products = customerService.findProductsByCustomerUid(savedCustomer.getCustomerUid());
        assertThat(products.size(), is(0));
    }

    @Test
    public void shouldRemoveProductByTypeAndProductCode() {
        // Given
        Customer customer = new Customer();
        customer.setName("Name");
        customer.setEmail("Email");
        customer.setPassword("Password");
        customer.setCustomerUid(UUID.randomUUID());
        customer.addProduct(Product.builder().productCode("abc").type(FAVOURITE).startDate(now()).productUid(UUID.randomUUID()).build());
        customer.addProduct(Product.builder().productCode("def").type(FAVOURITE).startDate(now()).productUid(UUID.randomUUID()).build());

        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setUserToken();

        final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = rest.exchange(BASE_URL + "/" + customer.getCustomerUid() + "/products?type=favourite&code=abc", DELETE, httpEntity, Object.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Product> savedProducts = customerService.findProductsByCustomerUid(savedCustomer.getCustomerUid());
        assertThat(savedProducts.size(), is(1));
        assertThat(savedProducts.get(0).getProductCode(), is("def"));

        response = rest.exchange(BASE_URL + "/" + customer.getCustomerUid() + "/products?type=favourite&code=def", DELETE, httpEntity, Object.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        savedProducts = customerService.findProductsByCustomerUid(savedCustomer.getCustomerUid());
        assertThat(savedProducts.size(), is(0));
    }

    @Test
    public void shouldAddPasswordResetToken() {
        // Given
        Customer customer = new Customer();
        customer.setName("John");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());
        Customer savedCustomer = customerRepository.save(customer);

        // When
        this.setGuestToken();
        String tokenRequest = "{" +
                "\"email\":\"john.smith@gmail.com\"," +
                "\"type\":\"PASSWORD_RESET\"" +
                "}";
        HttpEntity<String> payload = new HttpEntity<>(tokenRequest, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/tokens", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<Token> tokens = customerService.findTokensByCustomerUid(savedCustomer.getCustomerUid());
        assertThat(tokens.size(), is(1));
        assertThat(tokens.get(0).getType(), is(PASSWORD_RESET));
        assertThat(tokens.get(0).getText(), is(notNullValue()));
        assertThat(tokens.get(0).getStartDateTime(), is(notNullValue()));
        assertThat(tokens.get(0).getEndDateTime(), is(notNullValue()));
    }

    @Test
    public void shouldNotThrowExceptionIfEmailIsNotFound() {
        // When
        this.setGuestToken();
        String tokenRequest = "{" +
                "\"email\":\"abc@gmail.com\"," +
                "\"type\":\"PASSWORD_RESET\"" +
                "}";
        HttpEntity<String> payload = new HttpEntity<>(tokenRequest, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/tokens", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void shouldReturnTokenIfValid() {
        // Given
        Customer customer = new Customer();
        customer.setName("John");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());
        String tokenText = UUID.randomUUID().toString();
        customer.addToken(Token.builder()
                .startDateTime(LocalDateTime.now().minusMinutes(30))
                .endDateTime(LocalDateTime.now().plusMinutes(30))
                .text(tokenText)
                .type(PASSWORD_RESET)
                .tokenUid(UUID.randomUUID())
                .build()
        );

        customerRepository.save(customer);

        // When & Then
        this.setGuestToken();
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Token> response = rest.exchange(BASE_URL + "/tokens/" + tokenText, GET, httpEntity, Token.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getType(), is(PASSWORD_RESET));
        assertThat(response.getBody().getText(), is(tokenText));
    }

    @Test
    public void shouldNotReturnTokenIfExpired() {
        // Given
        Customer customer = new Customer();
        customer.setName("John");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());
        String tokenText = UUID.randomUUID().toString();
        customer.addToken(Token.builder()
                .startDateTime(LocalDateTime.now().minusMinutes(30))
                .endDateTime(LocalDateTime.now().minusMinutes(10))
                .text(tokenText)
                .type(PASSWORD_RESET)
                .tokenUid(UUID.randomUUID())
                .build()
        );

        customerRepository.save(customer);

        // When & Then
        this.setGuestToken();
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Token> response = rest.exchange(BASE_URL + "/tokens/" + tokenText, GET, httpEntity, Token.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(nullValue()));
    }

    @Test
    public void shouldNotReturnTokenIfTextIsInvalid() {
        // Given
        Customer customer = new Customer();
        customer.setName("John");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());
        String tokenText = UUID.randomUUID().toString();
        customer.addToken(Token.builder()
                .startDateTime(LocalDateTime.now().minusMinutes(30))
                .endDateTime(LocalDateTime.now().plusMinutes(10))
                .text(tokenText)
                .type(PASSWORD_RESET)
                .tokenUid(UUID.randomUUID())
                .build()
        );

        customerRepository.save(customer);

        // When & Then
        this.setGuestToken();
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Token> response = rest.exchange(BASE_URL + "/tokens/invalid-token-text", GET, httpEntity, Token.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(nullValue()));
    }

    @Test
    public void shouldResetPasswordWithToken() {
        // Given
        Customer customer = new Customer();
        customer.setName("John");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());
        String tokenText = UUID.randomUUID().toString();
        customer.addToken(Token.builder()
                .startDateTime(LocalDateTime.now().minusMinutes(30))
                .endDateTime(LocalDateTime.now().plusMinutes(10))
                .text(tokenText)
                .type(PASSWORD_RESET)
                .tokenUid(UUID.randomUUID())
                .build()
        );

        customerRepository.save(customer);

        // When
        this.setGuestToken();
        String tokenRequest = String.format("{" +
                "\"token\":\"%s\"," +
                "\"password\":\"Password321\"" +
                "}", tokenText);
        HttpEntity<String> payload = new HttpEntity<>(tokenRequest, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/reset-password", POST, payload, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        // Then
        Customer updatedCustomer = customerService.findByEmail("john.smith@gmail.com");
        assertThat(updatedCustomer.getPassword(), is(notNullValue()));
        assertThat(updatedCustomer.getPassword(), is(not("Password123")));
    }

    @Test
    public void shouldNotThrowExceptionIfPasswordRestTokenIsNotFound() {
        // Given
        Customer customer = new Customer();
        customer.setName("John");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());
        String tokenText = UUID.randomUUID().toString();
        customer.addToken(Token.builder()
                .startDateTime(LocalDateTime.now().minusMinutes(30))
                .endDateTime(LocalDateTime.now().plusMinutes(10))
                .text(tokenText)
                .type(PASSWORD_RESET)
                .tokenUid(UUID.randomUUID())
                .build()
        );

        customerRepository.save(customer);

        // When
        this.setGuestToken();
        String tokenRequest = String.format("{" +
                "\"token\":\"%s\"," +
                "\"password\":\"Password321\"" +
                "}", "invalid-token-text");
        HttpEntity<String> payload = new HttpEntity<>(tokenRequest, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/reset-password", POST, payload, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        // Then
        Customer updatedCustomer = customerService.findByEmail("john.smith@gmail.com");
        assertThat(updatedCustomer.getPassword(), is(notNullValue()));
        assertThat(updatedCustomer.getPassword(), is("Password123"));
    }

    @Test
    public void shouldNotThrowExceptionIfPasswordRestTokenIsExpired() {
        // Given
        Customer customer = new Customer();
        customer.setName("John");
        customer.setEmail("john.smith@gmail.com");
        customer.setPassword("Password123");
        customer.setCustomerUid(UUID.randomUUID());
        String tokenText = UUID.randomUUID().toString();
        customer.addToken(Token.builder()
                .startDateTime(LocalDateTime.now().minusMinutes(30))
                .endDateTime(LocalDateTime.now().minusMinutes(10))
                .text(tokenText)
                .type(PASSWORD_RESET)
                .tokenUid(UUID.randomUUID())
                .build()
        );

        customerRepository.save(customer);

        // When
        this.setGuestToken();
        String tokenRequest = String.format("{" +
                "\"token\":\"%s\"," +
                "\"password\":\"Password321\"" +
                "}", tokenText);
        HttpEntity<String> payload = new HttpEntity<>(tokenRequest, headers);
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/reset-password", POST, payload, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        // Then
        Customer updatedCustomer = customerService.findByEmail("john.smith@gmail.com");
        assertThat(updatedCustomer.getPassword(), is(notNullValue()));
        assertThat(updatedCustomer.getPassword(), is("Password123"));
    }

}
