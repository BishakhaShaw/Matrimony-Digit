package digit.matrimony.mapper;

import digit.matrimony.dto.InterestDTO;
import digit.matrimony.entity.Interest;
import digit.matrimony.entity.User;
import org.springframework.stereotype.Component;

@Component
public class InterestMapper {

    public InterestDTO toDto(Interest interest) {
        if (interest == null) return null;

        return InterestDTO.builder()
                .id(interest.getId())
                .senderId(interest.getSender() != null ? interest.getSender().getId() : null)
                .receiverId(interest.getReceiver() != null ? interest.getReceiver().getId() : null)
                .status(interest.getStatus())
                .sentAt(interest.getSentAt())
                .build();
    }

    public Interest toEntity(InterestDTO dto, User sender, User receiver) {
        if (dto == null) return null;

        return Interest.builder()
                .id(dto.getId())
                .sender(sender)
                .receiver(receiver)
                .status(dto.getStatus() != null ? dto.getStatus() : "pending")
                .sentAt(dto.getSentAt())
                .build();
    }
}
