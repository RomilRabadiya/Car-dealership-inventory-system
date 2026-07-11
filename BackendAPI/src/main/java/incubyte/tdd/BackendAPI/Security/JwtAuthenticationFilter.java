package incubyte.tdd.BackendAPI.Security;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import incubyte.tdd.BackendAPI.Services.impl.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.springframework.security.core.userdetails.UserDetails;
import incubyte.tdd.BackendAPI.Entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        // Service for JWT generation and validation
        private final JwtService jwtService;

        // Service to load user details from the database
        private final CustomUserDetailsService userDetailsService;

        @Override
        protected void doFilterInternal(

                        HttpServletRequest request,

                        HttpServletResponse response,

                        FilterChain filterChain

        ) throws ServletException, IOException {

                // Retrieve the Authorization header
                String header = request.getHeader("Authorization");

                // Skip authentication if no Bearer token is present
                if (header == null || !header.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);
                        return;
                }

                // Extract the JWT token from the Authorization header
                String token = header.substring(7);

                // Extract the username (email) from the JWT token
                String username = jwtService.extractUsername(token);

                // Load user details using the extracted username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create a user object for token validation
                User user = User.builder()
                                .email(userDetails.getUsername())
                                .build();

                // Validate the JWT token
                if (jwtService.isTokenValid(token, user)) {

                        // Create an authenticated security token
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());

                        // Attach request details to the authentication object
                        auth.setDetails(
                                        new WebAuthenticationDetailsSource()
                                                        .buildDetails(request));

                        // Store the authenticated user in the Security Context
                        SecurityContextHolder.getContext()
                                        .setAuthentication(auth);
                }

                // Continue processing the request
                filterChain.doFilter(request, response);
        }
}