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

    // Initialize the JWT service before each test
    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        jwtService.setExpirationInMillis(3600000);
        org.springframework.test.util.ReflectionTestUtils.setField(jwtService, "secretKey", "12345678901234567890123456789012");
    }

    @Test
    @DisplayName("TC-014: Should generate JWT token")
    void shouldGenerateJwtToken() {

        // Arrange: Create a dummy user
        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        // Act: Generate a JWT token
        String token = jwtService.generateToken(user);

        // Assert: Verify the generated token is not null or empty
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("TC-015: Should extract username from JWT")
    void shouldExtractUsernameFromJwt() {

        // Arrange: Create a dummy user and generate a JWT token
        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        // Act: Extract the username (email) from the JWT token
        String username = jwtService.extractUsername(token);

        // Assert: Verify the extracted username matches the user's email
        assertEquals(
                "romil@gmail.com",
                username
        );
    }

    @Test
    @DisplayName("TC-016: Should validate JWT token")
    void shouldValidateJwtToken() {

        // Arrange: Create a dummy user and generate a JWT token
        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        // Act: Validate the generated JWT token
        boolean valid = jwtService.isTokenValid(token, user);

        // Assert: Verify the token is valid for the given user
        assertTrue(valid);
    }


    @Test
    @DisplayName("TC-017: Should reject expired JWT")
    void shouldRejectExpiredJwt() throws InterruptedException {

        // Arrange
        jwtService.setExpirationInMillis(1);

        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        Thread.sleep(5);

        // Act
        boolean valid = jwtService.isTokenValid(token, user);

        // Assert
        assertFalse(valid);
    }


    @Test
    @DisplayName("TC-018: Should reject malformed JWT")
    void shouldRejectMalformedJwt() {

        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        String invalidToken = "this-is-not-a-valid-jwt";

        // Act
        boolean valid = jwtService.isTokenValid(
                invalidToken,
                user
        );

        // Assert
        assertFalse(valid);
    }
}