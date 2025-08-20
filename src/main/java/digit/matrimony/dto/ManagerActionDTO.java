package digit.matrimony.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
@Builder
@Data
public class ManagerActionDTO {
    private Long id;
    private Long userId;
    private Long targetUserId;
    private String actionType;
    private Long matchId;
    private LocalDateTime actionTimestamp;
    private String notes;
}
