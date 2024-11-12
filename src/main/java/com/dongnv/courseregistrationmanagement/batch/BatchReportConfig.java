package com.dongnv.courseregistrationmanagement.batch;

import com.dongnv.courseregistrationmanagement.dto.response.CountEnrollmentInWeekResponse;
import com.dongnv.courseregistrationmanagement.repository.CourseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchReportConfig {
    JobRepository jobRepository;
    CourseRepository courseRepository;

    @Bean
    public ItemReader<CountEnrollmentInWeekResponse> readerForExportReport() {
        LocalDate today = LocalDate.now().minusDays(1);  // run in 3:00 AM Monday
        LocalDateTime startOfWeek = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        HashMap<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);

        return new RepositoryItemReaderBuilder<CountEnrollmentInWeekResponse>()
                .name("exportReportReader")
                .repository(courseRepository)
                .methodName("countEnrollmentInWeek")
                .arguments(startOfWeek, endOfWeek)
                .pageSize(100)
                .sorts(sorts)
                .build();
    }

    @Bean
    public LineAggregator<CountEnrollmentInWeekResponse> lineAggregator() {
        return new LineAggregator<CountEnrollmentInWeekResponse>() {
            @Override
            public String aggregate(CountEnrollmentInWeekResponse item) {


                return  item.getId() + ", " +
                        item.getTitle() + ", " +
                        item.getCountEnrollmentInWeek() + ", " +
                        item.getCurrentEnrollments() + "/" + item.getMaxEnrollments();
            }
        };
    }

    @Bean
    public ItemWriter<CountEnrollmentInWeekResponse> writerForExportReport() {
        FlatFileItemWriter<CountEnrollmentInWeekResponse> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("report/" + LocalDate.now().minusDays(1) + ".csv"));
        // Thêm header vào file CSV
        writer.setHeaderCallback(writer1 -> writer1.write("id, title, countEnrollmentInWeek, currentEnrollment"));
        writer.setLineAggregator(lineAggregator());
        return writer;
    }

    @Bean
    public Step exportReportStep1(PlatformTransactionManager batchTransactionManager) {
        return new StepBuilder("exportReportStep1", jobRepository)
                .<CountEnrollmentInWeekResponse, CountEnrollmentInWeekResponse>chunk(100, batchTransactionManager)
                .reader(readerForExportReport())
                .writer(writerForExportReport())
                .build();
    }

    @Bean
    public Job exportReportJob(Step exportReportStep1) {
        return new JobBuilder("exportReportJob1", jobRepository)
                .start(exportReportStep1)
                .listener(new JobCompletionNotificationListener())
                .build();
    }
}
