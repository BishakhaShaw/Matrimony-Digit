package digit.matrimony.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
@Builder
@Data
public class FamilyFeedbackDTO {
    private Long id;
    private Long familyUserId;
    private Long matchId;
    private Long viewedProfileId;
    private String feedbackFlag;
    private String comments;
    private LocalDateTime feedbackDate;
}
