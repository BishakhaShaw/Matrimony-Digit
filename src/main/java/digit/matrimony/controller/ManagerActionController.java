package digit.matrimony.controller;

import digit.matrimony.dto.ManagerActionRequestDTO;
import digit.matrimony.dto.ManagerActionResponseDTO;
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
    public List<ManagerActionResponseDTO> getAllManagerActions() {
        return managerActionService.getAllManagerActions();
    }

    @GetMapping("/{id}")
    public ManagerActionResponseDTO getManagerActionById(@PathVariable Long id) {
        return managerActionService.getManagerActionById(id);
    }

    @PostMapping
    public ManagerActionResponseDTO createManagerAction(@RequestBody ManagerActionRequestDTO requestDTO) {
        return managerActionService.createManagerAction(requestDTO);
    }

    @PutMapping("/{id}")
    public ManagerActionResponseDTO updateManagerAction(@PathVariable Long id,
                                                        @RequestBody ManagerActionRequestDTO requestDTO) {
        return managerActionService.updateManagerAction(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteManagerAction(@PathVariable Long id) {
        managerActionService.deleteManagerAction(id);
    }
}
