package digit.matrimony.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDTO {

    private Long id;
    private Long user1Id;
    private Long user2Id;
    private LocalDateTime matchedAt;
    private Boolean isActive;
    private String deletedBy;
    private LocalDateTime deletedAt;
}
