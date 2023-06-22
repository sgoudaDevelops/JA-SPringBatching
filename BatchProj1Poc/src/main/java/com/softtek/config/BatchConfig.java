package com.softtek.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.softtek.listner.JobMonitoringListner;
import com.softtek.processor.BookDetailProcessor;
import com.softtek.reader.BookDetailsReader;
import com.softtek.writer.BookDetailsWriter;

@Configuration
@EnableBatchProcessing // gives spring batch feature through autoconfiguration like giving
						// inMemoryJobRepository, Job building fatory, stepBuilding factory ext
public class BatchConfig {
	
//	@Autowired
//	private JobBuilderFactory jobFactory;
	@Autowired
	private JobBuilderFactory jobFactory;
	
	@Autowired
	private StepBuilderFactory stepFactory;

	@Autowired
	private BookDetailsReader bdReader;

	@Autowired
	private BookDetailProcessor bdProcessor;

	@Autowired
	private BookDetailsWriter bdWriter;

	@Autowired
	private JobMonitoringListner joblistner;

	// create step using the StepBuilder factory
	@Bean(name = "step1")
	public Step createStep1() {
		System.out.println("BatchConfig.createStep1()");
		return stepFactory.get("step1")
				.<String, String>chunk(1)
				.reader(bdReader)
				.writer(bdWriter)
				.processor(bdProcessor).build();

	}
	@Bean(name = "job1")
	public Job createJob() {
	System.out.println("BatchConfig.createJob()");
	return jobFactory.get("job1")
	.incrementer(new RunIdIncrementer())
	.listener(joblistner)
	.start(createStep1()).build();
	}
	
}
