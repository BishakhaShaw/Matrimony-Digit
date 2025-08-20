package digit.matrimony.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class SubscriptionRequestDTO {
    private Long userId;
    private String planName;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}

