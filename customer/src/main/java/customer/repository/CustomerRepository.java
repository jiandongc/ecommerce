package customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import customer.domain.Customer;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
    Customer findByCustomerUid(UUID customerUid);
}
