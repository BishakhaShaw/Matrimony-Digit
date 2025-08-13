package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sender of interest
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User sender;

    // Receiver of interest
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User receiver;

    private String status = "pending";

    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}
