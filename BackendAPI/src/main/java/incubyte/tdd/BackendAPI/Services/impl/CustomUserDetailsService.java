package incubyte.tdd.BackendAPI.Services.impl;

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