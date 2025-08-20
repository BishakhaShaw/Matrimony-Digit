package digit.matrimony.controller;

import digit.matrimony.dto.MessageDTO;
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
    public ResponseEntity<MessageDTO> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content) {
        return ResponseEntity.ok(messageService.sendMessage(senderId, receiverId, content));
    }

    @GetMapping("/between")
    public ResponseEntity<List<MessageDTO>> getMessagesBetweenUsers(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(messageService.getMessagesBetweenUsers(senderId, receiverId));
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<MessageDTO>> getMessagesReceived(@PathVariable Long receiverId) {
        return ResponseEntity.ok(messageService.getMessagesReceivedByUser(receiverId));
    }
}
