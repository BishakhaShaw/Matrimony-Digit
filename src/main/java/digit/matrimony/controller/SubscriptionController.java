package digit.matrimony.controller;

import digit.matrimony.dto.SubscriptionRequestDTO;
import digit.matrimony.dto.SubscriptionResponseDTO;
import digit.matrimony.entity.Subscription;
import digit.matrimony.mapper.SubscriptionMapper;
import digit.matrimony.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions()
                .stream()
                .map(SubscriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SubscriptionResponseDTO getSubscriptionById(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id);
        return SubscriptionMapper.toDTO(subscription);
    }

    @PostMapping
    public ResponseEntity<?> createSubscription(@RequestBody SubscriptionRequestDTO dto) {
        try {
            Subscription created = subscriptionService.createSubscription(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(SubscriptionMapper.toDTO(created));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscription(@PathVariable Long id, @RequestBody SubscriptionRequestDTO dto) {
        try {
            Subscription updated = subscriptionService.updateSubscription(id, dto);
            return ResponseEntity.ok(SubscriptionMapper.toDTO(updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
