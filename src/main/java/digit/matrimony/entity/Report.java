package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // who reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User reporter;

    // who was reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User reportedUser;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private String status = "open";

    // reviewed by user (manager/admin)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User reviewedByUser;

    @Column(name = "escalated_to_admin")
    private Boolean escalatedToAdmin = false;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;
}