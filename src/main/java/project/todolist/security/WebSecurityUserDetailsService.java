package project.todolist.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.todolist.model.User;
import project.todolist.repository.UserRepository;

@Service("securityUserDetailsService")
@Slf4j
@RequiredArgsConstructor
public class WebSecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByEmail = userRepository.findUserByEmail(username).orElseThrow(() -> {
            log.error("WebSecurityUserDetailsService#loadUserByUsername: User with username=" + username + " was not found");
            throw new UsernameNotFoundException("User with username=" + username + " was not found");
        });
        return new WebSecurityUserDetails(userByEmail);
    }
}
