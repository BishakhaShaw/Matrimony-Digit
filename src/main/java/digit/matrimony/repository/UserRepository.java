
package digit.matrimony.repository;

import digit.matrimony.entity.Role;
import digit.matrimony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    long countByIsActiveTrue();

    long countBySubscriptionType(String type);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    List<User> findByRoleName(String roleName);

}
