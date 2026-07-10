package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Dto.Request.LoginRequest;
import incubyte.tdd.BackendAPI.Dto.Response.LoginResponse;
import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Exception.UserNotFoundException;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Security.JwtService;
import incubyte.tdd.BackendAPI.Services.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository repository;

    @Mock
    PasswordEncoder encoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthServiceImpl service;


    @Test
    @DisplayName("TC-011: Should login successfully")
    void shouldLoginSuccessfully() {

        // Arrange

        LoginRequest request =
                new LoginRequest(
                        "romil@gmail.com",
                        "password123"
                );

        User user =
                User.builder()

                        .id(1L)

                        .name("Romil")

                        .email("romil@gmail.com")

                        .password("encryptedPassword")

                        .role(Role.USER)

                        .build();

        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(
                encoder.matches(
                        request.getPassword(),
                        user.getPassword()
                )
        ).thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        // Act

        LoginResponse response =
                service.login(request);

        // Assert

        assertAll(

                () -> assertNotNull(response),

                () -> assertEquals(
                        "jwt-token",
                        response.getToken()
                )

        );

    }

    @Test
    @DisplayName("TC-012: Should throw exception when email does not exist")
    void shouldThrowExceptionWhenEmailDoesNotExist() {

        // Arrange
        LoginRequest request =
                new LoginRequest(
                        "romil@gmail.com",
                        "password123"
                );

        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception =
                assertThrows(
                        UserNotFoundException.class,
                        () -> service.login(request)
                );

        assertEquals(
                "User not found.",
                exception.getMessage()
        );

        verify(jwtService, never())
                .generateToken(any());

    }

}