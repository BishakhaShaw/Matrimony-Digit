

package digit.matrimony.dto;


import lombok.*;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private Long userId;
    private String token;
    private java.util.Date expiresAt;
}