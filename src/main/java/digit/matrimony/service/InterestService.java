package digit.matrimony.service;

import digit.matrimony.dto.InterestDTO;
import digit.matrimony.entity.Interest;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
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
    private final MatchService matchService;

    public InterestDTO sendInterest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found with ID: " + senderId));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found with ID: " + receiverId));

        int activeMatchCount = matchService.getActiveMatchCount(senderId);
        if (activeMatchCount >= 3) {
            throw new BadRequestException("You already have 3 active matches. Please remove one to send new interests.");
        }

        Interest interest = Interest.builder()
                .sender(sender)
                .receiver(receiver)
                .status("pending")
                .sentAt(LocalDateTime.now())
                .build();

        Interest saved = interestRepository.save(interest);
        return interestMapper.toDto(saved);
    }

    public InterestDTO acceptInterest(Long interestId) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new ResourceNotFoundException("Interest not found with ID: " + interestId));

        interest.setStatus("accepted");
        interestRepository.save(interest);

        matchService.createMatch(interest.getSender().getId(), interest.getReceiver().getId());

        return interestMapper.toDto(interest);
    }

    public InterestDTO rejectInterest(Long interestId) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new ResourceNotFoundException("Interest not found with ID: " + interestId));

        if (!"pending".equalsIgnoreCase(interest.getStatus())) {
            throw new BadRequestException("Interest is not pending and cannot be rejected.");
        }

        interest.setStatus("rejected");
        return interestMapper.toDto(interestRepository.save(interest));
    }

    public List<InterestDTO> getInterestsSentByUser(Long senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found with ID: " + senderId));

        return interestRepository.findBySender(sender).stream()
                .map(interestMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<InterestDTO> getInterestsReceivedByUser(Long receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found with ID: " + receiverId));

        return interestRepository.findByReceiver(receiver).stream()
                .map(interestMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteInterest(Long interestId) {
        interestRepository.deleteById(interestId);
    }
}
