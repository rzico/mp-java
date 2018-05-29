/**
 * @Title：WeiXinUtils.java 
 * @Package：net.wit.weixin.v3.utils 
 * @Description：
 * @author：Chenlf
 * @date：2015年3月1日 下午7:11:46 
 * @version：V1.0   
 */

package net.wit.plat.weixin.util;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

import net.sf.json.JSONObject;
import net.wit.entity.Member;
import net.wit.plat.weixin.pojo.AccessToken;
import net.wit.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * @ClassName：CommonUtils
 * @Description：
 * @author：Chenlf
 * @date：2015年3月1日 下午7:11:46
 */
public class WeiXinUtils {

	private static Logger log = LoggerFactory.getLogger(WeiXinUtils.class);

	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static boolean IsNumeric(String str) {
		if (str.matches("\\d *")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author 李欣桦
	 * @date 2014-12-5下午2:32:05
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters 请求参数
	 * @return
	 */
	public static String getRequestXml(HashMap<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static String ArrayToXml(HashMap<String, String> arr) {
		String xml = "<xml>";

		Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			if (IsNumeric(val)) {
				xml += "<" + key + ">" + val + "</" + key + ">";

			} else
				xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
		}

		xml += "</xml>";
		return xml;
	}

	public static String signMapValue(HashMap<String, Object> paraMap) throws Exception {
		StringBuilder buff = new StringBuilder();
		List<Entry<String, Object>> infoIds = new ArrayList<>(paraMap.entrySet());
		Collections.sort(infoIds, new Comparator<Entry<String, Object>>() {
			public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
				return (o1.getValue()).toString().compareTo(o2.getValue().toString());
			}
		});
		for (Entry<String, Object> item : infoIds) {
			String val = (String) item.getValue();
			buff.append(val);
		}
		return buff.toString();
	}

	public static String FormatBizQueryParaMap(HashMap<String, Object> paraMap, boolean urlencode) throws Exception {

		String buff = "";
		List<Entry<String, Object>> infoIds = new ArrayList<Entry<String, Object>>(paraMap.entrySet());

		Collections.sort(infoIds, new Comparator<Entry<String, Object>>() {
			public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		for (int i = 0; i < infoIds.size(); i++) {
			Entry<String, Object> item = infoIds.get(i);
			String key = item.getKey();
			String val = (String) item.getValue();
			if (null != val && !"".equals(val) && !"sign".equals(key) && !"key".equals(key)) {
				if (urlencode) {
					val = URLEncoder.encode(val, "utf-8");
				}
				buff += key + "=" + val + "&";

			}
		}

		if (buff.isEmpty() == false) {
			buff = buff.substring(0, buff.length() - 1);
		}
		return buff;
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
	public static HashMap<String,Object> doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		HashMap<String,Object> m = new HashMap<String,Object>();
		StringReader sr = new StringReader(strxml);
		InputSource is = new InputSource(sr);
		Document doc = (new SAXBuilder()).build(is); 
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		return m;
	}
	
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 上传图片至CDN
	 * 1.上传的图片限制文件大小限制1MB，仅支持JPG、PNG格式。
	 * 2.调用接口获取图片url仅支持在微信相关业务下使用。
	 */
	public static JSONObject uploadImageToCDN(File file) {
		JSONObject jsonObject = null;
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			HttpClient client = new DefaultHttpClient();
			AccessToken accessToken = WeixinApi.getAccessToken(bundle.getString("weixin.appid"),bundle.getString("weixin.secret"));
			HttpPost filePost = new HttpPost("https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=" + accessToken.getToken());
			filePost.setHeader("Content-Type", "multipart/form-data");
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("buffer", new FileBody(file)).build();
			filePost.setEntity(reqEntity);
			String res = EntityUtils.toString(client.execute(filePost).getEntity());
			jsonObject = JSONObject.fromObject(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}



	/**
	 * 创建会员卡
	 *
	 * @param backgroundPicUrl 商家自定义会员卡背景图，须 先调用上传图片接口将背景图上传至CDN，否则报错， 卡面设计请遵循微信会员卡自定义背景设计规范  ,像素大小控制在 1000 像素 *600像素以下
	 * @param prerogative      （必填）会员卡特权说明
	 * @param logoUrl          （必填）卡券的商户logo，建议像素为300*300
	 * @param title            （必填）卡券名，字数上限为9个汉字
	 * @param description      （必填）卡券使用说明，字数上限为1024个汉字
	 */
	public static JSONObject createMemberCard(String backgroundPicUrl,
											  String prerogative,
											  String topicName,
											  String logoUrl,
											  String title,
											  String description,
											  String color,Member member
	) {
		JSONObject jsonObject = null;
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			HttpClient client = new DefaultHttpClient();
			AccessToken accessToken = WeixinApi.getAccessToken(bundle.getString("weixin.appid"),bundle.getString("weixin.secret"));
			HttpPost post = new HttpPost("https://api.weixin.qq.com/card/create?access_token=" + accessToken.getToken());

			Map<String, Object> map = new HashMap<>();
			Map<String, Object> card = new HashMap<>();
			map.put("card", card);//卡券
			card.put("card_type", "MEMBER_CARD"); //（必填）会员卡类型
			Map<String, Object> memberCard = new HashMap<>();
			card.put("member_card", memberCard);  //会员卡

			if (StringUtils.isNotBlank(backgroundPicUrl)) memberCard.put("background_pic_url", backgroundPicUrl);
			memberCard.put("prerogative", prerogative);
			memberCard.put("auto_activate", true);//设置为true时用户领取会员卡后系统自动将其激活，无需调用激活接口
			memberCard.put("supply_bonus", false);//（必填）显示积分，填写true或false，如填写true，积分相关字段均为必填
			memberCard.put("supply_balance", false);//（必填）是否支持储值，填写true或false。如填写true，储值相关字段均为必填

			Map<String, Object> baseInfo = new HashMap<>();
			memberCard.put("base_info", baseInfo);//基本的卡券数据
			baseInfo.put("logo_url", logoUrl);
			baseInfo.put("brand_name", topicName);
			baseInfo.put("code_type", "CODE_TYPE_NONE");//（必填）Code展示类型，"CODE_TYPE_TEXT" 文本，"CODE_TYPE_BARCODE" 一维码，"CODE_TYPE_QRCODE" 二维码，"CODE_TYPE_ONLY_QRCODE" 仅显示二维码，"CODE_TYPE_ONLY_BARCODE" 仅显示一维码，"CODE_TYPE_NONE" 不显示任何码型
			baseInfo.put("title", title);
			baseInfo.put("color",color);//（必填）券颜色。按色彩规范标注填写Color010-Color100
			baseInfo.put("notice", "消费时请出示会员卡");
			baseInfo.put("description", description);
			baseInfo.put("get_limit", 1);//每人可领券的数量限制，建议会员卡每人限领一张

			Map<String, Object> sku = new HashMap<>();
			baseInfo.put("sku", sku);//商品信息
			sku.put("quantity", 1000000000);

			Map<String, Object> dateInfo = new HashMap<>();
			baseInfo.put("date_info", dateInfo);//使用日期，有效期的信息
			dateInfo.put("type", "DATE_TYPE_PERMANENT");//使用时间的类型

			baseInfo.put("center_title", "去付款");//卡券中部居中的按钮，仅在卡券激活后且可用状态时显示
			baseInfo.put("center_sub_title", "出示付款二维码");//显示在入口下方的提示语，仅在卡券激活后且可用状态时显示
			baseInfo.put("center_url", "http://"+bundle.getString("weixin.url") + "/website/member/card/index.jhtml?id="+member.getId());//顶部居中的url，仅在卡券激活后且可用状态时显示

			Map<String, Object> customCell1 = new HashMap<>();
			memberCard.put("custom_cell1", customCell1);//自定义会员信息类目，会员卡激活后显示。
			customCell1.put("name", "我的芸店");
			customCell1.put("url", "http://"+bundle.getString("weixin.url") + "/website/topic/index.jhtml?id="+member.getId());
			customCell1.put("tips", "逛商城");

			Map<String, Object> customCell2 = new HashMap<>();
			memberCard.put("custom_cell2", customCell2);//自定义会员信息类目，会员卡激活后显示。
			customCell2.put("name", "消费记录");
			customCell2.put("url", "http://"+bundle.getString("weixin.url") + "/website/member/card/deposit.jhtml?id="+member.getId());
			customCell2.put("tips", "会员卡");

			String data = JsonUtils.toJson(map);
			post.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
			String res = EntityUtils.toString(client.execute(post).getEntity());
			jsonObject = JSONObject.fromObject(res);
			System.out.println(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static JSONObject updateMemberCard(String cardId,
											  String backgroundPicUrl,
											  String prerogative,
											  String topicName,
											  String logoUrl,
											  String title,
											  String description,
											  String color,Member member
	) {
		JSONObject jsonObject = null;
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			HttpClient client = new DefaultHttpClient();
			AccessToken accessToken = WeixinApi.getAccessToken(bundle.getString("weixin.appid"),bundle.getString("weixin.secret"));
			HttpPost post = new HttpPost("https://api.weixin.qq.com/card/update?access_token=" + accessToken.getToken());

			Map<String, Object> map = new HashMap<>();
			map.put("card_id",cardId); //（必填）会员卡类型
			Map<String, Object> memberCard = new HashMap<>();
			map.put("member_card", memberCard);  //会员卡
			memberCard.put("prerogative", prerogative);

			Map<String, Object> baseInfo = new HashMap<>();
			memberCard.put("base_info", baseInfo);//基本的卡券数据
			baseInfo.put("logo_url", logoUrl);
			baseInfo.put("title", title);
			baseInfo.put("color", color);//（必填）券颜色。按色彩规范标注填写Color010-Color100
			baseInfo.put("notice", "消费时请出示会员卡");
			baseInfo.put("description", description);

			baseInfo.put("center_title", "去付款");//卡券中部居中的按钮，仅在卡券激活后且可用状态时显示
			baseInfo.put("center_sub_title", "出示付款二维码");//显示在入口下方的提示语，仅在卡券激活后且可用状态时显示
			baseInfo.put("center_url", "http://"+bundle.getString("weixin.url") + "/website/member/card/index.jhtml?id="+member.getId());//顶部居中的url，仅在卡券激活后且可用状态时显示

			Map<String, Object> customCell1 = new HashMap<>();
			memberCard.put("custom_cell1", customCell1);//自定义会员信息类目，会员卡激活后显示。
			customCell1.put("name", "我的芸店");
			customCell1.put("url", "http://"+bundle.getString("weixin.url") + "/website/topic/index.jhtml?id="+member.getId());
			customCell1.put("tips", "逛商城");

			Map<String, Object> customCell2 = new HashMap<>();
			memberCard.put("custom_cell2", customCell2);//自定义会员信息类目，会员卡激活后显示。
			customCell2.put("name", "消费记录");
			customCell2.put("url", "http://"+bundle.getString("weixin.url") + "/website/member/card/deposit.jhtml?id="+member.getId());
			customCell2.put("tips", "会员卡");

			String data = JsonUtils.toJson(map);
			post.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
			String res = EntityUtils.toString(client.execute(post).getEntity());
			jsonObject = JSONObject.fromObject(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}


}
