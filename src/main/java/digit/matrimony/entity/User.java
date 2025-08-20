


package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "password_salt", nullable = false, length = 255)
    private String passwordSalt;

    @Column(name = "permanent_location", length = 100)
    private String permanentLocation;

    //  Role
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_users_roles"))
    private Role role;

    //  Family Member ↔ Linked User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_user_id", foreignKey = @ForeignKey(name = "fk_users_linked_users"))
    @ToString.Exclude
    private User linkedUser;

    //  Manager ↔ Users (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "manager_user_mapping",
            joinColumns = @JoinColumn(name = "manager_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    private Set<User> managedUsers = new HashSet<>();

    @ManyToMany(mappedBy = "managedUsers")
    @ToString.Exclude
    private Set<User> managers = new HashSet<>();

    //  Subscription & Status
    @Builder.Default
    @Column(name = "subscription_type", length = 1)
    private String subscriptionType = "N";

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
