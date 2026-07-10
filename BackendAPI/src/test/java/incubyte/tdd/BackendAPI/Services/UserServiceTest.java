package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;
import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Exception.DuplicateEmailException;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Services.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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


        @Test
        @DisplayName("TC-002: Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {

                // Arrange: Create a dummy registration request
                RegisterRequest request = new RegisterRequest(
                        "Romil",
                        "romil@gmail.com",
                        "password123"
                );

                // Mock repository to indicate that the email already exists
                when(repository.existsByEmail(request.getEmail()))
                        .thenReturn(true);

                // Act & Assert: Verify that DuplicateEmailException is thrown
                DuplicateEmailException exception = assertThrows(
                        DuplicateEmailException.class,
                        () -> service.register(request)
                );

                // Verify the exception message
                assertEquals("Email already exists.", exception.getMessage());

                // Verify that no user is saved when registration fails
                verify(repository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("TC-003: Should encrypt password before saving user")
        void shouldEncryptPasswordBeforeSavingUser() {

                // Arrange: Create a dummy registration request
                RegisterRequest request = new RegisterRequest(
                        "Romil",
                        "romil@gmail.com",
                        "password123"
                );

                // Mock email availability check
                when(repository.existsByEmail(request.getEmail()))
                        .thenReturn(false);

                // Mock password encryption
                when(encoder.encode(request.getPassword()))
                        .thenReturn("encryptedPassword");

                // Create a mock saved user
                User savedUser = User.builder()
                        .id(1L)
                        .name("Romil")
                        .email("romil@gmail.com")
                        .password("encryptedPassword")
                        .role(Role.USER)
                        .build();

                // Mock repository save operation
                when(repository.save(any(User.class)))
                        .thenReturn(savedUser);

                // Act: Register the user
                service.register(request);

                // Capture the user object passed to the repository
                ArgumentCaptor<User> captor =
                        ArgumentCaptor.forClass(User.class);

                verify(repository).save(captor.capture());

                User capturedUser = captor.getValue();

                // Assert: Verify the password is encrypted before saving
                assertEquals(
                        "encryptedPassword",
                        capturedUser.getPassword()
                );

                // Assert: Verify the plain-text password is not stored
                assertNotEquals(
                        "password123",
                        capturedUser.getPassword()
                );
        }

        @Test
        @DisplayName("TC-004: Should assign USER role to newly registered user")
        void shouldAssignUserRoleByDefault() {

                // Arrange: Create a dummy registration request
                RegisterRequest request = new RegisterRequest(
                        "Romil",
                        "romil@gmail.com",
                        "password123"
                );

                // Mock email availability check
                when(repository.existsByEmail(request.getEmail()))
                        .thenReturn(false);

                // Mock password encryption
                when(encoder.encode(request.getPassword()))
                        .thenReturn("encryptedPassword");

                // Create a mock saved user with the default USER role
                User savedUser = User.builder()
                        .id(1L)
                        .name("Romil")
                        .email("romil@gmail.com")
                        .password("encryptedPassword")
                        .role(Role.USER)
                        .build();

                // Mock repository save operation
                when(repository.save(any(User.class)))
                        .thenReturn(savedUser);

                // Act: Register the user
                service.register(request);

                // Capture the User object passed to the repository
                ArgumentCaptor<User> captor =
                        ArgumentCaptor.forClass(User.class);

                verify(repository).save(captor.capture());

                User capturedUser = captor.getValue();

                // Assert: Verify that the default role assigned is USER
                assertEquals(Role.USER, capturedUser.getRole());
        }
}