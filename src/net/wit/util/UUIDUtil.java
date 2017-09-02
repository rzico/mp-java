package net.wit.util;

import java.util.UUID;

/**
 * Created by Asus on 2017/8/24.
 */
public class UUIDUtil {
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }
}
