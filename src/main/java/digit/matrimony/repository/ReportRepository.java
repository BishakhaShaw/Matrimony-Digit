package digit.matrimony.repository;

import digit.matrimony.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE r.reporter.id = :reporterId AND r.reportedUser.id = :reportedUserId AND r.reason = :reason")
    Optional<Report> findDuplicateReport(@Param("reporterId") Long reporterId,
                                         @Param("reportedUserId") Long reportedUserId,
                                         @Param("reason") String reason);

}
