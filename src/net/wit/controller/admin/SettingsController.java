package net.wit.controller.admin;

import net.wit.Message;
import net.wit.entity.PluginConfig;
import net.wit.service.PluginConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 2018/4/3.
 */
@Controller("adminSettingsController")
@RequestMapping("/admin/settings")
public class SettingsController {
    @Resource(name = "pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    /**
     * 设置主页
     */
    @RequestMapping("/index")
    public String setting(int type, ModelMap modelMap) {
        switch (type) {
            case 0:
                PluginConfig android = pluginConfigService.findByPluginId("androidVersionPlugin");
                modelMap.addAllAttributes(android.getAttributes());
                break;
            case 1:
                PluginConfig ios = pluginConfigService.findByPluginId("iosVersionPlugin");
                modelMap.addAllAttributes(ios.getAttributes());
                break;
            case 2:
                PluginConfig resource = pluginConfigService.findByPluginId("resourceVersionPlugin");
                modelMap.addAllAttributes(resource.getAttributes());
                break;
            case 3:
                PluginConfig three = pluginConfigService.findByPluginId("threePlugin");
                modelMap.addAllAttributes(three.getAttributes());
                break;
            default:
                return "/404";
        }
        modelMap.addAttribute("type", type);
        return "/admin/common/setting";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Message edit(String android, String androidMin, String androidUrl,
                        String ios, String iosMin, String iosUrl,
                        String resource, String resourceUrl,
                        String codeVersion, String templateId, int type) {
        switch (type) {
            case 0:
                PluginConfig androidPlugin = pluginConfigService.findByPluginId("androidVersionPlugin");
                androidPlugin.setAttribute("androidVersion", android);
                androidPlugin.setAttribute("androidMinVersion", androidMin);
                androidPlugin.setAttribute("androidUrl", androidUrl);
                pluginConfigService.update(androidPlugin);
                break;
            case 1:
                PluginConfig iosPlugin = pluginConfigService.findByPluginId("iosVersionPlugin");
                iosPlugin.setAttribute("iosVersion", ios);
                iosPlugin.setAttribute("iosMinVersion", iosMin);
                iosPlugin.setAttribute("iosUrl", iosUrl);
                pluginConfigService.update(iosPlugin);
                break;
            case 2:
                PluginConfig resourcePlugin = pluginConfigService.findByPluginId("resourceVersionPlugin");
                resourcePlugin.setAttribute("resourceVersion", resource);
                resourcePlugin.setAttribute("resourceUrl", resourceUrl);
                pluginConfigService.update(resourcePlugin);
                break;
            case 3:
                PluginConfig threePlugin = pluginConfigService.findByPluginId("threePlugin");
                threePlugin.setAttribute("codeVersion", codeVersion);
                threePlugin.setAttribute("templateId", templateId);
                pluginConfigService.update(threePlugin);
                break;
            default:
                return Message.error("修改失败");
        }
        return Message.success("修改成功!");
    }
}
