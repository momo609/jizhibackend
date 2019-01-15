package com.jizhibackend.bean;

public class TestResult {
private int resultid;
private int testid;
private int studentid;
private String answers;
private String answer_trace;
private String time_used;
private long total_time_used;
private String look_back_times;
private String proportion;
private String nickname;
private String tagproportion;
public String getStudentname() {
	return nickname;
}
public void setStudentname(String studentname) {
	this.nickname = studentname;
}
public String getProportion() {
	return proportion;
}
public void setProportion(String proportion) {
	this.proportion = proportion;
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

private int score;
public int getResultid() {
	return resultid;
}
public void setResultid(int resultid) {
	this.resultid = resultid;
}
public int getTestid() {
	return testid;
}
public void setTestid(int testid) {
	this.testid = testid;
}
public int getStudentid() {
	return studentid;
}
public void setStudentid(int studentid) {
	this.studentid = studentid;
}
public String getAnswers() {
	return answers;
}
public void setAnswers(String answers) {
	this.answers = answers;
}
public int getScore() {
	return score;
}
public void setScore(int score) {
	this.score = score;
}
public String getTagproportion() {
	return tagproportion;
}
public void setTagproportion(String tagproportion) {
	this.tagproportion = tagproportion;
}
@Override
public String toString() {
	return "TestResult [resultid=" + resultid + ", testid=" + testid
			+ ", studentid=" + studentid + ", answers=" + answers
			+ ", answer_trace=" + answer_trace + ", time_used=" + time_used
			+ ", total_time_used=" + total_time_used + ", look_back_times="
			+ look_back_times + ", proportion=" + proportion + ", nickname="
			+ nickname + ", tagproportion=" + tagproportion + ", score="
			+ score + "]";
}
}
