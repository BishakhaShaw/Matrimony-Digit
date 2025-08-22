

package digit.matrimony.repository;

import digit.matrimony.entity.Role;
import digit.matrimony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Find by username/email (case-sensitive)
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // 🔍 Case-insensitive search
    Optional<User> findByUsernameIgnoreCase(String username);

    // 🔍 Case-insensitive login support (username OR email)
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    // 📊 Counts
    long countByIsActiveTrue();
    long countBySubscriptionType(String type);

    // ✅ Existence checks
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByRole(Role role);
    List<User> findByRoleName(String roleName);

}
