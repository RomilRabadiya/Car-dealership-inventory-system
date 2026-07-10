package incubyte.tdd.BackendAPI.Security;

import incubyte.tdd.BackendAPI.Entity.User;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String generateToken(User user) {
        // TODO: Implement JWT generation in a dedicated test
        return null;
    }
}
