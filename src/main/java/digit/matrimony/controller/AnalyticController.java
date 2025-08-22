package digit.matrimony.controller;

import digit.matrimony.service.AnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticController {

    @Autowired
    private AnalyticService analyticService;

    //Only users with ROLE_ADMIN can access this endpoint
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/snapshot")
    public ResponseEntity<?> getAnalyticsSnapshot(@RequestParam(required = false, defaultValue = "global") String region) {
        return ResponseEntity.ok(analyticService.generateAnalyticsSnapshot(region));
    }
}
