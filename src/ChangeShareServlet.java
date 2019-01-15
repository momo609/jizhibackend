/**
 * ��������Ƿ����
 * @author Wang Junbin
 *
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class ChangeShareServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		int paperid = Integer.parseInt(request.getParameter("paperid")) ;
		int type = Integer.parseInt(request.getParameter("type")) ;//0��Ϊ˽�ܣ�1Ϊ����
		UserDaoImpl userdao=new UserDaoImpl();
		PaperDaoImpl paperdao = new PaperDaoImpl();
		//ȡ��paper����isShare�ֶ�ֵ�������0(�ַ�������Ϊ1)�������֮ǰ��˽��״̬�����Ƿ�0��������޸Ĺ���������
		String Share_old = paperdao.getIsShare(paperid);
		
		
		if(type==0){//֮ǰ���ǹ������˽�ܣ�������Ϊ˽��
			if(Share_old.length()!=1){//���֮ǰ��˽�ܣ������κθı�,����˽�ܣ������������õı�Ҫ
				//1.��֮ǰ���еĽ�ʦ�����ڸ��Լ�¼��ȥ��
				String[] s = Share_old.split(",");
				String shareid;
				for (int i = 0; i < s.length; i++) {
					String teac_id = s[i].split("-")[0];
					shareid = userdao.findShareid(Integer.parseInt(teac_id));
					String[] shareids = shareid.split(",");
					for (int j = 0; j < shareids.length; j++) {
						if(Integer.parseInt(shareids[j])==paperid){
							shareids[j] = 0+"";
						}
					}
					shareid=0+"";
					for (int j = 0; j < shareids.length; j++) {
						if(Integer.parseInt(shareids[j])!=0){
							shareid = shareid + shareids[j] + ",";
						}
					}
//					System.out.println(shareid);
					if(shareid.length()>1){
						shareid = shareid.substring(1,shareid.length()-1);
					}
					
					userdao.setSharePaper(Integer.parseInt(shareid), Integer.parseInt(teac_id));
				}
				//2.����paper�Ĺ���ֵΪ0
				paperdao.changeShare(0+"",userid, paperid);
			}
			
		}else{//���������1.֮ǰ��˽�ܣ�������Ϊ����   2.֮ǰҲ�ǹ����������޸�����
			
			//�ȴ��������Ľ�ʦ����
			String ids=request.getParameter("ids");
			String[] teacId = ids.split(",");
			for (int i = 0; i < teacId.length; i++) {
				teacId[i] = teacId[i] + "-" + userdao.findUser(Integer.parseInt(teacId[i])).getNickname();
			}
			String Share_new = null;
			for (int i = 0; i < teacId.length; i++) {
				Share_new = Share_new + teacId[i] + ",";
			}
			Share_new = Share_new.substring(4);
			
			
			//֮ǰ��˽�ܣ�������Ϊ����
			if(Share_old.length()==1){
				//�ڶ�Ӧpaper�����ù����ʦ����
				paperdao.changeShare(Share_new,userid, paperid);
				
				//�ڶ�Ӧ��ʦ�������ø�paperid
				for (int j = 0; j < teacId.length; j++) {
					userdao.updateSharePaper(paperid, Integer.parseInt(teacId[j].split("-")[0]));
				}
			}else{//֮ǰҲ�ǹ����������޸�����:1.ȡ��ԭ������������������Ӧ    2.����paper���ֶ�
				
				String[] old = Share_old.split(",");//������
				String[] last = ids.split(",");//������
				
				//1.ȡ��ԭ�����������������Ƚϣ�һһ�޸�
				//��ɾ��
				for (int i = 0; i < old.length; i++) {
					int t = Integer.parseInt(old[i].split("-")[0]);
					for (int j = 0; j < last.length; j++) {
						if(t==Integer.parseInt(last[j]))
							break;//����������������ɴ��ڣ�������ѭ��
						if(j==last.length-1 && t!=Integer.parseInt(last[j]) ){
							//˵�������������У�ȥ���ý�ʦid
							String y = userdao.findShareid(t);
							String[] shareids = y.split(",");
							for (int m = 0; m < shareids.length; m++) {
								if(Integer.parseInt(shareids[m])==paperid){
									shareids[m] = 0+"";
								}
							}
							y = 0+"";
							for (int m = 0; m < shareids.length; m++) {
								if(Integer.parseInt(shareids[j])!=0){
									y = y + shareids[j] + ",";
								}
							}
							if(y.length()>1){
								y = y.substring(1,y.length()-1);
							}
							userdao.setSharePaper(Integer.parseInt(y), t);
						}
					}
				}
				
				
				//������
				for(int i = 0; i < last.length; i++){
					int o = Integer.parseInt(last[i]);
					for (int j = 0; j < old.length; j++) {
						int p = Integer.parseInt(old[j].split("-")[0]);
						if(o==p)
							break;//����ھ����������ɴ��ڣ�������ѭ��
						if(j==old.length-1 && o!=p ){
							//�ڶ�Ӧ��ʦ�������ø�paperid
							userdao.updateSharePaper(paperid, o);
						}
					}
				}
				//2.����paper���ֶ�
				paperdao.changeShare(Share_new,userid, paperid);
			}
		}
		out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		out.close();
	}

}
