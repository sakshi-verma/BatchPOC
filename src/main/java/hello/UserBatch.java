package hello;

import javax.sql.DataSource;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

@Configuration
@EnableBatchProcessing
public class UserBatch {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public JobLauncher jobLauncher;

    @Bean
    @StepScope
    public ItemReader<User> userReader() {
        return new UserReader();
    }

    @Bean
    public UserItemProcessor userProcessor() {
        return new UserItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<User> userWriter() {
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
        writer.setSql("INSERT INTO user (name, email) VALUES (:name, :email)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job addUserJob(UserJobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("addUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(addUserStep())
                .end()
                .build();
    }

    @Bean
    public Step addUserStep() {
        return stepBuilderFactory.get("addUserStep")
                .<User, User> chunk(1)
                .reader(userReader())
                .processor(userProcessor())
                .writer(userWriter())
                .build();
    }

    public void runBatch() throws Exception{
        System.out.println("Job Started at :" + new Date());

        JobParameters param = new JobParametersBuilder().addString("JobID",
                String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution execution = jobLauncher.run(addUserJob(new UserJobCompletionNotificationListener(new JdbcTemplate())), param);

        System.out.println("Job finished with status :" + execution.getStatus());
    }
}

