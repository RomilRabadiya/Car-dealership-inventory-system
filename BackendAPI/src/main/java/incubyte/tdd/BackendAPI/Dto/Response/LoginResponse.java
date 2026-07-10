package incubyte.tdd.BackendAPI.Dto.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;

}