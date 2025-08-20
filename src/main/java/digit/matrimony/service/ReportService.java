package digit.matrimony.service;

import digit.matrimony.dto.ReportDTO;
import digit.matrimony.entity.Report;
import digit.matrimony.entity.User;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ReportMapper;
import digit.matrimony.repository.ReportRepository;
import digit.matrimony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReportDTO getReportById(Long id) {
        return reportRepository.findById(id)
                .map(ReportMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    public ReportDTO createReport(ReportDTO dto) {
        if (dto.getReporterId().equals(dto.getReportedUserId())) {
            throw new IllegalArgumentException("Reporter and reported user cannot be the same.");
        }

        Report report = ReportMapper.toEntity(dto);

        User reporter = userRepository.findById(dto.getReporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporter user not found with id: " + dto.getReporterId()));
        report.setReporter(reporter);

        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reported user not found with id: " + dto.getReportedUserId()));
        report.setReportedUser(reportedUser);

        if (dto.getReviewedByUserId() != null) {
            User reviewer = userRepository.findById(dto.getReviewedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reviewer user not found with id: " + dto.getReviewedByUserId()));
            report.setReviewedByUser(reviewer);
        }

        Report saved = reportRepository.save(report);
        return ReportMapper.toDTO(saved);
    }

        //  Validate reviewer if provided

    public ReportDTO updateReport(Long id, ReportDTO dto) {
        Report existing = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        Report updated = ReportMapper.toEntity(dto);
        updated.setId(id);

        //  Validate reporter
        User reporter = userRepository.findById(dto.getReporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporter user not found with id: " + dto.getReporterId()));
        updated.setReporter(reporter);

        //  Validate reported user
        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reported user not found with id: " + dto.getReportedUserId()));
        updated.setReportedUser(reportedUser);

        //  Validate reviewer if provided
        if (dto.getReviewedByUserId() != null) {
            User reviewer = userRepository.findById(dto.getReviewedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reviewer user not found with id: " + dto.getReviewedByUserId()));
            updated.setReviewedByUser(reviewer);
        }

        Report saved = reportRepository.save(updated);
        return ReportMapper.toDTO(saved);
    }

    public void deleteReport(Long id) {
        Report existing = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        reportRepository.delete(existing);
    }
}
