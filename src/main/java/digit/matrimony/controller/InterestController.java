package digit.matrimony.controller;

import digit.matrimony.dto.InterestDTO;
import digit.matrimony.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @PostMapping("/send")
    public ResponseEntity<InterestDTO> sendInterest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        InterestDTO interest = interestService.sendInterest(senderId, receiverId);
        return ResponseEntity.ok(interest);
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<InterestDTO>> getSentInterests(@PathVariable Long senderId) {
        List<InterestDTO> interests = interestService.getInterestsSentByUser(senderId);
        return ResponseEntity.ok(interests);
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<InterestDTO>> getReceivedInterests(@PathVariable Long receiverId) {
        List<InterestDTO> interests = interestService.getInterestsReceivedByUser(receiverId);
        return ResponseEntity.ok(interests);
    }

    @PutMapping("/{interestId}/status")
    public ResponseEntity<InterestDTO> updateStatus(@PathVariable Long interestId, @RequestParam String status) {
        InterestDTO updated = interestService.updateInterestStatus(interestId, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{interestId}")
    public ResponseEntity<Void> deleteInterest(@PathVariable Long interestId) {
        interestService.deleteInterest(interestId);
        return ResponseEntity.noContent().build();
    }
}
