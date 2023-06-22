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
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.BeanProperty;
import com.softtek.listner.JobMonitoringListner;
import com.softtek.model.Employee;
import com.softtek.processor.EmployeInfoItemProcessor;


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
	private EmployeInfoItemProcessor processor;
	@Autowired
	private DataSource ds;

	@Bean(name="ffireader")
	public FlatFileItemReader<Employee> createFFIReader()
	{
		return new FlatFileItemReaderBuilder<Employee>() 
				.name("file-reader")
				.resource(new ClassPathResource("Employee.csv"))
				.delimited()
				.names("empno", "ename", "eadd", "salary", "gender")
				.targetType(Employee.class)
				.build();
		
	}
	
//	@Bean(name = "ffireader")
//	public FlatFileItemReader<Employee> createFFIREADER() {
//		// create reader object
//		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
//		// set csv file as resource
//		reader.setResource(new ClassPathResource("Employee.csv"));
//		// create line mapper object(to get each line from the csv )
////		DefaultLineMapper<Employee> LineMapper = new DefaultLineMapper<Employee>();
//		// Create lineTokenizer (to get the tokens from lines)
////		DelimitedLineTokenizer Tokenizer = new DelimitedLineTokenizer();
////		Tokenizer.setDelimiter(","); // , setted as delimeter
////		Tokenizer.setNames("empno", "ename", "eadd", "salary", "gender");
//
//		// create fieldSetMapper (To set Tokens to model Class object properties)
////		BeanWrapperFieldSetMapper<Employee> FieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
////		FieldSetMapper.setTargetType(Employee.class);
//
//		// set tokenizer & fieldset mapper to the lineMapper
////		LineMapper.setFieldSetMapper(FieldSetMapper);
////		LineMapper.setLineTokenizer(Tokenizer);
//		
//		// set line mapper to reader object
////		reader.setLineMapper(LineMapper);
////		return reader;
//		reader.setLineMapper(new DefaultLineMapper<Employee>() {
//			{
//				setLineTokenizer(new DelimitedLineTokenizer() {
//					{
//						setDelimiter(",");
//						setNames("empno", "ename", "eadd", "salary", "gender");
//					}
//					 
//				});
//				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>(){
//				{
//					setTargetType(Employee.class); 
//				}
//			});
//			}
//		});
//		return reader;
//	}

//	@Bean(name = "jbiw")
//	public JdbcBatchItemWriter<Employee> createJBIWriter() {
//		// create jdbcBatch ItemWriter object
//		JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<>();
//
//		// set Datasource
//		writer.setDataSource(ds);
//
//		// set sql query
//		writer.setSql("Insert into csvdata values(:empno,:ename,:eadd,:salary,:gender,:grossSalary,:netSalary)");
//
//		// create the BeanPropertyItemsqlParametrSourceprovider object
//		BeanPropertyItemSqlParameterSourceProvider<Employee> sourceProvider = new BeanPropertyItemSqlParameterSourceProvider<>();
//
//		// set source provider
//		writer.setItemSqlParameterSourceProvider(sourceProvider);
//		return writer;
//	}
	@Bean(name = "jbiw")
	public JdbcBatchItemWriter<Employee> createJBIWriter() 
	{
		return new JdbcBatchItemWriterBuilder<Employee>()
				.dataSource(ds)
				.sql("Insert into csvdata values(:empno,:ename,:eadd,:salary,:gender,:grossSalary,:netSalary)")
				.beanMapped().build();	
	}
	
	
	@Bean(name = "step1")
	public Step createStep1() {
		return stepFactory.get("step1").<Employee, Employee>chunk(5).reader(createFFIReader()).processor(processor)
				.writer(createJBIWriter()).build();
	}

	@Bean(name = "job1")
	public Job createJob1() {
		return jobFactory.get("job1").incrementer(new RunIdIncrementer()).listener(listener).start(createStep1())
				.build();
	}

}
