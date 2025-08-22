package digit.matrimony.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestionDTO {

    private Long id;
    private Long userId;
    private Long suggestedUserId;
    private BigDecimal matchScore;
    private LocalDateTime generatedAt;
}
