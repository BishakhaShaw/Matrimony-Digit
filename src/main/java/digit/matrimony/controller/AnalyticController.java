package digit.matrimony.controller;

import digit.matrimony.dto.AnalyticDTO;
import digit.matrimony.entity.Analytic;
import digit.matrimony.service.AnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticController {

    @Autowired private AnalyticService analyticService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/snapshot")
    public ResponseEntity<?> getAnalyticsSnapshot(@RequestParam int role) {
        if (role != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Admins only");
        }

        return ResponseEntity.ok(analyticService.generateAnalyticsSnapshot("global"));
    }
}
