package incubyte.tdd.BackendAPI.Services.impl;

import incubyte.tdd.BackendAPI.Dto.Request.LoginRequest;
import incubyte.tdd.BackendAPI.Dto.Response.LoginResponse;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Exception.InvalidCredentialsException;
import incubyte.tdd.BackendAPI.Exception.UserNotFoundException;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Security.JwtService;
import incubyte.tdd.BackendAPI.Services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticate(request);

        User user = findUserByEmail(request.getEmail());

        String token = generateJwt(user);

        return new LoginResponse(token);
    }

    private void authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    private User findUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found.")
                );
    }

    private String generateJwt(User user) {
        return jwtService.generateToken(user);
    }
}
