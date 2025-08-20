package digit.matrimony.controller;

import digit.matrimony.dto.SuggestionDTO;
import digit.matrimony.dto.SuggestionRequestDTO;
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

    @PostMapping("/generate")
    public ResponseEntity<List<SuggestionDTO>> generateSuggestions(@RequestBody SuggestionRequestDTO request) {
        return ResponseEntity.ok(suggestionService.generateTopSuggestions(request.getUserId()));
    }

    @PostMapping("/user")
    public ResponseEntity<List<SuggestionDTO>> getSuggestionsForUser(@RequestBody SuggestionRequestDTO request) {
        return ResponseEntity.ok(suggestionService.getSuggestionsForUser(request.getUserId()));
    }

    @DeleteMapping("/{suggestionId}")
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long suggestionId) {
        suggestionService.deleteSuggestion(suggestionId);
        return ResponseEntity.noContent().build();
    }
}
