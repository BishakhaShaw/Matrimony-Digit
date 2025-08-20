package digit.matrimony.controller;

import digit.matrimony.dto.AnalyticDTO;
import digit.matrimony.entity.Analytic;
import digit.matrimony.service.AnalyticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/analytics")
public class AnalyticController {

    @Autowired private AnalyticService analyticService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/snapshot")
    public AnalyticDTO getAnalyticsSnapshot(@RequestParam(defaultValue = "global") String region) {
        return analyticService.generateAnalyticsSnapshot(region);
    }
}
