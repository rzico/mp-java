package net.wit.plat.alipay.label;

import javax.jws.WebParam.Mode;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayOpenPublicLabelCreateModel;
import com.alipay.api.domain.AlipayOpenPublicLabelDeleteModel;
import com.alipay.api.domain.AlipayOpenPublicLabelModifyModel;
import com.alipay.api.domain.AlipayOpenPublicLabelUserCreateModel;
import com.alipay.api.domain.AlipayOpenPublicLabelUserQueryModel;
import com.alipay.api.request.AlipayOpenPublicLabelCreateRequest;
import com.alipay.api.request.AlipayOpenPublicLabelDeleteRequest;
import com.alipay.api.request.AlipayOpenPublicLabelModifyRequest;
import com.alipay.api.request.AlipayOpenPublicLabelQueryRequest;
import com.alipay.api.request.AlipayOpenPublicLabelUserCreateRequest;
import com.alipay.api.request.AlipayOpenPublicLabelUserDeleteRequest;
import com.alipay.api.request.AlipayOpenPublicLabelUserQueryRequest;
import com.alipay.api.response.AlipayOpenPublicLabelCreateResponse;
import com.alipay.api.response.AlipayOpenPublicLabelDeleteResponse;
import com.alipay.api.response.AlipayOpenPublicLabelModifyResponse;
import com.alipay.api.response.AlipayOpenPublicLabelQueryResponse;
import com.alipay.api.response.AlipayOpenPublicLabelUserCreateResponse;
import com.alipay.api.response.AlipayOpenPublicLabelUserDeleteResponse;
import com.alipay.api.response.AlipayOpenPublicLabelUserQueryResponse;
import net.wit.plat.alipay.factory.AlipayAPIClientFactory;

/**
 * 服务窗标签接口调用示例
 * 
 * @author liliang
 *
 */
public class AlipayPublicLabel {

	public static void main(String[] args) {
		lableCreate();
		// lablequery();
		// lableedit();
		// labledel();
		// lableuseradd();
		// lableuserQuery();
		// lableuserDel();
	}

	// 服务窗创建标签接口
	public static void lableCreate() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicLabelCreateRequest request = new AlipayOpenPublicLabelCreateRequest();
		AlipayOpenPublicLabelCreateModel model = new AlipayOpenPublicLabelCreateModel();
		model.setName("test0001");
		request.setBizModel(model);
		AlipayOpenPublicLabelCreateResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 服务窗查询标签接口
	public static void lablequery() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicLabelQueryRequest request = new AlipayOpenPublicLabelQueryRequest();
		AlipayOpenPublicLabelQueryResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 修改标签接口
	public static void lableedit() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicLabelModifyRequest request = new AlipayOpenPublicLabelModifyRequest();
		AlipayOpenPublicLabelModifyModel model = new AlipayOpenPublicLabelModifyModel();
		model.setId("100768231");
		model.setName("测试标签0222");
		request.setBizModel(model);
		AlipayOpenPublicLabelModifyResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 删除标签接口
	public static void labledel() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicLabelDeleteRequest request = new AlipayOpenPublicLabelDeleteRequest();
		AlipayOpenPublicLabelDeleteModel model = new AlipayOpenPublicLabelDeleteModel();
		model.setId("100768");
		AlipayOpenPublicLabelDeleteResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 给用户增加标签接口
	public static void lableuseradd() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicLabelUserCreateRequest request = new AlipayOpenPublicLabelUserCreateRequest();
		AlipayOpenPublicLabelUserCreateModel model = new AlipayOpenPublicLabelUserCreateModel();
		model.setUserId("2088802608984030");
		model.setLabelId(100889l);
		request.setBizModel(model);
		AlipayOpenPublicLabelUserCreateResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 查询用户标签接口
	public static void lableuserQuery() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicLabelUserQueryRequest request = new AlipayOpenPublicLabelUserQueryRequest();
		AlipayOpenPublicLabelUserQueryModel model = new AlipayOpenPublicLabelUserQueryModel();
		model.setUserId("2088802608984030");
		request.setBizModel(model);
		AlipayOpenPublicLabelUserQueryResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 删除用户标签接口
	public static void lableuserDel() {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayOpenPublicLabelUserDeleteRequest request = new AlipayOpenPublicLabelUserDeleteRequest();
		request.setBizContent("{\"user_id\":\"2088802608984030\",\"labelId\" : \"100652\"}");
		AlipayOpenPublicLabelUserDeleteResponse response = null;
		try {
			response = alipayClient.execute(request);
			System.out.println(response.getBody());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
