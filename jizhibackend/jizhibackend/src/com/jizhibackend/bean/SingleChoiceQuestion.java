package com.jizhibackend.bean;

public class SingleChoiceQuestion {
	private int id;
	private String stem;
	private String choices;
	private String answer;
	private String answerkey;
	private int q_order;
	private int point;
	private int level;
	private String tag;
	private int type;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStem() {
		return stem;
	}
	public void setStem(String stem) {
		this.stem = stem;
	}
	public String getChoices() {
		return choices;
	}
	public void setChoices(String choices) {
		this.choices = choices;
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
	@Override
	public String toString() {
		return "SingleChoiceQuestion [id=" + id + ", stem=" + stem
				+ ", choices=" + choices + ", answer=" + answer
				+ ", answerkey=" + answerkey + ", q_order=" + q_order
				+ ", point=" + point + ", level=" + level + ", tag=" + tag
				+ ", type=" + type + "]";
	}
	
	

}
