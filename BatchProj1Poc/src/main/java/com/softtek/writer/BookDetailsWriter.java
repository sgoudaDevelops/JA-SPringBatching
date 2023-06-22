package com.softtek.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("bdWriter")
public class BookDetailsWriter implements ItemWriter<String> {

	
	public BookDetailsWriter() {
		super();
		System.out.println("BookDetailsReader. o param constructorr");
	}

	
	@Override
	public void write(List<? extends String> items) throws Exception {
		System.out.println("BookDetailsReader.write()");
		items.forEach(System.out::println);
		
	}

}
