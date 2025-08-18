package digit.matrimony.repository;

import digit.matrimony.entity.Match;
import digit.matrimony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    long countByIsActiveTrue();
    List<Match> findByUser1OrUser2(User user1, User user2);
}
