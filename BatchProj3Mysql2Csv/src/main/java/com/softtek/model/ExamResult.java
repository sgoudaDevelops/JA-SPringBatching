package com.softtek.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {

	private Integer id;
	
	private String dob;
	
	private Double percentage;
	
	private Integer semester;
}
