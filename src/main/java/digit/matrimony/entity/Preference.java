package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // preferences belong to a user

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;


    @Column(name = "preferred_age_min")
    private Integer preferredAgeMin;

    @Column(name = "preferred_age_max")
    private Integer preferredAgeMax;

    @Column(name = "preferred_religion", length = 50)
    private String preferredReligion;

    @Column(name = "preferred_caste", length = 50)
    private String preferredCaste;

    @Column(name = "preferred_location", length = 100)
    private String preferredLocation;

    @Column(name = "preferred_education", length = 100)
    private String preferredEducation;

    @Column(name = "preferred_marital_status", length = 20)
    private String preferredMaritalStatus;

    @Column(name = "preferred_gender", length = 10)
    private String preferredGender;

}