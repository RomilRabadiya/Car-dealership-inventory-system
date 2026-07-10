package incubyte.tdd.BackendAPI.Security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthorizationSecurityConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http)
                throws Exception {

                http
                        .csrf(AbstractHttpConfigurer::disable)

                        .authorizeHttpRequests(auth -> auth

                                .requestMatchers("/api/auth/**")
                                .permitAll()

                                .anyRequest()
                                .authenticated()

                        )

                        .exceptionHandling(exception ->

                                exception.authenticationEntryPoint(

                                        (request, response, authException) ->

                                                response.sendError(
                                                        HttpServletResponse.SC_UNAUTHORIZED,
                                                        "Unauthorized"
                                                )

                                )

                        );

                return http.build();
        }
}
