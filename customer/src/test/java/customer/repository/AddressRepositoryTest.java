package customer.repository;

import customer.domain.Address;
import customer.domain.Customer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddressRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void shouldGetAddressesByCustomerId(){
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
        List<Address> addresses = addressRepository.findByCustomerId(savedCustomer.getId());
        assertThat(addresses.size(), is(2));
    }

}