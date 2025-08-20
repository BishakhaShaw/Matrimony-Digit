package digit.matrimony.repository;

import digit.matrimony.entity.ManagerAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerActionRepository extends JpaRepository<ManagerAction, Long> {
}
