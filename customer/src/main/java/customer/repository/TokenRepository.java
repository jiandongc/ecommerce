package customer.repository;

import customer.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findByCustomerId(long customerId);

    Token findByText(String text);

}
