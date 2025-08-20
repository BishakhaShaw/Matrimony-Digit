package digit.matrimony.mapper;

import digit.matrimony.dto.ReportRequestDTO;
import digit.matrimony.dto.ReportResponseDTO;
import digit.matrimony.entity.Report;

public class ReportMapper {

    public static Report toEntity(ReportRequestDTO dto) {
        return Report.builder()
                .reason(dto.getReason())
                .status("open")
                .escalatedToAdmin(false)
                .reportedAt(java.time.LocalDateTime.now())
                .build();
    }

    public static ReportResponseDTO toDTO(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reportedUserId(report.getReportedUser().getId())
                .reason(report.getReason())
                .status(report.getStatus())
                .reviewedByUserId(report.getReviewedByUser() != null ? report.getReviewedByUser().getId() : null)
                .escalatedToAdmin(report.getEscalatedToAdmin())
                .reportedAt(report.getReportedAt())
                .build();
    }
}
