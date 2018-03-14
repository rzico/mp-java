
package net.wit.plugin.sms;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import net.wit.FileInfo;
import net.wit.entity.PluginConfig;
import net.wit.plugin.SmsPlugin;
import net.wit.plugin.StoragePlugin;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plugin - 互亿无线
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Component("iHuYiSmsPlugin")
public class IHuYiPlugin extends SmsPlugin {

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
		int mobile_code = (int)((Math.random()*9+1)*100000);
		String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
		data.put("account",config.getAttribute("appid"));
		data.put("password",config.getAttribute("appkey"));
		data.put("mobile",mobile);
		data.put("content",content);
		String resp = post(Url,data);

		Document doc = null;
		try {
			doc = DocumentHelper.parseText(resp);
			Element root = doc.getRootElement();
			String code = root.elementText("code");
			return "2".equals(code);
		} catch (DocumentException e1) {
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
		int mobile_code = (int)((Math.random()*9+1)*100000);
		String Url = Url = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";
		data.put("account",config.getAttribute("i_appid"));
		data.put("password",config.getAttribute("i_appkey"));
		data.put("mobile",mobile);
		data.put("content",content);
		String resp = post(Url,data);

		Document doc = null;
		try {
			doc = DocumentHelper.parseText(resp);
			Element root = doc.getRootElement();
			String code = root.elementText("code");
			return "2".equals(code);
		} catch (DocumentException e1) {
			return false;
		}
	}

}