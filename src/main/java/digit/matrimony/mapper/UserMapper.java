
package digit.matrimony.mapper;

import digit.matrimony.dto.UserDTO;
import digit.matrimony.entity.Role;
import digit.matrimony.entity.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .permanentLocation(user.getPermanentLocation())
                .subscriptionType(user.getSubscriptionType())
                .isActive(user.getIsActive())
                .roleId(user.getRole() != null ? user.getRole().getId() : null)

                // --- Family Member Flow ---
                .isFamilyMember(user.getIsFamilyMember())
                .linkedUserId(user.getLinkedUser() != null ? user.getLinkedUser().getId() : null)
                .linkedUsername(user.getLinkedUser() != null ? user.getLinkedUser().getUsername() : null)

                // --- System Generated ---
                .generatedPassword(user.getGeneratedPassword())
                .build();
    }

    public static User toEntity(UserDTO dto, Role role, User linkedUser) {
        if (dto == null) return null;

        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .permanentLocation(dto.getPermanentLocation())
                .subscriptionType(dto.getSubscriptionType())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .role(role)

                // --- Family Member Flow ---
                .isFamilyMember(dto.getIsFamilyMember() != null ? dto.getIsFamilyMember() : false)
                .linkedUser(linkedUser)

                // --- System Generated ---
                .generatedPassword(dto.getGeneratedPassword())
                .build();
    }
}
