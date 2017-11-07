package net.wit.plat.alipay.menu;

import java.util.*;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayOpenPublicMenuCreateModel;
import com.alipay.api.domain.AlipayOpenPublicMenuModifyModel;
import com.alipay.api.domain.ButtonObject;
import com.alipay.api.request.AlipayOpenPublicMenuCreateRequest;
import com.alipay.api.request.AlipayOpenPublicMenuModifyRequest;
import com.alipay.api.request.AlipayOpenPublicMenuQueryRequest;
import com.alipay.api.response.AlipayOpenPublicMenuCreateResponse;
import com.alipay.api.response.AlipayOpenPublicMenuModifyResponse;
import com.alipay.api.response.AlipayOpenPublicMenuQueryResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.plat.alipay.factory.AlipayAPIClientFactory;

/**
 * 服务窗菜单相关接口调用示例
 * 
 * @author liliang
 *
 */
public class AlipayPublicMenu {

	// 创建服务窗菜单信息,创建以后，都调用下面修改接口
	public static void createMenu() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicMenuCreateRequest request = new AlipayOpenPublicMenuCreateRequest();
//		AlipayOpenPublicMenuCreateModel model = new AlipayOpenPublicMenuCreateModel();
//		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//		List<ButtonObject> list = new ArrayList<ButtonObject>();
//		ButtonObject button = new ButtonObject();
//		button.setName("进入商城");
//		button.setActionType("link");
//		button.setActionParam(bundle.getString("WeiXinSiteUrl") + "/alipay/index.jhtml");
//		list.add(button);
//		model.setButton(list);
//		request.setBizModel(model);
		//菜单
		JSONObject jsonMenu = new JSONObject();
		//主菜单
		JSONArray jsonButton = new JSONArray();
		//菜单信息
		JSONObject jsonButtonInfo = new JSONObject();
		jsonButtonInfo.put("name","进入商城");
		jsonButtonInfo.put("action_type","link");//link:链接类型,out:事件类型,tel:点击拨打电话
		jsonButtonInfo.put("action_param","http://dev.ruishangquan.com/alipay/index.jhtml");
		jsonButton.add(jsonButtonInfo);
		jsonMenu.put("button",jsonButton);
		jsonMenu.put("type","text");
		request.setBizContent(jsonMenu.toString());
		AlipayOpenPublicMenuCreateResponse response = null;
		try {
			 response = alipayClient.execute(request);
			 System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 查询服务窗菜单信息
	public static void queryMenu() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicMenuQueryRequest request = new AlipayOpenPublicMenuQueryRequest();
		AlipayOpenPublicMenuQueryResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 修改服务窗菜单信息
	public static void modifyMenu() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicMenuModifyRequest request = new AlipayOpenPublicMenuModifyRequest();
		//菜单
		JSONObject jsonMenu = new JSONObject();
		//主菜单
		JSONArray jsonButton = new JSONArray();
		//菜单信息
		JSONObject jsonButtonInfo = new JSONObject();
		jsonButtonInfo.put("name","进入商城");
		jsonButtonInfo.put("action_type","link");//link:链接类型,out:事件类型,tel:点击拨打电话
		jsonButtonInfo.put("action_param","http://dev.ruishangquan.com/alipay/index.jhtml");
		jsonButton.add(jsonButtonInfo);
		JSONObject jsonButtonInfo1 = new JSONObject();
		jsonButtonInfo1.put("name","更多");
		jsonButtonInfo1.put("action_type","out");
		//子菜单
		JSONArray jsonSubButton = new JSONArray();
		//子菜单信息
		JSONObject jsonSubButtonInfo = new JSONObject();
		jsonSubButtonInfo.put("name","支付测试");
		jsonSubButtonInfo.put("action_type","link");//link:链接类型,out:事件类型,tel:点击拨打电话
		jsonSubButtonInfo.put("action_param","http://dev.ruishangquan.com/alipay/loginAuth.jhtml");
		jsonSubButton.add(jsonSubButtonInfo);
		jsonButtonInfo1.put("sub_button",jsonSubButton);
		//jsonButton.add(jsonButtonInfo1);
		jsonMenu.put("button",jsonButton);
		jsonMenu.put("type","text");
		request.setBizContent(jsonMenu.toString());
		AlipayOpenPublicMenuModifyResponse response = null;
		try {
			 response = alipayClient.execute(request);
			 System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args) {
//		createMenu();
//		queryMenu();
			modifyMenu();
	}
}
