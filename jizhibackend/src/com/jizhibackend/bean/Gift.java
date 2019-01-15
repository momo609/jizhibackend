package com.jizhibackend.bean;

public class Gift {
	private int gift_id;
	private String description;
	private String dp_url;
	private int price;
	private String name;
	private int numbers;
	private int class_id;
	private int ownerid;
	
	public int getGift_id() {
		return gift_id;
	}
	public void setGift_id(int gift_id) {
		this.gift_id = gift_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDp_url() {
		return dp_url;
	}
	public void setDp_url(String dp_url) {
		this.dp_url = dp_url;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumbers() {
		return numbers;
	}
	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}
	
	
}
