package net.wit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric on 2018/2/25.
 */
public class RegularUtil {

    /**
     * 判断字符串是否匹配正则表达式
     *
     * @param string
     *            正则表达式
     * @param stringBuffer
     *            被判断的字符串
     * @return boolean
     */
    public static boolean toTrue(String string,StringBuffer stringBuffer){
        if(Pattern.compile(string).matcher(stringBuffer).find()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断字符串是否匹配正则表达式
     *
     * @param string
     *            正则表达式
     * @param stringBuffer
     *            被判断的字符串
     * @return boolean
     */
    public static boolean toTrue(String string,String stringBuffer){
        if(Pattern.compile(string).matcher(stringBuffer).find()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断字符串是否匹配正则表达式
     *
     * @param string
     *            正则表达式
     * @param stringBuffer
     *            被判断的字符串
     * @return 返回第一条匹配值
     */
    public static String toString(String string,StringBuffer stringBuffer){
        Matcher matcher;
        if((matcher=Pattern.compile(string).matcher(stringBuffer)).find()){
            return matcher.group();
        }
        else {
            return null;
        }
    }

    /**
     * 判断字符串是否匹配正则表达式
     *
     * @param string
     *            正则表达式
     * @param stringBuffer
     *            被判断的字符串
     * @return 返回第一条匹配值
     */
    public static String toString(String string,String stringBuffer){
        Matcher matcher;
        if((matcher=Pattern.compile(string).matcher(stringBuffer)).find()){
            return matcher.group();
        }
        else {
            return null;
        }
    }

    /**
     * 判断字符串是否匹配正则表达式
     *
     * @param string
     *            正则表达式
     * @param stringBuffer
     *            被判断的字符串
     * @return 返回所有匹配值
     */
    public static List<String> toListString(String string, StringBuffer stringBuffer){
        Matcher matcher=Pattern.compile(string).matcher(stringBuffer);
        List<String> strings=new ArrayList<>();
        while(matcher.find()){
            strings.add(matcher.group());
        }
        return strings;
    }

    /**
     * 判断字符串是否匹配正则表达式
     *
     * @param string
     *            正则表达式
     * @param stringBuffer
     *            被判断的字符串
     * @return 返回所有匹配值
     */
    public static List<String> toListString(String string, String stringBuffer){
        Matcher matcher=Pattern.compile(string).matcher(stringBuffer);
        List<String> strings=new ArrayList<>();
        while(matcher.find()){
            strings.add(matcher.group());
        }
        return strings;
    }


}
