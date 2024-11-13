package com.dongnv.courseregistrationmanagement.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.dongnv.courseregistrationmanagement.dto.PageResponse;
import com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse;
import com.dongnv.courseregistrationmanagement.service.ReportService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @GetMapping("/report")
    String report(
            @RequestParam(value = "weekAgo", defaultValue = "0") int weekAgo,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        weekAgo = weekAgo < 0 ? 0 : weekAgo;
        PageResponse<CountEnrollmentInWeekResponse> countEnrollment =
                reportService.countEnrollmentInWeek(weekAgo, page, size);
        List<String> weeks = reportService.getWeeks();

        model.addAttribute("reportCountEnrollment", countEnrollment);
        model.addAttribute("weeks", weeks);
        model.addAttribute("weekAgo", weekAgo);
        return "report/report";
    }

    @GetMapping("/report/export")
    ResponseEntity<FileSystemResource> exportReport(@RequestParam(value = "weekAgo", defaultValue = "1") int weekAgo)
            throws NoResourceFoundException {
        // The current week has not processed the batch yet so it cannot be exported
        weekAgo = weekAgo < 1 ? 1 : weekAgo;
        LocalDate dateExport = LocalDate.now().minusWeeks(weekAgo).with(DayOfWeek.SUNDAY);
        FileSystemResource fileResource = new FileSystemResource("report/" + dateExport.toString() + ".csv");

        if (!fileResource.exists()) {
            throw new NoResourceFoundException(HttpMethod.GET, dateExport.toString() + ".csv");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }
}
