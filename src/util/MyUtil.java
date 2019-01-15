package util;

import java.util.ArrayList;
import java.util.List;

public class MyUtil {
public static List<Integer> String2IntArray(String src)
{
//	System.out.println("timeused2:"+src);
	String s=src.substring(0,src.length()-1);
	List<Integer> list=new ArrayList<Integer>();
	String[] ss=s.split(",");
	for(String timeused:ss)
	{
		list.add(Integer.valueOf(timeused.trim()));
	}
	return list;
	
}



}
