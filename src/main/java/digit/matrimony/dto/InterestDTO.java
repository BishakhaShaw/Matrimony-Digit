package digit.matrimony.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestDTO {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private String status;

    private LocalDateTime sentAt;
}
