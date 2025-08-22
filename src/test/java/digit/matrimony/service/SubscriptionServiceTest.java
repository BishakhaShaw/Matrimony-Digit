package digit.matrimony.service;

import digit.matrimony.dto.SubscriptionRequestDTO;
import digit.matrimony.entity.Role;
import digit.matrimony.entity.Subscription;
import digit.matrimony.entity.User;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.repository.SubscriptionRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private User user;
    private Subscription subscription;
    private SubscriptionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setSubscriptionType("F");

        user.setRole(new Role(Short.valueOf("3"), "USER"));


        subscription = new Subscription();
        subscription.setId(100L);
        subscription.setUser(user);
        subscription.setAmount(new BigDecimal("999"));
        subscription.setPlanName("Premium");
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));

        requestDTO = new SubscriptionRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setAmount(new BigDecimal("999"));
        requestDTO.setPlanName("Premium");
        requestDTO.setStartDate(LocalDate.now());
        requestDTO.setEndDate(LocalDate.now().plusMonths(1));
    }

    @Test
    void testGetAllSubscriptions() {
        when(subscriptionRepository.findAll()).thenReturn(List.of(subscription));
        List<Subscription> result = subscriptionService.getAllSubscriptions();
        assertEquals(1, result.size());
    }

    @Test
    void testGetSubscriptionById_Success() {
        when(subscriptionRepository.findById(100L)).thenReturn(Optional.of(subscription));
        Subscription result = subscriptionService.getSubscriptionById(100L);
        assertEquals(100L, result.getId());
    }

    @Test
    void testGetSubscriptionById_NotFound() {
        when(subscriptionRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subscriptionService.getSubscriptionById(100L));
    }

    @Test
    void testCreateSubscription_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.existsByUserIdAndPlanNameAndStartDateAndEndDate(
                1L, "Premium", requestDTO.getStartDate(), requestDTO.getEndDate())).thenReturn(false);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        when(userRepository.save(any(User.class))).thenReturn(user);

        Subscription result = subscriptionService.createSubscription(requestDTO);
        assertEquals("Premium", result.getPlanName());
        assertEquals("P", user.getSubscriptionType());
    }

    @Test
    void testCreateSubscription_AlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.existsByUserIdAndPlanNameAndStartDateAndEndDate(
                1L, "Premium", requestDTO.getStartDate(), requestDTO.getEndDate())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> subscriptionService.createSubscription(requestDTO));
    }

    @Test
    void testCreateSubscription_InvalidRole() {

        user.setRole(new Role(Short.valueOf("2"), "MANAGER"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> subscriptionService.createSubscription(requestDTO));
    }

    @Test
    void testUpdateSubscription_Success() {
        when(subscriptionRepository.findById(100L)).thenReturn(Optional.of(subscription));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionService.updateSubscription(100L, requestDTO);
        assertEquals("Premium", result.getPlanName());
    }

    @Test
    void testDeleteSubscription_Success() {
        when(subscriptionRepository.findById(100L)).thenReturn(Optional.of(subscription));
        doNothing().when(subscriptionRepository).delete(subscription);

        subscriptionService.deleteSubscription(100L);
        verify(subscriptionRepository, times(1)).delete(subscription);
    }

    @Test
    void testHasActiveSubscription() {
        when(subscriptionRepository.findActiveSubscriptionByUserId(1L)).thenReturn(Optional.of(subscription));
        boolean result = subscriptionService.hasActiveSubscription(1L);
        assertTrue(result);
    }
}

