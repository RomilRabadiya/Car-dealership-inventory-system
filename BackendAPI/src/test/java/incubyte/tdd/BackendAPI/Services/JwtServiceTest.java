package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Security.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {

        jwtService = new JwtServiceImpl();

    }

    @Test
    @DisplayName("TC-014: Should generate JWT token")
    void shouldGenerateJwtToken() {

        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        assertNotNull(token);

        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("TC-015: Should extract username from JWT")
    void shouldExtractUsernameFromJwt() {

        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals(
                "romil@gmail.com",
                username
        );
    }

}