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
        //����������
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //�����µ�һҳ
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        UserDaoImpl dao = new UserDaoImpl();
        int c_x = 0;//��ʼ��
        int c_y = 4;//��ֹ��
        int r_x = 0;//��ʼ��
        int r_y = 0;//��ֹ��
        int m = 1; //��
//        System.out.println("�����ж��ٸ���"+studentId.size());
//        System.out.println("�������"+studentVoteDetails);
        for(int i=0;i<studentId.size();i++){
        	String fileTitle = title1 + "  " + studentId.get(i) + " " + studentName.get(i) + " ��־������" + " (����: " + studentScore.get(i) + "��)";
        	//����ֱ�ͷ
            sheet.mergeCells(c_x, r_x, c_y,r_y);//��Ӻϲ���Ԫ�񣬵�һ����������ʼ�У��ڶ�����������ʼ�У���������������ֹ�У����ĸ���������ֹ��
            WritableFont bold = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);//������������ͺ�����ʾ,����ΪArial,�ֺŴ�СΪ10,���ú�����ʾ
            WritableCellFormat titleFormate = new WritableCellFormat(bold);//����һ����Ԫ����ʽ���ƶ���
            titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//��Ԫ���е�����ˮƽ�������
            titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//��Ԫ������ݴ�ֱ�������
            Label title = new Label(0,m-1,fileTitle,titleFormate);
            sheet.setRowView(0, 600, false);//���õ�һ�еĸ߶�
            sheet.addCell(title);
            
            //����Ҫ��ʾ�ľ�������
            WritableFont color = new WritableFont(WritableFont.ARIAL);//ѡ������
            WritableCellFormat colorFormat = new WritableCellFormat(color);
            Label student_id = new Label(0,m,"ѧ��",colorFormat);
            sheet.addCell(student_id);
            Label student_name = new Label(1,m,"����");
            sheet.addCell(student_name);
            Label student_score = new Label(2,m,"ͶƱ�ɼ�");
            sheet.addCell(student_score);
            
//            System.out.println("---------------");
            
            List<String> studentVoteDetail = studentVoteDetails.get(i);
            //д���������
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
        
        //�Ѵ���������д�뵽������У����ر������
        workbook.write();
        workbook.close();
        os.close();
        
    }
}
