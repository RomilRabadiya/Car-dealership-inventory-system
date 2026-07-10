package incubyte.tdd.BackendAPI.Services.impl;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) {

        User user = repository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User not found."
                        )
                );

        return org.springframework.security.core.userdetails.User

                .withUsername(user.getEmail())

                .password(user.getPassword())

                .roles(user.getRole().name())

                .build();

    }

}