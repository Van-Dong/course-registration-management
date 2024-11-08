//package com.dongnv.courseregistrationmanagement.batch;
//
//import com.dongnv.courseregistrationmanagement.model.User;
//import com.dongnv.courseregistrationmanagement.repository.UserRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class AddRecordBatchConfig {
//    JobRepository jobRepository;
//    PlatformTransactionManager transactionManager;
//    UserRepository userRepository;
//
//    @Bean
//    public Step stepForAddRecord() {
//        return new StepBuilder("stepForAddRecord", jobRepository)
//                .tasklet(dataPrepareTasklet(), transactionManager)
//                .build();
//    }
//
//    @Bean
//    public Tasklet dataPrepareTasklet() {
//        return ((contribution, chunkContext) -> {
//            for (int i = 0; i < 1000; i++) {
//                User user = userRepository.save(User.builder()
//                                .fullName("user" + i)
//                                .email("email" + i + "@email.com")
//                                .password("123456")
//                        .build());
//            }
//            return RepeatStatus.FINISHED;
//        });
//    }
//
//    @Bean
//    public Job jobAddRecord() {
//        return new JobBuilder("jobAddRecord", jobRepository)
//                .start(stepForAddRecord())
//                .build();
//    }
//}
