package digit.matrimony.repository;

import digit.matrimony.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByUserIdAndPlanNameAndStartDateAndEndDate(
            Long userId,
            String planName,
            LocalDate startDate,
            LocalDate endDate
    );


    //  Count subscriptions started in a date range
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.startDate >= :start AND s.startDate < :end")
    long countSoldLastMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);

    //  Sum revenue in a date range
    @Query("SELECT SUM(s.amount) FROM Subscription s WHERE s.startDate >= :start AND s.startDate < :end")
    BigDecimal sumRevenueLastMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.endDate >= CURRENT_DATE AND s.paymentStatus = 'completed'")
    Optional<Subscription> findActiveSubscriptionByUserId(@Param("userId") Long userId);
}
