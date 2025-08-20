package digit.matrimony.repository;

import digit.matrimony.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // ✅ Count subscriptions started in a date range
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.startDate >= :start AND s.startDate < :end")
    long countSoldLastMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // ✅ Sum revenue in a date range
    @Query("SELECT SUM(s.amount) FROM Subscription s WHERE s.startDate >= :start AND s.startDate < :end")
    BigDecimal sumRevenueLastMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
