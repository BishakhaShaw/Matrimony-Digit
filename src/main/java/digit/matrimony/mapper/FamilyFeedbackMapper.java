package digit.matrimony.mapper;

import digit.matrimony.dto.FamilyFeedbackDTO;
import digit.matrimony.entity.FamilyFeedback;
import digit.matrimony.entity.Match;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.User;

public class FamilyFeedbackMapper {

    public static FamilyFeedbackDTO toDTO(FamilyFeedback feedback) {
        if (feedback == null) return null;

        return FamilyFeedbackDTO.builder()
                .id(feedback.getId())
                .familyUserId(feedback.getFamilyUser() != null ? feedback.getFamilyUser().getId() : null)
                .matchId(feedback.getMatch() != null ? feedback.getMatch().getId() : null)
                .viewedProfileId(feedback.getViewedProfile() != null ? feedback.getViewedProfile().getId() : null)
                .feedbackFlag(feedback.getFeedbackFlag())
                .comments(feedback.getComments())
                .feedbackDate(feedback.getFeedbackDate())
                .build();
    }

    public static FamilyFeedback toEntity(FamilyFeedbackDTO dto, User familyUser, Match match, Profile viewedProfile) {
        return FamilyFeedback.builder()
                .id(dto.getId())
                .familyUser(familyUser)
                .match(match)
                .viewedProfile(viewedProfile)
                .feedbackFlag(dto.getFeedbackFlag())
                .comments(dto.getComments())
                .feedbackDate(dto.getFeedbackDate())
                .build();
    }

}
