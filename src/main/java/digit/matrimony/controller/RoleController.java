package digit.matrimony.controller;

import digit.matrimony.dto.RoleDTO;
import digit.matrimony.service.RoleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public RoleDTO getRole(@PathVariable Short id) {
        return roleService.getRoleById(id);
    }

    @PostMapping
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        return roleService.createRole(roleDTO);
    }
}