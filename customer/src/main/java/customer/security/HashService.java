package customer.security;

public interface HashService {

    String generateHash(String text);

    boolean matches(String text, String hash);
}
