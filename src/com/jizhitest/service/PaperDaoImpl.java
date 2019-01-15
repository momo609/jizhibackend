package com.jizhitest.service;

import com.jizhibackend.bean.KnowledgePoint;
import com.jizhibackend.bean.MultipleChoiceQuestion;
import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.RpaperQuestion;
import com.jizhibackend.bean.SingleChoiceQuestion;
import com.jizhibackend.bean.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.lucene3x.Lucene3xCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;

import util.LuceneUtil;

import com.jizhitest.db.DBConn;
import com.mysql.jdbc.Statement;

public class PaperDaoImpl {
	private SqlSessionFactory sessionFactory;
	private SqlSession session;
	private static final String resource = "conf.xml";
	private  String createPaperStmt = "com.jizhitest.mapping.paperMapping.createPaper";
	private  String createTestPaperStmt = "com.jizhitest.mapping.paperMapping.createTestPaper";
	private  static final String getRpaperQuestionByIdStmt = "com.jizhitest.mapping.questionMapping.getRpaperQuestionId";
	private  static final String insertRpaperQuestionByIdStmt = "com.jizhitest.mapping.questionMapping.insertRpaperQuestion";
	private  String deletePaperStmt = "com.jizhitest.mapping.paperMapping.deletePaper";
	private  String addSCQStmt = "com.jizhitest.mapping.paperMapping.addSCQ";
	private  String addSCQ2Stmt = "com.jizhitest.mapping.paperMapping.addSCQ2";
	private  String addQStmt= "com.jizhitest.mapping.paperMapping.addQ";
	private  String delQStmt= "com.jizhitest.mapping.paperMapping.delQ";
	private  String addQ2PStmt= "com.jizhitest.mapping.paperMapping.addQtoP";
	private  String getPaperByKeywordStmt= "com.jizhitest.mapping.paperMapping.searchPaper";
	private  String getPaperByIdsStmt= "com.jizhitest.mapping.paperMapping.getpaperbyids";
	private  String getPaperByIdStmt= "com.jizhitest.mapping.paperMapping.getpaperbyid";
	private  String addKStmt="com.jizhitest.mapping.paperMapping.addK";
	private String getKStmt="com.jizhitest.mapping.paperMapping.getK";
	private  static final String addChoiceSql = "insert into choices(qid,choices) values(?,?)";
	private  static final String getknowledgeid = "select knowledgeid from knowledge_point where knowledge=?";
	private  static final String getChoiceSql = "select choices from choices where qid=?";
	private  String addChoicesStmt= "com.jizhitest.mapping.paperMapping.addChoices";
	private LuceneUtil util=null;
	public PaperDaoImpl() {
	
	        InputStream is = PaperDaoImpl.class.getClassLoader().getResourceAsStream(resource);
	         sessionFactory = new SqlSessionFactoryBuilder().build(is);
	         session=sessionFactory.openSession();
	         util=new LuceneUtil(LuceneUtil.pIndexpath);
	}
	/*public List<SingleChoiceQuestion> getSingleChoiceQustions(int paperid)
	{
		int i=0;
		List<SingleChoiceQuestion> list=new ArrayList<SingleChoiceQuestion>();
		
		try {
			String sql="select * from question_single_choice where paperid=?";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ps.setInt(1, paperid);
			rs=ps.executeQuery();
			while(rs.next())
			{
				SingleChoiceQuestion sq=new SingleChoiceQuestion();
				sq.setId(rs.getInt(1));
				sq.setPaperid(rs.getInt(2));
				sq.setStem(rs.getString(3));
				sq.setChoices(rs.getString(4));
				sq.setAnswer(rs.getString(5));
				sq.setAnswerkey(rs.getString(6));
				sq.setOrder(rs.getInt(7));
				sq.setPoint(rs.getInt(8));
				list.add(sq);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}*/
	//查询指定用户的所有试卷
	public List<Paper> getPapers(int userid)
	{
		int i=0;
		ResultSet rs = null;
		List<Paper> paperlist=new ArrayList<Paper>();
		try {
			
			String sql="select * from paper where owner=? order by createtime desc";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ps.setInt(1, userid);
			rs=ps.executeQuery();
			while(rs.next())
			{
				Paper paper= new Paper();
				paper.setId(rs.getInt(1));
				paper.setTitle(rs.getString(2));
				paper.setCreatetime(rs.getLong(3));
				paper.setOwner(rs.getInt(4));
				paper.setDescription(rs.getString(5));
				paper.setQuestion_num(rs.getInt(6));
				paper.setIsShare(rs.getString(7));
				paperlist.add(paper);
				
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return paperlist;
	}
	
	//查询指定用户的在线试卷
		public List<Paper> getOnlinePapers(int userid)
		{
			int i=0;
			ResultSet rs = null;
			List<Paper> paperlist=new ArrayList<Paper>();
			try {
				
				String sql="select * from paper where owner!=? and isShare=1 order by createtime desc";
			    Connection conn=DBConn.getConnection();
			    UserDaoImpl userdao = new UserDaoImpl();
			    PreparedStatement ps=conn.prepareStatement(sql);
			    ps.setInt(1, userid);
				rs=ps.executeQuery();
				while(rs.next())
				{
					Paper paper= new Paper();
					paper.setId(rs.getInt(1));
					paper.setTitle(rs.getString(2));
					paper.setCreatetime(rs.getLong(3));
					paper.setOwner(rs.getInt(4));
					paper.setOwnerName(userdao.findUser(rs.getInt(4)).getNickname());
					paper.setDescription(rs.getString(5));
					paper.setQuestion_num(rs.getInt(6));
					paperlist.add(paper);
					
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return paperlist;
		}
	
//	public int updatePaperbefore(String content){
//		String sql="insert into paperbefore(paperContent) values(?)";
//		Connection conn=DBConn.getConnection();
//		ResultSet rs=null;
//		int i=0;
//		int id = 0;
//		try {
//			PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
//			ps.setString(1, content);
//			i=ps.executeUpdate();
//			if(i!=0){
//				rs = ps.getGeneratedKeys();//这一句代码就是得到插入的记录的id
//				while(rs.next()){
//				    id=rs.getInt(1);
//				   }
//			}
//			conn.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return id;
//			
//	}
	
//	public String findPaperContent(int id)
//	{
//		Connection conn=DBConn.getConnection();
//		String sql="select paperContent from paperbefore where id=?";
//		PreparedStatement ps;
//		String content = null;
//		try {
//			ps = conn.prepareStatement(sql);
//			ps.setInt(1, id);
//			ResultSet rs=ps.executeQuery();
//			if(rs.next())
//			{
//				content = rs.getString(1);
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		return content;
//	}
	
	
	public Paper getPaperByid(int paperid)
	{
		 Paper paper=session.selectOne(getPaperByIdStmt, paperid);
	     return paper;
	
	
	}
	public int createPaper(Paper paper)
	{
		
		int i=session.insert(createPaperStmt, paper);
		session.commit();
		if(i!=0)
			try {
				util.createIndex(paper);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return i;
		
	}
	public int createTestPaper(Paper paper)
	{
		int paperid=paper.getId();
		int i=session.insert(createTestPaperStmt, paper);
		List<RpaperQuestion> list=session.selectList(getRpaperQuestionByIdStmt,paperid);
		for(RpaperQuestion rq:list)
		{
			rq.setPaperid(paper.getId());
			session.insert(insertRpaperQuestionByIdStmt,rq);
		}
		
		
		session.commit();
	
		return i;
	}
	public int addSingleChoiceQustionList(Paper paper,List<SingleChoiceQuestion> list)
	{
		int paperid=paper.getId();
		int i = 0;
		for(SingleChoiceQuestion scq:list)
		{
			addChoiceQustion(paperid,scq,0);
			i++;
		}
		return i;
	}
	public int addMultipleChoiceQustionList(Paper paper,List<MultipleChoiceQuestion> list)
	{
		int paperid=paper.getId();
		int i = 0;
		for(MultipleChoiceQuestion mcq:list)
		{
			addChoiceQustion(paperid,mcq,0);
			i++;
		}
		return i;
	}
	public int addQustionList(Paper paper,List<Question> list)
	{
		int paperid=paper.getId();
		int i = 0;
		for(Question q:list)
		{
//			System.out.println("问题："+q.getStem());
//			System.out.println("顺序："+q.getQ_order());
			addQuestion(paperid, q, q.getQ_order());
			i++;
		}
		return i;
	}
public int addChoiceQustion(int paperid,MultipleChoiceQuestion scq,int flag)
	{
		int i=0;
		int id;
		Question q=new Question();
		q.setType(4);
		q.setStem(scq.getStem());
		q.setAnswer(scq.getAnswer());
		q.setAnswerkey(scq.getAnswerkey());
		q.setPoint(scq.getPoint());
		q.setTag(scq.getTag());
		q.setLevel(scq.getLevel());
		if(flag==1)
		{
			id=addkg(scq);
			if(id==-1)
			{
				return i;
			}
			else
			   q.setKnowledgeid(id);
		}
		else
			 q.setKnowledgeid(scq.getKnowledgeid());
		if(addQuestion(paperid, q,scq.getQ_order())!=0)
		{
			Connection conn=DBConn.getConnection();		   
			try {
				PreparedStatement ps=conn.prepareStatement(addChoiceSql);
				ps.setInt(1, q.getId());
				ps.setString(2, scq.getChoices());
				i=ps.executeUpdate();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return i;
	}

	public int addChoiceQustion(int paperid,SingleChoiceQuestion scq,int flag)
	{
		int i=0;
		Question q=new Question();
		q.setType(1);
		q.setStem(scq.getStem());
		q.setAnswer(scq.getAnswer());
		q.setPoint(scq.getPoint());
		q.setAnswerkey(scq.getAnswerkey());
		q.setTag(scq.getTag());
		q.setLevel(scq.getLevel());
		int id;
		if(flag==1)
		{
			id=addkg(scq);
			if(id==-1)
			{
				return i;
			}
			else
			   q.setKnowledgeid(id);
		}
		else
			 q.setKnowledgeid(scq.getKnowledgeid());
      System.out.println(q.toString());
		if(addQuestion(paperid, q,scq.getQ_order())!=0)
		{
			Connection conn=DBConn.getConnection();		   
			try {
				PreparedStatement ps=conn.prepareStatement(addChoiceSql);
				ps.setInt(1, q.getId());
				ps.setString(2, scq.getChoices());
				i=ps.executeUpdate();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return i;
	}
	public int addkg(MultipleChoiceQuestion scq)
	{
		int j;
		KnowledgePoint k=new KnowledgePoint();
		k.setKnowledge(scq.getTag());
		j=session.insert(addKStmt,k);
		ResultSet rs = null;
		int a = 0;
		Connection conn=DBConn.getConnection();	
		 try {
	         PreparedStatement ps=conn.prepareStatement(getknowledgeid);
			ps.setString(1, scq.getTag());
			rs=ps.executeQuery();
			while(rs.next())
			{
				a=rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			a=-1;
		}
		
	    return a;
	}

	public int addkg(SingleChoiceQuestion scq)
	{
		int j;
		KnowledgePoint k=new KnowledgePoint();
		k.setKnowledge(scq.getTag());
		j=session.insert(addKStmt,k);
		ResultSet rs = null;
		int a = 0;
		Connection conn=DBConn.getConnection();	
		 try {
	         PreparedStatement ps=conn.prepareStatement(getknowledgeid);
			ps.setString(1, scq.getTag());
			rs=ps.executeQuery();
			while(rs.next())
			{
				a=rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			a=-1;
		}
		
	    return a;
	}
	public int addkg(Question q)
	{
        int j;
		KnowledgePoint k=new KnowledgePoint();
		k.setKnowledge(q.getTag());
		j=session.insert(addKStmt,k);
		ResultSet rs = null;
		int a = 0;
		Connection conn=DBConn.getConnection();	
		 try {
	         PreparedStatement ps=conn.prepareStatement(getknowledgeid);
			ps.setString(1, q.getTag());
			rs=ps.executeQuery();
			while(rs.next())
			{
				a=rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			a=-1;
		}
		
	    return a;
	}
	
		public int addQuestion(int paperid,Question q,int order)
	{
		int i=0;
		
		  i=session.insert(addQStmt, q);
		 if(i!=0)
		{
		    LuceneUtil util=new LuceneUtil("");
		    try {
				util.createIndex(q);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RpaperQuestion r=new RpaperQuestion();
			r.setQuestionid(q.getId());
			r.setPaperid(paperid);
			r.setQ_order(order);
			r.setPoint(q.getPoint());
			i=session.insert(addQ2PStmt, r);
		}
		
		session.commit();
		return i;
	}

		public int addQuestion(int paperid,Question q,int order,int flag)
		{
			int i=0;
			int j=0;
			int id;
			if(flag==1)
			{
				id=addkg(q);
				if(id==-1)
				{
					return i;
				}
				else
				   q.setKnowledgeid(id);
			}
			else
				 q.setKnowledgeid(q.getKnowledgeid());
			i=session.insert(addQStmt, q);
			 if(i!=0)
			{
			    LuceneUtil util=new LuceneUtil("");
			    try {
					util.createIndex(q);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				RpaperQuestion r=new RpaperQuestion();
				r.setQuestionid(q.getId());
				r.setPaperid(paperid);
				r.setQ_order(order);
				r.setPoint(q.getPoint());
				i=session.insert(addQ2PStmt, r);
			}
			
			session.commit();
			return i;
		}
		
	public Set<Integer>copyQuestion(Set<Integer> ids)
	{ 
		Set<Integer> newids=new HashSet<Integer>();
		List<Question> qlist=new QuestionDaoImpl().getQuestionsByids(ids);
		for(Question q:qlist)
		{
			int oldid=q.getId();
			q.setId(0);
			session.insert(addQStmt,q);
			newids.add(q.getId());
		
			if(q.getType()==1||q.getType()==4)
			{
				String choices=getChoices(oldid);
				addChoices(q.getId(), choices);
		    }
		}
		
		session.commit();
		return newids;
	}
	public String getChoices(int qid)
	{
		Connection conn=DBConn.getConnection();
		String choices=null;
		try {
			PreparedStatement ps=conn.prepareStatement(getChoiceSql);
			ps.setInt(1, qid);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			 choices=rs.getString(1);	
		
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return choices;
	}
	public int addChoices(int qid,String choices)
	{
		int i=0;
		Map map=new HashMap<String, Object>();
		map.put("qid", qid);
		map.put("choices", choices);
		session.insert(addChoicesStmt,map);
		session.commit();
		return i;
	}
	public static void main(String[] args) {
		Set<Integer> ids=new HashSet<Integer>();
	    ids.add(648);
	    ids.add(649);
		new PaperDaoImpl().copyQuestion(ids);
	}

	
	public int addExistQuestion(int paperid,Set<Integer> qs,int order)
	{
		int i=0;
		qs=copyQuestion(qs);
		for(int q:qs)
		{
		RpaperQuestion r=new RpaperQuestion();
		r.setQuestionid(q);
		r.setPaperid(paperid);
		r.setQ_order(++order);
		r.setPoint(0);
		i+=session.insert(addQ2PStmt, r);
		}
		session.commit();
		return i;
	}
	public int deletePaper(int paperid)
	{
		int i=0;
		i=	session.delete(deletePaperStmt,paperid);
		session.commit();
        return i;
	}
	public List<Paper> searchPaper(String keyword,int userid)
	{	
		String result=null;
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("keyword", keyword);
		map.put("userid", userid);
		List<Paper> resultList=session.selectList(getPaperByKeywordStmt,map);
		session.commit();
		return resultList;	
	}
	public ResultSet getAllPaper()
	{
		ResultSet rs=null;
		String sql="select * from paper";
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
	//设置paper的共享教师名单
	public int changeShare(String isShare,int userid,int paperid){
		String sql = "update paper set isShare=? where owner=? and id=?";
		Connection conn=DBConn.getConnection();
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, isShare);
			ps.setInt(2, userid);
			ps.setInt(3,paperid);
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	
	//取出paper的共享教师名单
	public String getIsShare(int id)
	{
		ResultSet rs=null;
		String r = null;
		String sql="select isShare from paper where id=?";
		Connection conn=DBConn.getConnection();
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next())
			{
				r = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return r;
	}
	
	public List<Paper> getPaperByids(List<Integer> ids)
	{
		List<Paper> paper=session.selectList(getPaperByIdsStmt, ids);
		session.commit();
		return paper;
		
	}
	public boolean delQ(int paperId,int qId)
	{
		try{
		Map map=new HashMap<String, Object>();
		map.put("paperId", paperId);
		map.put("qId", qId);
		session.delete(delQStmt,map);
		session.commit();
		}catch(Exception e)
		{
			session.rollback();
			return false;
		}
		return true;
		
	}
    
    public List<KnowledgePoint> getKnowledgepoint()
	{
		List<KnowledgePoint> knowledgepoint=session.selectList(getKStmt);
		session.commit();
		return knowledgepoint;
		
	}
	

}
