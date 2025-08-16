

package digit.matrimony.mapper;

import digit.matrimony.dto.RoleDTO;
import digit.matrimony.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;

        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;

        return Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
