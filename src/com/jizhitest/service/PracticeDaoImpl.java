package com.jizhitest.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.jizhibackend.bean.PracticeResult;
import com.jizhibackend.bean.TestResult;
import com.jizhitest.db.DBConn;

public class PracticeDaoImpl extends BaseDaoImpl{
	private  String addPracticeResultStmt = "com.jizhitest.mapping.practiceMapping.addPracticeResult";
	private  String findPracticeResultStmt = "com.jizhitest.mapping.practiceMapping.findPracticeResult";
	private  String findPracticeResultByIdStmt = "com.jizhitest.mapping.practiceMapping.findPracticeResultById";

	public int addPracticeResult(PracticeResult t)
	{
		int i=session.insert(addPracticeResultStmt,t);
		session.commit();
		return i;
		
	}
	public TestResult findPracticeResult(int paperid,int userid)
	{
		
		TestResult t=new TestResult();
		t.setTestid(paperid);
		t.setStudentid(userid);
		 t=session.selectOne(findPracticeResultStmt,t);

		return t;
	}
	public List<TestResult> findPracticeResultByPaperid(int paperid) {
		List<TestResult> t=null;
		
		 t=session.selectList(findPracticeResultByIdStmt,paperid);
		return t;
	}
}
