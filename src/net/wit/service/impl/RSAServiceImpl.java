/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.wit.RSAKey;
import net.wit.entity.Redis;
import net.wit.service.RSAService;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import net.wit.util.RSAUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - RSA安全
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("rsaServiceImpl")
public class RSAServiceImpl implements RSAService {
	@Resource(name = "redisServiceImpl")
	private RedisService redisService;

	/** "私钥"参数名称 */
	private static final String PRIVATE_KEY_ATTRIBUTE_NAME = "privateKey";

	public RSAPublicKey generateKey(HttpServletRequest request) {
		Assert.notNull(request);
		KeyPair keyPair = RSAUtils.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey key = new RSAKey();
		key.setModule(Base64.encodeBase64String(privateKey.getModulus().toByteArray()));
		key.setExponent(Base64.encodeBase64String(privateKey.getPrivateExponent().toByteArray()));
		redisService.put(PRIVATE_KEY_ATTRIBUTE_NAME,JsonUtils.toJson(key));
		return publicKey;
	}

	public void removePrivateKey(HttpServletRequest request) {
		Assert.notNull(request);
		redisService.remove(PRIVATE_KEY_ATTRIBUTE_NAME);
	}

	public String decryptParameter(String name, HttpServletRequest request) {
		if (name != null) {
			try{
			Redis redis = redisService.findKey(PRIVATE_KEY_ATTRIBUTE_NAME);
			if (redis!=null) {
		     	RSAKey key = JsonUtils.toObject(redis.getValue(),RSAKey.class);
			    RSAPrivateKey privateKey = RSAUtils.getPrivateKey(
			    		Base64.decodeBase64(key.getModule()),
						Base64.decodeBase64(key.getExponent())
				);
			    String parameter = request.getParameter(name);
				return RSAUtils.decrypt(privateKey, parameter);
				
			}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return null;
	}

}