package digit.matrimony.service;

import digit.matrimony.dto.InterestDTO;
import digit.matrimony.entity.Interest;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.InterestMapper;
import digit.matrimony.repository.InterestRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class InterestServiceTest {

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterestMapper interestMapper;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private InterestService interestService;

    @Test
    void testSendInterest_success() {
        Long senderId = 1L;
        Long receiverId = 2L;

        User sender = new User(); sender.setId(senderId);
        User receiver = new User(); receiver.setId(receiverId);

        Interest interest = Interest.builder()
                .sender(sender)
                .receiver(receiver)
                .status("pending")
                .sentAt(LocalDateTime.now())
                .build();

        InterestDTO expectedDto = new InterestDTO();
        expectedDto.setSenderId(senderId);
        expectedDto.setReceiverId(receiverId);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(matchService.getActiveMatchCount(senderId)).thenReturn(0);
        when(interestRepository.save(any())).thenReturn(interest);
        when(interestMapper.toDto(any())).thenReturn(expectedDto);

        InterestDTO result = interestService.sendInterest(senderId, receiverId);

        assertNotNull(result);
        assertEquals(senderId, result.getSenderId());
        assertEquals(receiverId, result.getReceiverId());
    }

    @Test
    void testSendInterest_exceedsMatchLimit_shouldThrowBadRequest() {
        Long senderId = 1L;
        Long receiverId = 2L;

        User sender = new User(); sender.setId(senderId);
        User receiver = new User(); receiver.setId(receiverId);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(matchService.getActiveMatchCount(senderId)).thenReturn(3);

        assertThrows(BadRequestException.class, () -> {
            interestService.sendInterest(senderId, receiverId);
        });
    }

    @Test
    void testAcceptInterest_success() {
        Long interestId = 10L;
        User sender = new User(); sender.setId(1L);
        User receiver = new User(); receiver.setId(2L);

        Interest interest = Interest.builder()
                .id(interestId)
                .sender(sender)
                .receiver(receiver)
                .status("pending")
                .build();

        InterestDTO expectedDto = new InterestDTO();
        expectedDto.setSenderId(sender.getId());
        expectedDto.setReceiverId(receiver.getId());

        when(interestRepository.findById(interestId)).thenReturn(Optional.of(interest));
        when(interestRepository.save(any())).thenReturn(interest);
        when(interestMapper.toDto(any())).thenReturn(expectedDto);

        InterestDTO result = interestService.acceptInterest(interestId);

        assertEquals(sender.getId(), result.getSenderId());
        assertEquals(receiver.getId(), result.getReceiverId());
        verify(matchService).createMatch(sender.getId(), receiver.getId());
    }

    @Test
    void testRejectInterest_success() {
        Long interestId = 20L;
        User sender = new User(); sender.setId(1L);
        User receiver = new User(); receiver.setId(2L);

        Interest interest = Interest.builder()
                .id(interestId)
                .sender(sender)
                .receiver(receiver)
                .status("pending")
                .build();

        InterestDTO expectedDto = new InterestDTO();
        expectedDto.setSenderId(sender.getId());
        expectedDto.setReceiverId(receiver.getId());

        when(interestRepository.findById(interestId)).thenReturn(Optional.of(interest));
        when(interestRepository.save(any())).thenReturn(interest);
        when(interestMapper.toDto(any())).thenReturn(expectedDto);

        InterestDTO result = interestService.rejectInterest(interestId);

        assertEquals("rejected", interest.getStatus());
        assertEquals(sender.getId(), result.getSenderId());
        assertEquals(receiver.getId(), result.getReceiverId());
    }

    @Test
    void testRejectInterest_notPending_shouldThrowBadRequest() {
        Long interestId = 30L;
        Interest interest = Interest.builder()
                .id(interestId)
                .status("accepted")
                .build();

        when(interestRepository.findById(interestId)).thenReturn(Optional.of(interest));

        assertThrows(BadRequestException.class, () -> {
            interestService.rejectInterest(interestId);
        });
    }
}
