package com.dongnv.courseregistrationmanagement.controller;

import com.dongnv.courseregistrationmanagement.dto.PageResponse;
import com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse;
import com.dongnv.courseregistrationmanagement.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @GetMapping("/report")
    String report(@RequestParam(value = "weekAgo", defaultValue = "0") int weekAgo,
                  @RequestParam(value = "page", defaultValue = "1") int page,
                  @RequestParam(value = "size", defaultValue = "10") int size,
                  Model model) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        weekAgo = weekAgo < 0 ? 0 : weekAgo;
        PageResponse<CountEnrollmentInWeekResponse> countEnrollment = reportService.countEnrollmentInWeek(weekAgo, page, size);
        List<String> weeks = reportService.getWeeks();

        model.addAttribute("reportCountEnrollment", countEnrollment);
        model.addAttribute("weeks", weeks);
        model.addAttribute("weekAgo", weekAgo);
        return "report/report";
    }

    @GetMapping("/report/export")
    ResponseEntity<FileSystemResource> exportReport(@RequestParam(value = "weekAgo", defaultValue = "0") int weekAgo) {
        weekAgo = weekAgo < 0 ? 0 : weekAgo;
        LocalDate dateExport = LocalDate.now().minusWeeks(weekAgo).with(DayOfWeek.SUNDAY);
        if (weekAgo == 0) {
            // Need to process because batch not run for current week
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        FileSystemResource fileResource = new FileSystemResource("report/" + dateExport.toString() + ".csv");

        if (!fileResource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }
}
