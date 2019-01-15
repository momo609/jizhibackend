package com.jizhitest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import util.LuceneUtil;

import com.jizhibackend.bean.FillBlankQuestion;
import com.jizhibackend.bean.JudgeQuestion;
import com.jizhibackend.bean.MultipleChoiceQuestion;
import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.SingleChoiceQuestion;
import com.jizhibackend.bean.WrongQCount;
import com.jizhitest.db.DBConn;

public class QuestionDaoImpl extends BaseDaoImpl{
	private  static final String getscqStmt = "com.jizhitest.mapping.questionMapping.getSingleChoiceById";
	private  static final String getfbqStmt = "com.jizhitest.mapping.questionMapping.getFillBlankQuestionById";
	private  static final String getjqStmt = "com.jizhitest.mapping.questionMapping.getJudgeQuestionById";
	private  static final String getqStmt = "com.jizhitest.mapping.questionMapping.getQuestionsByPaperId";
	private  static final String getqByTestPaperIdStmt = "com.jizhitest.mapping.questionMapping.getQuestionsByTestPaperId";
	private  static final String getmqStmt = "com.jizhitest.mapping.questionMapping.getMarkedQuestions";
	private  static final String getqByidsStmt = "com.jizhitest.mapping.questionMapping.getQuestionsByids";
	private  static final String getqByidStmt = "com.jizhitest.mapping.questionMapping.getQuestionsByid";
	private  static final String updateQuestionStmt = "com.jizhitest.mapping.questionMapping.updateQuestion";
	private  static final String updateQuestionPointStmt = "com.jizhitest.mapping.questionMapping.updateQuestionPoint";
	private  static final String updateChoicesStmt = "com.jizhitest.mapping.questionMapping.updateChoices";
	private  static final String getChoiceSql = "select choices from choices where qid=?";
	private  static final String addWrongQSql = "insert into wrong(user_id,question_id,test_id,test_title,add_time) values(?,?,?,?,?)";
	
	public List<Question> getQustionsOfPaper(int paperid)
	{
		List<Question> list=null;
        list=session.selectList(getqStmt, paperid);
		return list;
	}
	
	public List<Question> getQustionsOfTestPaper(int testpaperid)
	{
		List<Question> list=null;
        list=session.selectList(getqByTestPaperIdStmt, testpaperid);
		return list;
	}
	public List<Integer> getQuestion(int testpaperid)
	{
		List<Integer> list=new ArrayList<Integer>();
		 String sql=" SELECT questionid FROM r_paper_question,question WHERE r_paper_question.paperid=? AND r_paper_question.questionid=question.id order by r_paper_question.q_order asc";
		 Connection conn=DBConn.getConnection();		   
			try {
				PreparedStatement ps=conn.prepareStatement(sql);
				ps.setInt(1, testpaperid);
				ResultSet rs=ps.executeQuery();
				while (rs.next()) {
					Question q=new Question();
					list.add(rs.getInt(1));
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
	}
	public List<Question> getMarkedQuestions(int userid,int type)
	{
		int i=1;
		List<Question> list=new ArrayList<Question>();
        String sql="SELECT id,question.type,stem,answer,answerkey,tag,add_time FROM collection,question WHERE collection.user_id=? AND collection.type=? AND collection.question_id=question.id ORDER BY collection_id desc";
        Connection conn=DBConn.getConnection();		   
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ps.setInt(2, type);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				Question q=new Question();
				System.out.println(rs.getInt(1));
				q.setId(rs.getInt(1));
				q.setType(rs.getInt(2));
				q.setStem(rs.getString(3));
				q.setAnswer(rs.getString(4));
				q.setAnswerkey(rs.getString(5));
				q.setTag(rs.getString(6));
				q.setAddTime(rs.getLong(7));
				q.setQ_order(i++);
				list.add(q);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<Question> getQuestionsofconcepts(String tag)
	{
		int i=1;
		List<Question> list=new ArrayList<Question>();
        String sql="SELECT stem,answer,answerkey,tag,question.id,paperid FROM r_paper_question,question WHERE question.tag like '"+tag+"%' AND question.id=r_paper_question.questionid AND r_paper_question.paperid=155 ";
        Connection conn=DBConn.getConnection();	
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				Question q=new Question();
				//System.out.println(rs.getInt(1));
				q.setStem(rs.getString(1));
				q.setAnswer(rs.getString(2));
				q.setAnswerkey(rs.getString(3));
				q.setTag(rs.getString(4));
				q.setId(rs.getInt(5));
				q.setQ_order(i++);
				list.add(q);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public boolean DeleteQuestion(int paperId,int qId)
	{
		
		return false;
		
	}
	public boolean updateQuestion(Question q,int paperid)
	{
		try
		{
		session.update(updateQuestionStmt,q);
	    Map map=new HashMap<String,Object>();
	    map.put("questionid", q.getQuestionid());
	    map.put("paperid", paperid);
	    map.put("point", q.getPoint());
	    session.update(updateQuestionPointStmt,map);
		if(q.getChoices()!=null)
			session.update(updateChoicesStmt,q);
		session.commit();
		}catch (Exception e)
		{
			e.printStackTrace();
			session.rollback();
			return false;
		}
		return true;
	}

	public List<Question> getWrongQuestions(int userid,int testid)
	{
		int i=1;
		List<Question> list=new ArrayList<Question>();
        String sql="SELECT id,type,stem,answer,answerkey,tag FROM wrong,question WHERE wrong.user_id=? AND wrong.test_id=? AND wrong.question_id=question.id";
        Connection conn=DBConn.getConnection();		   
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ps.setInt(2, testid);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				Question q=new Question();
				q.setId(rs.getInt(1));
				q.setType(rs.getInt(2));
				q.setStem(rs.getString(3));
				q.setAnswer(rs.getString(4));
				q.setAnswerkey(rs.getString(5));
				q.setTag(rs.getString(6));
				q.setQ_order(i++);
				list.add(q);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<Question> getWrongQuestions(int userid)
	{
		int i=1;
		List<Question> list=new ArrayList<Question>();
        String sql="SELECT id,type,stem,answer,answerkey,tag,add_time FROM wrong,question WHERE wrong.user_id=? AND wrong.question_id=question.id ORDER BY wq_id desc";
        Connection conn=DBConn.getConnection();	
        long currTime = System.currentTimeMillis();
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				if(currTime>rs.getLong(7)){
					Question q=new Question();
					q.setId(rs.getInt(1));
					q.setType(rs.getInt(2));
					q.setStem(rs.getString(3));
					q.setAnswer(rs.getString(4));
					q.setAnswerkey(rs.getString(5));
					q.setTag(rs.getString(6));
					q.setAddTime(rs.getLong(7));
					q.setQ_order(i++);
					list.add(q);
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<Integer> WrongQuestions(int userid,int testid)
	{
		int i=1;
		List<Integer> list=new ArrayList<Integer>();
        String sql="SELECT question_id FROM wrong WHERE wrong.user_id=? AND test_id=?";
        Connection conn=DBConn.getConnection();	
        long currTime = System.currentTimeMillis();
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ps.setInt(2, testid);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
					list.add(rs.getInt(1));
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<Question> getPracticeWrongQuestions(int userid)
	{
		int i=1;
		List<Question> list=new ArrayList<Question>();
        String sql="SELECT id,type,stem,answer,answerkey,tag,add_time FROM practice_wrong,question WHERE practice_wrong.user_id=? AND practice_wrong.question_id=question.id ORDER BY wq_id desc";
        Connection conn=DBConn.getConnection();		   
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				Question q=new Question();
				q.setId(rs.getInt(1));
				q.setType(rs.getInt(2));
				q.setStem(rs.getString(3));
				q.setAnswer(rs.getString(4));
				q.setAnswerkey(rs.getString(5));
				q.setTag(rs.getString(6));
				q.setAddTime(rs.getLong(7));
				q.setQ_order(i++);
				list.add(q);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<WrongQCount> getWrongCountOfTest(int userid)
	{
		List<WrongQCount> list=new ArrayList<WrongQCount>();
		String sql="SELECT test_id,test_title,count(wq_id) FROM wrong WHERE wrong.user_id=? group by test_id";
		 Connection conn=DBConn.getConnection();		   
			try {
				PreparedStatement ps=conn.prepareStatement(sql);
				ps.setInt(1, userid);
				ResultSet rs=ps.executeQuery();
				while (rs.next()) {
					WrongQCount qc=new WrongQCount();
					qc.setTestId(rs.getInt(1));
					qc.setTestTitle(rs.getString(2));
					qc.setCount(rs.getInt(3));
			
					list.add(qc);
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return list;
		
	}
	
	public Map<String, List<?>> getQuestionMapOfPaper (int paperid)
	{
		Map<String, List<?>> map=new HashMap<String, List<?>>();
		List<SingleChoiceQuestion> scqList=new ArrayList<SingleChoiceQuestion>();
		List<FillBlankQuestion> fbqList=new ArrayList<FillBlankQuestion>();
		List<JudgeQuestion> jqList=new ArrayList<JudgeQuestion>();
		List<MultipleChoiceQuestion> mcqList=new ArrayList<MultipleChoiceQuestion>();
		List<Question> list=getQustionsOfPaper(paperid);
		int order=1;
		for(Question q:list)
		{
			
			int id=q.getId();
			if(q.getType()==Question.SINGLE_CHOICE)
			{
			      SingleChoiceQuestion scq= new SingleChoiceQuestion();
			      
				  scq.setId(q.getId());
				  scq.setAnswerkey(q.getAnswerkey());
				  scq.setStem(q.getStem());
			      scq.setAnswer(q.getAnswer());
			      scq.setPoint(q.getPoint());
			      scq.setQ_order(order);
			      scq.setLevel(q.getLevel());
			      scq.setTag(q.getTag());
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						scq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      scqList.add(scq);
			  
			}else if(q.getType()==Question.MULTI_CHOICE)
			{
			      MultipleChoiceQuestion mcq= new MultipleChoiceQuestion();
			      
				  mcq.setId(q.getId());
				  mcq.setAnswerkey(q.getAnswerkey());
				  mcq.setStem(q.getStem());
				  mcq.setAnswer(q.getAnswer());
				  mcq.setPoint(q.getPoint());
				  mcq.setQ_order(order);
				  mcq.setLevel(q.getLevel());
				  mcq.setTag(q.getTag());
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						mcq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      mcqList.add(mcq);
			  
			}else if(q.getType()==Question.JUDGE)
			{
				JudgeQuestion jq= new JudgeQuestion();
				jq.setId(q.getId());
				jq.setAnswerkey(q.getAnswerkey());
				  jq.setStem(q.getStem());
				  jq.setAnswer(q.getAnswer());
				  jq.setPoint(q.getPoint());
				  jq.setQ_order(order);
				  jq.setLevel(q.getLevel());
				  jq.setTag(q.getTag());
				  jqList.add(jq);
				
			}
			else if(q.getType()==Question.FILL_BLANK)
			{
				FillBlankQuestion fbq= new FillBlankQuestion();
				fbq.setId(q.getId());
				fbq.setAnswerkey(q.getAnswerkey());
				fbq.setStem(q.getStem());
				fbq.setAnswer(q.getAnswer());
				fbq.setPoint(q.getPoint());
				fbq.setQ_order(order);
				fbq.setLevel(q.getLevel());
				fbq.setTag(q.getTag());
				fbqList.add(fbq);
				
			}
			order++;
		}
		map.put("SingleChoiceQuestion", scqList);
		map.put("JudgeQuestion", jqList);
		map.put("FillBlankQuestion", fbqList);
		map.put("MultipleChoiceQuestion", mcqList);
		return map;
		
	}
	public Map<String, List<?>> getQuestionMapOfTestPaper (int testpaperid)
	{
		Map<String, List<?>> map=new HashMap<String, List<?>>();
		List<SingleChoiceQuestion> scqList=new ArrayList<SingleChoiceQuestion>();
		List<FillBlankQuestion> fbqList=new ArrayList<FillBlankQuestion>();
		List<JudgeQuestion> jqList=new ArrayList<JudgeQuestion>();
		List<MultipleChoiceQuestion> mcqList=new ArrayList<MultipleChoiceQuestion>();
		List<Question> list=getQustionsOfTestPaper(testpaperid);
		
		int order=1;
		for(Question q:list)
		{
			
			int id=q.getId();
			if(q.getType()==Question.SINGLE_CHOICE)
			{
			      SingleChoiceQuestion scq= new SingleChoiceQuestion();
			      
				  scq.setId(q.getId());
				  scq.setAnswerkey(q.getAnswerkey());
				  scq.setStem(q.getStem());
			      scq.setAnswer(q.getAnswer());
			      scq.setPoint(q.getPoint());
			      scq.setQ_order(order);
			      scq.setLevel(q.getLevel());
			      scq.setTag(q.getTag());
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						scq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      scqList.add(scq);
			  
			}else if(q.getType()==Question.MULTI_CHOICE)
			{
			      MultipleChoiceQuestion mcq= new MultipleChoiceQuestion();
			      
				  mcq.setId(q.getId());
				  mcq.setAnswerkey(q.getAnswerkey());
				  mcq.setStem(q.getStem());
				  mcq.setAnswer(q.getAnswer());
				  mcq.setPoint(q.getPoint());
				  mcq.setQ_order(order);
				  mcq.setLevel(q.getLevel());
				  mcq.setTag(q.getTag());
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						mcq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      mcqList.add(mcq);
			  
			}else if(q.getType()==Question.JUDGE)
			{
				JudgeQuestion jq= new JudgeQuestion();
				jq.setId(q.getId());
				jq.setAnswerkey(q.getAnswerkey());
				  jq.setStem(q.getStem());
				  jq.setAnswer(q.getAnswer());
				  jq.setPoint(q.getPoint());
				  jq.setQ_order(order);
				  jq.setLevel(q.getLevel());
				  jq.setTag(q.getTag());
				  jqList.add(jq);
				
			}
			else if(q.getType()==Question.FILL_BLANK)
			{
				FillBlankQuestion fbq= new FillBlankQuestion();
				fbq.setId(q.getId());
				fbq.setAnswerkey(q.getAnswerkey());
				fbq.setStem(q.getStem());
				fbq.setAnswer(q.getAnswer());
				fbq.setPoint(q.getPoint());
				fbq.setQ_order(order);
				fbq.setLevel(q.getLevel());
				fbq.setTag(q.getTag());
				fbqList.add(fbq);
				
			}
			order++;
		}
		map.put("SingleChoiceQuestion", scqList);
		map.put("JudgeQuestion", jqList);
		map.put("FillBlankQuestion", fbqList);
		map.put("MultipleChoiceQuestion", mcqList);
		return map;
		
	}
	public Map<String, List<?>> getMarkedQuestionMap(int userid,int type)
	{
		Map<String, List<?>> map=new HashMap<String, List<?>>();
		List<SingleChoiceQuestion> scqList=new ArrayList<SingleChoiceQuestion>();
		List<FillBlankQuestion> fbqList=new ArrayList<FillBlankQuestion>();
		List<JudgeQuestion> jqList=new ArrayList<JudgeQuestion>();
		List<MultipleChoiceQuestion> mcqList=new ArrayList<MultipleChoiceQuestion>();
		List<Question> list=getMarkedQuestions(userid,type);
		int order=1;
		for(Question q:list)
		{
			
			int id=q.getId();
			if(q.getType()==Question.SINGLE_CHOICE)
			{
			      SingleChoiceQuestion scq= new SingleChoiceQuestion();
			      
				  scq.setId(q.getId());
				  scq.setAnswerkey(q.getAnswerkey());
				  scq.setStem(q.getStem());
			      scq.setAnswer(q.getAnswer());
			      scq.setPoint(q.getPoint());
			      scq.setQ_order(order);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						scq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      scqList.add(scq);
			  
			}else if(q.getType()==Question.MULTI_CHOICE)
			{
			      MultipleChoiceQuestion mcq= new MultipleChoiceQuestion();
			      
				  mcq.setId(q.getId());
				  mcq.setAnswerkey(q.getAnswerkey());
				  mcq.setStem(q.getStem());
				  mcq.setAnswer(q.getAnswer());
				  mcq.setPoint(q.getPoint());
				  mcq.setQ_order(order);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						mcq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      mcqList.add(mcq);
			  
			}else if(q.getType()==Question.JUDGE)
			{
				JudgeQuestion jq= new JudgeQuestion();
				jq.setId(q.getId());
				jq.setAnswerkey(q.getAnswerkey());
				  jq.setStem(q.getStem());
				  jq.setAnswer(q.getAnswer());
				  jq.setPoint(q.getPoint());
				  jq.setQ_order(order);
				  jqList.add(jq);
				
			}
			else if(q.getType()==Question.FILL_BLANK)
			{
				FillBlankQuestion fbq= new FillBlankQuestion();
				fbq.setId(q.getId());
				fbq.setAnswerkey(q.getAnswerkey());
				fbq.setStem(q.getStem());
				fbq.setAnswer(q.getAnswer());
				fbq.setPoint(q.getPoint());
				fbq.setQ_order(order);
				fbqList.add(fbq);
				
			}
			order++;
		 }
		map.put("SingleChoiceQuestion", scqList);
		map.put("JudgeQuestion", jqList);
		map.put("FillBlankQuestion", fbqList);
		map.put("MultipleChoiceQuestion", mcqList);
		return map;
		}
	
	public Map<String, List<?>> getWrongQuestionMap(int userid,int type)
	{
		Map<String, List<?>> map=new HashMap<String, List<?>>();
		List<SingleChoiceQuestion> scqList=new ArrayList<SingleChoiceQuestion>();
		List<FillBlankQuestion> fbqList=new ArrayList<FillBlankQuestion>();
		List<JudgeQuestion> jqList=new ArrayList<JudgeQuestion>();
		List<MultipleChoiceQuestion> mcqList=new ArrayList<MultipleChoiceQuestion>();
		List<Question> list;
		if(type==1)
		{
			list=getWrongQuestions(userid);
		}else
		{
			list=getPracticeWrongQuestions(userid);
		}
		int order=1;
		for(Question q:list)
		{
			int id=q.getId();
			if(q.getType()==Question.SINGLE_CHOICE)
			{
			      SingleChoiceQuestion scq= new SingleChoiceQuestion();
			      
				  scq.setId(q.getId());
				  scq.setAnswerkey(q.getAnswerkey());
				  scq.setStem(q.getStem());
			      scq.setAnswer(q.getAnswer());
			      scq.setPoint(q.getPoint());
			      scq.setQ_order(order);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						scq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      scqList.add(scq);
			  
			}else if(q.getType()==Question.MULTI_CHOICE)
			{
			      MultipleChoiceQuestion mcq= new MultipleChoiceQuestion();
			      
				  mcq.setId(q.getId());
				  mcq.setAnswerkey(q.getAnswerkey());
				  mcq.setStem(q.getStem());
				  mcq.setAnswer(q.getAnswer());
				  mcq.setPoint(q.getPoint());
				  mcq.setQ_order(order);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						mcq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      mcqList.add(mcq);
			  
			}else if(q.getType()==Question.JUDGE)
			{
				JudgeQuestion jq= new JudgeQuestion();
				jq.setId(q.getId());
				jq.setAnswerkey(q.getAnswerkey());
				  jq.setStem(q.getStem());
				  jq.setAnswer(q.getAnswer());
				  jq.setPoint(q.getPoint());
				  jq.setQ_order(order);
				  jqList.add(jq);
				
			}
			else if(q.getType()==Question.FILL_BLANK)
			{
				FillBlankQuestion fbq= new FillBlankQuestion();
				fbq.setId(q.getId());
				fbq.setAnswerkey(q.getAnswerkey());
				fbq.setStem(q.getStem());
				fbq.setAnswer(q.getAnswer());
				fbq.setPoint(q.getPoint());
				fbq.setQ_order(order);
				fbqList.add(fbq);
			}
			order++;
		}
		map.put("SingleChoiceQuestion", scqList);
		map.put("JudgeQuestion", jqList);
		map.put("FillBlankQuestion", fbqList);
		map.put("MultipleChoiceQuestion", mcqList);
		return map;
	
}

	public int bookmarkQuestion(int userid,int questionid,int type,int time)
	{
		int i=0;
	    
		try {
			String sql="insert into collection(user_id,question_id,add_time,type) values(?,?,?,?)";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ps.setInt(2, questionid);
			ps.setInt(3, time);
			ps.setInt(4, type);
	
			i=ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	  return i;
	}
	public int addBookmarkList(int testid,List<Integer> qIDlist,int userid,int type,int time)
	{
		int i=0;
		//找到testid对应的paperid
		 MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
		 MyTest myTest = myTestDaoImpl.getTestByid(testid);
		 QuestionDaoImpl questionDaoImpl =new QuestionDaoImpl();
		 List<Question> list=getQustionsOfTestPaper(myTest.getUse_paperid());
		for(Integer qid:qIDlist)
		{
			//
			//根据qid去找到真正的questionid
			int questionid = list.get(qid-1).getQuestionid();
			i+=bookmarkQuestion(userid, questionid,type,time);
			System.out.println("4个:"+userid+":"+questionid+":"+type+":"+time);
	
		}
		return i;
	}
	public int addWrongQuestions(int userid,String wrongIds,int testId,String testTitle,long endTime)
	{
		int i=0;
		String[]  wid=wrongIds.split(",");
//		long addTime=System.currentTimeMillis();
		for(String id:wid)
		{
		try {
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(addWrongQSql);
			ps.setInt(1, userid);
			ps.setInt(2, Integer.parseInt(id));
			ps.setInt(3, testId);
			ps.setString(4, testTitle);
			ps.setLong(5, endTime);
			i+=ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
		return i ;
	}
	public ResultSet getAllQuestion()
	{
		ResultSet rs=null;
		String sql="select * from question";
		Connection conn=DBConn.getConnection();
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public List<Question> getQuestionsByids(Set<Integer> ids)
	{
		List<Question> list=null;
		if(ids.size()>0)
		{
		list=session.selectList(getqByidsStmt,new ArrayList<Integer>(ids));
		session.commit();
		}
	    return list;
	}
	public Map<String, List<?>> getOneQuestionMap(int qid)
	{
		Map<String, List<?>> map=new HashMap<String, List<?>>();
		List<SingleChoiceQuestion> scqList=new ArrayList<SingleChoiceQuestion>();
		List<FillBlankQuestion> fbqList=new ArrayList<FillBlankQuestion>();
		List<JudgeQuestion> jqList=new ArrayList<JudgeQuestion>();
		List<MultipleChoiceQuestion> mcqList=new ArrayList<MultipleChoiceQuestion>();
		List<Question> list=new ArrayList<Question>();
		list.add(getQuestionsByid(qid));
		for(Question q:list)
		{
			int id=q.getId();
			if(q.getType()==Question.SINGLE_CHOICE)
			{
			      SingleChoiceQuestion scq= new SingleChoiceQuestion();
			      
				  scq.setId(q.getId());
				  scq.setAnswerkey(q.getAnswerkey());
				  scq.setStem(q.getStem());
			      scq.setAnswer(q.getAnswer());
			      scq.setPoint(q.getPoint());
			      scq.setQ_order(1);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						scq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			      scqList.add(scq);
			}else if(q.getType()==Question.MULTI_CHOICE)
			{
			      MultipleChoiceQuestion mcq= new MultipleChoiceQuestion();
			      
				  mcq.setId(q.getId());
				  mcq.setAnswerkey(q.getAnswerkey());
				  mcq.setStem(q.getStem());
				  mcq.setAnswer(q.getAnswer());
				  mcq.setPoint(q.getPoint());
				  mcq.setQ_order(1);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						mcq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			      mcqList.add(mcq);
			  
			}else if(q.getType()==Question.JUDGE)
			{
				JudgeQuestion jq= new JudgeQuestion();
				jq.setId(q.getId());
				jq.setAnswerkey(q.getAnswerkey());
				  jq.setStem(q.getStem());
				  jq.setAnswer(q.getAnswer());
				  jq.setPoint(q.getPoint());
				  jq.setQ_order(1);
				  jqList.add(jq);
				
			}
			else if(q.getType()==Question.FILL_BLANK)
			{
				FillBlankQuestion fbq= new FillBlankQuestion();
				fbq.setId(q.getId());
				fbq.setAnswerkey(q.getAnswerkey());
				fbq.setStem(q.getStem());
				fbq.setAnswer(q.getAnswer());
				fbq.setPoint(q.getPoint());
				fbq.setQ_order(1);
				fbqList.add(fbq);
				
			}
			map.put("SingleChoiceQuestion", scqList);
			map.put("JudgeQuestion", jqList);
			map.put("FillBlankQuestion", fbqList);
			map.put("MultipleChoiceQuestion", mcqList);
		}
	return map;
	
}
	//获取推荐题目的Map
	public Map<String, List<?>> getRecommendMap(int userid)
	{
		List<Question> wqList=getWrongQuestions(userid);
		List<Question> tcqList=getTimeConsumingQofUser(userid);
		//统计错题中各tag的出现次数
		Map<String,Integer> map=new HashMap<String, Integer>();
		countTag(wqList, map);
		countTag(tcqList, map);
		map=sortMapByValue(map);//标签排序
		Iterator<Entry<String, Integer>> iterator=map.entrySet().iterator();
		Set<Integer> idSet = new HashSet<Integer>();
		for(int i=0;i<10&&iterator.hasNext();i++)
		{
			Entry<String, Integer> e=iterator.next();
			System.out.println(e.getKey()+"   "+e.getValue());
			LuceneUtil util=new LuceneUtil(LuceneUtil.qIndexpath);
			try {
				List<Integer> list=util.searchByTerm("tag", e.getKey(), 2);
				idSet.addAll(list);
			} catch (InvalidTokenOffsetsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		List<Question> qList=getQuestionsByids(idSet);
		return getQuestionMap(qList);
	}
	private void countTag(List<Question> qlist,Map<String,Integer> map)
	{
		for(Question q:qlist)//统计tag出现次数
		{
			String[] tags=q.getTag().split(",");
			Integer i;
			for(String tag:tags)
			{
				if(tag.trim().equals("")||tag==null)
					continue;
				if((i=map.get(tag))!=null)
				{
					map.put(tag, ++i);
				}else
				{
					map.put(tag, 1);
				}
			}
		}
	}
	private Question getQuestionsByid(int qid) {
		Question q=session.selectOne(getqByidStmt,qid);
		session.commit();
		return q;
		
	}
	//将QuestionList转成QuestionMap
	public Map<String, List<?>> getQuestionMap(List<Question> qList)
	{
		Map<String, List<?>> map=new HashMap<String, List<?>>();
		List<SingleChoiceQuestion> scqList=new ArrayList<SingleChoiceQuestion>();
		List<FillBlankQuestion> fbqList=new ArrayList<FillBlankQuestion>();
		List<JudgeQuestion> jqList=new ArrayList<JudgeQuestion>();
		List<MultipleChoiceQuestion> mcqList=new ArrayList<MultipleChoiceQuestion>();
	    int i=1;
		for(Question q:qList)
		{
			int id=q.getId();
			if(q.getType()==Question.SINGLE_CHOICE)
			{
			      SingleChoiceQuestion scq= new SingleChoiceQuestion();
				  scq.setId(q.getId());
				  scq.setAnswerkey(q.getAnswerkey());
				  scq.setStem(q.getStem());
			      scq.setAnswer(q.getAnswer());
			      scq.setPoint(q.getPoint());
			      scq.setQ_order(i++);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						scq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			      scqList.add(scq);
			  
			}else if(q.getType()==Question.MULTI_CHOICE)
			{
			      MultipleChoiceQuestion mcq= new MultipleChoiceQuestion();
			      
				  mcq.setId(q.getId());
				  mcq.setAnswerkey(q.getAnswerkey());
				  mcq.setStem(q.getStem());
				  mcq.setAnswer(q.getAnswer());
				  mcq.setPoint(q.getPoint());
				  mcq.setQ_order(i++);
			      Connection conn=DBConn.getConnection();		   
					try {
						PreparedStatement ps=conn.prepareStatement(getChoiceSql);
						ps.setInt(1, id);
						ResultSet rs=ps.executeQuery();
						while (rs.next()) {
						String choices=rs.getString(1);
						mcq.setChoices(choices);
						}
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			      mcqList.add(mcq);
			  
			}else if(q.getType()==Question.JUDGE)
			{
				JudgeQuestion jq= new JudgeQuestion();
				jq.setId(q.getId());
				jq.setAnswerkey(q.getAnswerkey());
				  jq.setStem(q.getStem());
				  jq.setAnswer(q.getAnswer());
				  jq.setPoint(q.getPoint());
				  jq.setQ_order(i++);
				  jqList.add(jq);	
			}
			else if(q.getType()==Question.FILL_BLANK)
			{
				FillBlankQuestion fbq= new FillBlankQuestion();
				fbq.setId(q.getId());
				fbq.setAnswerkey(q.getAnswerkey());
				fbq.setStem(q.getStem());
				fbq.setAnswer(q.getAnswer());
				fbq.setPoint(q.getPoint());
				fbq.setQ_order(q.getQ_order());
				fbqList.add(fbq);
			}
			map.put("SingleChoiceQuestion", scqList);
			map.put("JudgeQuestion", jqList);
			map.put("FillBlankQuestion", fbqList);
			map.put("MultipleChoiceQuestion", mcqList);
		}
	return map;
	}
	public static void main(String[] args) {
		  	QuestionDaoImpl dao=new QuestionDaoImpl();
		  	dao.bookmarkQuestion(1069, 815, 1480000000,1);
//		  	Question q=new Question();
//		  	q.setQuestionid(618);
//		  	q.setStem("哈哈");
		
	}
	//获取用户的费时题目
	public List<Question> getTimeConsumingQofUser(int userid)
	{
		List<Question> timeConsumingQlist=new ArrayList<Question>();
		String sql="SELECT use_paperid,avg_time,time_used FROM test,test_result WHERE testid=test.id and studentid=? ORDER BY resultid desc limit 2";
        Connection conn=DBConn.getConnection();		   
		try {
			QuestionDaoImpl dao=new QuestionDaoImpl();
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
			List<Integer> qOrderList=new ArrayList<Integer>();
			int paperid=rs.getInt(1);
			String avgTime=rs.getString(2);
			String useTime=rs.getString(3);
			String[] avgTimes=avgTime.substring(1,avgTime.length()-1).split(",");
			String[] useTimes=useTime.substring(1,useTime.length()-1).split(",");
			for(int i=0;i<avgTimes.length-1;i++)
				{
				 if(Long.parseLong(avgTimes[i].trim())<Long.parseLong(useTimes[i].trim()))
				 {
					 qOrderList.add(i);
				 }
				}
			 List<Question> qlist=dao.getQustionsOfPaper(paperid);
			 for(int i:qOrderList)
			 {
				 timeConsumingQlist.add(qlist.get(i));
			 }
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeConsumingQlist;
	}
	//根据推荐题目的权重股对Map进行排序
	public Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {  
	    Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();  
	    if (oriMap != null && !oriMap.isEmpty()) {  
	        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(oriMap.entrySet());  
	        Collections.sort(entryList,  
	                new Comparator<Map.Entry<String, Integer>>() {  
	                    public int compare(Entry<String, Integer> entry1,  
	                            Entry<String, Integer> entry2) {  
	                        int value1 = 0, value2 = 0;  
	                        try {  
	                            value1 = entry1.getValue();  
	                            value2 = entry2.getValue();  
	                        } catch (NumberFormatException e) {  
	                            value1 = 0;  
	                            value2 = 0;  
	                        }  
	                        return value2 - value1;  
	                    }  
	                });  
	        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();  
	        Entry<String, Integer> tmpEntry = null;  
	        while (iter.hasNext()) {  
	            tmpEntry = iter.next();  
	            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
	        }  
	    }  
	    return sortedMap;  
	}  
}
