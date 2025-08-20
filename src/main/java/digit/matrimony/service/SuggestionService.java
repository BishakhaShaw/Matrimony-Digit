package digit.matrimony.service;

import digit.matrimony.dto.SuggestionDTO;
import digit.matrimony.entity.*;
import digit.matrimony.mapper.SuggestionMapper;
import digit.matrimony.repository.PreferenceRepository;
import digit.matrimony.repository.ProfileRepository;
import digit.matrimony.repository.SuggestionRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final SuggestionMapper suggestionMapper;
    private final PreferenceRepository preferenceRepository;
    private final ProfileRepository profileRepository;


    private double calculateMatchScore(Preference preference, Profile profile) {
        double score = 0;
        int totalCriteria = 0;

        if (preference.getPreferredReligion() != null) {
            totalCriteria++;
            if (preference.getPreferredReligion().equalsIgnoreCase(profile.getReligion())) score++;
        }

        if (preference.getPreferredCaste() != null) {
            totalCriteria++;
            if (preference.getPreferredCaste().equalsIgnoreCase(profile.getCaste())) score++;
        }

        if (preference.getPreferredLocation() != null) {
            totalCriteria++;
            if (preference.getPreferredLocation().equalsIgnoreCase(profile.getCurrentLocation())) score++;
        }

        if (preference.getPreferredEducation() != null) {
            totalCriteria++;
            if (preference.getPreferredEducation().equalsIgnoreCase(profile.getEducation())) score++;
        }

        if (preference.getPreferredMaritalStatus() != null) {
            totalCriteria++;
            if (preference.getPreferredMaritalStatus().equalsIgnoreCase(profile.getMaritalStatus())) score++;
        }

        if (preference.getPreferredAgeMin() != null && preference.getPreferredAgeMax() != null && profile.getDateOfBirth() != null) {
            totalCriteria++;
            int age = Period.between(profile.getDateOfBirth().toLocalDate(), LocalDate.now()).getYears();
            if (age >= preference.getPreferredAgeMin() && age <= preference.getPreferredAgeMax()) score++;
        }

        return totalCriteria == 0 ? 0 : (score / totalCriteria) * 100;
    }

    public List<SuggestionDTO> generateTopSuggestions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Preference not found for user ID: " + userId));

        List<Profile> candidateProfiles = profileRepository.findAll().stream()
                .filter(profile -> !profile.getUser().getId().equals(userId)) // exclude self
                .filter(profile -> profile.getUser().getRole().getName().equalsIgnoreCase("User")) // only regular users
                .collect(Collectors.toList());

        List<Suggestion> suggestions = candidateProfiles.stream()
                .map(profile -> {
                    double score = calculateMatchScore(preference, profile);
                    return Suggestion.builder()
                            .user(user)
                            .suggestedUser(profile.getUser())
                            .matchScore(BigDecimal.valueOf(score))
                            .generatedAt(LocalDateTime.now())
                            .build();
                })
                .sorted(Comparator.comparing(Suggestion::getMatchScore).reversed())
                .limit(3)
                .collect(Collectors.toList());

        suggestionRepository.saveAll(suggestions);
        return suggestions.stream().map(suggestionMapper::toDto).collect(Collectors.toList());
    }


    public List<SuggestionDTO> getSuggestionsForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return suggestionRepository.findByUser(user).stream()
                .map(suggestionMapper::toDto)
                .collect(Collectors.toList());
    }


    public SuggestionDTO createSuggestion(Long userId, Long suggestedUserId, double matchScore) {
        User user = userRepository.findById(userId).orElseThrow();
        User suggestedUser = userRepository.findById(suggestedUserId).orElseThrow();

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .suggestedUser(suggestedUser)
                .matchScore(BigDecimal.valueOf(matchScore))
                .generatedAt(LocalDateTime.now())
                .build();

        return suggestionMapper.toDto(suggestionRepository.save(suggestion));
    }

    public void deleteSuggestion(Long suggestionId) {
        suggestionRepository.deleteById(suggestionId);
    }
}

