package net.wit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asus on 2017/8/24.
 */
public class ScanUtil {


    public static Map<String,String> scanParser(String qcode){
        int s = qcode.indexOf("/q/8");
        int e = qcode.indexOf(".jhtml");
        String code = null;
        if (s!=-1 && e!=-1) {
            code = qcode.substring(s+3,e);
        }

        Map<String,String> data = new HashMap<String,String>();

        if (code==null) {
            //如果没有找到q/ 和 .jhtml中的字端，就执行该段代码
            if(qcode.substring(0,4) == "http"){
                data.put("type","000000");
                data.put("code",qcode);
            }else{
                data.put("type","999999");
                data.put("code","无效二维码");
            }
        } else {
            data.put("type",code.substring(0,6));
            data.put("code",code.substring(6,code.length()));
        }
        return data;
    }
}
