package digit.matrimony.mapper;

import digit.matrimony.dto.AnalyticDTO;
import digit.matrimony.entity.Analytic;

public class AnalyticMapper {

    public static AnalyticDTO toDTO(Analytic analytic) {
        if (analytic == null) return null;

        return AnalyticDTO.builder()
                .id(analytic.getId())
                .region(analytic.getRegion())
                .totalUsers(analytic.getTotalUsers())
                .activeUsers(analytic.getActiveUsers())
                .premiumUsers(analytic.getPremiumUsers())
                .activeMatches(analytic.getActiveMatches())
                .subscriptionsSoldLastMonth(analytic.getSubscriptionsSoldLastMonth())
                .revenueLastMonth(analytic.getRevenueLastMonth())
                .recordedAt(analytic.getRecordedAt())
                .build();
    }

    public static Analytic toEntity(AnalyticDTO dto) {
        Analytic analytic = new Analytic();
        analytic.setId(dto.getId());
        analytic.setRegion(dto.getRegion());
        analytic.setTotalUsers(dto.getTotalUsers());
        analytic.setActiveUsers(dto.getActiveUsers());
        analytic.setPremiumUsers(dto.getPremiumUsers());
        analytic.setActiveMatches(dto.getActiveMatches());
        analytic.setSubscriptionsSoldLastMonth(dto.getSubscriptionsSoldLastMonth());
        analytic.setRevenueLastMonth(dto.getRevenueLastMonth());
        analytic.setRecordedAt(dto.getRecordedAt());
        return analytic;
    }
}
