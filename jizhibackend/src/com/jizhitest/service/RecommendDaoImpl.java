package com.jizhitest.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jizhitest.db.DBConn;


public class RecommendDaoImpl {
	private SqlSessionFactory sessionFactory;
	protected SqlSession session;
	private static final String resource = "conf.xml";
	private  static final String getclassdatasql="SELECT * FROM 'handle_test_result','r_class_test' WHERE r_class_test.classid=? AND r_class_test.testid=handle_test_result.testid";
	private static final String getstudentdatasql="SELECT * FROM 'handle_test_result,'r_class_test' WHERE handle_result_result.studentid=?";
	private static final String addnoconeptsql="insert into noconcepts_student(studentid,concept) values(?,?)";
	private static final String addconceptratesql="insert into conceptrate_student(concept,rate) values(?,?)";
	
	public RecommendDaoImpl() {

        InputStream is = RecommendDaoImpl.class.getClassLoader().getResourceAsStream(resource);
         sessionFactory = new SqlSessionFactoryBuilder().build(is);
         session=sessionFactory.openSession();
	}
    
	static String results = null;
	static int[] correctcount=new int[11];	
//	static double[] correctrate=new double[11];
	static int[][] edge ={{1,1,1,0,0,0,0,0,0,0,0},
		                  {0,1,0,1,0,0,0,0,0,0,0},
		                  {0,0,1,0,1,0,0,0,0,0,0},
		                  {0,0,0,1,1,0,0,0,0,0,0},
		                  {0,0,0,0,1,1,0,0,0,0,0},
		                  {0,0,0,0,0,1,1,0,0,0,0},
		                  {0,0,0,0,0,0,1,1,0,0,0},
		                  {0,0,0,0,0,0,0,1,1,0,0},
		                  {0,0,0,0,0,0,0,0,1,1,1},
		                  {0,0,0,0,0,0,0,0,0,1,0},
		                  {0,0,0,0,0,0,0,0,0,0,1}};
	static Map<String,Integer>recommend=new HashMap<String,Integer>();
	static Map<String,Double>finalrecommend=new HashMap<String,Double>();
	static Map<String,Integer>recommends=new HashMap<String,Integer>();
	static Map<String,Double>finalrecommends=new HashMap<String,Double>();
	static Map<String,Double>conceptsrate=new HashMap<String,Double>();
	static Map<String,Double>c=new HashMap<String,Double>();
	static ArrayList<String>result=new ArrayList<String>();
	static double[] correctrate=new double[11];
	TestResultDaoImpl dao=new  TestResultDaoImpl();
	static String[] concepts=new String[11];
	//static String concepts[]={"1NF","2NF","3NF","BCNF","主属性","传递函数依赖","决定因素","函数依赖","码","部分函数依赖","非主属性"};
	
	public ArrayList<String> recommendforstudent(int studentid,int classid) throws FileNotFoundException, SQLException{
        String[] attrNames = new String[] {"time", "lookbacktime","answertrace","collect"};
    	// String[] attrNames = new String[] {"outlook","temperature","humidity","play"};
        // 读取样本集
        Map<Object, List<Sample>> samples = readSamples(attrNames,classid);
       // System.out.println(samples.toString());
  	  	//计算建模时间
        long startTime1 = System.currentTimeMillis();    //获取开始时间
        Object decisionTree = generateDecisionTree(samples, attrNames);
        long endTime1 = System.currentTimeMillis();    //获取结束时间
        System.out.println(">>>>>决策树模型建立完成");
        System.out.println("建模时间：" + (endTime1 - startTime1) + "ms");
        // 生成决策树
        // 输出决策树
        //outputDecisionTree(decisionTree, 0, null);
      
        
        //测试新的数据集
      Testnewdata(attrNames,decisionTree,studentid,classid);
//       for(int i=0;i<correctrate.length;i++)
//       {
//    	   System.out.println(correctrate[i]+" "+filenames[i]);
//       }
 	 for(int i=0;i<11;i++)
   	 {
    	recommends(i,correctrate);
  	 }
 	 Connection conn2=DBConn.getConnection();
 	 for(int i=0;i<11;i++)
 	 {
 		 conceptsrate.put(concepts[i], correctrate[i]);
 		
    	 try {
				PreparedStatement ps2=conn2.prepareStatement(addconceptratesql);
				ps2.setString(1, concepts[i]);
				ps2.setDouble(2, correctrate[i]);
				i=ps2.executeUpdate();
				conn2.close();
		     }catch (SQLException e) {
					e.printStackTrace();
		     }
 		 
 	 }
 	 List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(recommend.entrySet());            
     Collections.sort(list,new Comparator<Map.Entry<String, Integer>>() {  
              //升序排序  
     public int compare(Entry<String, Integer> o1, Entry<String,  Integer> o2) {  
               return o2.getValue().compareTo(o1.getValue());  
        }  
     });  
     String[] cc=new String[recommend.size()];
     int i=0;
     for (Entry<String, Integer> e: list) {  
	        System.out.print(e.getKey()+"="+e.getValue()+" ");
	        cc[i]=e.getKey();
	        i++;
	        recommends.put(e.getKey(),e.getValue());
        finalrecommend.put(e.getKey(),conceptsrate.get(e.getKey()));
    }  
     List<Entry<String, Double>> list2 = new ArrayList<Entry<String,Double>>(finalrecommend.entrySet());            
     Collections.sort(list2,new Comparator<Map.Entry<String, Double>>() {  
              //升序排序  
     public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
               return o1.getValue().compareTo(o2.getValue());  
        }  
     });  
     System.out.println();
     for (Entry<String, Double> e: list2) {  
    	 finalrecommends.put(e.getKey(),e.getValue());
	        System.out.print(e.getKey()+"="+e.getValue()+"  ");
            
     } 
     System.out.println();
     for(int z=0;z<cc.length-1;z++)
     {
    	 int j=z+1;
    	//System.out.println("55 "+(z==cc.length-2));
    	 if(recommends.get(cc[z])==recommends.get(cc[j]))
    	 {
    		// System.out.println("11 "+ z+" "+j+" "+recommends.get(cc[z])+" "+recommends.get(cc[j]));
    		 c.put(cc[z], finalrecommends.get(cc[z]));
    		 c.put(cc[j], finalrecommends.get(cc[z]));
    		/// System.out.println("22 "+c.toString());
    		 if(z==cc.length-2)
    		 {
    			 c= ss(c);
    		 }
    	 }
    	 else 
    	 {
    		// System.out.println("44 "+z+" "+c.size());
    		 if(c.size()!=0)
    			{
    		//	 System.out.println("33 "+c.toString());
    			 c= ss(c);
    			 c.clear();
    			}
    		 else
    			 result.add(cc[z]);
    	 }
    	
     }
     Connection conn=DBConn.getConnection();
     for(int j=0;j<result.size();j++)
     {
    	 try {
				PreparedStatement ps=conn.prepareStatement(addnoconeptsql);
				ps.setInt(1, studentid);
				ps.setString(2, result.get(j));
				i=ps.executeUpdate();
				conn.close();
		     }catch (SQLException e) {
					e.printStackTrace();
		     }
     }
     return result;
    }
    private static Map<String,Double> ss(Map<String,Double>c)
    {
    	  List<Entry<String, Double>> list3 = new ArrayList<Entry<String,Double>>(c.entrySet());
    	    
    	     Collections.sort(list3,new Comparator<Map.Entry<String, Double>>() {  
    	              //升序排序  
    	     public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
    	               return o1.getValue().compareTo(o2.getValue());  
    	        }  
    	     }); 
    	     for (Entry<String, Double> e: list3) {  
    	    	   result.add(e.getKey());
    	    	  // System.out.println("111 "+result.toString());
    		        //System.out.print("111 "+e.getKey()+"="+e.getValue()+"  ");
    	            
    	     }  
    	     //System.out.println("111 "+result.toString());
    	     return c;
    }
	private static void Testnewdata(String[] attrNames, Object decisionTree,int studentid,int classid) throws FileNotFoundException, SQLException {
		//读入测试集
//		 File file = new File("E:/知识图谱推荐/实验结果/1345"); 
//		 String[] flist = file.list();
		int correct=0;
		 HashMap<String, Map<String, Integer>> allNormalTF = new HashMap<String, Map<String,Integer>>();
		 for(int j=0;j<concepts.length;j++)
		 {
			  correct=0;
//			  String filename="E:/知识图谱推荐/实验结果/1345/" + flist[j];
//			  filenames[j]=flist[j];
//			  System.out.println(filename);
		      Object[][] TestData=gettestdata(studentid,classid);
				int count=1;
//				ArrayList<Object> list=new ArrayList();
				
		        long startTime = System.currentTimeMillis();    //获取开始时间
				//得到每个测试样本的分类值。
			    for (Object[] row : TestData) {
		            Sample sample = new Sample();       
		            int i = 0;
		            for (int n = row.length; i < n; i++)
		            {
		            	sample.setAttribute(attrNames[i], row[i]); //把数据跟属性值相关联
		            }
		           // System.out.println(sample);
		            String result=getcategoty(sample,decisionTree, 0);
		            if(result.equals("1"))
		            	correct++;
		            System.out.println(result);
		            count++;
		        }
			    correctcount[j]=correct;
			    System.out.println("correctcount[j]"+correctcount[j]);
			    correctrate[j]=(double)correctcount[j]/(double)5;
			    //System.out.println(count);
			    long endTime = System.currentTimeMillis();
			    System.out.println(">>>>>分类完成");
			    System.out.println("分类时间：" + (endTime - startTime) + "ms");
		 }
		
		 recommends(0,correctrate);
	}
	public static double avgcorrect(double correctrate[])
	{
		double sum=0;
		for (int i=0;i<correctrate.length;i++)
		{
			sum=sum+correctrate[i];
		}
		return sum/correctrate.length;
	}
	public static int recommends(int i,double correctrate[])
	{
		double avg=avgcorrect(correctrate);
		//System.out.println(avg);
			if(correctrate[i]<=avg)
		   {
			 // System.out.println(concepts[i]);
			  if(recommend.get(concepts[i])==null)
			    {
				  recommend.put(concepts[i],1);
			    }
			  else
			  {
				  int a=recommend.get(concepts[i])+1;
				  recommend.put(concepts[i],a);
			  }
			  for(int j=0;j<11;j++)
			   {
				  if(edge[j][i]==1&&j!=i)
				   {
				    	if(correctrate[j]<=avg)
				    		{
				    		 // System.out.println(concepts[i]);
				    		  if(recommend.get(concepts[j])==null)
						      {
							    recommend.put(concepts[j],1);
						      }
						      else
						      {
							     int a=recommend.get(concepts[j])+1;
							     recommend.put(concepts[j],a);
						      }
				    		    recommends(j,correctrate);
				    		}
				    }
			   }
			 
			  return 1;
		   } 
			else
			  return 0;
		
	}
	public static String getcategoty( Sample sample, Object decisionTree, int level) {	
    		Boolean find=false;
             Tree tree = (Tree) decisionTree;
             String attrName = tree.getAttribute();
            // System.out.println("attName "+attrName+" level "+level);
             Object sampleValue =sample.getAttribute(attrName);
            // System.out.println( sampleValue);
             for (Object attrValue : tree.getAttributeValues()) {
            	 if(sampleValue!=null)
            	 {
            		 if(sampleValue.equals(attrValue)){
            		  //System.out.println(sampleValue+" "+ attrValue);
            		  find=true;
                	 Object child = tree.getChild(attrValue);
                	 if (child instanceof Tree) {
                		 getcategoty(sample,child, level + 1);
                	 }
                	 else{
//                		 System.out.println(sample);
                		 results=child.toString();
                		 break;
                	 }          
                   }    

            	 }
             }
             if(find==false)
             {
            	 results="No Result";
             
             }
             //System.out.println("11"+results);
             return results;
    }


	/**
     * 读取已分类的样本集，返回Map：分类 -> 属于该分类的样本的列表
     * @throws FileNotFoundException 
	 * @throws SQLException 
     */    
//    返回map类型的数
    static Map<Object, List<Sample>> readSamples(String[] attrNames,int classid) throws FileNotFoundException, SQLException {  
        Object[][] rawData=getrawdata(classid);
      //  System.out.println(rawData.length);
        System.out.println("训练集输入完成");
        // 读取样本属性及其所属分类，构造表示样本的Sample对象，并按分类划分样本集
        Map<Object, List<Sample>> ret = new HashMap<Object, List<Sample>>();
        for (Object[] row : rawData) {
        	//System.out.println(row[1]);
            Sample sample = new Sample();
            int i = 0;
           // System.out.println("row.length-1 "+ (row.length-1));
            for (int n = row.length-1; i < n; i++)
            {
            	//System.out.println(i);
            	
            	sample.setAttribute(attrNames[i], row[i]); //把数据跟属性值相关联
            	//System.out.println(" 111  "+   attrNames[i]+"  "+row[i]);
            }
            //System.out.println("111  "+i);
            sample.setCategory(row[i]); //封装 category=row[i]
            List<Sample> samples = ret.get(row[i]);
            if (samples == null) {
                samples = new LinkedList<Sample>();
                ret.put(row[i], samples);
            }
            samples.add(sample); // 把样本加入到对应的数据结构中
        }
 
        return ret;
    }
     //输入训练集
    private static Object[][] getrawdata(int classid) throws FileNotFoundException, SQLException  {
    	Object[][] TestData=new Object[5][4];
		Connection conn=DBConn.getConnection();
    	PreparedStatement ps=conn.prepareStatement(getclassdatasql);
		ps.setInt(1, classid);
		ResultSet rs=ps.executeQuery();
		int i=0;
		while(rs.next())
		{
			for(int j=0;j<4;j++)
			{
				//System.out.println("j  "+dataIn[j]);
				TestData[i][j]=rs.getInt(j);
			}
				i++;
		}
		return TestData;
	}
    
    //输入没有标签的测试集
   public static  Object[][] gettestdata(int studentid,int classid) throws FileNotFoundException, SQLException {
		Object[][] TestData=new Object[5][4];
		Connection conn=DBConn.getConnection();
    	PreparedStatement ps=conn.prepareStatement(getstudentdatasql);
		ps.setInt(1, studentid);
		ResultSet rs=ps.executeQuery();
		int i=0;
		while(rs.next())
		{
			for(int j=0;j<4;j++)
			{
				//System.out.println("j  "+dataIn[j]);
				TestData[i][j]=rs.getInt(j);
			}
				i++;
		}
		return TestData;
	}
	/**
     * 构造决策树

     */
    static Object generateDecisionTree(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {
      
        // 如果只有一个样本，将该样本所属分类作为新样本的分类
//        if (categoryToSamples.size() == 1)
//        {	 System.out.println(categoryToSamples.size());
//        	System.out.println("222");
//            return categoryToSamples.keySet().iterator().next();
//        }
 
        // 如果没有供决策的属性，则将样本集中具有最多样本的分类作为新样本的分类，即投票选举出分类
        if (attrNames.length == 0) {
        	//System.out.println("111");
            int max = 0;
            Object maxCategory = null;
            for (Entry<Object, List<Sample>> entry : categoryToSamples
                    .entrySet()) {
                int cur = entry.getValue().size();
                if (cur > max) {
                    max = cur;
                    maxCategory = entry.getKey();
                }
            }
            return maxCategory;
        }
 
        // 选取测试属性
        Object[] rst = chooseBestTestAttribute(categoryToSamples, attrNames);
 
        // 决策树根结点，分支属性为选取的测试属性
        Tree tree = new Tree(attrNames[(Integer) rst[0]]);
        // 已用过的测试属性不应再次被选为测试属性
        String[] subA = new String[attrNames.length - 1];
        for (int i = 0, j = 0; i < attrNames.length; i++)
            if (i != (Integer) rst[0])
            {
            	System.out.println(attrNames[i]);
            	subA[j++] = attrNames[i];  //把最重要的去掉
            }
 
        // 根据分支属性生成分支
        @SuppressWarnings("unchecked")
        
        Map<Object, Map<Object, List<Sample>>> splits =
        /* NEW LINE */(Map<Object, Map<Object, List<Sample>>>) rst[2];
        
        for (Entry<Object, Map<Object, List<Sample>>> entry : splits.entrySet()) {
           // System.out.println(entry);
        	Object attrValue = entry.getKey();//得到最重要的属性所对应的值
            Map<Object, List<Sample>> split = entry.getValue();
            Object child = generateDecisionTree(split, subA);
            tree.setChild(attrValue, child);
        }   
        return tree;
    }
 
    /**
     * 选取最优测试属性。最优是指如果根据选取的测试属性分支，则从各分支确定新样本
     * 的分类需要的信息量之和最小，这等价于确定新样本的测试属性获得的信息增益最大
     * 返回数组：选取的属性下标、信息量之和、Map(属性值->(分类->样本列表))
     */
    static Object[] chooseBestTestAttribute(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {
 
        int minIndex = -1; // 最优属性下标
        double minValue = Double.MAX_VALUE; // 最小信息量
        Map<Object, Map<Object, List<Sample>>> minSplits = null; // 最优分支方案
 
        // 对每一个属性，计算将其作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和，选取最小为最优
        for (int attrIndex = 0; attrIndex < attrNames.length; attrIndex++) {
            int allCount = 0; // 统计样本总数的计数器
 
            // 按当前属性构建Map：属性值->(分类->样本列表)
            Map<Object, Map<Object, List<Sample>>> curSplits =
            /* NEW LINE */new HashMap<Object, Map<Object, List<Sample>>>();  //
            for (Entry<Object, List<Sample>> entry : categoryToSamples  //遍历数据结构（0，1）
                    .entrySet()) {
                Object category = entry.getKey();  //得到0值
                List<Sample> samples = entry.getValue();  //得到样本集
                for (Sample sample : samples) {   //样本集的每个样本遍历
                    Object attrValue = sample
                            .getAttribute(attrNames[attrIndex]);   //把属性值拿出来
                    Map<Object, List<Sample>> split = curSplits.get(attrValue); //split=两个数据结构都有
                    if (split == null) {
                        split = new HashMap<Object, List<Sample>>();
                        curSplits.put(attrValue, split);
                    }
                    List<Sample> splitSamples = split.get(category);  //得到样本集为XXX的
                    if (splitSamples == null) {
                        splitSamples = new LinkedList<Sample>();
                        split.put(category, splitSamples);
                    }
                    splitSamples.add(sample);
                }
                allCount += samples.size();
            }
 
            // 计算将当前属性作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和
            double curValue = 0.0; // 计数器：累加各分支
            for (Map<Object, List<Sample>> splits : curSplits.values()) {
                double perSplitCount = 0;
                for (List<Sample> list : splits.values())
                    perSplitCount += list.size(); // 累计当前分支样本数
                double perSplitValue = 0.0; // 计数器：当前分支
                for (List<Sample> list : splits.values()) {
                    double p = list.size() / perSplitCount;
                    perSplitValue -= p * (Math.log(p) / Math.log(2));
                }
                curValue += (perSplitCount / allCount) * perSplitValue;
            }
 
            // 选取最小为最优
            if (minValue > curValue) {
                minIndex = attrIndex;
                minValue = curValue;
                minSplits = curSplits;
            }
        }
        return new Object[] { minIndex, minValue, minSplits };
    }
 
    /**
     * 将决策树输出到标准输出
     */
    static void outputDecisionTree(Object obj, int level, Object from) {
        for (int i = 0; i < level; i++)
            System.out.print("|-----");
        if (from != null)
        	
            System.out.printf("(%s):", from);
        if (obj instanceof Tree) {
            Tree tree = (Tree) obj;
            String attrName = tree.getAttribute();
            System.out.printf("[%s = ?]\n", attrName);
            for (Object attrValue : tree.getAttributeValues()) {
                Object child = tree.getChild(attrValue);
                outputDecisionTree(child, level + 1, attrName + " ==== "
                        + attrValue);
            }
        } else {
            System.out.printf("[CATEGORY = %s]\n", obj);
        }
    }
 
    /**
     * 样本，包含多个属性和一个指明样本所属分类的分类值
     */
    static class Sample {
 
        private Map<String, Object> attributes = new HashMap<String, Object>();
 
        private Object category;
 
        public Object getAttribute(String name) {
            return attributes.get(name);
        }
 
        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }
 
        public Object getCategory() {
            return category;
        }
 
        public void setCategory(Object category) {
            this.category = category;
        }
 
        public String toString() {
            return attributes.toString();
        }
 
    }
 
    /**
     * 决策树（非叶结点），决策树中的每个非叶结点都引导了一棵决策树
     * 每个非叶结点包含一个分支属性和多个分支，分支属性的每个值对应一个分支，该分支引导了一棵子决策树
     */
    static class Tree {
 
        private String attribute;
 
        private Map<Object, Object> children = new HashMap<Object, Object>();
 
        public Tree(String attribute) {
            this.attribute = attribute;
        }
 
        public String getAttribute() {
            return attribute;
        }
 
        public Object getChild(Object attrValue) {
            return children.get(attrValue);
        }
 
        public void setChild(Object attrValue, Object child) {
            children.put(attrValue, child);
        }
 
        public Set<Object> getAttributeValues() {
            return children.keySet();
        }
 
    }
}
