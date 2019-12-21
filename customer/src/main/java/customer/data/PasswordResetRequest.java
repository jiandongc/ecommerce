package customer.data;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String token;
    private String password;
}
