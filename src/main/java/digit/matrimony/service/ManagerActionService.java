package digit.matrimony.service;

import digit.matrimony.dto.ManagerActionRequestDTO;
import digit.matrimony.dto.ManagerActionResponseDTO;
import digit.matrimony.entity.ManagerAction;
import digit.matrimony.entity.Match;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ManagerActionMapper;
import digit.matrimony.repository.ManagerActionRepository;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerActionService {

    @Autowired
    private ManagerActionRepository managerActionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    public List<ManagerActionResponseDTO> getAllManagerActions() {
        return managerActionRepository.findAll().stream()
                .map(ManagerActionMapper::toResponseDTO)
                .toList();
    }

    public ManagerActionResponseDTO getManagerActionById(Long id) {
        ManagerAction action = managerActionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ManagerAction not found with id: " + id));
        return ManagerActionMapper.toResponseDTO(action);
    }

    public ManagerActionResponseDTO createManagerAction(ManagerActionRequestDTO dto) {
        validateRoles(dto.getUserId(), dto.getTargetUserId());
        validateNoDuplicate(dto.getUserId(), dto.getTargetUserId(), dto.getMatchId());

        ManagerAction action = ManagerActionMapper.toEntity(dto, userRepository, matchRepository);
        handleActionType(dto);

        ManagerAction saved = managerActionRepository.save(action);
        return ManagerActionMapper.toResponseDTO(saved);
    }

    public ManagerActionResponseDTO updateManagerAction(Long id, ManagerActionRequestDTO dto) {
        ManagerAction existing = managerActionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ManagerAction not found with id: " + id));

        validateRoles(dto.getUserId(), dto.getTargetUserId());

        ManagerAction updated = ManagerActionMapper.toEntity(dto, userRepository, matchRepository);
        updated.setId(existing.getId()); // preserve original ID

        handleActionType(dto);

        ManagerAction saved = managerActionRepository.save(updated);
        return ManagerActionMapper.toResponseDTO(saved);
    }

    public void deleteManagerAction(Long id) {
        ManagerAction action = managerActionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ManagerAction not found with id: " + id));
        managerActionRepository.delete(action);
    }

    private void handleActionType(ManagerActionRequestDTO dto) {
        String type = dto.getActionType();

        if ("DELETE_MATCH".equalsIgnoreCase(type)) {
            Match match = matchRepository.findById(dto.getMatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + dto.getMatchId()));
            matchRepository.delete(match);
        } else if ("APPROVE".equalsIgnoreCase(type)) {
            User targetUser = userRepository.findById(dto.getTargetUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Target user not found with id: " + dto.getTargetUserId()));
            userRepository.save(targetUser); // Optional: update status or flags
        } else if ("DISAPPROVE".equalsIgnoreCase(type)) {
            User targetUser = userRepository.findById(dto.getTargetUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Target user not found with id: " + dto.getTargetUserId()));
            userRepository.delete(targetUser);
        }
    }

    private void validateRoles(Long managerId, Long userId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + managerId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Short managerRoleId = manager.getRole() != null ? manager.getRole().getId() : null;
        Short userRoleId = user.getRole() != null ? user.getRole().getId() : null;

        if (managerRoleId == null || managerRoleId != 2) {
            throw new BadRequestException("Only managers (roleId = 2) can perform this action.");
        }

        if (userRoleId == null || userRoleId != 3) {
            throw new BadRequestException("Target user must have roleId = 3.");
        }
    }

    private void validateNoDuplicate(Long managerId, Long userId, Long matchId) {
        boolean exists = managerActionRepository.existsByUserIdAndTargetUserIdAndMatchId(managerId, userId, matchId);
        if (exists) {
            throw new BadRequestException("Duplicate action: this manager already acted on this match and user.");
        }
    }
}
