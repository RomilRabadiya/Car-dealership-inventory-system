package incubyte.tdd.BackendAPI.Security;

import incubyte.tdd.BackendAPI.Entity.User;

public interface JwtService {
    String generateToken(User user);

    String extractUsername(String token);

    boolean isTokenValid(String token, User user);

}
