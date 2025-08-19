package digit.matrimony.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class SubscriptionResponseDTO {
    private Long id;
    private Long userId;
    private String planName;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String paymentStatus;
}
