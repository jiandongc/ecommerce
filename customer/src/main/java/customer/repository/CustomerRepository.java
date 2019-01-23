package customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import customer.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}
