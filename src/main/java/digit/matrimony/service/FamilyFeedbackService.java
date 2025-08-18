package digit.matrimony.service;

import digit.matrimony.dto.FamilyFeedbackDTO;
import digit.matrimony.entity.FamilyFeedback;
import digit.matrimony.entity.Match;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.User;
import digit.matrimony.mapper.FamilyFeedbackMapper;
import digit.matrimony.repository.FamilyFeedbackRepository;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.ProfileRepository;
import digit.matrimony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyFeedbackService {

    @Autowired private FamilyFeedbackRepository feedbackRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private MatchRepository matchRepo;
    @Autowired private ProfileRepository profileRepo;

    public List<FamilyFeedback> getAllFeedback() {
        return feedbackRepo.findAll();
    }

    public FamilyFeedback getFeedbackById(Long id) {
        return feedbackRepo.findById(id).orElse(null);
    }

    public FamilyFeedback createFeedback(FamilyFeedbackDTO dto) {
        validate(dto);
        User familyUser = userRepo.findById(dto.getFamilyUserId()).get();
        Match match = matchRepo.findById(dto.getMatchId()).get();
        Profile viewedProfile = profileRepo.findById(dto.getViewedProfileId()).get();

        FamilyFeedback feedback = FamilyFeedbackMapper.toEntity(dto, familyUser, match, viewedProfile);
        return feedbackRepo.save(feedback);
    }

    public FamilyFeedback updateFeedback(Long id, FamilyFeedbackDTO dto) {
        if (!feedbackRepo.existsById(id)) throw new IllegalArgumentException("Feedback ID not found");
        validate(dto);
        User familyUser = userRepo.findById(dto.getFamilyUserId()).get();
        Match match = matchRepo.findById(dto.getMatchId()).get();
        Profile viewedProfile = profileRepo.findById(dto.getViewedProfileId()).get();

        FamilyFeedback feedback = FamilyFeedbackMapper.toEntity(dto, familyUser, match, viewedProfile);
        feedback.setId(id);
        return feedbackRepo.save(feedback);
    }

    public void deleteFeedback(Long id) {
        feedbackRepo.deleteById(id);
    }

    private void validate(FamilyFeedbackDTO dto) {
        User familyUser = userRepo.findById(dto.getFamilyUserId())
                .orElseThrow(() -> new IllegalArgumentException("Family user not found"));

        if (familyUser.getRole() == null || familyUser.getRole().getId() != 4) {
            throw new IllegalArgumentException("User is not a FAMILY_MEMBER");
        }

        User linkedUser = familyUser.getLinkedUser();
        if (linkedUser == null) {
            throw new IllegalArgumentException("Family user is not linked to any candidate");
        }

        Match match = matchRepo.findById(dto.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        boolean isLinkedUserInMatch = match.getUser1().getId().equals(linkedUser.getId()) ||
                match.getUser2().getId().equals(linkedUser.getId());

        if (!isLinkedUserInMatch) {
            throw new IllegalArgumentException("Match does not involve the linked user");
        }

        if (!userRepo.existsById(match.getUser1().getId()) || !userRepo.existsById(match.getUser2().getId())) {
            throw new IllegalArgumentException("One or both users in the match do not exist");
        }

        if (!profileRepo.existsById(dto.getViewedProfileId())) {
            throw new IllegalArgumentException("Viewed profile not found");
        }
    }


}
