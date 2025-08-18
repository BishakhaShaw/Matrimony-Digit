package digit.matrimony.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String message;
    private LocalDateTime sentAt;
}
