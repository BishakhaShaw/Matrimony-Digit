package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "suggestions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // who receives suggestion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    // suggested user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggested_user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User suggestedUser;

    @Column(name = "match_score", precision = 5, scale = 2)
    private BigDecimal matchScore;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}