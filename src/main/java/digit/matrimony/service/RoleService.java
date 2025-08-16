package digit.matrimony.service;

import digit.matrimony.dto.RoleDTO;
import digit.matrimony.entity.Role;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.RoleMapper;
import digit.matrimony.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDTO) // static method
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Short id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return RoleMapper.toDTO(role); // static method
    }

    public RoleDTO createRole(RoleDTO dto) {
        Role role = RoleMapper.toEntity(dto); // static method
        role = roleRepository.save(role);
        return RoleMapper.toDTO(role); // static method
    }
}
