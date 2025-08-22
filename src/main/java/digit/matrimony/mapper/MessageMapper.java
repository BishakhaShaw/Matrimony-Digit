package digit.matrimony.mapper;

import digit.matrimony.dto.MessageDTO;
import digit.matrimony.entity.Message;
import digit.matrimony.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDTO toDto(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .build();
    }

    public Message toEntity(MessageDTO dto, User sender, User receiver) {
        return Message.builder()
                .id(dto.getId())
                .sender(sender)
                .receiver(receiver)
                .message(dto.getMessage())
                .sentAt(dto.getSentAt())
                .build();
    }
}
