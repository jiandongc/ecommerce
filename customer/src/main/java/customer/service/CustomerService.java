package customer.service;

import customer.domain.Customer;

public interface CustomerService {
	public Customer save(Customer customer);
	public Customer findById(Long Id);	
	public Customer findByEmail(String email);
}
