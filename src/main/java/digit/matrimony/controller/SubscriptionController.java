package digit.matrimony.controller;

import digit.matrimony.dto.SubscriptionDTO;
import digit.matrimony.entity.Subscription;
import digit.matrimony.mapper.SubscriptionMapper;
import digit.matrimony.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions()
                .stream()
                .map(SubscriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SubscriptionDTO getSubscriptionById(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id);
        return SubscriptionMapper.toDTO(subscription);
    }

    @PostMapping
    public SubscriptionDTO createSubscription(@RequestBody SubscriptionDTO dto) {
        Subscription created = subscriptionService.createSubscription(dto);
        return SubscriptionMapper.toDTO(created);
    }

    @PutMapping("/{id}")
    public SubscriptionDTO updateSubscription(@PathVariable Long id, @RequestBody SubscriptionDTO dto) {
        Subscription updated = subscriptionService.updateSubscription(id, dto);
        return SubscriptionMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
    }
}
