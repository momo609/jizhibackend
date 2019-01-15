package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jizhibackend.bean.Student1;

/**
 * 解析Excel，获取待导入的班级成员
 * @author Administrator
 *
 */
public class AnalysisExcel {
	public ArrayList<Student1> getStudentList(String path){
		ArrayList<Student1> stulist=new ArrayList<Student1>();
        File excelfile=new File(path);
        try {
            FileInputStream is=new FileInputStream(excelfile);
            Workbook workbook= WorkbookFactory.create(is);

            org.apache.poi.ss.usermodel.Sheet sheet=workbook.getSheetAt(0);
            int rowcount=sheet.getPhysicalNumberOfRows();
            for(int r=5;r<rowcount;r++)
            {
                Row row=sheet.getRow(r);
                String StuNo=row.getCell(1).getStringCellValue();
                String StuName= row.getCell(2).getStringCellValue();
                String StuClass= row.getCell(3).getStringCellValue();
                if((!StuName.equals(""))&&(!StuNo.equals(""))&&(!StuClass.equals(""))) {
                    Student1 stu = new Student1(Integer.parseInt(StuNo), StuName,StuClass);
                    stulist.add(stu);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stulist;
	}
}
