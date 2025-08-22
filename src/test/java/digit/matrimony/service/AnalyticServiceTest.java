package digit.matrimony.service;

import digit.matrimony.dto.AnalyticDTO;
import digit.matrimony.entity.Analytic;
import digit.matrimony.mapper.AnalyticMapper;
import digit.matrimony.repository.AnalyticRepository;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.SubscriptionRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyticServiceTest {

    @InjectMocks
    private AnalyticService analyticService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private AnalyticRepository analyticRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAnalyticsSnapshot() {
        String region = "West";

        // Mock repository responses
        when(userRepository.count()).thenReturn(100L);
        when(userRepository.countByIsActiveTrue()).thenReturn(80L);
        when(userRepository.countBySubscriptionType("P")).thenReturn(50L);
        when(matchRepository.countByIsActiveTrue()).thenReturn(30L);
        when(subscriptionRepository.countSoldLastMonth(any(LocalDate.class), any(LocalDate.class))).thenReturn(20L);
        when(subscriptionRepository.sumRevenueLastMonth(any(LocalDate.class), any(LocalDate.class))).thenReturn(new BigDecimal("1500.00"));

        // Capture the saved Analytic entity
        ArgumentCaptor<Analytic> analyticCaptor = ArgumentCaptor.forClass(Analytic.class);
        when(analyticRepository.save(any(Analytic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        AnalyticDTO result = analyticService.generateAnalyticsSnapshot(region);

        // Verify repository interactions
        verify(userRepository).count();
        verify(userRepository).countByIsActiveTrue();
        verify(userRepository).countBySubscriptionType("P");
        verify(matchRepository).countByIsActiveTrue();
        verify(subscriptionRepository).countSoldLastMonth(any(LocalDate.class), any(LocalDate.class));
        verify(subscriptionRepository).sumRevenueLastMonth(any(LocalDate.class), any(LocalDate.class));
        verify(analyticRepository).save(analyticCaptor.capture());

        // Assert values
        Analytic saved = analyticCaptor.getValue();
        assertEquals(region, saved.getRegion());
        assertEquals(100, saved.getTotalUsers());
        assertEquals(80, saved.getActiveUsers());
        assertEquals(50, saved.getPremiumUsers());
        assertEquals(30, saved.getActiveMatches());
        assertEquals(20, saved.getSubscriptionsSoldLastMonth());
        assertEquals(new BigDecimal("1500.00"), saved.getRevenueLastMonth());

        assertEquals(region, result.getRegion());
        assertEquals(100, result.getTotalUsers());
    }
}
