package test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import com.jizhitest.db.DBConn;
import com.jizhitest.service.TestResultDaoImpl;
import com.mysql.jdbc.PreparedStatement;

public class InsertTest {

	@Test
	public void test()    {
		TestResultDaoImpl dao=new TestResultDaoImpl();
		try {
			dao.inserttest();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
