package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    @DisplayName("TC-019: Should load user by email")
    void shouldLoadUserByUsername() {

        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .password("encryptedPassword")
                .role(Role.USER)
                .build();

        when(repository.findByEmail("romil@gmail.com"))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails =
                service.loadUserByUsername("romil@gmail.com");

        // Assert
        assertAll(

                () -> assertNotNull(userDetails),

                () -> assertEquals(
                        "romil@gmail.com",
                        userDetails.getUsername()
                ),

                () -> assertEquals(
                        "encryptedPassword",
                        userDetails.getPassword()
                )

        );

    }

}