package digit.matrimony.repository;

import digit.matrimony.entity.Match;
import digit.matrimony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    long countByIsActiveTrue();
    List<Match> findByUser1OrUser2(User user1, User user2);
    @Query("SELECT m FROM Match m WHERE (m.user1.id = :user1Id AND m.user2.id = :user2Id) OR (m.user1.id = :user2Id AND m.user2.id = :user1Id)")

    Optional<Match> findByUserIds(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Match m WHERE m.user1 = :user1 AND m.user2 = :user2 AND m.matchedAt IS NOT NULL")
    boolean existsMatchedBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT COUNT(m) FROM Match m WHERE (m.user1.id = :userId OR m.user2.id = :userId) AND m.isActive = true")
    int countActiveMatchesByUserId(@Param("userId") Long userId);




}
