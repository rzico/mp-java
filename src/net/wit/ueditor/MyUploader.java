package net.wit.ueditor;

import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.Base64Uploader;
import net.wit.plugin.StoragePlugin;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public class MyUploader {
    private HttpServletRequest request = null;
    private Map<String, Object> conf = null;


    public MyUploader(HttpServletRequest request, Map<String, Object> conf) {
        this.request = request;
        this.conf = conf;
    }


    public final State doExec(StoragePlugin oss) {
        String filedName = (String)this.conf.get("fieldName");
        State state = null;
        if("true".equals(this.conf.get("isBase64"))) {
            state = Base64Uploader.save(this.request.getParameter(filedName), this.conf);
        } else {
            state = MyBinaryUploader.save(this.request, this.conf,oss);
        }


        return state;
    }
}
