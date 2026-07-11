package incubyte.tdd.BackendAPI.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpMethod;

// Spring Security configuration
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        // Custom JWT authentication filter
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                http

                                // Disable CSRF for stateless REST APIs
                                .csrf(AbstractHttpConfigurer::disable)

                                // Use JWT instead of HTTP sessions
                                .sessionManagement(session ->

                                session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS)

                                )

                                // Handle authentication exceptions (return 401 instead of 403)
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                                // Configure endpoint authorization rules
                                .authorizeHttpRequests(auth -> auth

                                                // Public endpoints
                                                .requestMatchers("/api/auth/**")
                                                .permitAll()

                                                // All other endpoints require authentication
                                                .anyRequest()
                                                .authenticated()

                                )

                                // Execute the JWT filter before Spring's authentication filter
                                .addFilterBefore(

                                                jwtAuthenticationFilter,

                                                UsernamePasswordAuthenticationFilter.class

                                );

                return http.build();
        }
}