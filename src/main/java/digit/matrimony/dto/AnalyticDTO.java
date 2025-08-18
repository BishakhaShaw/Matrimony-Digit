package digit.matrimony.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@Data
public class AnalyticDTO {
    private Long id;
    private String region;
    private Integer totalUsers;
    private Integer activeUsers;
    private Integer premiumUsers;
    private Integer activeMatches;
    private Integer subscriptionsSoldLastMonth;
    private BigDecimal revenueLastMonth;
    private LocalDateTime recordedAt;
}
//
//1. FamilyFeedback(pending)
//GET /api/feedback → Get all feedback
//GET /api/feedback/{id} → Get feedback by ID
//POST /api/feedback → Create new feedback
//PUT /api/feedback/{id} → Update feedback
//DELETE /api/feedback/{id} → Delete feedback
//2. Report
//GET /api/reports → Get all reports
//GET /api/reports/{id} → Get report by ID
//POST /api/reports → Create new report
//PUT /api/reports/{id} → Update report
//DELETE /api/reports/{id} → Delete report
//3. Subscription
//GET /api/subscriptions → Get all subscriptions
//GET /api/subscriptions/{id} → Get subscription by ID
//POST /api/subscriptions → Create new subscription
//PUT /api/subscriptions/{id} → Update subscription
//DELETE /api/subscriptions/{id} → Delete subscription
//4. ManagerAction
//GET /api/manager-actions → Get all manager actions
//GET /api/manager-actions/{id} → Get manager action by ID
//POST /api/manager-actions → Create new manager action
//PUT /api/manager-actions/{id} → Update manager action
//DELETE /api/manager-actions/{id} → Delete manager action
//5. Analytic(pending)
//GET /api/analytics/snapshot?region=global → Get analytics snapshot (admin only)