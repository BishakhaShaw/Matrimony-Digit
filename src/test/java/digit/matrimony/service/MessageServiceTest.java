package digit.matrimony.service;

import digit.matrimony.dto.MessageDTO;
import digit.matrimony.dto.MessageRequestDTO;
import digit.matrimony.entity.Message;
import digit.matrimony.entity.User;
import digit.matrimony.enums.MessageStatus;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.MessageMapper;
import digit.matrimony.repository.MessageRepository;
import digit.matrimony.repository.UserRepository;
import digit.matrimony.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    private User sender;
    private User receiver;
    private Message message;
    private MessageDTO messageDTO;
    private MessageRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1L);

        receiver = new User();
        receiver.setId(2L);

        message = Message.builder()
                .id(100L)
                .sender(sender)
                .receiver(receiver)
                .message("Hello")
                .status(MessageStatus.SENT)
                .build();

        messageDTO = new MessageDTO();
        messageDTO.setId(100L);
        messageDTO.setSenderId(1L);
        messageDTO.setReceiverId(2L);
        messageDTO.setMessage("Hello");

        requestDTO = new MessageRequestDTO();
        requestDTO.setSenderId(1L);
        requestDTO.setReceiverId(2L);
        requestDTO.setMessage("Hello");
    }

    @Test
    void testSendMessage_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(matchRepository.existsMatchedBetweenUsers(sender, receiver)).thenReturn(true);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.toDto(any(Message.class))).thenReturn(messageDTO);

        MessageDTO result = messageService.sendMessage(requestDTO);
        assertEquals("Hello", result.getMessage());
    }

    @Test
    void testSendMessage_NotMatched() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(matchRepository.existsMatchedBetweenUsers(sender, receiver)).thenReturn(false);
        when(matchRepository.existsMatchedBetweenUsers(receiver, sender)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> messageService.sendMessage(requestDTO));
    }

    @Test
    void testGetMessagesBetweenUsers() {
        Page<Message> messagePage = new PageImpl<>(List.of(message));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(messageRepository.findConversationBetweenUsers(eq(sender), eq(receiver), any(Pageable.class)))
                .thenReturn(messagePage);
        when(messageMapper.toDto(message)).thenReturn(messageDTO);

        List<MessageDTO> result = messageService.getMessagesBetweenUsers(1L, 2L, 0, 10);
        assertEquals(1, result.size());
        assertEquals("Hello", result.get(0).getMessage());
    }

    @Test
    void testGetMessagesReceivedByUser() {
        Page<Message> receivedPage = new PageImpl<>(List.of(message));

        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(messageRepository.findByReceiver(eq(receiver), any(Pageable.class)))
                .thenReturn(receivedPage);
        when(messageMapper.toDto(message)).thenReturn(messageDTO);

        List<MessageDTO> result = messageService.getMessagesReceivedByUser(2L, 0, 10);
        assertEquals(1, result.size());
        assertEquals("Hello", result.get(0).getMessage());
    }

    @Test
    void testMarkAsRead() {
        when(messageRepository.findById(100L)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        messageService.markAsRead(100L);
        assertEquals(MessageStatus.READ, message.getStatus());
    }

    @Test
    void testDeleteMessage() {
        when(messageRepository.findById(100L)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        messageService.deleteMessage(100L);
        assertEquals(MessageStatus.DELETED, message.getStatus());
    }
}
