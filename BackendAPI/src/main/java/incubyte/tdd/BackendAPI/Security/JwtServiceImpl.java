package incubyte.tdd.BackendAPI.Security;

import incubyte.tdd.BackendAPI.Entity.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET =
            "12345678901234567890123456789012";

    @Override
    public String generateToken(User user) {

        SecretKey key = getSigningKey();

        return Jwts.builder()

                .subject(user.getEmail())

                .claim("role", user.getRole().name())

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )

                .signWith(key)

                .compact();

    }

    @Override
    public String extractUsername(String token) {

        SecretKey key = getSigningKey();

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }

}
