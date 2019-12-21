package customer.data;

import customer.domain.Token;
import lombok.Data;

@Data
public class TokenRequest {
    private String email;
    private Token.Type type;
}
