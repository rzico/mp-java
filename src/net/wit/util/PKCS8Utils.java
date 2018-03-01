/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.util;

import net.wit.plat.unspay.Const;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.codec.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

/**
 * Utils - RSA加密解密
 * 
 * @author rsico Team
 * @version 3.0
 */
public final class PKCS8Utils {
	/** 安全服务提供者 */
	private static final Provider PROVIDER = new BouncyCastleProvider();

	public static String privateKey=
					"MIGEAgEAMBAGByqGSM49AgEGBSuBBAAKBG0wawIBAQQgilzRLbmHzOJTqgpaM5Ln\n"+
					"yyGZLYxr7Q0HGkm8YCz95f+hRANCAAQaFa7sPGBQbxMRrrce87loEfMH5v3+L5Rx\n"+
					"60tHHM/kXD1hh7TTv+dAwyQsfR7dAkoy3KVKxNcHP+OGHK/M1OS0\n";
	public static String publicKey=
					"MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEGhWu7DxgUG8TEa63HvO5aBHzB+b9/i+U\n"+
					"cetLRxzP5Fw9YYe007/nQMMkLH0e3QJKMtylSsTXBz/jhhyvzNTktA==\n";

	/**
	 * 不可实例化
	 */
	private PKCS8Utils() {
	}

	/**
	 * 获取私钥
	 * @return 当前的私钥对象
	 */
	public static RSAPrivateKey getPrivateKey() throws Exception {
		try {
			BASE64Decoder base64Decoder= new BASE64Decoder();
			byte[] buffer= base64Decoder.decodeBuffer(Const.privateKey);
			PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 获取公钥
	 * @return 当前的公钥对象
	 */
	public static RSAPublicKey getPublicKey()  throws Exception {
		try {
			BASE64Decoder base64Decoder= new BASE64Decoder();
			byte[] buffer= base64Decoder.decodeBuffer(Const.publicKey);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}



	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		Assert.notNull(publicKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;

			for(int i = 0; inputLen - offSet > 0; offSet = i * 116) {
				byte[] cache;
				if(inputLen - offSet > 116) {
					cache = cipher.doFinal(data, offSet, 116);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}

				out.write(cache, 0, cache.length);
				++i;
			}

			byte[] encryptedData = out.toByteArray();
			out.close();
			return encryptedData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 * 
	 * @return Base64编码字符串
	 */
	public static String encrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte[] data = encrypt(publicKey,text.getBytes());
		return data != null ? Base64.encodeBase64String(data) : null;
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 * 
	 * @return Base64编码字符串
	 */
	public static String hexEncrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte[] data = encrypt(publicKey,text.getBytes());
		return data != null ? Hex.encodeToString(data): null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		Assert.notNull(privateKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;

			for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
				byte[] cache;
				if(inputLen - offSet > 128) {
					cache = cipher.doFinal(data, offSet, 128);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}

				out.write(cache, 0, cache.length);
				++i;
			}
			byte[] decryptData = out.toByteArray();
			out.close();
			return decryptData;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		return data != null ? new String(data) : null;
	}
	
	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String hexDecrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte[] data = decrypt(privateKey, Hex.decode(text));
		return data != null ? new String(data) : null;
	}

		public static void main(String[] args) throws Exception {
			//System.out.println("------------准备公私钥--------------");
			//加密后的密文
			String mi1 = PKCS8Utils.encrypt(PKCS8Utils.getPublicKey(),"223333");
			System.out.println("加密后："+ mi1);
			//mi1 = "AT6xE3VPIEjmxXT9T+4L/Med8QUq6/2MTKLg4jx77+0TbMKHPHEeuV2VOR5ezXbCAiGvlK32AJrhPVIJd9+MX0olp3bWYfQpVBCgoWXCmFMJXYeV+OnafATO9LZQ+np03O9cpMF8xIaYuNimOVNcVvvAzj/2TXkCJRCy4BlxBDI=";

			//解密后的明文
			String ming1 = PKCS8Utils.decrypt(PKCS8Utils.getPrivateKey(),mi1);
			System.out.println("解密后："+ ming1);
			
//
//			//System.out.println("----------- hex后公私钥加解密--------------");
//			RSAPublicKey publicKey1 = RSAUtils.getPublicKey(Hex.decode(pm),Hex.decode(pe));
//
//			String ming2 = "lixinyu";
//			System.out.println("加密前："+ ming2);
//			//加密后的密文
//			String mi3 = RSAUtils.hexEncrypt(publicKey1,ming2);
//			System.out.println("加密后："+ mi3);
//
//			RSAPrivateKey privateKey2 = RSAUtils.getPrivateKey(Hex.decode(sm),Hex.decode(se));
//			//mi3 = "9ED8EC09B50324DF228EAEE1367D60D54BBFAD089DD21AEAC2C3D100C6FC395F92DECBD6F7059B65A25ADCB586370907E8B2E1BB8D4E4A0A7E8D804D32AFE219BEB8A2D62A33A7190561491CE17F7C242EA9DA4FAD6A27589E46AF7D0FFC5D7E103307D17C06FE3EA98E0AD6F452638F8DE27A3E11682471014C628CE79187C5";
//			ming1 = RSAUtils.hexDecrypt(privateKey2,mi3);
//			System.out.println("解密后："+ ming1);
//
//			//加密后的密文
//			String mi4 = "79D482974AEFF006F63034393E0BB9F0A5632E49A5303D4C369868D0425113B52A754EDF6E58AAD97C1D3BA5AB110E8024649BD5C09241744D9F5E007D2EDEB6CC87D90E712AA674C5C091152EA8B1EC85EA1A45EBBF989FC7FC092CB747411EA965F5215A7F6244FC34E2E7CAF523B4E79CADB32A5FFB88248376FC46BA3766";
//			System.out.println("delphi加密后："+ mi4);
//
//			RSAPrivateKey privateKey3 = RSAUtils.getPrivateKey(Hex.decode("53bba470e563a74144b7c3c04374451ecd56e3c8a855a7d49e095c4527d8ebc23905379fcdfdedc39dd5862ba2b0569c532145fb1a268a319089638dd231f5a9d998c932995dea3af9e4ddb1f11e1b85ced00458278d694070c8bbc48e718bfed0375ba1ff1e2bb01042e8bc052377bf86496189259c00cf9a6d746d492d0119"),Hex.decode("010001"));
//			ming1 = RSAUtils.hexDecrypt(privateKey3,mi4);
//			System.out.println("解密后："+ ming1);
//
			
		}
}