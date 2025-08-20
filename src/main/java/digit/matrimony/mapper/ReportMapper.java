package digit.matrimony.mapper;

import digit.matrimony.dto.ReportDTO;
import digit.matrimony.entity.Report;

public class ReportMapper {

    public static ReportDTO toDTO(Report report) {
        if (report == null) return null;

        return ReportDTO.builder()
                .id(report.getId())
                .reporterId(report.getReporter() != null ? report.getReporter().getId() : null)
                .reportedUserId(report.getReportedUser() != null ? report.getReportedUser().getId() : null)
                .reason(report.getReason())
                .status(report.getStatus())
                .reviewedByUserId(report.getReviewedByUser() != null ? report.getReviewedByUser().getId() : null)
                .escalatedToAdmin(report.getEscalatedToAdmin())
                .reportedAt(report.getReportedAt())
                .build();
    }

    public static Report toEntity(ReportDTO dto) {
        Report report = new Report();
        report.setId(dto.getId());
        report.setReason(dto.getReason());
        report.setStatus(dto.getStatus());
        report.setEscalatedToAdmin(dto.getEscalatedToAdmin());
        report.setReportedAt(dto.getReportedAt());
        // You’ll need to fetch and set User entities manually in service layer
        return report;
    }
}
