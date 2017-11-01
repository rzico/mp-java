package net.wit.plat.weixin.main;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.wit.plat.weixin.pojo.AccessToken;
import net.wit.plat.weixin.pojo.Button;
import net.wit.plat.weixin.pojo.CommonButton;
import net.wit.plat.weixin.pojo.Menu;
import net.wit.plat.weixin.util.WeixinApi;

/**
 * 菜单管理器类
 * @author Administrator
 */
public class MenuManager {
	public static void createMenu() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		// 第三方用户唯一凭证
		String appId = bundle.getString("APPID");// 睿商圈
		// String appId = "wxd9cfce3d40f0caf7";//测试号
		// 第三方用户唯一凭证密钥
		String appSecret = bundle.getString("APPSECRET");
		// 调用接口获取access_token
		AccessToken at = WeixinApi.getAccessToken(appId, appSecret);

		if (null != at) {
			// 调用接口创建菜单
		    int del = WeixinApi.deleteMenu(at.getToken());
			if (0 == del) {
				System.out.println("菜单创建成功！");
			} else {
				System.out.println("菜单创建失败！" + del);
			}
			int result = WeixinApi.createMenu(getMenu(), at.getToken());
			// JSONObject result = WeixinUtil.getMenu(at.getToken());
			// System.out.println(result.toString());

			// 判断菜单创建结果
			if (0 == result) {
				System.out.println("菜单创建成功！");
			} else {
				System.out.println("菜单创建失败！" + result);
			}
		}
	}

	/**
	 * 组装菜单数据
	 * @return
	 */
	public static Menu getMenu() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		Menu menu = new Menu();
		String wxMenu = bundle.getString("wxMenu");

		CommonButton btn1 = new CommonButton();
		btn1.setName("魔篇市场");
		btn1.setType("view");
		btn1.setUrl("https://m.1xx.me/wap/example/html/index.html");

		CommonButton btn2 = new CommonButton();
		btn2.setName("制作魔篇");
		btn2.setType("view");
		btn2.setUrl("https://m.1xx.me/wap/example/html/edit/theEditor.html");

		CommonButton btn3 = new CommonButton();
		btn3.setName("我的魔篇");
		btn3.setType("view");
		btn3.setUrl("https://m.1xx.me/wap/example/html/mine/mine.html");
		   menu.setButton(new Button[] { btn1, btn2 , btn3});
		return menu;
	}

	public static String codeUrlO2(String url) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String appId = bundle.getString("APPID");//
		// 第三方用户唯一凭证密钥
		try {
			url = WeixinApi.getOauth2Code(appId, url, "snsapi_base");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}
