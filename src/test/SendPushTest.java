package test;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class SendPushTest {
	public static final String MASTER_SECRET="694d18ad233b96d597e47be2";
    public static final String APP_KEY="f8f40a234cb34c0148da3bad";

	@Test
	public void test() {
		
		JPushClient client=new JPushClient(MASTER_SECRET, APP_KEY);
		Message msg=Message.newBuilder().setMsgContent("1@asda@10003@dsfsd").build();
		PushPayload payload=PushPayload.newBuilder().setPlatform(Platform.android())
				.setAudience(Audience.alias("user11"))
				.setMessage(msg).build();
		try {
			client.sendPush(payload);
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
