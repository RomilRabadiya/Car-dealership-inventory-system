package incubyte.tdd.BackendAPI.Dto.Response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String message;
    private String path;
}
