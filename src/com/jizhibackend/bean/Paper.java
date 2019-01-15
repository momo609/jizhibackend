package com.jizhibackend.bean;

public class Paper {
private int id;
private String title;
private long createtime;
private int owner;
private int question_num;
private String description;
private String ownerName;
private String isShare;//Ã‚ø‚ «∑Òπ≤œÌ
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getQuestion_num() {
	return question_num;
}
public void setQuestion_num(int question_num) {
	this.question_num = question_num;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public long getCreatetime() {
	return createtime;
}
public void setCreatetime(long createtime) {
	this.createtime = createtime;
}
public int getOwner() {
	return owner;
}
public void setOwner(int owner) {
	this.owner = owner;
}
public String getOwnerName() {
	return ownerName;
}
public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
}
public String getIsShare() {
	return isShare;
}
public void setIsShare(String isShare) {
	this.isShare = isShare;
}

}
