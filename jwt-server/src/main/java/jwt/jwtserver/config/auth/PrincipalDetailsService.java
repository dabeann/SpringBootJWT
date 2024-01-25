package jwt.jwtserver.config.auth;


import jwt.jwtserver.model.Users;
import jwt.jwtserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login -> 여기서 동작을 안한다!
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        Users user = userRepository.findByUsername(username);

        // session.setAttribute("loginUser", user);
        return new PrincipalDetails(user);
    }
}