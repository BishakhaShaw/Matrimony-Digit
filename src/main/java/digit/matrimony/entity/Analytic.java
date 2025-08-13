package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analytic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String region;
    private Integer totalUsers;
    private Integer activeUsers;
    private Integer premiumUsers;
    private Integer activeMatches;
    private Integer subscriptionsSoldLastMonth;

    @Column(name = "revenue_last_month", precision = 10, scale = 2)
    private BigDecimal revenueLastMonth;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
}