package com.softtek.model;

import lombok.Data;

@Data
public class Employee {
private Integer empno;
private String ename;
private String eadd;
private Double salary;
private String gender;
private Long grossSalary;
private Long netSalary;
}
