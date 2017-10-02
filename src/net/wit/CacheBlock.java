package net.wit;

import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jinlesoft on 2017/9/4.
 */
public class CacheBlock {

    //    md5校验
    private String md5;
    //    返回的数据
    private Object data;

    public static Message bind(Object data,HttpServletRequest request){
        String js = JsonUtils.toJson(data);
        String md5 = MD5Utils.getMD5Str(js);
        String rmd5 = request.getParameter("md5");
        if (rmd5!=null && md5.equals(rmd5)) {
            return Message.warn(Message.CACHE_SUCCESS);
        }
        CacheBlock block = new CacheBlock();
        block.md5 = MD5Utils.getMD5Str(js);
        block.data = data;
        return Message.success(block,"success");
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
