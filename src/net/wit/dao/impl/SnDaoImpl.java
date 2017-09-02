/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import net.wit.dao.SnDao;
import net.wit.entity.Sn;
import net.wit.entity.Sn.Type;
import net.wit.util.FreemarkerUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import freemarker.template.TemplateException;

/**
 * Dao - 序列号
 * @author rsico Team
 * @version 3.0
 */
@Repository("snDaoImpl")
public class SnDaoImpl implements SnDao, InitializingBean {

	private HiloOptimizer paymentHiloOptimizer;

	private HiloOptimizer refundsHiloOptimizer;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${sn.payment.prefix}")
	private String paymentPrefix;

	@Value("${sn.payment.maxLo}")
	private int paymentMaxLo;

	@Value("${sn.refunds.prefix}")
	private String refundsPrefix;

	@Value("${sn.refunds.maxLo}")
	private int refundsMaxLo;

	public void afterPropertiesSet() throws Exception {
		paymentHiloOptimizer = new HiloOptimizer(Type.payment, paymentPrefix, paymentMaxLo);
		refundsHiloOptimizer = new HiloOptimizer(Type.refunds, refundsPrefix, refundsMaxLo);
	}

	public String generate(Type type) {
		Assert.notNull(type);
		if (type == Type.payment) {
			return paymentHiloOptimizer.generate();
		} else if (type == Type.refunds) {
			return refundsHiloOptimizer.generate();
		}
		return null;
	}

	private long getLastValue(Type type) {
		String jpql = "select sn from Sn sn where sn.type = :type";
		Sn sn = entityManager.createQuery(jpql, Sn.class).setFlushMode(FlushModeType.COMMIT).setLockMode(LockModeType.PESSIMISTIC_WRITE).setParameter("type", type).getSingleResult();
		long lastValue = sn.getLastValue();
		sn.setLastValue(lastValue + 1);
		entityManager.merge(sn);
		return lastValue;
	}

	/**
	 * 高低位算法
	 */
	private class HiloOptimizer {

		private Type type;

		private String prefix;

		private int maxLo;

		private int lo;

		private long hi;

		private long lastValue;

		public HiloOptimizer(Type type, String prefix, int maxLo) {
			this.type = type;
			this.prefix = prefix != null ? prefix.replace("{", "${") : "";
			this.maxLo = maxLo;
			this.lo = maxLo + 1;
		}

		public synchronized String generate() {
			if (lo > maxLo) {
				lastValue = getLastValue(type);
				lo = lastValue == 0 ? 1 : 0;
				hi = lastValue * (maxLo + 1);
			}
			try {
				return FreemarkerUtils.process(prefix, null) + (hi + lo++);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			return String.valueOf(hi + lo++);
		}
	}

}