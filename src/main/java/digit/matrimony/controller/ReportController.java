package digit.matrimony.controller;

import digit.matrimony.dto.ReportDTO;
import digit.matrimony.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import digit.matrimony.dto.ReportDTO;
import digit.matrimony.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public List<ReportDTO> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ReportDTO getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @PostMapping
    public ReportDTO createReport(@RequestBody @Valid ReportDTO dto) {
        return reportService.createReport(dto);
    }

    @PutMapping("/{id}")
    public ReportDTO updateReport(@PathVariable Long id, @RequestBody @Valid ReportDTO dto) {
        return reportService.updateReport(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
