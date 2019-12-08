package customer.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptHashServiceImpl implements HashService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String generateHash(String text) {
        return bCryptPasswordEncoder.encode(text);
    }

    @Override
    public boolean matches(String text, String hash) {
        return bCryptPasswordEncoder.matches(text, hash);
    }

}
