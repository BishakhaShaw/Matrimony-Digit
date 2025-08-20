package digit.matrimony.service;

import digit.matrimony.dto.ManagerActionDTO;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManagerActionService {

    @Autowired
    private ManagerActionRepository managerActionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    public List<ManagerActionDTO> getAllManagerActions() {
        return managerActionRepository.findAll().stream()
                .map(ManagerActionMapper::toDTO)
                .toList();
    }

    public ManagerActionDTO getManagerActionById(Long id) {
        ManagerAction action = managerActionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ManagerAction not found with id: " + id));
        return ManagerActionMapper.toDTO(action);
    }

    public ManagerActionDTO createManagerAction(ManagerActionDTO dto) {
        ManagerAction action = new ManagerAction();
        populateAndValidateEntities(action, dto);
        validateNotes(dto.getNotes());
        action.setNotes(dto.getNotes());
        action.setActionType(dto.getActionType());
        action.setActionTimestamp(LocalDateTime.now());

        ManagerAction saved = managerActionRepository.save(action);
        return ManagerActionMapper.toDTO(saved);
    }

    public ManagerActionDTO updateManagerAction(Long id, ManagerActionDTO dto) {
        ManagerAction existing = managerActionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ManagerAction not found with id: " + id));

        populateAndValidateEntities(existing, dto);
        validateNotes(dto.getNotes());
        existing.setNotes(dto.getNotes());
        existing.setActionType(dto.getActionType());
        existing.setActionTimestamp(LocalDateTime.now());

        ManagerAction saved = managerActionRepository.save(existing);
        return ManagerActionMapper.toDTO(saved);
    }

    public void deleteManagerAction(Long id) {
        ManagerAction existing = managerActionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ManagerAction not found with id: " + id));
        managerActionRepository.delete(existing);
    }

    private void populateAndValidateEntities(ManagerAction action, ManagerActionDTO dto) {
        if (dto.getUserId() == null || dto.getTargetUserId() == null) {
            throw new BadRequestException("User IDs must not be null.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        User targetUser = userRepository.findById(dto.getTargetUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found with id: " + dto.getTargetUserId()));

        action.setUser(user);
        action.setTargetUser(targetUser);

        if (dto.getMatchId() != null) {
            Match match = matchRepository.findById(dto.getMatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + dto.getMatchId()));
            action.setMatch(match);
        } else {
            action.setMatch(null);
        }
    }

    private void validateNotes(String notes) {
        if (notes == null || notes.trim().length() < 3) {
            throw new BadRequestException("Notes must be at least 3 characters long.");
        }
    }
}
