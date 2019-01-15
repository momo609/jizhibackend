package com.jizhitest.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import util.LuceneUtil;

import com.jizhibackend.bean.Gift;
import com.jizhibackend.bean.GiftMessage;
import com.jizhitest.db.DBConn;

public class GiftDaoImpl extends BaseDaoImpl{
	private SqlSessionFactory sessionFactory;
	private SqlSession session;
	private static final String resource = "conf.xml";
	
	private  String getGiftStmt = "com.jizhitest.mapping.giftMapping.getGift";
	private  String getGiftByIdStmt = "com.jizhitest.mapping.giftMapping.getGiftByID";
	private  String addGiftStmt  =  "com.jizhitest.mapping.giftMapping.addGift";
	private LuceneUtil util=null;
	public GiftDaoImpl() {
		
        InputStream is = PaperDaoImpl.class.getClassLoader().getResourceAsStream(resource);
         sessionFactory = new SqlSessionFactoryBuilder().build(is);
         session=sessionFactory.openSession();
         util=new LuceneUtil(LuceneUtil.pIndexpath);
}
	
    public List<Gift> getGift(int classid)
    {
//		return session.selectList(getGiftStmt);	
    	int i=0;
		ResultSet rs = null;
		List<Gift> giftlist=new ArrayList<Gift>();
		try {	
			String sql="select * from gift where class_id=? order by gift_id desc";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ps.setInt(1, classid);
			rs=ps.executeQuery();
			while(rs.next())
			{
				Gift gift = new Gift();
				gift.setGift_id(rs.getInt(1));
				gift.setName(rs.getString(2));
				gift.setPrice(rs.getInt(3));
				gift.setNumbers(rs.getInt(4));
				gift.setDp_url(rs.getString(5));
				gift.setClass_id(rs.getInt(6));
				gift.setOwnerid(rs.getInt(8));
				giftlist.add(gift);
				
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return giftlist;
    	
    }
    public List<Gift> getGiftforTeac(int userid)
    {
//		return session.selectList(getGiftStmt);	
    	int i=0;
		ResultSet rs = null;
		List<Gift> giftlist=new ArrayList<Gift>();
		try {	
			String sql="select * from gift where ownerid=? order by gift_id desc";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ps.setInt(1, userid);
			rs=ps.executeQuery();
			while(rs.next())
			{
				Gift gift = new Gift();
				gift.setGift_id(rs.getInt(1));
				gift.setName(rs.getString(2));
				gift.setPrice(rs.getInt(3));
				gift.setNumbers(rs.getInt(4));
				gift.setDp_url(rs.getString(5));
				gift.setClass_id(rs.getInt(6));
				gift.setOwnerid(rs.getInt(8));
				giftlist.add(gift);
				
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return giftlist;
    	
    }
    public List<GiftMessage> getGiftMessage(int userid){
    	int i=0;
		ResultSet rs = null;
		List<GiftMessage> messagelist=new ArrayList<GiftMessage>();
		try {	
			String sql="SELECT giftid,apply_gift.numbers,isRead,name,className,nickname FROM apply_gift,gift,user WHERE apply_gift.ownerid=? AND studentid=user_id AND giftid=gift_id ORDER BY id DESC";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ps.setInt(1, userid);
			rs=ps.executeQuery();
			while(rs.next())
			{
				GiftMessage gmess = new GiftMessage();
				gmess.setGiftid(rs.getInt(1));
				gmess.setNumbers(rs.getInt(2));
				gmess.setIsRead(rs.getInt(3));
				gmess.setName(rs.getString(4));
				gmess.setClassName(rs.getString(5));
				gmess.setNickname(rs.getString(6));
				messagelist.add(gmess);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messagelist;
    }
    public Gift getGiftById(int giftid)
    {
		return session.selectOne(getGiftByIdStmt, giftid);
    }
    
    public boolean addGift(Gift gift)
	{
	
		int i=0;
		try {
			String sql="insert into gift(name,price,numbers,dp_url,class_id,description,ownerid) values(?,?,?,?,?,?,?)";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, gift.getName());
			ps.setInt(2, gift.getPrice());
			ps.setInt(3, gift.getNumbers());
			ps.setString(4, gift.getDp_url());
			ps.setInt(5, gift.getClass_id());	
			ps.setString(6, gift.getDescription());
			ps.setInt(7, gift.getOwnerid());
			i=ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(i==0)
			return false;
		else return true;
	
	}
    public boolean addApply(int userid,int giftid,int numbers,int ownerid){
    	int i=0;
		try {
			String sql="insert into apply_gift(studentid,giftid,numbers,ownerid) values(?,?,?,?)";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ps.setInt(2, giftid);
			ps.setInt(3, numbers);
			ps.setInt(4, ownerid);	
			i=ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(i==0)
			return false;
		else return true;
    }
    public int updategiftNumbers(int numbers,int giftid,int coins,int userid)
	{
		Connection conn=DBConn.getConnection();
		
		String sql="UPDATE user,gift SET gift.numbers=?,user.coins=? WHERE gift_id=? AND user_id=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, numbers);
			ps.setInt(2,coins);
			ps.setInt(3,giftid);
			ps.setInt(4,userid);
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
    public int updategift(Gift gift)
	{
		Connection conn=DBConn.getConnection();
		
		String sql="UPDATE gift SET numbers=?,name=?,price=? WHERE gift_id=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, gift.getNumbers());
			ps.setString(2,gift.getName());
			ps.setInt(3,gift.getPrice());
			ps.setInt(4,gift.getGift_id());
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
    public int updateGiftMessage(int userid){
    	Connection conn=DBConn.getConnection();
		String sql="UPDATE apply_gift SET isRead=1 WHERE ownerid=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userid);
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
    }
    public boolean deleteGift(int giftid)
	{
		Connection conn=DBConn.getConnection();
		String sql="DELETE FROM gift WHERE gift_id=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, giftid);
		    i=ps.executeUpdate();
		    conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     if(i==0)
	    	 return false;
	     else 
	    	 return true;
	}
    
    
}
