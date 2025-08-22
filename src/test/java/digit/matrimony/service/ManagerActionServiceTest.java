package digit.matrimony.service;

import digit.matrimony.dto.ManagerActionRequestDTO;
import digit.matrimony.dto.ManagerActionResponseDTO;
import digit.matrimony.entity.*;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ManagerActionMapper;
import digit.matrimony.repository.ManagerActionRepository;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerActionServiceTest {

    @InjectMocks
    private ManagerActionService managerActionService;

    @Mock private ManagerActionRepository managerActionRepository;
    @Mock private UserRepository userRepository;
    @Mock private MatchRepository matchRepository;

    private User manager;
    private User targetUser;
    private Match match;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role managerRole = Role.builder().id((short) 2).build();
        Role userRole = Role.builder().id((short) 3).build();

        manager = User.builder().id(1L).username("manager1").role(managerRole).build();
        targetUser = User.builder().id(2L).username("user2").role(userRole).build();
        match = Match.builder().id(3L).user1(manager).user2(targetUser).isActive(true).build();
    }

    @Test
    void testCreateManagerAction_Success() {
        ManagerActionRequestDTO dto = new ManagerActionRequestDTO();
        dto.setUserId(1L);
        dto.setTargetUserId(2L);
        dto.setMatchId(3L);
        dto.setActionType("APPROVE");
        dto.setNotes("Approved for messaging");

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(managerActionRepository.existsByUserIdAndTargetUserIdAndMatchId(1L, 2L, 3L)).thenReturn(false);
        when(matchRepository.findById(3L)).thenReturn(Optional.of(match));
        when(managerActionRepository.save(any(ManagerAction.class))).thenAnswer(inv -> inv.getArgument(0));

        ManagerActionResponseDTO response = managerActionService.createManagerAction(dto);
        assertEquals("APPROVE", response.getActionType());
        assertEquals(2L, response.getTargetUserId());
    }

    @Test
    void testCreateManagerAction_InvalidManagerRole() {
        manager.setRole(Role.builder().id((short) 1).build()); // Not manager
        ManagerActionRequestDTO dto = new ManagerActionRequestDTO();
        dto.setUserId(1L);
        dto.setTargetUserId(2L);
        dto.setMatchId(3L);
        dto.setActionType("APPROVE");

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));

        assertThrows(BadRequestException.class, () -> managerActionService.createManagerAction(dto));
    }

    @Test
    void testCreateManagerAction_Duplicate() {
        ManagerActionRequestDTO dto = new ManagerActionRequestDTO();
        dto.setUserId(1L);
        dto.setTargetUserId(2L);
        dto.setMatchId(3L);
        dto.setActionType("APPROVE");

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(managerActionRepository.existsByUserIdAndTargetUserIdAndMatchId(1L, 2L, 3L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> managerActionService.createManagerAction(dto));
    }

    @Test
    void testCreateManagerAction_DeleteMatch() {
        ManagerActionRequestDTO dto = new ManagerActionRequestDTO();
        dto.setUserId(1L);
        dto.setTargetUserId(2L);
        dto.setMatchId(3L);
        dto.setActionType("DELETE_MATCH");

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(managerActionRepository.existsByUserIdAndTargetUserIdAndMatchId(1L, 2L, 3L)).thenReturn(false);
        when(matchRepository.findById(3L)).thenReturn(Optional.of(match));
        when(managerActionRepository.save(any(ManagerAction.class))).thenAnswer(inv -> inv.getArgument(0));

        ManagerActionResponseDTO response = managerActionService.createManagerAction(dto);
        assertEquals("DELETE_MATCH", response.getActionType());
        verify(matchRepository).delete(match);
    }

    @Test
    void testUpdateManagerAction_Success() {
        ManagerActionRequestDTO dto = new ManagerActionRequestDTO();
        dto.setUserId(1L);
        dto.setTargetUserId(2L);
        dto.setMatchId(3L);
        dto.setActionType("DISAPPROVE");
        dto.setNotes("Disapproved");

        ManagerAction existing = ManagerAction.builder().id(5L).build();

        when(managerActionRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(matchRepository.findById(3L)).thenReturn(Optional.of(match));
        when(managerActionRepository.save(any(ManagerAction.class))).thenAnswer(inv -> inv.getArgument(0));

        ManagerActionResponseDTO response = managerActionService.updateManagerAction(5L, dto);
        assertEquals("DISAPPROVE", response.getActionType());
        verify(userRepository).delete(targetUser);
    }

    @Test
    void testDeleteManagerAction_Success() {
        ManagerAction action = ManagerAction.builder().id(5L).build();
        when(managerActionRepository.findById(5L)).thenReturn(Optional.of(action));

        managerActionService.deleteManagerAction(5L);
        verify(managerActionRepository).delete(action);
    }

    @Test
    void testDeleteManagerAction_NotFound() {
        when(managerActionRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> managerActionService.deleteManagerAction(5L));
    }
}
