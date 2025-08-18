package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Profile belongs to a user. One-to-One (profile.user -> users.id)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    private String gender;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    private String phone;

    @Column(name = "aadhaar_number", unique = true, length = 12)
    private String aadhaarNumber;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String religion;
    private String caste;
    private String education;
    private String occupation;
    private String income;

    @Column(name = "current_location", length = 100)
    private String currentLocation;

    private BigDecimal height;

    @Column(name = "marital_status", length = 20)
    private String maritalStatus;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<ProfilePhoto> photos = new ArrayList<>();
}
