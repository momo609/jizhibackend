package test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.junit.Test;

import com.jizhibackend.bean.SingleChoiceQuestion;

public class MapTest {

	@Test
	public void test() {
       Map<Integer,Object> map=new HashMap<Integer,Object>();
       SingleChoiceQuestion question=new SingleChoiceQuestion();
       String s=new String("呵呵");
       map.put(1, question);
       map.put(2, s);
       
       if(map.get(1) instanceof SingleChoiceQuestion)
       {
    	   System.out.print("我是选择题");
       }
	}
   
}
