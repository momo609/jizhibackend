package util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class WXUtil {
public static final String APP_SECRET="586b8d93f57160359be1162f957cadbf";
public static final String APPID="wx78854eb9992842da";
	public static String getOpenid(String code) {
//		Map<String,String> info = new HashMap<String,String>();
		String urlBuilder="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		CloseableHttpClient httpClient = HttpUtil.createSSLClientDefault();
		HttpGet get = new HttpGet();
		String url=urlBuilder.replace("APPID", APPID).replace("CODE", code).replace("SECRET", APP_SECRET);
		String openid=null;
//		String refresh_token = null;
		try {
			get.setURI(new URI(url));
			HttpResponse response= httpClient.execute(get);
            String msg= EntityUtils.toString(response.getEntity());
            JSONObject resJson=JSONObject.fromObject(msg);
            System.out.println(msg);
            System.out.println(url);
            System.out.println(openid);
            openid=resJson.getString("openid");
//            refresh_token = resJson.getString("refresh_token");
            
//            info.put("openid", openid);
//            info.put("refresh_token", refresh_token);
           
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		catch (JSONException e) {
			return null;
		}
		return openid;
	}
	
//	public static String refreshOpenid(String code){
//		
//	}
	
public static void main(String[] args) {
	WXUtil.getOpenid("dasdsadsa");
}
}
