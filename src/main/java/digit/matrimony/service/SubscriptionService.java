package digit.matrimony.service;

import digit.matrimony.dto.SubscriptionRequestDTO;
import digit.matrimony.entity.Subscription;
import digit.matrimony.entity.User;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.SubscriptionMapper;
import digit.matrimony.repository.SubscriptionRepository;
import digit.matrimony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
    }

    public Subscription createSubscription(SubscriptionRequestDTO dto) {
        validateUserId(dto.getUserId());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        // Check roleId
        if (user.getRole() == null || user.getRole().getId() != 3) {
            throw new IllegalArgumentException("Only users with role ID = 3 can subscribe.");
        }

        // Check for existing identical subscription
        boolean exists = subscriptionRepository.existsByUserIdAndPlanNameAndStartDateAndEndDate(
                dto.getUserId(),
                dto.getPlanName(),
                dto.getStartDate(),
                dto.getEndDate()
        );

        if (exists) {
            throw new IllegalStateException("User already has this subscription.");
        }

        Subscription subscription = SubscriptionMapper.toEntity(dto);
        subscription.setUser(user);

        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long id, SubscriptionRequestDTO dto) {
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));

        validateUserId(dto.getUserId());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        if (user.getRole() == null || user.getRole().getId() != 3) {
            throw new IllegalArgumentException("Only users with role ID = 3 can subscribe.");
        }

        Subscription updated = SubscriptionMapper.toEntity(dto);
        updated.setId(id);
        updated.setUser(user);

        return subscriptionRepository.save(updated);
    }

    public void deleteSubscription(Long id) {
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
        subscriptionRepository.delete(existing);
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must be provided.");
        }
    }

    public boolean hasActiveSubscription(Long userId) {
        return subscriptionRepository.findActiveSubscriptionByUserId(userId).isPresent();
    }
}
