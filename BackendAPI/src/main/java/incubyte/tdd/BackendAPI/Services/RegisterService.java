package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;

public interface RegisterService {
    UserResponse register(RegisterRequest request);

}
