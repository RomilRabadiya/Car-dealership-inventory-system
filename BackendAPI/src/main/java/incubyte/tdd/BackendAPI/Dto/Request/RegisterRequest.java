package incubyte.tdd.BackendAPI.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
}
