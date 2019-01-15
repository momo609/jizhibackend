package util;
/**
 * ����ѧ���ɼ���Excel
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
        //����������
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //�����µ�һҳ
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        //�����ͷ
        sheet.mergeCells(0, 0, 4, 0);//��Ӻϲ���Ԫ�񣬵�һ����������ʼ�У��ڶ�����������ʼ�У���������������ֹ�У����ĸ���������ֹ��
        WritableFont bold = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);//������������ͺ�����ʾ,����ΪArial,�ֺŴ�СΪ10,���ú�����ʾ
        WritableCellFormat titleFormate = new WritableCellFormat(bold);//����һ����Ԫ����ʽ���ƶ���
        titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//��Ԫ���е�����ˮƽ�������
        titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��Ԫ������ݴ�ֱ�������
        Label title = new Label(0,0,fileTitle,titleFormate);
        sheet.setRowView(0, 600, false);//���õ�һ�еĸ߶�
        sheet.addCell(title);
        
        //����Ҫ��ʾ�ľ�������
        WritableFont color = new WritableFont(WritableFont.ARIAL);//ѡ������
//        color.setColour(Colour.GOLD);//����������ɫΪ���ɫ
        WritableCellFormat colorFormat = new WritableCellFormat(color);
        Label student_id = new Label(0,1,"ѧ��",colorFormat);
        sheet.addCell(student_id);
        Label student_name = new Label(1,1,"����");
        sheet.addCell(student_name);
        Label student_class = new Label(2,1,"�༶");
        sheet.addCell(student_class);
        Label student_score = new Label(3,1,"���Գɼ�");
        sheet.addCell(student_score);
        Label student_time = new Label(4,1,"����ʱ");
        sheet.addCell(student_time);
        Label note = new Label(5,1,"��ע");
        sheet.addCell(note);
        
        //����ͳ����Ϣ
        Label baifenzhi = new Label(7,1,"�ٷ���",colorFormat);
        sheet.addCell(baifenzhi);
        Label distribution1 = new Label(7,2,"90������",colorFormat);
        sheet.addCell(distribution1);
        Label distribution2 = new Label(7,3,"80-89��",colorFormat);
        sheet.addCell(distribution2);
        Label distribution3 = new Label(7,4,"70-79��",colorFormat);
        sheet.addCell(distribution3);
        Label distribution4 = new Label(7,5,"60-69��",colorFormat);
        sheet.addCell(distribution4);
        Label distribution5 = new Label(7,6,"60������",colorFormat);
        sheet.addCell(distribution5);
        Label numbers = new Label(8,1,"����",colorFormat);
        sheet.addCell(numbers);
        Label baifenbi = new Label(9,1,"�ٷֱ�",colorFormat);
        sheet.addCell(baifenbi);
        
        //ͳ��ÿ�������ε�����
        int d1=0,d2=0,d3=0,d4=0,d5=0;
        
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//��ʼ��Formatter��ת����ʽ��
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
        DecimalFormat df = new DecimalFormat("0.00");//��ʽ��С�� 
        String s1 = df.format((double)d1/num*100);//���ص���String���� 
        String s2 = df.format((double)d2/num*100);//���ص���String���� 
        String s3 = df.format((double)d3/num*100);//���ص���String���� 
        String s4 = df.format((double)d4/num*100);//���ص���String���� 
        String s5 = df.format((double)d5/num*100);//���ص���String���� 
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
        //�Ѵ���������д�뵽������У����ر������
        workbook.write();
        workbook.close();
        os.close();
        
    }
}
