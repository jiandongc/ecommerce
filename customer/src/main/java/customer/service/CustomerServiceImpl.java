package customer.service;

import customer.domain.*;
import customer.repository.TokenRepository;
import customer.security.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import customer.repository.CustomerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

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
        customer.setCustomerUid(UUID.randomUUID());

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        final Customer theCustomer = customerRepository.findByCustomerUid(customer.getCustomerUid());
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
    public Customer updatePassword(UUID customerUid, String password) {
        final Customer customer = customerRepository.findByCustomerUid(customerUid);
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
    public Customer findByUid(UUID uuid) {
        return customerRepository.findByCustomerUid(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAddressesByCustomerUid(UUID uuid) {
        Customer customer = customerRepository.findByCustomerUid(uuid);
        if (customer != null) {
            return customer.getAddresses();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Address findAddressByUid(UUID customerUid, UUID addressUid) {
        Customer customer = customerRepository.findByCustomerUid(customerUid);
        if (customer != null) {
            return customer.getAddresses().stream()
                    .filter(address -> address.getAddressUid().equals(addressUid))
                    .findFirst()
                    .orElse(null);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Address addAddress(UUID customerUid, Address newAddress) {
        Customer customer = customerRepository.findByCustomerUid(customerUid);
        if (newAddress.isDefaultAddress()) {
            customer.getAddresses().forEach(address -> address.setDefaultAddress(false));
        }
        newAddress.setAddressUid(UUID.randomUUID());
        customer.addAddress(newAddress);
        return newAddress;
    }

    @Override
    @Transactional
    public Address updateAddress(UUID customerUid, UUID addressUid, Address address) {
        final Customer customer = customerRepository.findByCustomerUid(customerUid);
        final List<Address> addresses = customer.getAddresses();
        final Address savedAddress = addresses.stream().filter(a -> addressUid.equals(a.getAddressUid())).findFirst().orElseThrow(() -> new IllegalArgumentException("Address not found"));
        savedAddress.setTitle(address.getTitle());
        savedAddress.setName(address.getName());
        savedAddress.setMobile(address.getMobile());
        savedAddress.setAddressLine1(address.getAddressLine1());
        savedAddress.setAddressLine2(address.getAddressLine2());
        savedAddress.setAddressLine3(address.getAddressLine3());
        savedAddress.setCity(address.getCity());
        savedAddress.setCountry(address.getCountry());
        savedAddress.setPostcode(address.getPostcode());
        savedAddress.setDefaultAddress(address.isDefaultAddress());

        if (address.isDefaultAddress()) {
            addresses.stream().filter(a -> !addressUid.equals(a.getAddressUid())).forEach(a -> a.setDefaultAddress(false));
        }

        return savedAddress;
    }

    @Override
    @Transactional
    public void removeAddress(UUID customerUid, UUID addressUid) {
        final Customer customer = customerRepository.findByCustomerUid(customerUid);
        final List<Address> addresses = customer.getAddresses();
        final Address savedAddress = addresses.stream().filter(a -> addressUid.equals(a.getAddressUid())).findFirst().orElseThrow(() -> new IllegalArgumentException("Address not found"));
        customer.removeAddress(savedAddress);
    }

    @Override
    @Transactional
    public Product addProduct(UUID customerUid, Product product) {
        final Customer customer = customerRepository.findByCustomerUid(customerUid);
        final List<Product> validProducts = customer.getValidProducts();

        Optional<Product> existingProduct = validProducts.stream().filter(p -> p.hasSameProductCodeAndType(product)).findFirst();
        if (existingProduct.isPresent()) {
            existingProduct.get().setStartDate(LocalDate.now());
        } else {
            product.setStartDate(LocalDate.now());
            product.setProductUid(UUID.randomUUID());
            customer.addProduct(product);
        }

        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByCustomerUid(UUID customerUid) {
        Customer customer = customerRepository.findByCustomerUid(customerUid);
        if (customer != null) {
            return customer.getValidProducts();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public void removeProduct(UUID customerUid, UUID productUid) {
        final Customer customer = customerRepository.findByCustomerUid(customerUid);
        final List<Product> products = customer.getValidProducts();
        final Product savedProduct = products.stream().filter(a -> productUid.equals(a.getProductUid())).findFirst().orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        customer.removeProduct(savedProduct);
    }

    @Override
    @Transactional
    public void removeProductByTypeAndCode(UUID customerUid, String type, String productCode) {
        final Product.Type productType = Product.Type.valueOf(type.toUpperCase());
        final Customer customer = customerRepository.findByCustomerUid(customerUid);
        final List<Product> products = customer.getValidProducts();
        products.stream().filter(product -> product.getType().equals(productType) && product.getProductCode().equals(productCode)).forEach(customer::removeProduct);
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
                .tokenUid(UUID.randomUUID())
                .build();
        customer.addToken(token);
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Token> findTokensByCustomerUid(UUID customerUid) {
        Customer customer = customerRepository.findByCustomerUid(customerUid);
        if (customer != null) {
            return customer.getValidTokens();
        } else {
            return Collections.emptyList();
        }
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
