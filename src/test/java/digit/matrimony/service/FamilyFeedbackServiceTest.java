package digit.matrimony.service;

import digit.matrimony.dto.FamilyFeedbackDTO;
import digit.matrimony.entity.*;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.FamilyFeedbackMapper;
import digit.matrimony.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FamilyFeedbackServiceTest {

    @InjectMocks
    private FamilyFeedbackService feedbackService;

    @Mock private FamilyFeedbackRepository feedbackRepo;
    @Mock private UserRepository userRepo;
    @Mock private MatchRepository matchRepo;
    @Mock private ProfileRepository profileRepo;

    private User familyUser;
    private User linkedUser;
    private Match match;
    private Profile profile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        linkedUser = User.builder().id(10L).build();
        Role familyRole = Role.builder().id((short) 4).build();
        familyUser = User.builder().id(1L).role(familyRole).linkedUser(linkedUser).build();

        match = Match.builder()
                .id(2L)
                .user1(linkedUser)
                .user2(User.builder().id(11L).build())
                .isActive(true)
                .build();

        profile = Profile.builder().id(3L).build();
    }

    @Test
    void testCreateFeedback_Success() {
        FamilyFeedbackDTO dto = FamilyFeedbackDTO.builder()
                .familyUserId(1L)
                .matchId(2L)
                .viewedProfileId(3L)
                .feedbackFlag("GREEN")
                .comments("Looks good")
                .feedbackDate(LocalDateTime.now())
                .build();

        when(userRepo.findById(1L)).thenReturn(Optional.of(familyUser));
        when(matchRepo.findById(2L)).thenReturn(Optional.of(match));
        when(profileRepo.findById(3L)).thenReturn(Optional.of(profile));
        when(userRepo.existsById(10L)).thenReturn(true);
        when(userRepo.existsById(11L)).thenReturn(true);
        when(profileRepo.existsById(3L)).thenReturn(true);

        FamilyFeedback feedback = FamilyFeedbackMapper.toEntity(dto, familyUser, match, profile);
        when(feedbackRepo.save(any(FamilyFeedback.class))).thenReturn(feedback);

        FamilyFeedback result = feedbackService.createFeedback(dto);
        assertEquals("GREEN", result.getFeedbackFlag());
        verify(feedbackRepo).save(any(FamilyFeedback.class));
    }

    @Test
    void testCreateFeedback_InvalidRole() {
        familyUser.setRole(Role.builder().id((short) 2).build()); // Not FAMILY_MEMBER
        when(userRepo.findById(1L)).thenReturn(Optional.of(familyUser));

        FamilyFeedbackDTO dto = FamilyFeedbackDTO.builder()
                .familyUserId(1L)
                .matchId(2L)
                .viewedProfileId(3L)
                .build();

        assertThrows(BadRequestException.class, () -> feedbackService.createFeedback(dto));
    }

    @Test
    void testCreateFeedback_LinkedUserNotInMatch() {
        match.setUser1(User.builder().id(99L).build()); // Not linked user
        when(userRepo.findById(1L)).thenReturn(Optional.of(familyUser));
        when(matchRepo.findById(2L)).thenReturn(Optional.of(match));

        FamilyFeedbackDTO dto = FamilyFeedbackDTO.builder()
                .familyUserId(1L)
                .matchId(2L)
                .viewedProfileId(3L)
                .build();

        assertThrows(BadRequestException.class, () -> feedbackService.createFeedback(dto));
    }

    @Test
    void testUpdateFeedback_Success() {
        FamilyFeedbackDTO dto = FamilyFeedbackDTO.builder()
                .familyUserId(1L)
                .matchId(2L)
                .viewedProfileId(3L)
                .feedbackFlag("RED")
                .comments("Not suitable")
                .feedbackDate(LocalDateTime.now())
                .build();

        when(feedbackRepo.existsById(5L)).thenReturn(true);
        when(userRepo.findById(1L)).thenReturn(Optional.of(familyUser));
        when(matchRepo.findById(2L)).thenReturn(Optional.of(match));
        when(profileRepo.findById(3L)).thenReturn(Optional.of(profile));
        when(userRepo.existsById(10L)).thenReturn(true);
        when(userRepo.existsById(11L)).thenReturn(true);
        when(profileRepo.existsById(3L)).thenReturn(true);

        FamilyFeedback feedback = FamilyFeedbackMapper.toEntity(dto, familyUser, match, profile);
        feedback.setId(5L);
        when(feedbackRepo.save(any(FamilyFeedback.class))).thenReturn(feedback);

        FamilyFeedback result = feedbackService.updateFeedback(5L, dto);
        assertEquals("RED", result.getFeedbackFlag());
    }

    @Test
    void testDeleteFeedback_Success() {
        when(feedbackRepo.existsById(5L)).thenReturn(true);
        feedbackService.deleteFeedback(5L);
        verify(feedbackRepo).deleteById(5L);
    }

    @Test
    void testDeleteFeedback_NotFound() {
        when(feedbackRepo.existsById(5L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> feedbackService.deleteFeedback(5L));
    }
}
