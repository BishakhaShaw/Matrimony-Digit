

package digit.matrimony.repository;

import digit.matrimony.entity.Role;
import digit.matrimony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username/email
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    long countByIsActiveTrue();
    long countBySubscriptionType(String type);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Case-insensitive login support
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    boolean existsByRole(Role role);
}
