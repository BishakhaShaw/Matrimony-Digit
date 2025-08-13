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
public class Role {
    // Not generated because schema has SMALLINT PRIMARY KEY (you may insert fixed ids)
    @Id
    private Short id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
