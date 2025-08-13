package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "manager_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // manager who performed the action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    // target user of action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User targetUser;

    @Column(name = "action_type", length = 50)
    private String actionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Match match;

    @Column(name = "action_timestamp")
    private LocalDateTime actionTimestamp;

    @Column(columnDefinition = "TEXT")
    private String notes;
}