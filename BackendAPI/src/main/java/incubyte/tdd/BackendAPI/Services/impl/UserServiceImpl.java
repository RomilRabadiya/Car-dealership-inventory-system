package incubyte.tdd.BackendAPI.Services.impl;

import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;
import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Exception.DuplicateEmailException;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        validateRequestNotNull(request);
        validateEmailNotExists(request.getEmail());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = repository.save(user);
        return mapToUserResponse(savedUser);
    }

    private void validateRequestNotNull(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Register request cannot be null.");
        }
    }

    private void validateEmailNotExists(String email) {
        if (repository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists.");
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}