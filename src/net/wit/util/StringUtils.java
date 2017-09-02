/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;
import net.wit.Setting;

/**
 * Utils - Spring
 * 
 * @author rsico Team
 * @version 3.0
 */
public class StringUtils  {

	/** 中文字符配比 */
	private static final Pattern PATTERN = Pattern.compile("[\\u4e00-\\u9fa5\\ufe30-\\uffa0]+$");
	
	public static String abbreviate(String str, Integer width, String ellipsis) {
		if (width != null) {
			int strLength = 0;
			for (int strWidth = 0; strLength < str.length(); strLength++) {
				strWidth = PATTERN.matcher(String.valueOf(str.charAt(strLength))).find() ? strWidth + 2 : strWidth + 1;
				if (strWidth >= width) {
					break;
				}
			}
			if (strLength < str.length()) {
				if (ellipsis != null) {
					return str.substring(0, strLength + 1) + ellipsis;
				} else {
					return str.substring(0, strLength + 1);
				}
			} else {
				return str;
			}
		} else {
			if (ellipsis != null) {
				return str + ellipsis;
			} else {
				return str;
			}
		}
	}
	
	/**
	 * 
	 * 字符串缩略
	 * 
	 * @param str
	 *            原字符串
	 * @param width
	 *            宽度
	 * @param ellipsis
	 *            省略符
	 * @return 缩略字符
	 */

	public static String mosaic(String str, Integer width, String ellipsis) {
		String value = str;
		if (value.length()>(width+4)) {
		   return abbreviate(value, width, ellipsis)+value.substring(value.length()-4);
		} else {
		   return abbreviate(value, width, ellipsis);
		}
	}

	/**
	 * 获取验证码
	 *
	 * @return 验证码
	 */
	public static int Random6Code() {
		int [] array = {0,1,2,3,4,5,6,7,8,9};
		Random rand = new Random();
		for (int i = 10;i>1;i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i-1];
			array[i-1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < 6; i++)
			result = result*10+array[i];
		if (result<100000) {
			result = result + 100000;
		}
		return result;
	}

}