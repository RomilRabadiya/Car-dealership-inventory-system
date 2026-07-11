package incubyte.tdd.BackendAPI.Integration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Security.JwtAuthenticationFilter;
import incubyte.tdd.BackendAPI.Security.JwtService;
import incubyte.tdd.BackendAPI.Services.impl.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Test
    @DisplayName("TC-024: Should authenticate request with valid JWT")
    void shouldAuthenticateRequestWithValidJwt()
            throws Exception {

        // Arrange: Create a valid JWT token
        String token = "valid-jwt";

        // Create mock user details
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("romil@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        // Mock the Authorization header
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        // Mock username extraction from the JWT
        when(jwtService.extractUsername(token))
                .thenReturn("romil@gmail.com");

        // Mock loading user details from the database
        when(userDetailsService.loadUserByUsername("romil@gmail.com"))
                .thenReturn(userDetails);

        // Create a user object for token validation
        User user = User.builder()
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        // Mock successful JWT validation
        when(jwtService.isTokenValid(org.mockito.ArgumentMatchers.eq(token), org.mockito.ArgumentMatchers.any(User.class)))
                .thenReturn(true);

        // Act: Execute the JWT authentication filter
        filter.doFilter(
                request,
                response,
                filterChain);

        // Retrieve the authenticated user from the Security Context
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // Assert: Verify the user is successfully authenticated
        assertNotNull(authentication);

        assertEquals(
                "romil@gmail.com",
                authentication.getName());

        // Verify the request continues through the filter chain
        verify(filterChain)
                .doFilter(request, response);
    }


}