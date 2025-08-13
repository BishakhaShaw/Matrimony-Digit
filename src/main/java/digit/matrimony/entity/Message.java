package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // sender
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User sender;

    // receiver
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}