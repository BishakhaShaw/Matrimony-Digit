package digit.matrimony.service;

import digit.matrimony.dto.MatchDTO;
import digit.matrimony.entity.Match;
import digit.matrimony.entity.User;
import digit.matrimony.exception.ResourceNotFoundException;
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
        User user1 = userRepository.findById(request.getUser1Id())
                .orElseThrow(() -> new ResourceNotFoundException("User1 not found with ID: " + request.getUser1Id()));
        User user2 = userRepository.findById(request.getUser2Id())
                .orElseThrow(() -> new ResourceNotFoundException("User2 not found with ID: " + request.getUser2Id()));

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

        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new ResourceNotFoundException("User1 not found with ID: " + user1Id));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new ResourceNotFoundException("User2 not found with ID: " + user2Id));

        Match match = Match.builder()
                .user1(user1)
                .user2(user2)
                .matchedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return matchMapper.toDto(matchRepository.save(match));
    }

    public int getActiveMatchCount(Long userId) {
        return matchRepository.countActiveMatchesByUserId(userId);
    }

    public List<MatchDTO> getMatchesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return matchRepository.findByUser1OrUser2(user, user).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    public MatchDTO deactivateMatch(Long matchId, String deletedBy) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with ID: " + matchId));

        match.setIsActive(false);
        match.setDeletedBy(deletedBy);
        match.setDeletedAt(LocalDateTime.now());
        return matchMapper.toDto(matchRepository.save(match));
    }

    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }
}
