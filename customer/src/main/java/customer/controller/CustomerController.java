package customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import customer.domain.Customer;
import customer.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private CustomerService customerService;
	
	@Autowired
	public CustomerController(CustomerService customerService){
		this.customerService = customerService;
	}

	@PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
	@RequestMapping(method=RequestMethod.POST)
	public Customer save(@RequestBody Customer customer){
		return customerService.save(customer);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(method=RequestMethod.GET)
	public Customer findByEmail(@RequestParam("email") String email){
		return customerService.findByEmail(email);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    public Customer findById(@PathVariable long id) {
    	Customer customer = customerService.findById(id);
        return customer;
    }

}
