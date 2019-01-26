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
	Address addAddress(Long customerId, Address address);
	Address updateAddress(Long customerId, Long addressId, Address address);
}
