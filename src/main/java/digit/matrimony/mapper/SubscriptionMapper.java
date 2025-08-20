package digit.matrimony.mapper;

import digit.matrimony.dto.SubscriptionDTO;
import digit.matrimony.entity.Subscription;

public class SubscriptionMapper {

    public static SubscriptionDTO toDTO(Subscription subscription) {
        if (subscription == null) return null;

        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .userId(subscription.getUser() != null ? subscription.getUser().getId() : null)
                .planName(subscription.getPlanName())
                .amount(subscription.getAmount())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .paymentStatus(subscription.getPaymentStatus())
                .build();
    }

    public static Subscription toEntity(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());
        subscription.setPlanName(dto.getPlanName());
        subscription.setAmount(dto.getAmount());
        subscription.setStartDate(dto.getStartDate());
        subscription.setEndDate(dto.getEndDate());
        subscription.setPaymentStatus(dto.getPaymentStatus());
        return subscription;
    }

}
