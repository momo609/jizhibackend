package com.jizhitest.service;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jizhibackend.bean.*;
import com.jizhitest.db.DBConn;
public class MyClassDaoImpl extends BaseDaoImpl{
	private  String getClassManagerStmt = "com.jizhitest.mapping.classManagerMapping.getClassManagersByid";
	private  String addClassManagerStmt = "com.jizhitest.mapping.classManagerMapping.addClassManager";
	private  String delClassManagerStmt = "com.jizhitest.mapping.classManagerMapping.deleteClassManager";
	private String createClassStmt= "com.jizhitest.mapping.classManagerMapping.createClass";
	private String updateClassNameStmt= "com.jizhitest.mapping.classManagerMapping.updateClassName";
	private String updateClassNicknameStmt= "com.jizhitest.mapping.classManagerMapping.updateClassNickname";
	public boolean createClass(MyClass myclass)
	{
		int i=0;
        i= session.insert(createClassStmt,myclass);
        session.commit();
		if(i==0)
			return false;
		else return true;
	}
	public boolean updateClassName(int classid,String classname)
	{
		int i=0;
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("classid", classid);
		map.put("classname", classname);
        i= session.update(updateClassNameStmt,map);
        session.commit();
		if(i==0)
			return false;
		else return true;
	}
	public MyClass findClass(int classid)
	{
		Connection conn=DBConn.getConnection();
		String sql="select * from class where id=?";
		PreparedStatement ps;
		MyClass myclass=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, classid);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				myclass=new MyClass();
				myclass.setId(rs.getInt(1));
				myclass.setName(rs.getString(2));
				myclass.setOwner(rs.getInt(3));
				myclass.setCreatetime(rs.getLong(4));
				myclass.setMembernum(rs.getInt(5));
				myclass.setDp_url(rs.getString(8));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return myclass;
	}
	public List<MyClass> ClassofOwner(int Owner)
	{
		Connection conn=DBConn.getConnection();
		String sql="select * from class where Owner=?";
		PreparedStatement ps;
		List<MyClass> list=new ArrayList<MyClass>();
		MyClass myclass=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Owner);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				myclass=new MyClass();
				myclass.setId(rs.getInt(1));
				myclass.setName(rs.getString(2));
				myclass.setOwner(rs.getInt(3));
				myclass.setCreatetime(rs.getLong(4));
				myclass.setMembernum(rs.getInt(5));
				myclass.setDp_url(rs.getString(8));
				list.add(myclass);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return list;
	}
	public int Classoftest(int testid)
	{
		Connection conn=DBConn.getConnection();
		String sql="select classid from r_class_test where testid=?";
		PreparedStatement ps;
		int classid = 0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, testid);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
				classid=rs.getInt(1);	
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return classid;
	}
	public int searchApply(int classid,int studentid)
	{
		Connection conn=DBConn.getConnection();
		int i = 0;
		String sql="select * from applyjoinclass where classid=? AND studentid=? AND result=0";
		PreparedStatement ps;
		MyClass myclass=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, classid);
			ps.setInt(2, studentid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				i += 1;
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	public List<MyClass> ClassofStudent(int studentid)
	{
		Connection conn=DBConn.getConnection();
		String sql="SELECT * FROM r_student_class,class WHERE student_id=? and class_id=class.id";
		PreparedStatement ps;
		List<MyClass> list=new ArrayList<MyClass>();
		MyClass myclass=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, studentid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				myclass=new MyClass();
				myclass.setId(rs.getInt(5));
				myclass.setName(rs.getString(6));
				myclass.setOwner(rs.getInt(7));
				myclass.setCreatetime(rs.getLong(8));
				myclass.setMembernum(rs.getInt(9));
				myclass.setDp_url(rs.getString(12));
				list.add(myclass);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return list;
	}
	public List<Map<String,String>> getClassMember(int classid)
	{
		Connection conn=DBConn.getConnection();
		String sql="SELECT student_id,remark_name FROM r_student_class where class_id=?";
		PreparedStatement ps;
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		MyClass myclass=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, classid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				Map<String,String> map=new HashMap<String,String>();
				
				map.put("student_id", rs.getString(1));
				map.put("remark_name", rs.getString(2));
				list.add(map);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return list;
	}
	
	public List<Map<String,String>> getTestStudentMember(int classid)
	{
		Connection conn=DBConn.getConnection();
		String sql="SELECT student_id,remark_name FROM r_student_class where class_id=?";
		PreparedStatement ps;
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		UserDaoImpl userdao = new UserDaoImpl();
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, classid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				Map<String,String> map=new HashMap<String,String>();
//				System.out.println("sss:"+rs.getInt(1));
				if(userdao.findUser(rs.getInt(1)).getUsertype()==1){
					map.put("student_id", rs.getInt(1)+"");
					map.put("remark_name", rs.getString(2));
					list.add(map);
				}	
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return list;
	}
	
	
	public List<Student> getClassMembers(int classid)
	{
		Connection conn=DBConn.getConnection();
		String sql="select student_id,remark_name,dp_url from  r_student_class,user where class_id=? AND student_id=user_id";
		PreparedStatement ps;
		List<Student> list=new ArrayList<Student>();
		Student student=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, classid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				student=new Student();
				student.setName(rs.getString(2));
				student.setId(rs.getInt(1));
				student.setDp_url(rs.getString(3));
				list.add(student);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	public List<Integer> getClassMemberIds(int classid)
	{
		List<Integer> list=new ArrayList<Integer>();
		List<Student> usrList=getClassMembers(classid);
		for(Student user:usrList)
		{
			list.add(user.getId());
		}
		return list;
	}
	public boolean dismissClass(int classid)
	{
		Connection conn=DBConn.getConnection();
		String sql="delete from class where id=?";
		PreparedStatement ps;
		int i=0;
		List<Student> list=new ArrayList<Student>();
		Student student=null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, classid);
		    i=ps.executeUpdate();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	     if(i==0)return false;
	     else return true;
	}
	public boolean removeClassMember(int classid,int studentid)
	{
		Connection conn=DBConn.getConnection();
		String sql="DELETE FROM r_student_class WHERE student_id=? AND class_id=?";
		PreparedStatement ps;
		int i=0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, studentid);
			ps.setInt(2, classid);
		    i=ps.executeUpdate();
		    conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     if(i==0)return false;
	     else return true;
	}
	public boolean addClassMember(int classid,int studentid,String remarkname)
	{
		Connection conn=DBConn.getConnection();
		String sql="INSERT INTO r_student_class(student_id,class_id,remark_name) VALUES(?,?,?)";
		String sql2="update class SET member_num=member_num+1 WHERE id=?";
		PreparedStatement ps;
		PreparedStatement ps2;
		int i=0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, studentid);
			ps.setInt(2, classid);
			ps.setString(3,remarkname);
		    i=ps.executeUpdate();
		   /*
		    ps=conn.prepareStatement(sql2);
		    ps.setInt(1,classid);
		    ps.executeUpdate();
		    */
		    conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     if(i==0)return false;
	     else return true;
	}
	
	public boolean applyjoinClass(int classid,int studentid)
	{
		Connection conn=DBConn.getConnection();
		String sql="INSERT INTO applyjoinclass(studentid,classid) VALUES(?,?)";
		PreparedStatement ps;
		int i=0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, studentid);
			ps.setInt(2, classid);
		    i=ps.executeUpdate();
		    conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     if(i==0)return false;
	     else return true;
	}
	
	public ArrayList<Integer> getApplyjoinClass(int classid)
	{
		Connection conn=DBConn.getConnection();
		ArrayList<Integer> al = new ArrayList<Integer>();
		String sql="select studentid from applyjoinclass where classid=? AND result=?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, classid);
			ps.setInt(2, 0);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				al.add(rs.getInt(1));
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return al;
	}
	public Map<String,ArrayList<Integer>> getApplyResultClass(int studentid)
	{
		Connection conn=DBConn.getConnection();
		Map<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();
		ArrayList<Integer> classid = new ArrayList<Integer>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		String sql="select classid,result from applyjoinclass where studentid=?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, studentid);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				classid.add(rs.getInt(1));
				result.add(rs.getInt(2));
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		map.put("classid", classid);
		map.put("result", result);
		return map;
	}
	
	public int removeMember(int userid,int classid)
	{
		int i=0;
		Connection conn=DBConn.getConnection();
		String sql="DELETE FROM r_student_class WHERE student_id=? AND class_id=?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ps.setInt(2, classid);
		    i=ps.executeUpdate();
		    conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		return i;
	}
	public int setClassDp(int classid,String url)
	{
		int i=0;
		Connection conn=DBConn.getConnection();
		String sql="UPDATE  class SET dp_url=? WHERE id=? ";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, url);
			ps.setInt(2, classid);
		    i=ps.executeUpdate();
		    conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		return i;
	}
	public int setJoinClassResult(int classid,int studentid,int result)
	{
		int i=0;
		Connection conn=DBConn.getConnection();
		String sql="UPDATE applyjoinclass SET result=? WHERE studentid=? AND classid=?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, result);
			ps.setInt(2, studentid);
			ps.setInt(3, classid);
		    i=ps.executeUpdate();
		    conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		return i;
	}
public List<Integer> getClassManagerById(int id)
{
	List<Integer> list=session.selectList(getClassManagerStmt,id);
	
	return list;
}
public int addClassManager(int classid,int userid)
{
	Map<String,Integer> map=new HashMap<String, Integer>();
	map.put("classid", classid);
	map.put("userid", userid);
	int i=session.insert(addClassManagerStmt,map);
	session.commit();
	return i;
}
public List<ClassFile> getClassFileList(String path)
{
	 List<ClassFile> list=new ArrayList<ClassFile>();
	File file=new File(path);
	if(!file.exists())
		file.mkdirs();
	File[] fileList=file.listFiles();
	for(File f:fileList)
	{
		ClassFile classFile=new ClassFile();
		classFile.setName(f.getName());
		classFile.setSize(f.length());
		list.add(classFile);
	}
	return list;
}
public int delClassManager(int classid,int userid)
{
	Map<String,Integer> map=new HashMap<String, Integer>();
	map.put("classid", classid);
	map.put("userid", userid);
	int i=session.delete(delClassManagerStmt,map);
	session.commit();
	return i;
}
public static void main(String[] args) {
	new MyClassDaoImpl().changeClassNickname(10066, 11, "ÎûÎû");
}
public boolean changeClassNickname(int classid, int  userid, String nickname) {
	try{
	Map map=new HashMap<String, Object>();
	map.put("classid", classid);
	map.put("nickname", nickname);
	map.put("userid", userid);
	session.update(updateClassNicknameStmt,map);
	session.commit();
	}catch (Exception e)
	{
		session.rollback();
		return false;
	}
	return true;
	
	
}
//public void closeCon(){
//	
//}
}
