package digit.matrimony.mapper;

import digit.matrimony.dto.SuggestionDTO;
import digit.matrimony.entity.Suggestion;
import digit.matrimony.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SuggestionMapper {

    public SuggestionDTO toDto(Suggestion suggestion) {
        return SuggestionDTO.builder()
                .id(suggestion.getId())
                .userId(suggestion.getUser().getId())
                .suggestedUserId(suggestion.getSuggestedUser().getId())
                .matchScore(suggestion.getMatchScore())
                .generatedAt(suggestion.getGeneratedAt())
                .build();
    }

    public Suggestion toEntity(SuggestionDTO dto, User user, User suggestedUser) {
        return Suggestion.builder()
                .id(dto.getId())
                .user(user)
                .suggestedUser(suggestedUser)
                .matchScore(dto.getMatchScore())
                .generatedAt(dto.getGeneratedAt())
                .build();
    }
}
