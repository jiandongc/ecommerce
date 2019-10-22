package customer.service;

import customer.domain.Address;
import customer.domain.Customer;
import customer.domain.Product;

import java.util.List;

public interface CustomerService {
	Customer save(Customer customer);
	Customer update(Customer customer);
	Customer findById(Long Id);
	Customer findByEmail(String email);
	List<Address> findAddressesByCustomerId(Long customerId);
	Address findAddressById(Long addressId);
	Address addAddress(Long customerId, Address address);
	Address updateAddress(Long customerId, Long addressId, Address address);
	void removeAddress(Long addressId);
	Product addProduct(Long customerId, Product product);
	List<Product> findProductsByCustomerId(Long customerId);
}
