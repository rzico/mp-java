
package net.wit.plugin.sms;

import com.chuanglan.sms.request.SmsSendRequest;
import com.chuanglan.sms.response.SmsSendResponse;
import com.chuanglan.sms.util.ChuangLanSmsUtil;
import net.wit.entity.PluginConfig;
import net.wit.plugin.SmsPlugin;
import net.wit.util.JsonUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Plugin - 创兰无线
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Component("chuangLanSmsPlugin")
public class ChuangLanSmsPlugin extends SmsPlugin {

	@Override
	public String getName() {
		return "互亿无线";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}


	/**
	 * 发送国内短信
	 * @param mobile 手机
	 * @param content 内容
	 * @return 请求参数
	 */
	@Override
	public Boolean sendSms(String mobile, String content) {
		PluginConfig config = getPluginConfig();
		Map<String,Object> data = new HashMap<String,Object>();
		try {
			SmsSendRequest smsSingleRequest = new SmsSendRequest(config.getAttribute("appid"), config.getAttribute("appkey"), "【"+config.getAttribute("sign")+"】"+content, mobile,"true");
			String requestJson = JsonUtils.toJson(smsSingleRequest);
			String response = ChuangLanSmsUtil.sendSmsByPost("http://smssh1.253.com/msg/send/json", requestJson);
			SmsSendResponse smsSingleResponse = JsonUtils.toObject(response, SmsSendResponse.class);
			if (smsSingleResponse.getCode().equals("0")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 发送国际短信
	 * @param mobile 手机
	 * @param content 内容
	 * @return 请求参数
	 */
	@Override
	public  Boolean sendIRTSms(String mobile, String content) {
		PluginConfig config = getPluginConfig();
		Map<String,Object> data = new HashMap<String,Object>();
		try {
			SmsSendRequest smsSingleRequest = new SmsSendRequest(config.getAttribute("appid"), config.getAttribute("appkey"), "【"+config.getAttribute("sign")+"】"+content, mobile,"true");
			String requestJson = JsonUtils.toJson(smsSingleRequest);
			String response = ChuangLanSmsUtil.sendSmsByPost("http://smssh1.253.com/msg/send/json", requestJson);
			SmsSendResponse smsSingleResponse = JsonUtils.toObject(response, SmsSendResponse.class);
			if (smsSingleResponse.getCode().equals("0")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}