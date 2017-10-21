package net.wit.dao.impl;

import java.io.IOException;
import java.util.Calendar;

import java.util.Date;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import freemarker.template.TemplateException;
import net.wit.util.FreemarkerUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SnDao;
import net.wit.entity.Sn;


/**
 * @ClassName: SnDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

@Repository("snDaoImpl")
public class SnDaoImpl extends BaseDaoImpl<Sn, Long> implements SnDao, InitializingBean {

	private HiloOptimizer paymentHiloOptimizer;

	private HiloOptimizer refundsHiloOptimizer;

	private HiloOptimizer transferHiloOptimizer;

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

	@Value("${sn.refunds.prefix}")
	private String transferPrefix;

	@Value("${sn.transfer.maxLo}")
	private int transferMaxLo;


	public void afterPropertiesSet() throws Exception {
		paymentHiloOptimizer = new HiloOptimizer(Sn.Type.payment, paymentPrefix, paymentMaxLo);
		refundsHiloOptimizer = new HiloOptimizer(Sn.Type.refunds, refundsPrefix, refundsMaxLo);
		transferHiloOptimizer = new HiloOptimizer(Sn.Type.transfer, transferPrefix, transferMaxLo);
	}

	public String generate(Sn.Type type) {
		Assert.notNull(type);
		if (type == Sn.Type.payment) {
			return paymentHiloOptimizer.generate();
		} else if (type == Sn.Type.refunds) {
			return refundsHiloOptimizer.generate();
		} else if (type == Sn.Type.transfer) {
			return transferHiloOptimizer.generate();
		}
		return null;
	}

	private long getLastValue(Sn.Type type) {
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

		private Sn.Type type;

		private String prefix;

		private int maxLo;

		private int lo;

		private long hi;

		private long lastValue;

		public HiloOptimizer(Sn.Type type, String prefix, int maxLo) {
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


	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Sn>
	 */
	public Page<Sn> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Sn> criteriaQuery = criteriaBuilder.createQuery(Sn.class);
		Root<Sn> root = criteriaQuery.from(Sn.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.conjunction();
		if (beginDate!=null) {
			Date b = DateUtils.truncate(beginDate,Calendar.DATE);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), b));
		}
		if (endDate!=null) {
			Date e = DateUtils.truncate(endDate,Calendar.DATE);
			e =DateUtils.addDays(e,1);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date> get("createDate"), e));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}
}