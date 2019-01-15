package test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class Maptest2 {

	@Test
	public void test() {
		 Set<Integer> s=new HashSet<Integer>();
		 s.add(1);
		 s.add(5);
		 s.add(7);
		 s.remove(5);
		 s.add(9);
		 s.remove(1);
		 for(Integer i:s)
		 {
			 System.out.println(i);
		 }
	}

}
