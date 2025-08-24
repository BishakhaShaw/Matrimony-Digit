package digit.matrimony.dto;

import lombok.Data;

@Data
public class ManagerActionRequestDTO {
    private Long userId;
    private Long targetUserId;
    private Long matchId;
    private String actionType;
    private String notes;
}
