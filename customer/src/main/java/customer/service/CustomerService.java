package customer.service;

import customer.domain.Address;
import customer.domain.Customer;

import java.util.List;

public interface CustomerService {
	Customer save(Customer customer);
	Customer update(Customer customer);
	Customer findById(Long Id);
	Customer findByEmail(String email);
	List<Address> findAddressesByCustomerId(Long customerId);
}
