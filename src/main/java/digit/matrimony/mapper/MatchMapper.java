package digit.matrimony.mapper;

import digit.matrimony.dto.MatchDTO;
import digit.matrimony.entity.Match;
import digit.matrimony.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public MatchDTO toDto(Match match) {
        return MatchDTO.builder()
                .id(match.getId())
                .user1Id(match.getUser1().getId())
                .user2Id(match.getUser2().getId())
                .matchedAt(match.getMatchedAt())
                .isActive(match.getIsActive())
                .deletedBy(match.getDeletedBy())
                .deletedAt(match.getDeletedAt())
                .build();
    }

    public Match toEntity(MatchDTO dto, User user1, User user2) {
        return Match.builder()
                .id(dto.getId())
                .user1(user1)
                .user2(user2)
                .matchedAt(dto.getMatchedAt())
                .isActive(dto.getIsActive())
                .deletedBy(dto.getDeletedBy())
                .deletedAt(dto.getDeletedAt())
                .build();
    }
}
