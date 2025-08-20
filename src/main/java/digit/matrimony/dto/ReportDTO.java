package digit.matrimony.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ReportDTO {

    private Long id;

    @NotNull(message = "Reporter ID is required")
    private Long reporterId;

    @NotNull(message = "Reported user ID is required")
    private Long reportedUserId;

    @NotNull(message = "Reason is required")
    @Size(min = 5, max = 1000, message = "Reason must be between 5 and 1000 characters")
    private String reason;

    private String status;

    private Long reviewedByUserId;

    private Boolean escalatedToAdmin;

    private LocalDateTime reportedAt;
}
