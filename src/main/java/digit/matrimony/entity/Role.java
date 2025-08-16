//package digit.matrimony.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "roles")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Role {
//
//    /**
//     * Role ID — SMALLINT because roles are limited and predefined.
//     * Example IDs: 1 = Admin, 2 = Manager, 3 = User, 4 = Family Member
//     */
//    @Id
//    private Short id;
//
//    /**
//     * Name of the role (Admin, Manager, User, Family Member)
//     */
//    @Column(nullable = false, unique = true, length = 50)
//    private String name;
//}










package digit.matrimony.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    /**
     * Role ID — SMALLINT because roles are limited and predefined.
     * Example IDs: 1 = Admin, 2 = Manager, 3 = User, 4 = Family Member
     */
    @Id
    @EqualsAndHashCode.Include
    private Short id;

    /**
     * Name of the role (Admin, Manager, User, Family Member)
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
