package customer.service;

import customer.domain.Address;
import customer.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import customer.domain.Customer;
import customer.repository.CustomerRepository;

import java.util.List;

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

}
