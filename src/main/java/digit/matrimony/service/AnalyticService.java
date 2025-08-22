package digit.matrimony.service;

import digit.matrimony.dto.AnalyticDTO;
import digit.matrimony.entity.Analytic;
import digit.matrimony.mapper.AnalyticMapper;
import digit.matrimony.repository.AnalyticRepository;
import digit.matrimony.repository.MatchRepository;
import digit.matrimony.repository.SubscriptionRepository;
import digit.matrimony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AnalyticService {

    @Autowired private UserRepository userRepository;
    @Autowired private MatchRepository matchRepository;
    @Autowired private SubscriptionRepository subscriptionRepository;
    @Autowired private AnalyticRepository analyticRepository;

    public AnalyticDTO generateAnalyticsSnapshot(String region) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = startOfLastMonth.plusMonths(1);

        Analytic analytic = Analytic.builder()
                .region(region)
                .recordedAt(now)
                .totalUsers((int) userRepository.count())
                .activeUsers((int) userRepository.countByIsActiveTrue())
                .premiumUsers((int) userRepository.countBySubscriptionType("P"))
                .activeMatches((int) matchRepository.countByIsActiveTrue())
                .subscriptionsSoldLastMonth((int) subscriptionRepository.countSoldLastMonth(startOfLastMonth, endOfLastMonth))
                .revenueLastMonth(
                        Optional.ofNullable(subscriptionRepository.sumRevenueLastMonth(startOfLastMonth, endOfLastMonth))
                                .orElse(BigDecimal.ZERO)
                )
                .build();

        analyticRepository.save(analytic); // Persist to DB

        return AnalyticMapper.toDTO(analytic);
    }
}
