package customer.service;

import customer.domain.Address;
import customer.domain.Customer;
import customer.domain.Product;
import customer.domain.Token;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
	Customer save(Customer customer);
	Customer update(Customer customer);
	Customer updatePassword(UUID customerUid, String password);
	void updatePassword(Token token, String password);
	Customer findByUid(UUID uuid);
	Customer findByEmail(String email);
	List<Address> findAddressesByCustomerUid(UUID uuid);
	Address findAddressByUid(UUID customerUid, UUID addressUid);
	Address addAddress(UUID customerUid, Address address);
	Address updateAddress(UUID customerUid, UUID addressUid, Address address);
	void removeAddress(UUID customerUid, UUID addressUid);
	Product addProduct(UUID customerUid, Product product);
	List<Product> findProductsByCustomerUid(UUID customerUid);
	void removeProduct(UUID customerUid, UUID productUid);
	void removeProductByTypeAndCode(UUID customerUid, String type, String productCode);
	Token addToken(Long customerId, Token.Type type);
	List<Token> findTokensByCustomerUid(UUID customerUid);
	Token findValidToken(String text);
}
