package incubyte.tdd.BackendAPI.Services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

// Unit Testing of UserService using Mockito
public class UserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    @DisplayName("TC-001: Should register a new user successfully")
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request = new RegisterRequest(
                "Romil",
                "romil@gmail.com",
                "password123"
        );

        when(repository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(encoder.encode(request.getPassword()))
                .thenReturn("encryptedPassword");

        User savedUser = new User();

        savedUser.setId(1L);
        savedUser.setName("Romil");
        savedUser.setEmail("romil@gmail.com");
        savedUser.setPassword("encryptedPassword");
        savedUser.setRole(Role.USER);

        when(repository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponse response = service.register(request);

        assertNotNull(response);
        assertEquals("Romil", response.getName());
        assertEquals("romil@gmail.com", response.getEmail());

        verify(repository).save(any(User.class));
    }
}
