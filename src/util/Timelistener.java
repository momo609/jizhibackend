package util;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.jizhitest.service.RecommendDaoImpl;
/**
 * describe : 指定某一时间点执行任务操作
 * @author northeasttycoon
 */

public class Timelistener {
	public static int testid;
	private static long endtime;
	public Timelistener(long  endtime,int testid) throws ParseException{
		this.endtime=endtime;
		Timer timer = new Timer();
        Task task = new Task(testid);
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate1 = dateFormat1.parse("2018-04-23 16:56:00");
//        String format =  "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat sf = new SimpleDateFormat(format);
//        Date date3 = new Date(time);
//        System.out.println(sf.format(date3));
        System.out.println(myDate1.getTime());
        System.out.println(endtime);
        Date data2=new Date(endtime);
        timer.schedule(task, data2);
        
	}
	  
 }
class Task extends TimerTask{
	public int testid;
	public Task(int testid){
		this.testid=testid;
	}
 @Override
public void run() {
      System.out.println("Do work...");
      RecommendDaoImpl rdao=new RecommendDaoImpl();
	   try {
		rdao.addhandledata(testid);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      System.gc();
 }
}