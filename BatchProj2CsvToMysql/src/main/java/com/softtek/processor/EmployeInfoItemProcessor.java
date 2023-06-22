package com.softtek.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.softtek.model.Employee;

@Component
public class EmployeInfoItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee emp) throws Exception {
		if (emp.getSalary() >= 10000) {
        emp.setGrossSalary(Math.round(emp.getSalary() + emp.getSalary() * 0.4f));
		emp.setNetSalary(Math.round(emp.getGrossSalary() - emp.getSalary() * 0.2f));
		return emp;
		} else
			return null;
	}

}
