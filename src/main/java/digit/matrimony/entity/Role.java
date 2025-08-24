
package digit.matrimony.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name = "uk_roles_name", columnNames = {"name"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;


    @Column(nullable = false, length = 50)
    private String name; // ADMIN, USER, MANAGER, FAMILY_MEMBER
}