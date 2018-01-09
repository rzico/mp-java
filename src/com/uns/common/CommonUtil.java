package com.uns.common;

import com.uns.util.CryptoUtil;
import com.uns.util.XmlUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {
	private static Logger log = Logger.getLogger(CommonUtil.class);
	
	
	public static String getDate(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	public static String getDate(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}
	
	
	
	
	
	/**
	 * 生成随机AES对称密钥
	 * @param size
	 *            位数
	 * @return
	 */
	public static String generateRandomKey(int size) {
		StringBuilder key = new StringBuilder();
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < size; i++) {
			int index = (int) (Math.random() * (chars.length() - 1));
			key.append(chars.charAt(index));
		}
		return key.toString();
	}
	
	public static Map<String, String> encryptData(Map<String,Map<String, Object>> map, PrivateKey priKey, PublicKey pubKey) {
		Map<String, String> rtrmap = new HashMap<>();
		try {
			String message = XmlUtils.maptoXmls(map,"merchant");
			// 随机生成密钥
			String cooperatorAESKey = generateRandomKey(16);

			// AES对称密钥加密并使用Base64编码(加密后的数据)
			byte[] base64EncryptDataBytes = Base64.encodeBase64(CryptoUtil.AESEncrypt(message.getBytes("UTF-8"),
					cooperatorAESKey.getBytes("UTF-8"), "AES", "AES/ECB/PKCS5Padding", null));
			String encryptDatas = new String(base64EncryptDataBytes, "UTF-8");

			// 私钥签名要传给用户的数据(生成本地签名)
			byte[] base64SingDataBytes = Base64
					.encodeBase64(CryptoUtil.digitalSign(message.getBytes("UTF-8"), priKey, "SHA1WithRSA"));
			String SingData = new String(base64SingDataBytes, "UTF-8");

			// RSA公钥加密(加密后的 解数据用的密钥)
			byte[] base64EncyrptKeyBytes = Base64.encodeBase64(CryptoUtil.RSAEncrypt(cooperatorAESKey.getBytes("UTF-8"),
					pubKey, 2048, 11, "RSA/ECB/PKCS1Padding"));
			String encryptKeys = new String(base64EncyrptKeyBytes, "UTF-8");

			rtrmap.put("encryptData", encryptDatas); // 加密后的应答报文
			rtrmap.put("encryptKey", encryptKeys); // 加密后的AES对称密钥
			rtrmap.put("signData", SingData); // 应答报文签名
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtrmap;
	}
	
	
	
	public static String decryptData(String encryptData,String encryptKey,String signData,
			PrivateKey priKey,PublicKey pubKey) throws Exception {
		String s = null;
		try {
			// 1.解密 AES KEY 获取明文
			// base64解码后用 RSA 私钥 解密
			byte[] enKey = CryptoUtil.RSADecrypt(Base64.decodeBase64(encryptKey.getBytes("UTF-8")), priKey, 2048, 11,
					"RSA/ECB/PKCS1Padding");
			// 2.解密上送的加密数据 获取明文
			// base64解码后用 AES KEY 明文解密
			byte[] enRes = CryptoUtil.AESDecrypt(Base64.decodeBase64(encryptData.getBytes("UTF-8")), enKey, "AES",
					"AES/ECB/PKCS5Padding", null);
			// 3.用商户的公钥 对 上送的明文数据进行验签
			boolean check = CryptoUtil.verifyDigitalSign(enRes, Base64.decodeBase64(signData.getBytes("UTF-8")), pubKey,
					"SHA1WithRSA");
			// 如果签名不通过,则不进行后续处理
			if (!check) {
				throw new Exception("签名失败");
			}
			s = new String(enRes, "utf-8");
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("签名失败");
		}
		return s;
	}
}
