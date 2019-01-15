package com.jizhibackend.bean;

public class MyVoteTest {
	private int votetestid;
	private String title;
	private long start_time;
	private long end_time;
	private long create_time;
	private int owner;
	private String studentid;
	private String studentname;
//	private int use_paperid;
//	private int privilege;
//	private int qNum;
//	public int getqNum() {
//		return qNum;
//	}
//	public void setqNum(int qCount) {
//		this.qNum = qCount;
//	}
//	public int getPrivilege() {
//		return privilege;
//	}
//	public void setPrivilege(int privilege) {
//		this.privilege = privilege;
//	}
	private MyClass myclass=new MyClass();
	public MyClass getMyclass() {
		return myclass;
	}
	public void setMyclass(MyClass myclass) {
		this.myclass = myclass;
	}
	public int getVoteTestid() {
		return votetestid;
	}
	public void setVoteTestid(int id) {
		this.votetestid = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	public String getStudentname() {
		return studentname;
	}
	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}
	public long getStart_time() {
		return start_time;
	}
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	public long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
//	public int getUse_paperid() {
//		return use_paperid;
//	}
//	public void setUse_paperid(int use_paperid) {
//		this.use_paperid = use_paperid;
//	}

}
