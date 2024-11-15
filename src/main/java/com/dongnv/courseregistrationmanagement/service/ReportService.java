package com.dongnv.courseregistrationmanagement.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dongnv.courseregistrationmanagement.dto.PageResponse;
import com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;
import com.dongnv.courseregistrationmanagement.repository.EnrollmentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    CourseRepository courseRepository;
    EnrollmentRepository enrollmentRepository;

    // Tính số lượt đăng ký của từng khóa học trong một tuần nào đó
    public PageResponse<CountEnrollmentInWeekResponse> countEnrollmentInWeek(int weekAgo, int page, int size) {
        LocalDate startDay = LocalDate.now().minusWeeks(weekAgo).with(DayOfWeek.MONDAY);
        LocalDateTime startDateTime = startDay.atStartOfDay();
        LocalDateTime endDateTime = startDay.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        Page<CountEnrollmentInWeekResponse> result =
                courseRepository.countEnrollmentInWeek(startDateTime, endDateTime, PageRequest.of(page - 1, size));
        return PageResponse.<CountEnrollmentInWeekResponse>builder()
                .currentPage(page)
                .totalPages(result.getTotalPages())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .data(result.getContent())
                .build();
    }

    // Lấy danh sách weeks để làm dữ liệu cho tag <select>
    public List<String> getWeeks() {
        LocalDateTime oldestDatetime =
                enrollmentRepository.getOldestEnrollmentDate().orElse(LocalDateTime.now());
        return getWeeksFromStartDate(oldestDatetime.toLocalDate());
    }

    private List<String> getWeeksFromStartDate(LocalDate startDate) {
        List<String> weeks = new ArrayList<>();
        LocalDate endDate = LocalDate.now().with(DayOfWeek.SUNDAY);
        startDate = startDate.with(DayOfWeek.MONDAY);

        weeks.add("Current Weeks");
        endDate = endDate.minusWeeks(1);

        int i = 1;
        while (endDate.isAfter(startDate) && i < 5) {
            weeks.add(i + " Weeks Ago");
            endDate = endDate.minusWeeks(1);
            i++;
        }

        while (endDate.isAfter(startDate)) {
            weeks.add("Week from " + endDate.with(DayOfWeek.MONDAY) + " to " + endDate);
            endDate = endDate.minusWeeks(1);
        }

        return weeks;
    }
}
