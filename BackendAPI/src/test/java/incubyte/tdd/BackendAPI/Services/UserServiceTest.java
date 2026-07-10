package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;
import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Services.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



// Unit tests for UserServiceImpl using JUnit 5 and Mockito
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

        // Mock repository
        @Mock
        private UserRepository repository;

        // Mock password encoder
        @Mock
        private PasswordEncoder encoder;

        // Inject mocked dependencies
        @InjectMocks
        private UserServiceImpl service;

        @Test
        @DisplayName("TC-001: Should register a new user successfully")
        void shouldRegisterUserSuccessfully() {

                // Arrange: Create a dummy registration request
                RegisterRequest request = new RegisterRequest(
                        "Romil",
                        "romil@gmail.com",
                        "password123");

                // Mock email availability check
                when(repository.existsByEmail(request.getEmail()))
                        .thenReturn(false);

                // Mock password encryption
                when(encoder.encode(request.getPassword()))
                        .thenReturn("encryptedPassword");

                // Create a mock saved user
                User savedUser = new User();
                savedUser.setId(1L);
                savedUser.setName("Romil");
                savedUser.setEmail("romil@gmail.com");
                savedUser.setPassword("encryptedPassword");
                savedUser.setRole(Role.USER);

                // Mock repository save operation
                when(repository.save(any(User.class)))
                        .thenReturn(savedUser);

                // Act: Register the user
                UserResponse response = service.register(request);

                // Assert: Verify the returned user details
                assertNotNull(response);
                assertEquals("Romil", response.getName());
                assertEquals("romil@gmail.com", response.getEmail());

                // Verify that the user was saved to the repository
                verify(repository).save(any(User.class));
        }
}