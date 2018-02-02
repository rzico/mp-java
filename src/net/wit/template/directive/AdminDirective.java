
package net.wit.template.directive;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.entity.Admin;
import net.wit.entity.Enterprise;
import net.wit.service.AdminService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 管理员信息
 * 
 */
@Component("adminDirective")
public class AdminDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "admin";

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Admin admin = adminService.getCurrent();
		Map<String,Object> data = new HashMap<>();
		if (admin != null) {
			Enterprise enterprise = admin.getEnterprise();
			if (enterprise!=null) {
				data.put("type",enterprise.getType());
			} else {
				data.put("type","");
			}
			data.put("role",admin.roles());
			setLocalVariable(VARIABLE_NAME, data, env, body);
		} else {
			data.put("type","");
			data.put("role","");
			setLocalVariable(VARIABLE_NAME, data, env, body);
		}
	}

}