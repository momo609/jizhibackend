package com.jizhibackend.bean;

public class Student1 {
	public Student1()
	{

	}
	 public Student1(int stuNo, String stuName,String className)
	{
		id=stuNo;
		name=stuName;
		this.className=className;
		
	}
	private int id;
	private String name;
	private String className;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	

}