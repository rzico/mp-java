
package net.wit.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import net.sf.json.JSON;
import net.wit.plugin.StoragePlugin;
import net.wit.service.PluginService;
import net.wit.ueditor.MyActionEnter;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 文件处理
 */
@Controller("fileController")
@RequestMapping("/file")
public class FileController extends BaseController {

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	/**
	 * 上传
	 */
	@RequestMapping(value = "upload")
	public void upload(HttpServletRequest request,HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		rootPath = rootPath.replaceAll("\\\\","/");
		try {
			StoragePlugin oss = pluginService.getStoragePlugin("ossPlugin");
			String exec = new MyActionEnter(request,oss, rootPath+"/WEB-INF/classes", rootPath).exec();
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json; charset=utf-8");
			writer.write(exec);
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}