package com.softtek.BatchConfig;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.fasterxml.jackson.databind.BeanProperty;
import com.softtek.listner.JobMonitoringListner;

import com.softtek.model.ExamResult;
import com.softtek.processor.ExamResultInfoProcessor;

@Configuration
@EnableBatchProcessing
public class Config {
	@Autowired
	private JobBuilderFactory jobFactory;
	@Autowired
	private StepBuilderFactory stepFactory;
	@Autowired
	private JobMonitoringListner listener;
	@Autowired
	private ExamResultInfoProcessor processor;
	@Autowired
	private DataSource ds;

	@Bean
	public JdbcCursorItemReader<ExamResult> createReader(){
		System.out.println("Config.createReader()");
		return new JdbcCursorItemReaderBuilder<ExamResult>()
				.name("reader-sql")
				.dataSource(ds)
				.sql("select ID,DOB,percentage,semester from exam_result")
				.beanRowMapper(ExamResult.class) // internally it uses beanPropertyRowMapper to convert the records of rs to given model
				//con-db column name names & model class property names should match
				.build();
	}
	@Bean
	public FlatFileItemWriter<ExamResult> createWriter()
	{
		System.out.println("Config.createWriter()");
		 return new FlatFileItemWriterBuilder<ExamResult>()
				 .name("writer").append(true)
			     //.resource(new ClassPathResource("topbrains.csv"))
				 .resource(new FileSystemResource("C:\\batch\\TopBrains.csv"))
				 .lineSeparator("\r\n")
				 .delimited().delimiter(",")
				 .names("id","dob","percentage","semester")
				 .build();
	}
	
	@Bean(name = "step1")
	public Step createStep1() {
		System.out.println("Config.createStep1()");
		return stepFactory.get("step1").<ExamResult, ExamResult>chunk(100).reader(createReader()).processor(processor)
				.writer(createWriter()).build();
	}

	@Bean(name = "job1")
	public Job createJob1() {
		System.out.println("Config.createJob1()");
		return jobFactory.get("job1").incrementer(new RunIdIncrementer()).listener(listener).start(createStep1())
				.build();
	}
}