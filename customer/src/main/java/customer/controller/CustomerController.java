package customer.controller;

import customer.data.PasswordResetRequest;
import customer.data.TokenRequest;
import customer.domain.*;
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

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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
    @RequestMapping(method = GET)
    public Customer findByEmail(@RequestParam("email") String email) {
        return customerService.findByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method = GET)
    public Customer findById(@PathVariable long id) {
        return customerService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses", method = GET)
    public List<Address> findAddressesByCustomerId(@PathVariable long id) {
        return customerService.findAddressesByCustomerId(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = GET)
    public Address findAddressById(@PathVariable long addressId) {
        return customerService.findAddressById(addressId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses", method = POST)
    public Address addAddress(@PathVariable long id, @RequestBody Address address) {
        return customerService.addAddress(id, address);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = PUT)
    public ResponseEntity updateAddress(@PathVariable long id, @PathVariable long addressId, @RequestBody Address address) {
        try {
            return new ResponseEntity<>(customerService.updateAddress(id, addressId, address), HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = RequestMethod.DELETE)
    public ResponseEntity removeAddress(@PathVariable long addressId) {
        final Address address = customerService.findAddressById(addressId);
        if (address != null) {
            customerService.removeAddress(addressId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products", method = POST)
    public Product addProduct(@PathVariable long customerId, @RequestBody Product product) {
        return customerService.addProduct(customerId, product);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products", method = GET)
    public List<Product> getProducts(@PathVariable long customerId, @RequestParam("type") String type) {
        Customer customer = customerService.findById(customerId);
        List<Product> validProducts = customer.getValidProducts();
        return validProducts.stream()
                .filter(product -> product.getType().equals(Type.valueOf(type.toUpperCase())))
                .collect(toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products/{productId}", method = DELETE)
    public void removeProduct(@PathVariable long customerId, @PathVariable long productId) {
        customerService.removeProduct(customerId, productId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerId}/products", method = DELETE)
    public void removeProduct(@PathVariable long customerId, @RequestParam("type") String type, @RequestParam("code") String productCode) {
        customerService.removeProductByTypeAndCode(customerId, type, productCode);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/tokens", method = POST)
    public void createToken(@RequestBody TokenRequest tokenRequest) {
        Customer customer = customerService.findByEmail(tokenRequest.getEmail());
        if (customer != null) {
            customerService.addToken(customer.getId(), tokenRequest.getType());
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
