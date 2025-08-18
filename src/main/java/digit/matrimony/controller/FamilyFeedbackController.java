package digit.matrimony.controller;

import digit.matrimony.dto.FamilyFeedbackDTO;
import digit.matrimony.entity.FamilyFeedback;
import digit.matrimony.mapper.FamilyFeedbackMapper;
import digit.matrimony.service.FamilyFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FamilyFeedbackController {

    @Autowired private FamilyFeedbackService feedbackService;

    @GetMapping
    public List<FamilyFeedbackDTO> getAllFeedback() {
        return feedbackService.getAllFeedback().stream()
                .map(FamilyFeedbackMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public FamilyFeedbackDTO getFeedbackById(@PathVariable Long id) {
        FamilyFeedback feedback = feedbackService.getFeedbackById(id);
        return FamilyFeedbackMapper.toDTO(feedback);
    }

    @PostMapping
    public FamilyFeedbackDTO createFeedback(@RequestBody FamilyFeedbackDTO dto) {
        FamilyFeedback saved = feedbackService.createFeedback(dto);
        return FamilyFeedbackMapper.toDTO(saved);
    }

    @PutMapping("/{id}")
    public FamilyFeedbackDTO updateFeedback(@PathVariable Long id, @RequestBody FamilyFeedbackDTO dto) {
        FamilyFeedback updated = feedbackService.updateFeedback(id, dto);
        return FamilyFeedbackMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
    }
}
