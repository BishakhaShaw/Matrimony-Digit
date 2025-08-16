package digit.matrimony.service;

import digit.matrimony.dto.MessageDTO;
import digit.matrimony.entity.Message;
import digit.matrimony.entity.User;
import digit.matrimony.mapper.MessageMapper;
import digit.matrimony.repository.MessageRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public MessageDTO sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .message(content)
                .sentAt(LocalDateTime.now())
                .build();

        return messageMapper.toDto(messageRepository.save(message));
    }

    public List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();

        return messageRepository.findBySenderAndReceiver(sender, receiver).stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessagesReceivedByUser(Long receiverId) {
        User receiver = userRepository.findById(receiverId).orElseThrow();
        return messageRepository.findByReceiver(receiver).stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }
}
