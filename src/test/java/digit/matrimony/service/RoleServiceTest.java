package digit.matrimony.service;

import digit.matrimony.dto.RoleDTO;
import digit.matrimony.entity.Role;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.RoleMapper;
import digit.matrimony.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role((short) 3, "USER");

        roleDTO = new RoleDTO();
        roleDTO.setId((short) 3);
        roleDTO.setName("USER");
    }

    @Test
    void testGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(role));

        List<RoleDTO> result = roleService.getAllRoles();
        assertEquals(1, result.size());
        assertEquals("USER", result.get(0).getName());
    }

    @Test
    void testGetRoleById_Success() {
        when(roleRepository.findById((short) 3)).thenReturn(Optional.of(role));

        RoleDTO result = roleService.getRoleById((short) 3);
        assertEquals("USER", result.getName());
    }

    @Test
    void testGetRoleById_NotFound() {
        when(roleRepository.findById((short) 3)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById((short) 3));
    }

    @Test
    void testCreateRole() {
        Role role = new Role((short) 3, "USER");
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId((short) 3);
        roleDTO.setName("USER");

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleDTO result = roleService.createRole(roleDTO);
        assertNotNull(result);
        assertEquals("USER", result.getName());
    }


}
