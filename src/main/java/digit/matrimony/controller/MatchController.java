package digit.matrimony.controller;

import digit.matrimony.dto.MatchCreateRequestDTO;
import digit.matrimony.dto.MatchDTO;
import digit.matrimony.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Validated
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/create")
    public ResponseEntity<MatchDTO> createMatch(@Valid @RequestBody MatchCreateRequestDTO request) {
        return ResponseEntity.ok(matchService.createMatch(request));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MatchDTO>> getMatchesForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(matchService.getMatchesForUser(userId));
    }

    @PutMapping("/{matchId}/deactivate")
    public ResponseEntity<MatchDTO> deactivateMatch(@PathVariable Long matchId, @RequestParam String deletedBy) {
        return ResponseEntity.ok(matchService.deactivateMatch(matchId, deletedBy));
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatch(matchId);
        return ResponseEntity.noContent().build();
    }
}
