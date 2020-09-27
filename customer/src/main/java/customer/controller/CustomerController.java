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
import java.util.UUID;

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
    @RequestMapping(value = "{customerUid}/password", method = PUT)
    public ResponseEntity updatePassword(@PathVariable String customerUid, @RequestBody String password) {
        final Customer updatedCustomer = customerService.updatePassword(UUID.fromString(customerUid), password);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = GET)
    public Customer findByEmail(@RequestParam("email") String email) {
        return customerService.findByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}", method = GET)
    public Customer findByUid(@PathVariable String customerUid) {
        return customerService.findByUid(UUID.fromString(customerUid));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/addresses", method = GET)
    public List<Address> findAddressesByCustomerUid(@PathVariable String customerUid) {
        return customerService.findAddressesByCustomerUid(UUID.fromString(customerUid));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/addresses/{addressUid}", method = GET)
    public Address findAddressByUid(@PathVariable String customerUid, @PathVariable String addressUid) {
        return customerService.findAddressByUid(UUID.fromString(customerUid), UUID.fromString(addressUid));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/addresses", method = POST)
    public Address addAddress(@PathVariable String customerUid, @RequestBody Address address) {
        return customerService.addAddress(UUID.fromString(customerUid), address);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/addresses/{addressUid}", method = PUT)
    public ResponseEntity updateAddress(@PathVariable String customerUid, @PathVariable String addressUid, @RequestBody Address address) {
        try {
            return new ResponseEntity<>(customerService.updateAddress(UUID.fromString(customerUid), UUID.fromString(addressUid), address), HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/addresses/{addressUid}", method = RequestMethod.DELETE)
    public ResponseEntity removeAddress(@PathVariable String customerUid, @PathVariable String addressUid) {
        try {
            customerService.removeAddress(UUID.fromString(customerUid), UUID.fromString(addressUid));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/products", method = POST)
    public Product addProduct(@PathVariable String customerUid, @RequestBody Product product) {
        return customerService.addProduct(UUID.fromString(customerUid), product);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/products", method = GET)
    public List<Product> findProducts(@PathVariable UUID customerUid, @RequestParam("type") String type) {
        List<Product> validProducts = customerService.findProductsByCustomerUid(customerUid);
        return validProducts.stream()
                .filter(product -> product.getType().equals(Product.Type.valueOf(type.toUpperCase())))
                .collect(toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/products/{productUid}", method = DELETE)
    public void removeProduct(@PathVariable String customerUid, @PathVariable String productUid) {
        customerService.removeProduct(UUID.fromString(customerUid), UUID.fromString(productUid));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{customerUid}/products", method = DELETE)
    public void removeProduct(@PathVariable String customerUid, @RequestParam("type") String type, @RequestParam("code") String productCode) {
        customerService.removeProductByTypeAndCode(UUID.fromString(customerUid), type, productCode);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/products", method = POST)
    public void addProduct(@RequestBody Product product) {
        customerService.addProduct(product.getEmail(), product);
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
