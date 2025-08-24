package digit.matrimony.controller;

import digit.matrimony.dto.PreferenceDTO;
import digit.matrimony.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    @PostMapping("/create")
    public ResponseEntity<PreferenceDTO> createPreference(@RequestBody PreferenceDTO dto) {
        PreferenceDTO created = preferenceService.createPreference(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<PreferenceDTO> updatePreference(
            @PathVariable Long id,
            @RequestBody PreferenceDTO dto) {
        PreferenceDTO updated = preferenceService.updatePreference(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        preferenceService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}

