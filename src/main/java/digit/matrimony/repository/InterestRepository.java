package digit.matrimony.repository;

import digit.matrimony.entity.Interest;
import digit.matrimony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findBySender(User sender);
    List<Interest> findByReceiver(User receiver);
}
