package digit.matrimony.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@Data
public class AnalyticDTO {
    private Long id;
    private String region;
    private Integer totalUsers;
    private Integer activeUsers;
    private Integer premiumUsers;
    private Integer activeMatches;
    private Integer subscriptionsSoldLastMonth;
    private BigDecimal revenueLastMonth;
    private LocalDateTime recordedAt;
}
