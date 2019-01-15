package com.jizhibackend.bean;

public class HandleTestResult {
private int testid;
private int studentid;
private int answer_trace;
private int time;
private int look_back_times;
private int collect;
private int finalresults;
private int knowledgeid;

@Override
public String toString() {
	return "HandleTestResult [testid=" + testid + ", studentid=" + studentid
			+ ", answer_trace=" + answer_trace + ", time=" + time
			+ ", look_back_times=" + look_back_times + ", collect=" + collect
			+ ", finalresults=" + finalresults + ", knowledgeid=" + knowledgeid
			+ "]";
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
public int getAnswer_trace() {
	return answer_trace;
}
public void setAnswer_trace(int answer_trace) {
	this.answer_trace = answer_trace;
}
public int getTime() {
	return time;
}
public void setTime(int time) {
	this.time = time;
}
public int getLook_back_times() {
	return look_back_times;
}
public void setLook_back_times(int look_back_times) {
	this.look_back_times = look_back_times;
}
public int getCollect() {
	return collect;
}
public void setCollect(int collect) {
	this.collect = collect;
}
public int getFinalresults() {
	return finalresults;
}
public void setFinalresults(int finalresults) {
	this.finalresults = finalresults;
}
public int getKnowledgeid() {
	return knowledgeid;
}
public void setKnowledgeid(int knowledgeid) {
	this.knowledgeid = knowledgeid;
}

}
