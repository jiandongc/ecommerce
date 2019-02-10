package customer.service;

import customer.domain.Address;
import customer.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import customer.domain.Customer;
import customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public Customer save(Customer customer) {
        if (this.findByEmail(customer.getEmail()) != null) {
            throw new IllegalArgumentException("Email address is already used");
        }

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        final Customer theCustomer = customerRepository.findOne(customer.getId());
        theCustomer.setName(customer.getName());
        theCustomer.setTitle(customer.getTitle());
        theCustomer.setMobile(customer.getMobile());

        if (!customer.getEmail().equals(theCustomer.getEmail())) {
            if (this.findByEmail(customer.getEmail()) == null) {
                theCustomer.setEmail(customer.getEmail());
            } else {
                throw new IllegalArgumentException("Email address is already used");
            }
        }

        return theCustomer;
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long Id) {
        return customerRepository.findOne(Id);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAddressesByCustomerId(Long customerId) {
        return addressRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Address addAddress(Long customerId, Address newAddress) {
        final Customer customer = customerRepository.findOne(customerId);
        if(newAddress.isDefaultAddress()){
            customer.getAddresses().forEach(address -> address.setDefaultAddress(false));
        }
        customer.addAddress(newAddress);
        return newAddress;
    }

    @Override
    @Transactional
    public Address updateAddress(Long customerId, Long addressId, Address address) {
        final Customer customer = customerRepository.findOne(customerId);
        final List<Address> addresses = customer.getAddresses();
        final Optional<Address> savedAddress = addresses.stream().filter(a -> a.getId() == addressId).findFirst();

        savedAddress.ifPresent(a -> {
            a.setTitle(address.getTitle());
            a.setName(address.getName());
            a.setMobile(address.getMobile());
            a.setAddressLine1(address.getAddressLine1());
            a.setAddressLine2(address.getAddressLine2());
            a.setAddressLine3(address.getAddressLine3());
            a.setCity(address.getCity());
            a.setCountry(address.getCountry());
            a.setPostcode(address.getPostcode());
            a.setDefaultAddress(address.isDefaultAddress());
        });

        savedAddress.orElseThrow(() -> new IllegalArgumentException("Address not found"));

        if (address.isDefaultAddress()) {
            addresses.stream().filter(a -> a.getId() != addressId).forEach(a -> a.setDefaultAddress(false));
        }

        return savedAddress.get();
    }

}
