package digit.matrimony.service;

import digit.matrimony.dto.SuggestionDTO;
import digit.matrimony.entity.Suggestion;
import digit.matrimony.entity.User;
import digit.matrimony.mapper.SuggestionMapper;
import digit.matrimony.repository.SuggestionRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final SuggestionMapper suggestionMapper;

    public SuggestionDTO createSuggestion(Long userId, Long suggestedUserId, double matchScore) {
        User user = userRepository.findById(userId).orElseThrow();
        User suggestedUser = userRepository.findById(suggestedUserId).orElseThrow();

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .suggestedUser(suggestedUser)
                .matchScore(new java.math.BigDecimal(matchScore))
                .generatedAt(LocalDateTime.now())
                .build();

        return suggestionMapper.toDto(suggestionRepository.save(suggestion));
    }

    public List<SuggestionDTO> getSuggestionsForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return suggestionRepository.findByUser(user).stream()
                .map(suggestionMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteSuggestion(Long suggestionId) {
        suggestionRepository.deleteById(suggestionId);
    }
}

