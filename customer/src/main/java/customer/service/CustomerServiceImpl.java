package customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import customer.domain.Customer;
import customer.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{

	private CustomerRepository customerRepository;
	
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository){
		this.customerRepository = customerRepository;
	}
	
	@Override
	@Transactional
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findById(Long Id) {
		return customerRepository.findOne(Id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Customer findByEmail(String email){
		return customerRepository.findByEmail(email);
	}

}
