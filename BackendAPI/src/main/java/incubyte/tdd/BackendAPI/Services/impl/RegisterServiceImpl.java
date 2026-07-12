package incubyte.tdd.BackendAPI.Services.impl;

import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;
import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Exception.DuplicateEmailException;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Services.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import incubyte.tdd.BackendAPI.common.constants.ErrorMessages;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        validateRequestNotNull(request);
        validateEmailNotExists(request.getEmail());

        Role assignedRole = Role.USER;
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            assignedRole = Role.ADMIN;
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(assignedRole)
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
            throw new DuplicateEmailException(ErrorMessages.DUPLICATE_EMAIL);
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