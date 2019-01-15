package com.jizhibackend.bean;

public class VoteTestResult {
private int resultid;
private int votetestid;
private int studentid;
private String answers;
private String proportion;
private String nickname;
private int score;
private int userid;
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
public int getResultid() {
	return resultid;
}
public void setResultid(int resultid) {
	this.resultid = resultid;
}
public int getVoteTestid() {
	return votetestid;
}
public void setVoteTestid(int votetestid) {
	this.votetestid = votetestid;
}
public int getStudentid() {
	return studentid;
}
public void setStudentid(int studentid) {
	this.studentid = studentid;
}
public int getUserid() {
	return userid;
}
public void setUserid(int userid) {
	this.userid = userid;
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

}
