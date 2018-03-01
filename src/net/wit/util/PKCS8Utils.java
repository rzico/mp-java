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

	public static String privateKey =
			        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDrAYJ5zOLHd4Bi"+"\r"+
					"4yeDWVXAOpz6yVr9Gz1F5v4YKCwNH4Ry0rI1KzgKu2qKSNDwsBkJu9Ett1Y9I7Tz"+"\r"+
					"dFdrnep7p5UItVbJkjp6vSWEY+7fj8JDoGvVseG/SC//jC9ZR07jgePp5JzYQCKu"+"\r"+
					"e5XCj3Z4wxONL0TopnASdTmHELZe/YkiRK4vCVR6OOgNg3GnHyJbQ7ru9XkbOE7y"+"\r"+
					"lrQM55aIvcSD0X2qypCSGEogoyvms5wf+CnwoiOAnSosX5JoK0G7B5SvNqPTJjxC"+"\r"+
					"t8G0BOd5q0SqAt45CBppL1zqBDgn90Z3UtAEq9PEWeh5qbDoEcP5ugMArZ0msgTa"+"\r"+
					"KwshXoV1AgMBAAECggEBAMAQucxGEUoodrtbH14QUy8KOGYWztBxxMAuq5ierHIF"+"\r"+
					"QkB9xrwBmInc5pQiMvGjWrND1w/f+RF671bRzOjdZViue/VkD4wqgLTzhYgQDJiW"+"\r"+
					"a5TNtZQvD2s/2kqnDcOPrf/ulpEAENgEgtPLmXgrvA2ykXYgHdduv8W2HNAwbnxu"+"\r"+
					"dPqKWeAD1nCgoSgfwti2VzpSO96SHzT5CJOCgThqMFO1mup9gwhDEcXW0P9Wdab6"+"\r"+
					"vDxAcoIH9BY2WCmoXniGOkbIRo61JYSSnoElizIZirRjvwSaSc+mZENk9e8K5+aC"+"\r"+
					"q5crME5TckABHiBOdjErvjA2K6dZIvWFcMVku4fKAMECgYEA+ZTZtvEgaTuN4cYw"+"\r"+
					"841ZkHZbNWg27szqeqOsF7nUqsp8LPhQ1zevJwRe22+aWqB4SS5d5f+Qq64sK8gL"+"\r"+
					"fANgqbUsQmCXKYOzd6zB+sil/5y8oQTIebwCdyOe/bQv7E9YnvZ/RPh6Gq3wZerZ"+"\r"+
					"QxzJlJgCwRpONryrjqdXRmBn/q0CgYEA8QyzB3sZNXpXAukEphzJZ0H/HkGm2qTo"+"\r"+
					"VXeTSRltIqQfSBvrCOYmO6MnIK3/TTDHihXC1vxYfbDDTfryhJralQ4Eeeo8T7xf"+"\r"+
					"ZW1JRf02wQZqiVZcJyrmSN0T+9DLyMQkQClWqDlcnI53g2lPtG1pqfAkuliFzBI2"+"\r"+
					"1qeS5/7P4ukCgYB6tpxBXdeAxj5hlw/0gDhcVkVMQhxYV7qmaBkyZTVScFKTzdf5"+"\r"+
					"qbBd78EwBXSQQLxDxx91+a1JLE8di7NR21tItgK39EP+rnmsSu3pf4RW5Nq+FNr5"+"\r"+
					"N97Cc2o19cVmXDEHn809vSpUOdesVMdUPzBB9mfMSEHSmfuEHXVE7hvT1QKBgAxK"+"\r"+
					"x35kKp7thC5jz5bg9OxNE0NpuaaArlBdbqdVopkXoXi947hqdByqbz5dYR2AlUxX"+"\r"+
					"W7421BRkxTDe0Sst8mOTeWr2JOk0A/FaJ1hoVzh0qU4jl0NwDpo8m95FgX7VcbvL"+"\r"+
					"391oP27EXRfYcPYUdkTyOA1AomILs7wyg21NMzCxAoGAHbAP2vcHdkEd47POtAw4"+"\r"+
					"HfOgxA1pL5xuGYBgUF33YMpWPsEKEFaRiHlJuwsqWbSRIjRpnOZDOfkGYO7/lflo"+"\r"+
					"v5vMjWw8M0MoV99pbRmGSFmctHn+aPsK7fJH1CdqnJoCcUgBtX4oQkn4qjvzAdNc"+"\r"+
					"q+9QE68knjBttexCwuO7Qpg="+"\r";

	public static String publicKey =
			        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6wGCeczix3eAYuMng1lV"+"\r"+
					"wDqc+sla/Rs9Reb+GCgsDR+EctKyNSs4CrtqikjQ8LAZCbvRLbdWPSO083RXa53q"+"\r"+
					"e6eVCLVWyZI6er0lhGPu34/CQ6Br1bHhv0gv/4wvWUdO44Hj6eSc2EAirnuVwo92"+"\r"+
					"eMMTjS9E6KZwEnU5hxC2Xv2JIkSuLwlUejjoDYNxpx8iW0O67vV5GzhO8pa0DOeW"+"\r"+
					"iL3Eg9F9qsqQkhhKIKMr5rOcH/gp8KIjgJ0qLF+SaCtBuweUrzaj0yY8QrfBtATn"+"\r"+
					"eatEqgLeOQgaaS9c6gQ4J/dGd1LQBKvTxFnoeamw6BHD+boDAK2dJrIE2isLIV6F"+"\r"+
					"dQIDAQAB"+"\r";

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
	
    /** 
     * 使用模和指数生成RSA公钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public static RSAPublicKey getPublicKey(byte[] modulus, byte[] exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    /** 
     * 使用模和指数生成RSA私钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public static RSAPrivateKey getPrivateKey(byte[] modulus, byte[] exponent) {  
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  

		public static void main(String[] args) throws Exception {
			//System.out.println("------------准备公私钥--------------");
			KeyPair keyPir = PKCS8Utils.generateKeyPair();
			//生成公钥和私钥
			RSAPublicKey publicKey =  (RSAPublicKey) keyPir.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPir.getPrivate();
			
			String pm = Base64.encodeBase64String(publicKey.getModulus().toByteArray());
			String pe = Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray());

			//String pm = "AJzjn+4AbVr/aAhZVikzLQRf3/g11pF0HiiRV73gf9GEvZrYkmXW67DMeSP6Lp60epTWRddkvKMv12+YP6DLq1ZHWEicrSqlUnVykbXdDi356tMwoBIITjgnULwviS07Z0dyS3Ck1FQXEoiCr+puR5SiJDAO6D7CltmiJezb/2fj";
			//String pe = "AQAB";

			publicKey = getPublicKey(Base64.decodeBase64(pm),Base64.decodeBase64(pe));
			privateKey = getPrivateKey(Base64.decodeBase64(pm),Base64.decodeBase64(pe));

			String ming1 = "aa";
			System.out.println("加密前："+ ming1);
			
			//加密后的密文
			String mi1 = PKCS8Utils.encrypt(publicKey,ming1);
			System.out.println("加密后："+ mi1);
			//mi1 = "AT6xE3VPIEjmxXT9T+4L/Med8QUq6/2MTKLg4jx77+0TbMKHPHEeuV2VOR5ezXbCAiGvlK32AJrhPVIJd9+MX0olp3bWYfQpVBCgoWXCmFMJXYeV+OnafATO9LZQ+np03O9cpMF8xIaYuNimOVNcVvvAzj/2TXkCJRCy4BlxBDI=";

			//解密后的明文
			ming1 = PKCS8Utils.decrypt(privateKey,mi1);
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