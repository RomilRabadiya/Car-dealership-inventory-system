package incubyte.tdd.BackendAPI.Controller;

import incubyte.tdd.BackendAPI.Services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // TODO: Add /login endpoint here

}
