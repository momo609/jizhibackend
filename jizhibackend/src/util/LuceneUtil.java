package util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.Question;
import com.jizhitest.db.DBConn;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;



/**
 *
 * Lucene与数据库结合使用
 *
 * @author YipFun
 */
public class LuceneUtil {

    private static final String driverClassName="com.mysql.jdbc.Driver";
    private static final String url="jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8";
    private static final String username="****";
    private static final String password="****";

    private static final Version version = Version.LUCENE_46;
    private Directory directory = null;
    private DirectoryReader ireader = null;
    private IndexWriter iwriter = null;
    private IKAnalyzer analyzer;
    private Connection conn;
	private IndexWriter iwriter2;
    public static String qIndexpath="/var/lucene/question";
    public static String pIndexpath="/var/lucene/paper";

    public LuceneUtil(String path) {
    	File file=new File(path);
   
    	if(!file.exists())
    	{
    		file.mkdirs();
    	}
        try {
			directory =  FSDirectory.open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public IndexSearcher getSearcher(){
        try {
            if(ireader==null) {
                ireader = DirectoryReader.open(directory);
            } else {
                DirectoryReader tr = DirectoryReader.openIfChanged(ireader) ;
                if(tr!=null) {
                    ireader.close();
                    ireader = tr;
                }
            }
            return new IndexSearcher(ireader);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection(){
        if(this.conn == null){
            conn = DBConn.getConnection();

        }

        return conn;
    }

    private IKAnalyzer getAnalyzer(){
        if(analyzer == null){
            return new IKAnalyzer();
        }else{
            return analyzer;
        }
    }

    public void createIndex(Question q) throws IOException{
    	File file=new File(qIndexpath);
    	Directory directory=null;
    	if(!file.exists())
    	{
    		file.mkdirs();
    	}
        try {
			directory =  FSDirectory.open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            IndexWriterConfig iwConfig =  new IndexWriterConfig(version, getAnalyzer());
            iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            try {
				iwriter = new IndexWriter(directory,iwConfig);
				Document doc = new Document();
		        doc.add(new TextField("id", q.getId()+"",Field.Store.YES));
		        doc.add(new TextField("stem", q.getStem()+"",Field.Store.YES));
		        System.out.println(q.getId()+"-----"+q.getStem());
		        iwriter.addDocument(doc);  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
			    if(iwriter != null)
	                iwriter.close();
			}
    }
    public void createIndex() throws IOException{
    	File qfile=new File(qIndexpath);
    	File pfile=new File(pIndexpath);
    	Directory qdirectory=null;
    	Directory pdirectory=null;
    	if(!qfile.exists())
    	{
    		qfile.mkdirs();
    	}
    	if(!pfile.exists())
    	{
    		pfile.mkdirs();
    	}
        try {
			qdirectory =  FSDirectory.open(qfile);
			pdirectory =  FSDirectory.open(pfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        IndexWriterConfig iwConfig =  new IndexWriterConfig(version, getAnalyzer());
        IndexWriterConfig iwConfig2 =  new IndexWriterConfig(version, getAnalyzer());
        iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        QuestionDaoImpl qImpl=new QuestionDaoImpl();
        PaperDaoImpl pImpl=new PaperDaoImpl();
        
        try {
        	ResultSet rs=qImpl.getAllQuestion();
        	 iwriter = new IndexWriter(qdirectory,iwConfig);
        	while(rs.next())
        	{
			Document doc = new Document();
	        doc.add(new TextField("id", rs.getInt(1)+""+"",Field.Store.YES));
	        doc.add(new TextField("stem", rs.getString(2)+"",Field.Store.YES));
	        doc.add(new TextField("tag", rs.getString(6)+"",Field.Store.YES));
	        //System.out.println(q.getId()+"-----"+q.getStem());
	        iwriter.addDocument(doc);  
        	}
        	rs=pImpl.getAllPaper();
        	iwriter2 = new IndexWriter(pdirectory,iwConfig2);
        	while(rs.next())
        	{
			Document doc = new Document();
	        doc.add(new TextField("id", rs.getInt(1)+""+"",Field.Store.YES));
	        doc.add(new TextField("title", rs.getString(2)+"",Field.Store.YES));
	        //System.out.println(q.getId()+"-----"+q.getStem());
	        iwriter2.addDocument(doc);  
        	}
        	
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		    if(iwriter2 != null)
                iwriter2.close();
		    if(iwriter != null)
                iwriter.close();
		}
}

    public List<Integer> searchByTerm(String field,String keyword,int num) throws InvalidTokenOffsetsException{
         IndexSearcher isearcher = getSearcher();
         Analyzer analyzer =  getAnalyzer();
         List<Integer> ids=new ArrayList<Integer>();
        //使用QueryParser查询分析器构造Query对象
        QueryParser qp = new QueryParser(version,
                field,analyzer);
        //这句所起效果？
        qp.setDefaultOperator(QueryParser.OR_OPERATOR);
        try {
            Query query = qp.parse(keyword);
            ScoreDoc[] hits;

            //注意searcher的几个方法
            hits = isearcher.search(query, null, num).scoreDocs;

      
            for (int i = 0; i < hits.length; i++) {
                Document doc = isearcher.doc(hits[i].doc);
                ids.add(Integer.parseInt(doc.get("id")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ids;
    }

public static void main(String[] args) {
	LuceneUtil l=new LuceneUtil("");
    try {
		l.searchByTerm("stem", "语言", 100);
	} catch (InvalidTokenOffsetsException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public void createIndex(Paper p) throws IOException {
	File file=new File(pIndexpath);
	Directory directory=null;
	if(!file.exists())
	{
		file.mkdirs();
	}
    try {
		directory =  FSDirectory.open(file);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        IndexWriterConfig iwConfig =  new IndexWriterConfig(version, getAnalyzer());
        iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        try {
			iwriter = new IndexWriter(directory,iwConfig);
			Document doc = new Document();
	        doc.add(new TextField("id", p.getId()+"",Field.Store.YES));
	        doc.add(new TextField("title", p.getTitle()+"",Field.Store.YES));

	        iwriter.addDocument(doc);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		    if(iwriter != null)
                iwriter.close();
		}
	
}
	
}
