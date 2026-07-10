package incubyte.tdd.BackendAPI.Controller;

import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;
import incubyte.tdd.BackendAPI.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Service layer for user-related operations
    private final UserService service;

    /**
     * Registers a new user.
     *
     * @param request User registration details.
     * @return User information with HTTP 201 (Created).
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(

            @RequestBody RegisterRequest request

    ) {

        // Delegate registration to the service layer and return the created user
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.register(request));

    }

}