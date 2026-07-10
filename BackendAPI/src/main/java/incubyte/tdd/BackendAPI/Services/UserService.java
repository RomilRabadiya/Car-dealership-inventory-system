package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);

}
