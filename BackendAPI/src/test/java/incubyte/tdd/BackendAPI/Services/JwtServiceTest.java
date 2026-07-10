package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Security.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}