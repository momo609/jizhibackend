package com.jizhibackend.bean;

import java.sql.Date;

public class MyClass {
	private int id;
	
	private String name;
	private int owner;
	private long createtime;
	private int membernum;
	private String dp_url;
	public String getDp_url() {
		return dp_url;
	}
	public void setDp_url(String dp_url) {
		this.dp_url = dp_url;
	}
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
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public int getMembernum() {
		return membernum;
	}
	public void setMembernum(int membernum) {
		this.membernum = membernum;
	}

}
