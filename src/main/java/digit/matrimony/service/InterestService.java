package digit.matrimony.service;

import digit.matrimony.dto.InterestDTO;
import digit.matrimony.entity.Interest;
import digit.matrimony.entity.User;
import digit.matrimony.mapper.InterestMapper;
import digit.matrimony.repository.InterestRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final InterestMapper interestMapper;

    public InterestDTO sendInterest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Interest interest = Interest.builder()
                .sender(sender)
                .receiver(receiver)
                .status("pending")
                .sentAt(LocalDateTime.now())
                .build();

        Interest saved = interestRepository.save(interest);
        return interestMapper.toDto(saved);
    }

    public List<InterestDTO> getInterestsSentByUser(Long senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        return interestRepository.findBySender(sender).stream()
                .map(interestMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<InterestDTO> getInterestsReceivedByUser(Long receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        return interestRepository.findByReceiver(receiver).stream()
                .map(interestMapper::toDto)
                .collect(Collectors.toList());
    }

    public InterestDTO updateInterestStatus(Long interestId, String status) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        interest.setStatus(status);
        Interest updated = interestRepository.save(interest);
        return interestMapper.toDto(updated);
    }

    public void deleteInterest(Long interestId) {
        interestRepository.deleteById(interestId);
    }
}
