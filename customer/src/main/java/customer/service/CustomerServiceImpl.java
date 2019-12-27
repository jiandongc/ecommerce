package customer.service;

import customer.domain.*;
import customer.repository.AddressRepository;
import customer.repository.ProductRepository;
import customer.repository.TokenRepository;
import customer.security.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import customer.repository.CustomerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private HashService hashService;

    @Override
    @Transactional
    public Customer save(Customer customer) {
        if (this.findByEmail(customer.getEmail()) != null) {
            throw new IllegalArgumentException("Email address is already used");
        }

        String rawPassword = customer.getPassword();
        customer.setPassword(hashService.generateHash(rawPassword));

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
    @Transactional
    public Customer updatePassword(long customerId, String password) {
        final Customer customer = customerRepository.findOne(customerId);
        customer.setPassword(hashService.generateHash(password));
        return customer;
    }

    @Override
    @Transactional
    public void updatePassword(Token token, String password) {
        Token savedToken = tokenRepository.findByText(token.getText());
        if (savedToken != null) {
            long customerId = savedToken.getCustomer().getId();
            final Customer customer = customerRepository.findOne(customerId);
            customer.setPassword(hashService.generateHash(password));
        }
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
    @Transactional(readOnly = true)
    public Address findAddressById(Long addressId) {
        return addressRepository.findOne(addressId);
    }

    @Override
    @Transactional
    public Address addAddress(Long customerId, Address newAddress) {
        final Customer customer = customerRepository.findOne(customerId);
        if (newAddress.isDefaultAddress()) {
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

    @Override
    @Transactional
    public void removeAddress(Long addressId) {
        addressRepository.delete(addressId);
    }

    @Override
    @Transactional
    public Product addProduct(Long customerId, Product product) {
        final Customer customer = customerRepository.findOne(customerId);
        final List<Product> validProducts = customer.getValidProducts();

        Optional<Product> existingProduct = validProducts.stream().filter(p -> p.hasSameProductCodeAndType(product)).findFirst();
        if (existingProduct.isPresent()) {
            existingProduct.get().setStartDate(LocalDate.now());
        } else {
            product.setStartDate(LocalDate.now());
            customer.addProduct(product);
        }

        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByCustomerId(Long customerId) {
        return productRepository.findByCustomerId(customerId);
    }

    @Override
    public void removeProduct(Long customerId, Long productId) {
        Product product = productRepository.findOne(productId);
        if (product != null && product.getCustomer().getId() == customerId) {
            productRepository.delete(productId);
        }
    }

    @Override
    public void removeProductByTypeAndCode(Long customerId, String type, String productCode) {
        final Type productType = Type.valueOf(type.toUpperCase());
        final List<Product> products = productRepository.findByCustomerId(customerId);
        products.stream().filter(product -> product.getType().equals(productType) && product.getProductCode().equals(productCode))
                .forEach(product -> productRepository.delete(product.getId()));

    }

    @Override
    @Transactional
    public Token addToken(Long customerId, Token.Type type) {
        final Customer customer = customerRepository.findOne(customerId);
        Token token = Token.builder()
                .type(type)
                .text(UUID.randomUUID().toString())
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusMinutes(30L))
                .build();
        customer.addToken(token);
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Token> findTokensByCustomerId(Long customerId) {
        return tokenRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Token findValidToken(String text) {
        Token token = tokenRepository.findByText(text);
        if (token != null && token.isValid()) {
            return token;
        }

        return null;
    }

}
