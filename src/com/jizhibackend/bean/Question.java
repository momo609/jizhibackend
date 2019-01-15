package com.jizhibackend.bean;

public class Question {
	
	@Override
	public String toString() {
		return "Question [questionid=" + questionid + ", type=" + type
				+ ", stem=" + stem + ", answer=" + answer + ", level=" + level
				+ ", knowledgeid=" + knowledgeid + ", answerkey=" + answerkey
				+ ", tag=" + tag + ", point=" + point + ", q_order=" + q_order
				+ ", choices=" + choices + ", addTime=" + addTime + "]";
	}
	public static final int SINGLE_CHOICE=1;
	public static final int MULTI_CHOICE=4;
	public static final int JUDGE=2;
	public static final int FILL_BLANK=3;
	private int questionid;
	private int type;
	private String stem;
	private String answer;
	private int level;
	private int knowledgeid;
	
	public int getKnowledgeid() {
		return knowledgeid;
	}
	public void setKnowledgeid(int knowledgeid) {
		this.knowledgeid = knowledgeid;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getQuestionid() {
		return questionid;
	}
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}
	public String getStem() {
		return stem;
	}
	public void setStem(String stem) {
		this.stem = stem;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswerkey() {
		return answerkey;
	}
	public void setAnswerkey(String answerkey) {
		this.answerkey = answerkey;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	private String answerkey;
	private String tag;
	private int point;
	private int q_order;
	private String choices;
	public String getChoices() {
		return choices;
	}
	public void setChoices(String choices) {
		this.choices = choices;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getQ_order() {
		return q_order;
	}
	public void setQ_order(int order) {
		this.q_order = order;
	}
	public int getId() {
		return questionid;
	}
	public void setId(int id) {
		this.questionid = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private long addTime;
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	

}
