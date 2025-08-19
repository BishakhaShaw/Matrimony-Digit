package digit.matrimony.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ManagerActionResponseDTO {
    private Long id;
    private Long userId;
    private String managerName;
    private Long targetUserId;
    private String targetUsername;
    private Long matchId;
    private String actionType;
    private String notes;
    private LocalDateTime actionTimestamp;
}
