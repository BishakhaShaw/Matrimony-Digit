


package digit.matrimony.mapper;

import digit.matrimony.dto.UserDTO;
import digit.matrimony.entity.Role;
import digit.matrimony.entity.User;
import org.springframework.stereotype.Component;

@Component
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
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .linkedUserId(user.getLinkedUser() != null ? user.getLinkedUser().getId() : null)
                .linkedUsername(user.getLinkedUser() != null ? user.getLinkedUser().getUsername() : null)
                // ⚠️ Never expose passwordHash or passwordSalt in DTO
                .build();
    }

    /**
     * Convert DTO → Entity (Role & LinkedUser set in Service).
     * Password handling (hashing + salting) must be done in Service, not here.
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .permanentLocation(dto.getPermanentLocation())
                .subscriptionType(dto.getSubscriptionType())
                .isActive(dto.getIsActive())
                // ⚠️ Don't set passwordHash here, service must hash password
                .build();
    }

    /**
     * Helper when Role & LinkedUser already fetched in Service
     */
    public static User toEntity(UserDTO dto, Role role, User linkedUser) {
        if (dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .permanentLocation(dto.getPermanentLocation())
                .subscriptionType(dto.getSubscriptionType())
                .isActive(dto.getIsActive())
                .role(role)
                .linkedUser(linkedUser)
                .build();
    }
}
