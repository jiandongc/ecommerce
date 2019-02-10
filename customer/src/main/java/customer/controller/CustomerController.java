package customer.controller;

import customer.domain.Address;
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

import customer.domain.Customer;
import customer.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody Customer customer) {
        try {
            return new ResponseEntity<>(customerService.save(customer), HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody Customer customer) {
        try {
            final Customer updatedCustomer = customerService.update(customer);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public Customer findByEmail(@RequestParam("email") String email) {
        return customerService.findByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Customer findById(@PathVariable long id) {
        return customerService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.GET)
    public List<Address> findAddressesByCustomerId(@PathVariable long id){
        return customerService.findAddressesByCustomerId(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = RequestMethod.GET)
    public Address findAddressById(@PathVariable long addressId){
        return customerService.findAddressById(addressId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.POST)
    public Address addAddress(@PathVariable long id, @RequestBody Address address) {
        return customerService.addAddress(id, address);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/addresses/{addressId}", method = RequestMethod.PUT)
    public ResponseEntity updateAddress(@PathVariable long id, @PathVariable long addressId, @RequestBody Address address){
        try {
            return new ResponseEntity<>(customerService.updateAddress(id, addressId, address), HttpStatus.OK);
        } catch (IllegalArgumentException exe) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
