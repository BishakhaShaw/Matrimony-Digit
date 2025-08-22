package digit.matrimony.mapper;

import digit.matrimony.dto.ManagerActionRequestDTO;
import digit.matrimony.dto.ManagerActionResponseDTO;
import digit.matrimony.entity.ManagerAction;
import digit.matrimony.entity.User;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.UserRepository;

public class ManagerActionMapper {

    public static ManagerAction toEntity(
            ManagerActionRequestDTO dto,
            UserRepository userRepository,
            MatchRepository matchRepository
    ) {
        User manager = userRepository.findById(dto.getUserId()).orElse(null);
        User targetUser = userRepository.findById(dto.getTargetUserId()).orElse(null);

        return ManagerAction.builder()
                .user(manager)
                .targetUser(targetUser)
                .match(dto.getMatchId() != null ? matchRepository.findById(dto.getMatchId()).orElse(null) : null)
                .actionType(dto.getActionType())
                .notes(dto.getNotes())
                .actionTimestamp(java.time.LocalDateTime.now())
                .build();
    }

    public static ManagerActionResponseDTO toResponseDTO(ManagerAction action) {
        if (action == null) return null;

        return ManagerActionResponseDTO.builder()
                .id(action.getId())
                .userId(action.getUser() != null ? action.getUser().getId() : null)
                .managerName(action.getUser() != null ? action.getUser().getUsername() : null)
                .targetUserId(action.getTargetUser() != null ? action.getTargetUser().getId() : null)
                .targetUsername(action.getTargetUser() != null ? action.getTargetUser().getUsername() : null)
                .matchId(action.getMatch() != null ? action.getMatch().getId() : null)
                .actionType(action.getActionType())
                .notes(action.getNotes())
                .actionTimestamp(action.getActionTimestamp())
                .build();
    }
}
