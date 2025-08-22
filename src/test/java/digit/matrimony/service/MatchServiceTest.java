package digit.matrimony.service;

import digit.matrimony.dto.MatchDTO;
import digit.matrimony.entity.Match;
import digit.matrimony.entity.User;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.MatchMapper;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchMapper matchMapper;

    @InjectMocks
    private MatchService matchService;

    private User user1;
    private User user2;
    private Match match;
    private MatchDTO matchDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);

        match = Match.builder()
                .id(100L)
                .user1(user1)
                .user2(user2)
                .matchedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        matchDTO = new MatchDTO();
        matchDTO.setId(100L);
        matchDTO.setUser1Id(1L);
        matchDTO.setUser2Id(2L);
    }

    @Test
    void testCreateMatch_WithDTO_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(matchMapper.toDto(any(Match.class))).thenReturn(matchDTO);

        MatchDTO result = matchService.createMatch(matchDTO);
        assertEquals(100L, result.getId());
    }

    @Test
    void testCreateMatch_WithUserIds_NewMatch() {
        when(matchRepository.findByUserIds(1L, 2L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(matchMapper.toDto(any(Match.class))).thenReturn(matchDTO);

        MatchDTO result = matchService.createMatch(1L, 2L);
        assertEquals(100L, result.getId());
    }

    @Test
    void testGetActiveMatchCount() {
        when(matchRepository.countActiveMatchesByUserId(1L)).thenReturn(3);
        int count = matchService.getActiveMatchCount(1L);
        assertEquals(3, count);
    }

    @Test
    void testGetMatchesForUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(matchRepository.findByUser1OrUser2(user1, user1)).thenReturn(List.of(match));
        when(matchMapper.toDto(match)).thenReturn(matchDTO);

        List<MatchDTO> result = matchService.getMatchesForUser(1L);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
    }

    @Test
    void testDeactivateMatch() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(matchMapper.toDto(any(Match.class))).thenReturn(matchDTO);

        MatchDTO result = matchService.deactivateMatch(100L, "user1");
        assertFalse(match.getIsActive());
        assertEquals("user1", match.getDeletedBy());
        assertEquals(100L, result.getId());
    }
}
