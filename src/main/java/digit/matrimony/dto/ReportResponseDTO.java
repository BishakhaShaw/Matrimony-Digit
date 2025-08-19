package digit.matrimony.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportResponseDTO {

    private Long id;
    private Long reporterId;
    private Long reportedUserId;
    private String reason;
    private String status;
    private Long reviewedByUserId;
    private Boolean escalatedToAdmin;
    private LocalDateTime reportedAt;
}
