package digit.matrimony.service;

import digit.matrimony.dto.MatchCreateRequestDTO;
import digit.matrimony.dto.MatchDTO;
import digit.matrimony.entity.Match;
import digit.matrimony.entity.User;
import digit.matrimony.mapper.MatchMapper;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final MatchMapper matchMapper;

    public MatchDTO createMatch(MatchDTO request) {

        User user1 = userRepository.findById(request.getUser1Id()).orElseThrow();
        User user2 = userRepository.findById(request.getUser2Id()).orElseThrow();

        Match match = Match.builder()
                .user1(user1)
                .user2(user2)
                .matchedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return matchMapper.toDto(matchRepository.save(match));
    }
    public MatchDTO createMatch(Long user1Id, Long user2Id) {
        Optional<Match> existingMatch = matchRepository.findByUserIds(user1Id, user2Id);
        if (existingMatch.isPresent()) {
            return matchMapper.toDto(existingMatch.get());
        }

        User user1 = userRepository.findById(user1Id).orElseThrow(() -> new RuntimeException("User1 not found"));
        User user2 = userRepository.findById(user2Id).orElseThrow(() -> new RuntimeException("User2 not found"));

        Match match = Match.builder()
                .user1(user1)
                .user2(user2)
                .matchedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return matchMapper.toDto(matchRepository.save(match));
    }


    public List<MatchDTO> getMatchesForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return matchRepository.findByUser1OrUser2(user, user).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    public MatchDTO deactivateMatch(Long matchId, String deletedBy) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        match.setIsActive(false);
        match.setDeletedBy(deletedBy);
        match.setDeletedAt(LocalDateTime.now());
        return matchMapper.toDto(matchRepository.save(match));
    }

    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }
}
