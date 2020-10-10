package authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final List<String> admins;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository,
                                 @Value("${admin.emails}") String adminEmails) {
        this.userRepository = userRepository;
        this.admins = Arrays.asList(adminEmails.split(";"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        final ApplicationUser applicationUser = userRepository.findCustomerByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        if (admins.contains(email)) {
            return new User(applicationUser.getUsername(), applicationUser.getPassword(), createAuthorityList("user", "admin"));
        } else {
            return new User(applicationUser.getUsername(), applicationUser.getPassword(), createAuthorityList("user"));
        }
    }

}
