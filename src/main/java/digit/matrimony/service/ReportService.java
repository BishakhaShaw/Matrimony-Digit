package digit.matrimony.service;

import digit.matrimony.dto.ReportRequestDTO;
import digit.matrimony.dto.ReportResponseDTO;
import digit.matrimony.entity.Report;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ReportMapper;
import digit.matrimony.repository.ReportRepository;
import digit.matrimony.repository.SubscriptionRepository;
import digit.matrimony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<ReportResponseDTO> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReportResponseDTO getReportById(Long id) {
        return reportRepository.findById(id)
                .map(ReportMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    public ReportResponseDTO createReport(ReportRequestDTO dto) {
        // Prevent duplicate report with same reason
        Optional<Report> existing = reportRepository.findDuplicateReport(dto.getReporterId(), dto.getReportedUserId(), dto.getReason());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("You have already reported this user for the same reason.");
        }

        if (dto.getReporterId().equals(dto.getReportedUserId())) {
            throw new BadRequestException("Reporter and reported user cannot be the same.");
        }

        User reporter = userRepository.findById(dto.getReporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporter user not found with id: " + dto.getReporterId()));
        if (reporter.getRole() == null || reporter.getRole().getId() != 3) {
            throw new BadRequestException("Reporter must be a user with role ID 3.");
        }

        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reported user not found with id: " + dto.getReportedUserId()));
        if (reportedUser.getRole() == null || reportedUser.getRole().getId() != 3) {
            throw new BadRequestException("Reported user must be a user with role ID 3.");
        }

        Optional<User> manager = userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && user.getRole().getId() == 2)
                .findFirst();
        if (manager.isEmpty()) {
            throw new ResourceNotFoundException("No manager available to review the report.");
        }

        Report report = ReportMapper.toEntity(dto);
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReviewedByUser(manager.get());

        //  Escalate to admin if reporter has active subscription
        Optional<User> admin = userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && user.getRole().getId() == 1)
                .findFirst();

        boolean isSubscribed = subscriptionRepository.findActiveSubscriptionByUserId(dto.getReporterId()).isPresent();
        if (isSubscribed) {
            report.setEscalatedToAdmin(true);
        }


        Report saved = reportRepository.save(report);
        return ReportMapper.toDTO(saved);
    }

    public ReportResponseDTO updateReport(Long id, ReportRequestDTO dto) {
        Report existing = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        if (dto.getReporterId().equals(dto.getReportedUserId())) {
            throw new BadRequestException("Reporter and reported user cannot be the same.");
        }

        User reporter = userRepository.findById(dto.getReporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporter user not found with id: " + dto.getReporterId()));
        if (reporter.getRole() == null || reporter.getRole().getId() != 3) {
            throw new BadRequestException("Reporter must be a user with role ID 3.");
        }

        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reported user not found with id: " + dto.getReportedUserId()));
        if (reportedUser.getRole() == null || reportedUser.getRole().getId() != 3) {
            throw new BadRequestException("Reported user must be a user with role ID 3.");
        }

        Optional<User> manager = userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && user.getRole().getId() == 2)
                .findFirst();
        if (manager.isEmpty()) {
            throw new ResourceNotFoundException("No manager available to review the report.");
        }

        existing.setReason(dto.getReason());
        existing.setReporter(reporter);
        existing.setReportedUser(reportedUser);
        existing.setReviewedByUser(manager.get());

        Report saved = reportRepository.save(existing);
        return ReportMapper.toDTO(saved);
    }

    public void deleteReport(Long id) {
        Report existing = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        reportRepository.delete(existing);
    }
}
