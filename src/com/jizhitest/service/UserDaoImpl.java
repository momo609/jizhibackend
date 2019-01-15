package com.jizhitest.service;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.jizhibackend.bean.User;
import com.jizhitest.db.DBConn;

public class UserDaoImpl extends BaseDaoImpl{
	private  String regStmt= "com.jizhitest.mapping.userMapping.registerUser";
	private  String uptStmt= "com.jizhitest.mapping.userMapping.updateUser";
	public boolean registerUser(User user)
	{
		String sql="insert into user(username,password,email,usertype,nickname) values(?,?,?,?,?)";
		
		Connection conn=DBConn.getConnection();
		int i=0;
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.setInt(4, user.getUsertype());
			ps.setString(5, user.getNickname());
			
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
	public boolean registerUser2(User user)
	{
		int i=0;
		i=session.insert(regStmt,user);
		session.commit();
		if(i==0)
			return false;
		else 
			return true;
	}
	public int updateUser(User user)
	{
		int i=0;
		i=session.update(uptStmt,user);
		session.commit();
		return i;
	}
	public User findUser(String username)
	{
		Connection conn=DBConn.getConnection();
		String sql="select * from user where username=?";
		PreparedStatement ps;
		User user=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				user=new User();
				user.setUserid(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setUsertype(rs.getInt(5));
				user.setNickname(rs.getString(6));
				user.setDp_url(rs.getString(7));
				user.setCoins(rs.getInt(8));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return user;
	}
	public User findUser(int userid)
	{
		Connection conn=DBConn.getConnection();
		String sql="select * from user where user_id=?";
		PreparedStatement ps;
		User user=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				user=new User();
				user.setUserid(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setUsertype(rs.getInt(5));
				user.setNickname(rs.getString(6));
				user.setDp_url(rs.getString(7));
				user.setCoins(rs.getInt(8));
				user.setClassname(rs.getString(13));
				user.setSharePaper(rs.getString(14));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return user;
	}
	
	//查找所有教师
	public ArrayList<User> findAllTeachers(int userid){
		Connection conn = DBConn.getConnection();
		String sql = "select * from user where usertype=? AND user_id!=?";
		PreparedStatement ps;
		User user;
		ArrayList<User> list = new ArrayList<User>();
		try{
			ps = conn.prepareStatement(sql);
			ps.setInt(1,2);
			ps.setInt(2,userid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				user=new User();
				user.setUserid(rs.getInt(1));
				user.setNickname(rs.getString(6));
				list.add(user);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return list;
		
	}
	
	public User findUserByOpenid(String openid)
	{
		Connection conn=DBConn.getConnection();
		String sql="select * from user where openid=?";
		PreparedStatement ps;
		User user=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, openid);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				user=new User();
				user.setUserid(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setUsertype(rs.getInt(5));
				user.setNickname(rs.getString(6));
				user.setDp_url(rs.getString(7));
				user.setCoins(rs.getInt(8));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return user;
	}
	public int binduser(User user,String openid)
	{
		Connection conn=DBConn.getConnection();
		String sql="update user set openid=?  where user_id=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, openid);
			ps.setInt(2,user.getUserid());
			i=ps.executeUpdate();
			
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	public int updateCoins(int coins,int userid)
	{
		Connection conn=DBConn.getConnection();
		String sql="update user set coins=?  where user_id=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, coins);
			ps.setInt(2,userid);
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	//更改共享的paperID
	public int updateSharePaper(int id,int userid)
	{
		Connection conn=DBConn.getConnection();
		String sql = "UPDATE user u1 INNER JOIN user u2 ON u1.user_id=u2.user_id SET u1.SharePaper=CONCAT_WS(',',u2.SharePaper,?) WHERE u1.`user_id`=?;";
		//		String sql="update user set coins=?  where user_id=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2,userid);
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	
	
	//设置共享的paperID
	public int setSharePaper(int id,int userid)
	{
		Connection conn=DBConn.getConnection();
		String sql = "UPDATE user set SharePaper=? where user_id=?";
		//		String sql="update user set coins=?  where user_id=?";
		PreparedStatement ps;
		int i=0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2,userid);
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	
	
	//获取共享的paperid
	public String findShareid(int userid)
	{
		String shareid = null;
		Connection conn = DBConn.getConnection();
		String sql = "select SharePaper from user where user_id=? ";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1,userid);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				shareid = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shareid;
		
	}
	
	public int addCoinMessage(int userid,int type,int coins)
	{
		Connection conn=DBConn.getConnection();
		String sql="insert into coin_message(studentid,content,time) values(?,?,?)";
		PreparedStatement ps;
		int i=0;
		String content = "";
		long time = System.currentTimeMillis();
		if(type==0){
			content = "通过测试获得积分  "+coins;
		}else {
			content = "通过兑换礼品花费积分  "+coins;
		}
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ps.setString(2,content);
			ps.setLong(3,time);
			i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	public String findOpneid(User user)
	{
		String openid = null;
		Connection conn = DBConn.getConnection();
		String sql = "select openid from user where user_id=? ";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1,user.getUserid());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				openid = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return openid;
		
	}
	public ArrayList<String> findCoinMessage(int userid)
	{
		ArrayList<String> list = new ArrayList<String>();
		String content = null;
		long time = 0l;
		Connection conn = DBConn.getConnection();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = null;
		String sql = "select time,content from coin_message where studentid=? order by id desc";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1,userid);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				time = rs.getLong(1);
				content = rs.getString(2);
				dateString = format.format(time);
				list.add(dateString+" "+content);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
	public boolean changeNickname(User user,String name)
	{
		int i = 0;
		Connection conn=DBConn.getConnection();
		String sql="update user set nickname =? where user_id=?";
		PreparedStatement ps;
		
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,name);
			ps.setInt(2, user.getUserid());
			i=ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(i>0)return true;
		else
		return false;
		
	}
	public boolean setDp(User user,String url)
	{
		int i = 0;
		Connection conn=DBConn.getConnection();
		String sql="update user set dp_url =? where user_id=?";
		PreparedStatement ps;
		
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,url);
			ps.setInt(2, user.getUserid());
			i=ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(i>0)return true;
		else
		return false;
		
	}
	public boolean changePassword(User user,String password)
	{
		int i = 0;
		Connection conn=DBConn.getConnection();
		String sql="update user set password =? where user_id=?";
		PreparedStatement ps;
		
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,password);
			ps.setInt(2, user.getUserid());
			i=ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(i>0)return true;
		else
		return false;
		
	}
public static void main(String[] args) {
	UserDaoImpl dao=new UserDaoImpl();
	User user=dao.findUser(11);
	user.setCoins(user.getCoins()+10000);
	dao.updateUser(user);
}

}
