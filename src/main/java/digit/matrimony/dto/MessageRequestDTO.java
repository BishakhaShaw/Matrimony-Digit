package digit.matrimony.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    private Long senderId;
    private Long receiverId;
    private String message;
}
