package digit.matrimony.controller;

import digit.matrimony.dto.ManagerActionDTO;
import digit.matrimony.service.ManagerActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager-actions")
public class ManagerActionController {

    @Autowired
    private ManagerActionService managerActionService;

    @GetMapping
    public List<ManagerActionDTO> getAllManagerActions() {
        return managerActionService.getAllManagerActions();
    }

    @GetMapping("/{id}")
    public ManagerActionDTO getManagerActionById(@PathVariable Long id) {
        return managerActionService.getManagerActionById(id);
    }

    @PostMapping
    public ManagerActionDTO createManagerAction(@RequestBody ManagerActionDTO dto) {
        return managerActionService.createManagerAction(dto);
    }

    @PutMapping("/{id}")
    public ManagerActionDTO updateManagerAction(@PathVariable Long id, @RequestBody ManagerActionDTO dto) {
        return managerActionService.updateManagerAction(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteManagerAction(@PathVariable Long id) {
        managerActionService.deleteManagerAction(id);
    }
}
