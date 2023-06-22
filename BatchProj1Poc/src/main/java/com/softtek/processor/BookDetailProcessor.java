package com.softtek.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("bdProcessor")
public class BookDetailProcessor implements ItemProcessor<String, String> {

	public BookDetailProcessor() {
		super();
		System.out.println("BookDetailProcessor.o param constructor");
	}

	@Override
	public String process(String item) throws Exception {
		System.out.println("BookDetailProcessor.process()");
		String bookWithTitle=null;
		if (item.equalsIgnoreCase("CRJ"))
			bookWithTitle = item+ "by HS and Pn";
		else if (item.equalsIgnoreCase("trj"))
			bookWithTitle = item+ "by xyz";
		else if (item.equalsIgnoreCase("gih"))
			bookWithTitle = item+ "by gfi";
		else if (item.equalsIgnoreCase("HFJ"))
			bookWithTitle = item+ "by mnp";
		return bookWithTitle;
	}

}
