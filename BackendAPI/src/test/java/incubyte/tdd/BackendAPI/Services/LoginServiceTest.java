package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Dto.Request.LoginRequest;
import incubyte.tdd.BackendAPI.Dto.Response.LoginResponse;
import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Security.JwtService;
import incubyte.tdd.BackendAPI.Services.impl.LoginServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    UserRepository repository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtService jwtService;

    @InjectMocks
    LoginServiceImpl service;

    @Test
    @DisplayName("TC-011: Should login successfully")
    void shouldLoginSuccessfully() {

        // Arrange
        LoginRequest request = new LoginRequest(
                "romil@gmail.com",
                "password123"
        );

        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .password("encryptedPassword")
                .role(Role.USER)
                .build();

        // mock authentication to do nothing
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        // Act
        LoginResponse response = service.login(request);

        // Assert
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("jwt-token", response.getToken())
        );

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(repository).findByEmail(request.getEmail());
        verify(jwtService).generateToken(user);
    }

    @Test
    @DisplayName("TC-012: Should throw exception when email does not exist (post-auth)")
    void shouldThrowExceptionWhenEmailDoesNotExist() {

        // Arrange
        LoginRequest request = new LoginRequest(
                "romil@gmail.com",
                "password123"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> service.login(request)
        );

        assertEquals("User not found.", exception.getMessage());

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("TC-013: Should throw exception for incorrect credentials")
    void shouldThrowExceptionForIncorrectCredentials() {

        // Arrange
        LoginRequest request = new LoginRequest(
                "romil@gmail.com",
                "wrongPassword"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> service.login(request)
        );

        assertEquals("Bad credentials", exception.getMessage());

        verify(repository, never()).findByEmail(any());
        verify(jwtService, never()).generateToken(any());
    }
}