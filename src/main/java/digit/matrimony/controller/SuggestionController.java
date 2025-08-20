package digit.matrimony.controller;

import digit.matrimony.dto.SuggestionDTO;
import digit.matrimony.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    @PostMapping("/create")
    public ResponseEntity<SuggestionDTO> createSuggestion(
            @RequestParam Long userId,
            @RequestParam Long suggestedUserId,
            @RequestParam double matchScore) {
        return ResponseEntity.ok(suggestionService.createSuggestion(userId, suggestedUserId, matchScore));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SuggestionDTO>> getSuggestionsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(suggestionService.getSuggestionsForUser(userId));
    }

    @DeleteMapping("/{suggestionId}")
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long suggestionId) {
        suggestionService.deleteSuggestion(suggestionId);
        return ResponseEntity.noContent().build();
    }
}
