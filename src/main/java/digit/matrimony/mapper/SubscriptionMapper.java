package digit.matrimony.mapper;

import digit.matrimony.dto.SubscriptionRequestDTO;
import digit.matrimony.dto.SubscriptionResponseDTO;
import digit.matrimony.entity.Subscription;

public class SubscriptionMapper {


    public static SubscriptionResponseDTO toDTO(Subscription subscription) {
        return SubscriptionResponseDTO.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .planName(subscription.getPlanName())
                .amount(subscription.getAmount())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .paymentStatus(subscription.getPaymentStatus())
                .build();
    }


    public static Subscription toEntity(SubscriptionRequestDTO dto) {
        Subscription subscription = new Subscription();
        subscription.setPlanName(dto.getPlanName());
        subscription.setAmount(dto.getAmount());
        subscription.setStartDate(dto.getStartDate());
        subscription.setEndDate(dto.getEndDate());
        return subscription;
    }
}
