package com.jizhibackend.bean;

import util.ValidateException;

public class User {
	public static final int student=1;
	public static final int teacher=2;
	private String username;
	private String password;
	private int userid;
	private String email;
	private String nickname;
	private String dp_url;
	private String classname;
	private int coins;
	private String SharePaper;
	
	public String getSharePaper() {
		return SharePaper;
	}
	public void setSharePaper(String sharePaper) {
		SharePaper = sharePaper;
	}
	public String getDp_url() {
		return dp_url;
	}
	public void setDp_url(String dp_url) {
		this.dp_url = dp_url;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	private int usertype;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public boolean validate() throws ValidateException
	{
		if(username==null||username.equals(""))
		{
			throw new ValidateException("用户名不能为空");
		}
		if(password==null||password.equals(""))
		{
			throw new ValidateException("密码不能为空");
		}
		if(email==null||email.equals(""))
		{
			throw new ValidateException("邮箱不能为空");
		}
		if(usertype==1||usertype==2)
		{
			
		}else
			throw new ValidateException("用户类型参数不正确");
		
		return true;
		
	}
	
	
	
	

}
