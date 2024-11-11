package com.dongnv.courseregistrationmanagement.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
public class BatchConfig {
    @Value("${spring.batch.jdbc.initialize-schema}")
    private String initializeSchema;

    @Bean
    @BatchDataSource
    @ConfigurationProperties("spring.batch.datasource")
    public DataSource batchDataSource() {  // Database for Metadata of Batch
        return DataSourceBuilder.create().build();
    }

    @Bean
    @BatchTransactionManager
    public DataSourceTransactionManager batchTransactionManager(DataSource batchDataSource) {
        return new DataSourceTransactionManager(batchDataSource);
    }

    @Bean
    public DataSourceInitializer batchDataSourceInitializer(DataSource batchDataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(batchDataSource);
        if (!isSchemaInitialized(batchDataSource)) {
            if ("always".equals(initializeSchema) || "embedded".equals(initializeSchema)) {
                Resource schemaResource = new ClassPathResource("org/springframework/batch/core/schema-mysql.sql");
                initializer.setDatabasePopulator(databasePopulator(schemaResource));
            }
        }

        return initializer;
    }

    private DatabasePopulator databasePopulator(Resource schemaResource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaResource);
        return populator;
    }
    private boolean isSchemaInitialized(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            // Kiểm tra sự tồn tại của bảng mà bạn muốn kiểm tra (ví dụ: BATCH_JOB_INSTANCE)
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'BATCH_JOB_INSTANCE'";
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // Trả về true nếu bảng đã tồn tại
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Xử lý lỗi nếu có
        }
        return false;  // Trả về false nếu không thể kiểm tra
    }

    @Bean
    public JobLauncher customJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
