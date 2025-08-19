package digit.matrimony.controller;

import digit.matrimony.dto.ReportRequestDTO;
import digit.matrimony.dto.ReportResponseDTO;
import digit.matrimony.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public List<ReportResponseDTO> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ReportResponseDTO getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @PostMapping
    public ReportResponseDTO createReport(@RequestBody @Valid ReportRequestDTO dto) {
        return reportService.createReport(dto);
    }

    @PutMapping("/{id}")
    public ReportResponseDTO updateReport(@PathVariable Long id, @RequestBody @Valid ReportRequestDTO dto) {
        return reportService.updateReport(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
