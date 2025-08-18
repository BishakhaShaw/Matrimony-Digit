package digit.matrimony.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String message;
    private Long userId;

    public LoginResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }
}
