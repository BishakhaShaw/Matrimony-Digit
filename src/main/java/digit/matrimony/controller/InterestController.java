package digit.matrimony.controller;

import digit.matrimony.dto.InterestCreateRequestDTO;
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
    public ResponseEntity<InterestDTO> sendInterest(@RequestBody InterestCreateRequestDTO request) {
        InterestDTO interest = interestService.sendInterest(request.getSenderId(), request.getReceiverId());
        return ResponseEntity.ok(interest);
    }


    @PutMapping("/{interestId}/accept")
    public ResponseEntity<InterestDTO> acceptInterest(@PathVariable Long interestId) {
        return ResponseEntity.ok(interestService.acceptInterest(interestId));
    }

    @PutMapping("/{interestId}/reject")
    public ResponseEntity<InterestDTO> rejectInterest(@PathVariable Long interestId) {
        return ResponseEntity.ok(interestService.rejectInterest(interestId));
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

    @DeleteMapping("/{interestId}")
    public ResponseEntity<Void> deleteInterest(@PathVariable Long interestId) {
        interestService.deleteInterest(interestId);
        return ResponseEntity.noContent().build();
    }
}
