package com.jizhibackend.bean;

public class Concept {
  private String concept;
  private int conceptid;
@Override
public String toString() {
	return "Concept [concept=" + concept + ", conceptid=" + conceptid + "]";
}
public String getConcept() {
	return concept;
}
public void setConcept(String concept) {
	this.concept = concept;
}
public int getConceptid() {
	return conceptid;
}
public void setConceptid(int conceptid) {
	this.conceptid = conceptid;
}

}
