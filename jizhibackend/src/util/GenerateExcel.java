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
import java.util.List;
import java.util.TimeZone;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
public class GenerateExcel {
	public void createExcel(OutputStream os,String fileTitle,List<String> studentId,List<String> studentName,List<Integer> studentScore,List<String> studentClass,List<Long> studentTime,int totalScore) throws WriteException,IOException {
        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        //构造表头
        sheet.mergeCells(0, 0, 4, 0);//添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
        WritableFont bold = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
        WritableCellFormat titleFormate = new WritableCellFormat(bold);//生成一个单元格样式控制对象
        titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
        titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中
        Label title = new Label(0,0,fileTitle,titleFormate);
        sheet.setRowView(0, 600, false);//设置第一行的高度
        sheet.addCell(title);
        
        //创建要显示的具体内容
        WritableFont color = new WritableFont(WritableFont.ARIAL);//选择字体
//        color.setColour(Colour.GOLD);//设置字体颜色为金黄色
        WritableCellFormat colorFormat = new WritableCellFormat(color);
        Label student_id = new Label(0,1,"学号",colorFormat);
        sheet.addCell(student_id);
        Label student_name = new Label(1,1,"姓名");
        sheet.addCell(student_name);
        Label student_class = new Label(2,1,"班级");
        sheet.addCell(student_class);
        Label student_score = new Label(3,1,"考试成绩");
        sheet.addCell(student_score);
        Label student_time = new Label(4,1,"总用时");
        sheet.addCell(student_time);
        Label note = new Label(5,1,"备注");
        sheet.addCell(note);
        
        //创建统计信息
        Label baifenzhi = new Label(7,1,"百分制",colorFormat);
        sheet.addCell(baifenzhi);
        Label distribution1 = new Label(7,2,"90分以上",colorFormat);
        sheet.addCell(distribution1);
        Label distribution2 = new Label(7,3,"80-89分",colorFormat);
        sheet.addCell(distribution2);
        Label distribution3 = new Label(7,4,"70-79分",colorFormat);
        sheet.addCell(distribution3);
        Label distribution4 = new Label(7,5,"60-69分",colorFormat);
        sheet.addCell(distribution4);
        Label distribution5 = new Label(7,6,"60分以下",colorFormat);
        sheet.addCell(distribution5);
        Label numbers = new Label(8,1,"人数",colorFormat);
        sheet.addCell(numbers);
        Label baifenbi = new Label(9,1,"百分比",colorFormat);
        sheet.addCell(baifenbi);
        
        //统计每个分数段的人数
        int d1=0,d2=0,d3=0,d4=0,d5=0;
        
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        
        int k = 2;
        for(int i=0;i<studentId.size();i++){
        	double temp = (double)studentScore.get(i)/totalScore*100;
        	if(temp>=90){
        		d1++;
        	}else if(temp>=80&&temp<90){
        		d2++;
        	}else if(temp>=70&&temp<80){
        		d3++;
        	}else if(temp>=60&&temp<70){
        		d4++;
        	}else if(temp<60){
        		d5++;
        	}
        	 Label example = new Label(0,k,studentId.get(i),colorFormat);
             sheet.addCell(example);
             Label example1 = new Label(1,k,studentName.get(i),colorFormat);
             sheet.addCell(example1);
             Label example2 = new Label(2,k,studentClass.get(i),colorFormat);
             sheet.addCell(example2);
             Label example3 = new Label(3,k,studentScore.get(i)+"",colorFormat);
             sheet.addCell(example3);
             Label example4 = new Label(4,k,formatter.format(studentTime.get(i)),colorFormat);
             sheet.addCell(example4);
             k++;
        }
        Label dd1 = new Label(8,2,d1+"",colorFormat);
        sheet.addCell(dd1);
        Label dd2 = new Label(8,3,d2+"",colorFormat);
        sheet.addCell(dd2);
        Label dd3 = new Label(8,4,d3+"",colorFormat);
        sheet.addCell(dd3);
        Label dd4 = new Label(8,5,d4+"",colorFormat);
        sheet.addCell(dd4);
        Label dd5 = new Label(8,6,d5+"",colorFormat);
        sheet.addCell(dd5);
        
        int num = studentId.size();
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数 
        String s1 = df.format((double)d1/num*100);//返回的是String类型 
        String s2 = df.format((double)d2/num*100);//返回的是String类型 
        String s3 = df.format((double)d3/num*100);//返回的是String类型 
        String s4 = df.format((double)d4/num*100);//返回的是String类型 
        String s5 = df.format((double)d5/num*100);//返回的是String类型 
        Label ddd1 = new Label(9,2,s1+"%",colorFormat);
        sheet.addCell(ddd1);
        Label ddd2 = new Label(9,3,s2+"%",colorFormat);
        sheet.addCell(ddd2);
        Label ddd3 = new Label(9,4,s3+"%",colorFormat);
        sheet.addCell(ddd3);
        Label ddd4 = new Label(9,5,s4+"%",colorFormat);
        sheet.addCell(ddd4);
        Label ddd5 = new Label(9,6,s5+"%",colorFormat);
        sheet.addCell(ddd5);
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
        
    }
}
