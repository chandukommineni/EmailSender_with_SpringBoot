package emailsenderbackend.helper;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomResponse {
    private  String message;
    private HttpStatus status;
    private boolean success;


}
