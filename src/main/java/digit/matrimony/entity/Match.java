package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user1;

    // user2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user2;

    @Column(name = "matched_at")
    private LocalDateTime matchedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "deleted_by", length = 50)
    private String deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
