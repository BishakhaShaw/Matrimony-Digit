package digit.matrimony.repository;

import digit.matrimony.entity.FamilyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyFeedbackRepository extends JpaRepository<FamilyFeedback, Long> {
}
