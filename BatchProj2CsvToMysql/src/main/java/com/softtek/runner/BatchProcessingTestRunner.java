package com.softtek.runner;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchProcessingTestRunner implements CommandLineRunner {

	@Autowired
	private JobLauncher launcher;

	@Autowired
	private Job job;

	@Override
	public void run(String... args) throws Exception {
		// prepare the job parameter objects i.e - send some extra data
		JobParameters param = new JobParametersBuilder().addLong("run.id", new Random().nextLong(10000))
				.toJobParameters();

		// run the job
		JobExecution execution = launcher.run(job, param);
		System.out.println("exit status ::" + execution.getStatus());
		System.out.println(execution.getExitStatus());
		System.out.println(execution.getJobId());
	}

}
