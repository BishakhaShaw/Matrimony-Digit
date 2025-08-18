package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "family_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // family user (must be a user record with role family member)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User familyUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewed_profile_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profile viewedProfile;

    @Column(name = "feedback_flag", length = 10)
    private String feedbackFlag;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "feedback_date")
    private LocalDateTime feedbackDate;


}