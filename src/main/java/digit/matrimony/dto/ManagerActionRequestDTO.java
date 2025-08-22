package digit.matrimony.dto;

import lombok.Data;

@Data
public class ManagerActionRequestDTO {
    private Long userId;         // Manager performing the action
    private Long targetUserId;   // User affected by the action
    private Long matchId;        // Match being acted upon
    private String actionType;   // e.g. "DELETE_MATCH"
    private String notes;        // Optional notes
}
