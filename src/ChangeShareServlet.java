/**
 * 设置题库是否分享
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
		int type = Integer.parseInt(request.getParameter("type")) ;//0设为私密，1为共享
		UserDaoImpl userdao=new UserDaoImpl();
		PaperDaoImpl paperdao = new PaperDaoImpl();
		//取出paper表内isShare字段值，如果是0(字符串长度为1)，则代表之前是私密状态；若是非0，则代表修改共享名单。
		String Share_old = paperdao.getIsShare(paperid);
		
		
		if(type==0){//之前或是共享或是私密，现在设为私密
			if(Share_old.length()!=1){//如果之前是私密，则不做任何改变,不是私密，才有重新设置的必要
				//1.将之前所有的教师名单在各自记录中去掉
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
				//2.设置paper的共享值为0
				paperdao.changeShare(0+"",userid, paperid);
			}
			
		}else{//两种情况：1.之前是私密，现在设为共享   2.之前也是共享，现在是修改名单
			
			//先处理传过来的教师名单
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
			
			
			//之前是私密，现在设为共享
			if(Share_old.length()==1){
				//在对应paper里设置共享教师名单
				paperdao.changeShare(Share_new,userid, paperid);
				
				//在对应教师名单设置该paperid
				for (int j = 0; j < teacId.length; j++) {
					userdao.updateSharePaper(paperid, Integer.parseInt(teacId[j].split("-")[0]));
				}
			}else{//之前也是共享，现在是修改名单:1.取出原有名单并与新名单对应    2.重置paper的字段
				
				String[] old = Share_old.split(",");//旧名单
				String[] last = ids.split(",");//新名单
				
				//1.取出原有名单并与新名单比较，一一修改
				//先删除
				for (int i = 0; i < old.length; i++) {
					int t = Integer.parseInt(old[i].split("-")[0]);
					for (int j = 0; j < last.length; j++) {
						if(t==Integer.parseInt(last[j]))
							break;//如果在新名单中依旧存在，则跳出循环
						if(j==last.length-1 && t!=Integer.parseInt(last[j]) ){
							//说明不在新名单中，去掉该教师id
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
				
				
				//在增加
				for(int i = 0; i < last.length; i++){
					int o = Integer.parseInt(last[i]);
					for (int j = 0; j < old.length; j++) {
						int p = Integer.parseInt(old[j].split("-")[0]);
						if(o==p)
							break;//如果在旧名单中依旧存在，则跳出循环
						if(j==old.length-1 && o!=p ){
							//在对应教师名单设置该paperid
							userdao.updateSharePaper(paperid, o);
						}
					}
				}
				//2.重置paper的字段
				paperdao.changeShare(Share_new,userid, paperid);
			}
		}
		out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		out.close();
	}

}
