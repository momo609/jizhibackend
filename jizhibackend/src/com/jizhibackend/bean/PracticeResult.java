package com.jizhibackend.bean;

public class PracticeResult {
	private int pid;
	private int paper_id;
	private int stu_id;
	private String answers;
	private String answer_trace;
	private String time_used;
	private long total_time_used;
	private String look_back_times;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPaper_id() {
		return paper_id;
	}
	public void setPaper_id(int paper_id) {
		this.paper_id = paper_id;
	}
	public int getStu_id() {
		return stu_id;
	}
	public void setStu_id(int stu_id) {
		this.stu_id = stu_id;
	}
	public String getAnswers() {
		return answers;
	}
	public void setAnswers(String answers) {
		this.answers = answers;
	}
	public String getAnswer_trace() {
		return answer_trace;
	}
	public void setAnswer_trace(String answer_trace) {
		this.answer_trace = answer_trace;
	}
	public String getTime_used() {
		return time_used;
	}
	public void setTime_used(String time_used) {
		this.time_used = time_used;
	}
	public long getTotal_time_used() {
		return total_time_used;
	}
	public void setTotal_time_used(long total_time_used) {
		this.total_time_used = total_time_used;
	}
	public String getLook_back_times() {
		return look_back_times;
	}
	public void setLook_back_times(String look_back_times) {
		this.look_back_times = look_back_times;
	}

}
