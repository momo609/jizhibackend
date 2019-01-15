package util;
/**
 * 生成学生成绩表Excel
 * @author Administrator
 *
 */

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.jizhitest.service.UserDaoImpl;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
public class GenerateExcelForVote {
	public void createExcelforvote(OutputStream os,String title1,List<String> studentId,List<String> studentName,List<Integer> studentScore,List<List<String>> studentVoteDetails) throws WriteException,IOException {
        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        UserDaoImpl dao = new UserDaoImpl();
        int c_x = 0;//起始列
        int c_y = 4;//终止列
        int r_x = 0;//起始行
        int r_y = 0;//终止行
        int m = 1; //行
//        System.out.println("这里有多少个："+studentId.size());
//        System.out.println("打出来："+studentVoteDetails);
        for(int i=0;i<studentId.size();i++){
        	String fileTitle = title1 + "  " + studentId.get(i) + " " + studentName.get(i) + " 打分具体情况" + " (分数: " + studentScore.get(i) + "分)";
        	//构造分表头
            sheet.mergeCells(c_x, r_x, c_y,r_y);//添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
            WritableFont bold = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
            WritableCellFormat titleFormate = new WritableCellFormat(bold);//生成一个单元格样式控制对象
            titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
            titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中
            Label title = new Label(0,m-1,fileTitle,titleFormate);
            sheet.setRowView(0, 600, false);//设置第一行的高度
            sheet.addCell(title);
            
            //创建要显示的具体内容
            WritableFont color = new WritableFont(WritableFont.ARIAL);//选择字体
            WritableCellFormat colorFormat = new WritableCellFormat(color);
            Label student_id = new Label(0,m,"学号",colorFormat);
            sheet.addCell(student_id);
            Label student_name = new Label(1,m,"姓名");
            sheet.addCell(student_name);
            Label student_score = new Label(2,m,"投票成绩");
            sheet.addCell(student_score);
            
//            System.out.println("---------------");
            
            List<String> studentVoteDetail = studentVoteDetails.get(i);
            //写入具体内容
            int mm = m+1;
            for(int j=0;j<studentVoteDetail.size();j++){
	            String id = studentVoteDetail.get(j).split("@")[0];
//	            System.out.println("id:"+id);
	            String score = studentVoteDetail.get(j).split("@")[1];
	            String name = dao.findUser(Integer.parseInt(id)).getNickname();
	            String userid = dao.findUser(Integer.parseInt(id)).getUsername();
	           	Label example = new Label(0,mm,userid,colorFormat);
                sheet.addCell(example);
                Label example1 = new Label(1,mm,name,colorFormat);
                sheet.addCell(example1);
                Label example2 = new Label(2,mm,score,colorFormat);
                sheet.addCell(example2);
                mm = mm + 1;
           }
//            m = m + 1 + studentVoteDetail.size() + 1;
            r_x = r_x + 1 + studentVoteDetail.size() + 2;
            r_y = r_x;
            m = r_x + 1;
        }
        
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
        
    }
}
