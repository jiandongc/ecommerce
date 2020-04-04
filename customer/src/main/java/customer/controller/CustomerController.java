package customer.controller;

import customer.data.PasswordResetRequest;
import customer.data.TokenRequest;
import customer.domain.*;
import customer.service.CustomerEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import customer.service.CustomerService;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerEmailService customerEmailService;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerEmailService customerEmailService) {
        this.customerService = customerService;
        this.customerEmailService = customerEmailService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = POST)
    public ResponseEntity save(@RequestBody Customer customer) {
        try {
            return new ResponseEntity<>(customerService.save(customer), HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = PUT)
    public ResponseEntity update(@RequestBody Customer customer) {
        try {
            final Customer updatedCustomer = customerService.update(customer);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "{id}/password", method = PUT)
    public ResponseEntity updatePassword(@PathVariable String id, @RequestBody String password) {
        final Customer updatedCustomer = customerService.updatePassword(Long.valueOf(id), password);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = GET)
    public Customer findByEmail(@RequestParam("email") String email) {
        return customerService.findByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method = GET)
    public Customer findById(@PathVariable String id) {
        return customerService.findById(Long.valueOf(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses", method = GET)
    public List<Address> findAddressesByCustomerId(@PathVariable String id) {
        return customerService.findAddressesByCustomerId(Long.valueOf(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = GET)
    public Address findAddressById(@PathVariable String addressId) {
        return customerService.findAddressById(Long.valueOf(addressId));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses", method = POST)
    public Address addAddress(@PathVariable String id, @RequestBody Address address) {
        return customerService.addAddress(Long.valueOf(id), address);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = PUT)
    public ResponseEntity updateAddress(@PathVariable String id, @PathVariable String addressId, @RequestBody Address address) {
        try {
            return new ResponseEntity<>(customerService.updateAddress(Long.valueOf(id), Long.valueOf(addressId), address), HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = RequestMethod.DELETE)
    public ResponseEntity removeAddress(@PathVariable String addressId) {
        final Address address = customerService.findAddressById(Long.valueOf(addressId));
        if (address != null) {
            customerService.removeAddress(Long.valueOf(addressId));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products", method = POST)
    public Product addProduct(@PathVariable String customerId, @RequestBody Product product) {
        return customerService.addProduct(Long.valueOf(customerId), product);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products", method = GET)
    public List<Product> getProducts(@PathVariable String customerId, @RequestParam("type") String type) {
        Customer customer = customerService.findById(Long.valueOf(customerId));
        List<Product> validProducts = customer.getValidProducts();
        return validProducts.stream()
                .filter(product -> product.getType().equals(Type.valueOf(type.toUpperCase())))
                .collect(toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products/{productId}", method = DELETE)
    public void removeProduct(@PathVariable String customerId, @PathVariable String productId) {
        customerService.removeProduct(Long.valueOf(customerId), Long.valueOf(productId));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products", method = DELETE)
    public void removeProduct(@PathVariable String customerId, @RequestParam("type") String type, @RequestParam("code") String productCode) {
        customerService.removeProductByTypeAndCode(Long.valueOf(customerId), type, productCode);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/tokens", method = POST)
    public void createToken(@RequestBody TokenRequest tokenRequest) {
        Customer customer = customerService.findByEmail(tokenRequest.getEmail());
        if (customer != null) {
            Token token = customerService.addToken(customer.getId(), tokenRequest.getType());
            customerEmailService.sendPasswordResetEmail(customer, token.getText());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/tokens/{text}", method = GET)
    public Token retrieveValidToken(@PathVariable String text) {
        return customerService.findValidToken(text);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/reset-password", method = POST)
    public void resetPasswordWithToken(@RequestBody PasswordResetRequest passwordResetRequest) {
        Token token = customerService.findValidToken(passwordResetRequest.getToken());
        if (token != null) {
            customerService.updatePassword(token, passwordResetRequest.getPassword());
        }
    }

}
