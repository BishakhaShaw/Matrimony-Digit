package digit.matrimony.service;

import digit.matrimony.dto.MessageDTO;
import digit.matrimony.dto.MessageRequestDTO;
import digit.matrimony.entity.Message;
import digit.matrimony.entity.User;
import digit.matrimony.enums.MessageStatus;
import digit.matrimony.mapper.MessageMapper;
import digit.matrimony.repository.MessageRepository;
import digit.matrimony.repository.UserRepository;
import digit.matrimony.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MessageMapper messageMapper;

    public MessageDTO sendMessage(MessageRequestDTO request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        boolean isMatched = matchRepository.existsByUser1AndUser2AndMatchedTrue(sender, receiver)
                || matchRepository.existsByUser1AndUser2AndMatchedTrue(receiver, sender);

        if (!isMatched) {
            throw new RuntimeException("Users are not matched. Messaging not allowed.");
        }

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .message(request.getMessage())
                .build();

        return messageMapper.toDto(messageRepository.save(message));
    }

    public List<MessageDTO> getMessagesBetweenUsers(Long user1Id, Long user2Id, int page, int size) {
        User user1 = userRepository.findById(user1Id).orElseThrow();
        User user2 = userRepository.findById(user2Id).orElseThrow();

        Pageable pageable = PageRequest.of(page, size, Sort.by("sentAt").ascending());
        return messageRepository.findConversationBetweenUsers(user1, user2, pageable)
                .stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<MessageDTO> getMessagesReceivedByUser(Long receiverId, int page, int size) {
        User receiver = userRepository.findById(receiverId).orElseThrow();
        Pageable pageable = PageRequest.of(page, size, Sort.by("sentAt").descending());

        return messageRepository.findByReceiver(receiver, pageable).stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(MessageStatus.DELETED);
        messageRepository.save(message);
    }
}
