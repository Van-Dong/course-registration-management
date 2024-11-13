package com.dongnv.courseregistrationmanagement.batch;

import java.util.HashMap;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.dongnv.courseregistrationmanagement.constant.Role;
import com.dongnv.courseregistrationmanagement.dto.response.EmailContentDto;
import com.dongnv.courseregistrationmanagement.model.Course;
import com.dongnv.courseregistrationmanagement.model.User;
import com.dongnv.courseregistrationmanagement.repository.EnrollmentRepository;
import com.dongnv.courseregistrationmanagement.repository.UserRepository;
import com.dongnv.courseregistrationmanagement.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchSuggestCourseConfig {
    JobRepository jobRepository;
    UserRepository userRepository;
    EnrollmentRepository enrollmentRepository;
    EmailService emailService;

    @Bean
    public ItemReader<User> readerForSendSuggestedEmail() {
        HashMap<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);

        return new RepositoryItemReaderBuilder<User>()
                .name("sendSuggestedEmailReader")
                .repository(userRepository)
                .methodName("findAllUserByRole")
                .arguments(Role.STUDENT)
                .pageSize(100)
                .sorts(sorts)
                .build();
    }

    @Bean
    public ItemProcessor<User, EmailContentDto> processorForSendSuggestedEmail() {
        return user -> {
            List<Course> courses = enrollmentRepository.findAvailableCoursesForUser(user.getId());
            return EmailContentDto.builder()
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .courses(courses)
                    .build();
        };
    }

    @Bean
    public ItemWriter<EmailContentDto> writerForSendSuggestedEmail() {
        return items -> {
            for (EmailContentDto emailContentDto : items) {
                emailService.sendEmail(
                        emailContentDto.getEmail(), "Suggested course", buildEmailContent(emailContentDto));
            }
        };
    }

    @Bean
    public Step sendSuggestedEmailStep1(PlatformTransactionManager batchTransactionManager) {
        return new StepBuilder("sendSuggestedEmailStep1", jobRepository)
                .<User, EmailContentDto>chunk(100, batchTransactionManager)
                .reader(readerForSendSuggestedEmail())
                .processor(processorForSendSuggestedEmail())
                .writer(writerForSendSuggestedEmail())
                .build();
    }

    @Bean
    public Job sendSuggestedEmailJob(Step sendSuggestedEmailStep1) {
        return new JobBuilder("sendSuggestedEmailJob", jobRepository)
                .start(sendSuggestedEmailStep1)
                .listener(new JobCompletionNotificationListener())
                .build();
    }

    private String buildEmailContent(EmailContentDto emailContentDto) {
        StringBuilder courseDetails = new StringBuilder();
        for (Course course : emailContentDto.getCourses()) {
            courseDetails
                    .append("<div class=\"course\">")
                    .append("<strong>Khóa học:</strong> ")
                    .append(course.getTitle())
                    .append("<br>")
                    .append("<strong>Ngày bắt đầu:</strong> ")
                    .append(course.getStartDate())
                    .append("<br>")
                    .append("<strong>Số lượng đăng ký tối đa:</strong> ")
                    .append(course.getMaxEnrollments())
                    .append("<br>")
                    .append("<strong>Số lượng hiện tại:</strong> ")
                    .append(course.getCurrentEnrollments())
                    .append("</div><br>");
        }

        // Nội dung email HTML với thông tin khóa học
        return """
			<!DOCTYPE html>
			<html>
			<head>
				<style>
					.container {
						font-family: Arial, sans-serif;
						max-width: 600px;
						margin: 0 auto;
						padding: 20px;
						border: 1px solid #dddddd;
						border-radius: 10px;
						background-color: #f9f9f9;
					}
					h2 {
						color: #4CAF50;
					}
					p {
						font-size: 16px;
						color: #333333;
					}
					.course-list {
						margin-top: 20px;
					}
					.course {
						margin-bottom: 10px;
					}
					.button {
						display: inline-block;
						padding: 10px 15px;
						margin-top: 20px;
						color: #ffffff;
						background-color: #4CAF50;
						text-decoration: none;
						border-radius: 5px;
					}
					.footer {
						font-size: 12px;
						color: #777777;
						margin-top: 30px;
						text-align: center;
					}
				</style>
			</head>
			<body>
				<div class="container">
					<h2>Nhắc nhở đăng ký khóa học</h2>
					<p>Chào <i>%s</i>,</p>
					<p>Bạn vẫn chưa đăng ký một số khóa học bắt buộc cho kỳ học sắp tới. Hãy nhanh chóng hoàn thành đăng ký để đảm bảo bạn sẽ không bỏ lỡ các khóa học này.</p>
					<div class="course-list">
						<h3>Các khóa học bạn chưa đăng ký:</h3>
						%s
					</div>
					<p>Vui lòng đăng nhập vào hệ thống để hoàn tất đăng ký:</p>
					<a href="http://localhost:8080/login" class="button">Đăng nhập ngay</a>
					<div class="footer">
						<p>Đây là email tự động, vui lòng không trả lời lại email này.</p>
						<p>Bộ phận quản lý khóa học - Trường Đại học VietIS</p>
					</div>
				</div>
			</body>
			</html>
			"""
                .formatted(emailContentDto.getFullName(), courseDetails.toString());
    }
}
