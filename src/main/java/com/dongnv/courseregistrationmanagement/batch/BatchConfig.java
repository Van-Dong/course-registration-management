package com.dongnv.courseregistrationmanagement.batch;

import com.dongnv.courseregistrationmanagement.model.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BatchConfig {
    JobRepository jobRepository;

    @Bean
    public JpaPagingItemReader<User> reader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<User>()
                .name("Export user")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select u from User u order by u.id")
                .pageSize(1000)
                .build();
    }

    @Bean
    public LineAggregator<User> lineAggregator() {
        DelimitedLineAggregator<User> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");
        BeanWrapperFieldExtractor<User> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "fullName", "email", "password"});
        aggregator.setFieldExtractor(fieldExtractor);
        return aggregator;
    }

    @Bean
    public FlatFileItemWriter<User> writer() {
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("students.csv"));
        writer.setLineAggregator(lineAggregator());
        return writer;
    }

    @Bean
    public Step step1(ItemReader<User> reader, FlatFileItemWriter<User> writer, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job exportJob(Step step1) {
        return new JobBuilder("exportJob", jobRepository)
                .start(step1)
                .listener(new JobCompletionNotificationListener())
                .build();
    }
}
