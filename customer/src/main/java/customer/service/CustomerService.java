package customer.service;

import customer.domain.Customer;

public interface CustomerService {
	Customer save(Customer customer);
	Customer update(Customer customer);
	Customer findById(Long Id);
	Customer findByEmail(String email);
}
