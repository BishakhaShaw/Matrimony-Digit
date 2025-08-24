package digit.matrimony.controller;

import digit.matrimony.dto.MessageDTO;
import digit.matrimony.dto.MessageRequestDTO;
import digit.matrimony.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageRequestDTO request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    @GetMapping("/between")
    public ResponseEntity<List<MessageDTO>> getMessagesBetweenUsers(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(messageService.getMessagesBetweenUsers(user1Id, user2Id, page, size));
    }

    @GetMapping("/received")
    public ResponseEntity<List<MessageDTO>> getMessagesReceived(
            @RequestParam Long receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(messageService.getMessagesReceivedByUser(receiverId, page, size));
    }

    @PutMapping("/mark-read/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
