package digit.matrimony.mapper;

import digit.matrimony.dto.ManagerActionDTO;
import digit.matrimony.entity.ManagerAction;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.UserRepository;

public class ManagerActionMapper {

    public static ManagerActionDTO toDTO(ManagerAction action) {
        if (action == null) return null;

        return ManagerActionDTO.builder()
                .id(action.getId())
                .userId(action.getUser() != null ? action.getUser().getId() : null)
                .targetUserId(action.getTargetUser() != null ? action.getTargetUser().getId() : null)
                .actionType(action.getActionType())
                .matchId(action.getMatch() != null ? action.getMatch().getId() : null)
                .actionTimestamp(action.getActionTimestamp())
                .notes(action.getNotes())
                .build();
    }

    public static ManagerAction toEntity(
            ManagerActionDTO dto,
            UserRepository userRepository,
            MatchRepository matchRepository
    ) {
        return ManagerAction.builder()
                .id(dto.getId())
                .user(dto.getUserId() != null ? userRepository.findById(dto.getUserId()).orElse(null) : null)
                .targetUser(dto.getTargetUserId() != null ? userRepository.findById(dto.getTargetUserId()).orElse(null) : null)
                .match(dto.getMatchId() != null ? matchRepository.findById(dto.getMatchId()).orElse(null) : null)
                .actionType(dto.getActionType())
                .actionTimestamp(dto.getActionTimestamp())
                .notes(dto.getNotes())
                .build();
    }

}
