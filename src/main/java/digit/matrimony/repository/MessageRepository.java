package digit.matrimony.repository;

import digit.matrimony.entity.Message;
import digit.matrimony.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.receiver = :user2) OR " +
            "(m.sender = :user2 AND m.receiver = :user1)")
    Page<Message> findConversationBetweenUsers(@Param("user1") User user1,
                                               @Param("user2") User user2,
                                               Pageable pageable);


    Page<Message> findByReceiver(User receiver, Pageable pageable);
}
