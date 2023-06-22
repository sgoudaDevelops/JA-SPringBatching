package com.softtek.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


import com.softtek.model.ExamResult;

@Component
public class ExamResultInfoProcessor implements ItemProcessor<ExamResult, ExamResult> {

	@Override
	public ExamResult process(ExamResult exm) throws Exception {
		
		if(exm.getPercentage()>=90)
		{
			return exm;
		} else
			return null;
	}

}
