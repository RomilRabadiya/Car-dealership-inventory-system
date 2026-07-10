package incubyte.tdd.BackendAPI.Security;

import incubyte.tdd.BackendAPI.Entity.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// Service responsible for JWT generation and validation
@Service
public class JwtServiceImpl implements JwtService {

    // Secret key used to sign and verify JWT tokens
    private static final String SECRET =
            "12345678901234567890123456789012";

    private long expirationInMillis = 3600000;

    public void setExpirationInMillis(long expirationInMillis) {
        this.expirationInMillis = expirationInMillis;
    }

    @Override
    public String generateToken(User user) {

        // Generate the signing key
        SecretKey key = getSigningKey();

        // Create and sign the JWT token
        return Jwts.builder()

                // Store the user's email as the subject
                .subject(user.getEmail())

                // Include the user's role as a custom claim
                .claim("role", user.getRole().name())

                // Set token creation time
                .issuedAt(new Date())

                // Set token expiration time
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expirationInMillis
                        )
                )

                // Sign the token
                .signWith(key)

                .compact();

    }

    @Override
    public String extractUsername(String token) {

        SecretKey key = getSigningKey();

        // Extract the subject (email) from the JWT token
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Generate the HMAC signing key from the secret
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );

    }

    @Override
    public boolean isTokenValid(
            String token,
            User user
    ) {

        try {
            // Validate username and ensure the token has not expired
            String username = extractUsername(token);

            return username.equals(user.getEmail())
                    && !isTokenExpired(token);
        } catch (io.jsonwebtoken.JwtException e) {
            return false;
        }

    }

    // Check whether the JWT token has expired
    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());

    }

    // Extract the expiration date from the JWT token
    private Date extractExpiration(String token) {

        SecretKey key = getSigningKey();

        return Jwts.parser()

                .verifyWith(key)

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .getExpiration();

    }

}