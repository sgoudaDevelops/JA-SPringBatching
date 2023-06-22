package com.softtek.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component("bdReader")
public class BookDetailsReader implements ItemReader<String> {
	public BookDetailsReader() {
		super();
		System.out.println("BookDetailsReader.) param constructor");
	}

	String books[] = new String[] { "crj", "trj", "HFJ", "gih" };// source
	int count = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		System.out.println("BookDetailsReader.read()");
		if (count < books.length) {
			return books[count++];
		} else {
			return null;
		}
	}

}
