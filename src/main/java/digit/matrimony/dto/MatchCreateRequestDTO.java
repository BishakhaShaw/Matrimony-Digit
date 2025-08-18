package digit.matrimony.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchCreateRequestDTO {

    @NotNull(message = "User1 ID must not be null")
    private Long user1Id;

    @NotNull(message = "User2 ID must not be null")
    private Long user2Id;
}
