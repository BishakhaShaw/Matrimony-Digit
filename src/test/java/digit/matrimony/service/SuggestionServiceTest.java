package digit.matrimony.service;

import digit.matrimony.dto.SuggestionDTO;
import digit.matrimony.entity.*;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.SuggestionMapper;
import digit.matrimony.repository.PreferenceRepository;
import digit.matrimony.repository.ProfileRepository;
import digit.matrimony.repository.SuggestionRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SuggestionServiceTest {

    @InjectMocks
    private SuggestionService suggestionService;

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SuggestionMapper suggestionMapper;

    @Mock
    private PreferenceRepository preferenceRepository;

    @Mock
    private ProfileRepository profileRepository;

    private User user;
    private User suggestedUser;
    private Preference preference;
    private Profile profile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setRole(new Role(Short.valueOf("1"), "User"));


        suggestedUser = new User();
        suggestedUser.setId(2L);
        suggestedUser.setRole(new Role(Short.valueOf("1"), "User"));

        preference = new Preference();
        preference.setUser(user);
        preference.setPreferredReligion("Hindu");
        preference.setPreferredCaste("Brahmin");
        preference.setPreferredLocation("Mumbai");
        preference.setPreferredEducation("B.Tech");
        preference.setPreferredMaritalStatus("Single");
        preference.setPreferredAgeMin(25);
        preference.setPreferredAgeMax(30);

        profile = new Profile();
        profile.setUser(suggestedUser);
        profile.setReligion("Hindu");
        profile.setCaste("Brahmin");
        profile.setCurrentLocation("Mumbai");
        profile.setEducation("B.Tech");
        profile.setMaritalStatus("Single");
        profile.setDateOfBirth(LocalDate.of(1997, 1, 1).atStartOfDay());
    }

    @Test
    void testGenerateTopSuggestions_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(preferenceRepository.findByUser(user)).thenReturn(Optional.of(preference));
        when(profileRepository.findAll()).thenReturn(List.of(profile));

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .suggestedUser(suggestedUser)
                .matchScore(BigDecimal.valueOf(100))
                .generatedAt(LocalDateTime.now())
                .build();

        when(suggestionMapper.toDto(any(Suggestion.class))).thenReturn(new SuggestionDTO());

        List<SuggestionDTO> result = suggestionService.generateTopSuggestions(1L);
        assertEquals(1, result.size());
        verify(suggestionRepository).saveAll(anyList());
    }

    @Test
    void testGenerateTopSuggestions_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> suggestionService.generateTopSuggestions(1L));
    }

    @Test
    void testGenerateTopSuggestions_PreferenceNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(preferenceRepository.findByUser(user)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> suggestionService.generateTopSuggestions(1L));
    }

    @Test
    void testGetSuggestionsForUser_Success() {
        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .suggestedUser(suggestedUser)
                .matchScore(BigDecimal.valueOf(90))
                .generatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(suggestionRepository.findByUser(user)).thenReturn(List.of(suggestion));
        when(suggestionMapper.toDto(suggestion)).thenReturn(new SuggestionDTO());

        List<SuggestionDTO> result = suggestionService.getSuggestionsForUser(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateSuggestion_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(suggestedUser));

        Suggestion suggestion = Suggestion.builder()
                .user(user)
                .suggestedUser(suggestedUser)
                .matchScore(BigDecimal.valueOf(85))
                .generatedAt(LocalDateTime.now())
                .build();

        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(suggestion);
        when(suggestionMapper.toDto(suggestion)).thenReturn(new SuggestionDTO());

        SuggestionDTO result = suggestionService.createSuggestion(1L, 2L, 85.0);
        assertNotNull(result);
    }

    @Test
    void testCreateSuggestion_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> suggestionService.createSuggestion(1L, 2L, 85.0));
    }

    @Test
    void testCreateSuggestion_SuggestedUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> suggestionService.createSuggestion(1L, 2L, 85.0));
    }

    @Test
    void testDeleteSuggestion() {
        suggestionService.deleteSuggestion(10L);
        verify(suggestionRepository).deleteById(10L);
    }
}
