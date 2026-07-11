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

        // Arrange

        String token = "valid-jwt";

        UserDetails userDetails = User.withUsername("romil@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.extractUsername(token))
                .thenReturn("romil@gmail.com");

        when(userDetailsService.loadUserByUsername("romil@gmail.com"))
                .thenReturn(userDetails);

        User user = User.builder()
                .email("romil@gmail.com")
                .role(Role.USER)
                .build();

        when(jwtService.isTokenValid(token, user))
                .thenReturn(true);

        // Act

        filter.doFilterInternal(
                request,
                response,
                filterChain);

        // Assert

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        assertNotNull(authentication);

        assertEquals(
                "romil@gmail.com",
                authentication.getName());

        verify(filterChain)
                .doFilter(request, response);
    }

}