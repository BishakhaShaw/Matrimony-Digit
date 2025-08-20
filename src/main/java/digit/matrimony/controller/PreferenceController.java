package digit.matrimony.controller;

import digit.matrimony.dto.PreferenceDTO;
import digit.matrimony.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<PreferenceDTO> getPreferenceByUserId(@PathVariable Long userId) {
        PreferenceDTO dto = preferenceService.getPreferenceByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PreferenceDTO> saveOrUpdatePreference(@RequestBody PreferenceDTO dto) {
        PreferenceDTO saved = preferenceService.saveOrUpdatePreference(dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        preferenceService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}
