package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ConfigDao;
import net.wit.dao.VipDao;
import net.wit.entity.Config;
import net.wit.entity.Product;
import net.wit.entity.Vip;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName: ConfigDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

@Repository("configDaoImpl")
public class ConfigDaoImpl extends BaseDaoImpl<Config, Long> implements ConfigDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Vip>
	 */
	public Page<Config> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Config> criteriaQuery = criteriaBuilder.createQuery(Config.class);
		Root<Config> root = criteriaQuery.from(Config.class);
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

	public Config find(String key) {
		if (key == null) {
			return null;
		}
		String jpql = "select config from Config config where lower(config.key) = lower(:key)";
		try {
			return entityManager.createQuery(jpql, Config.class).setFlushMode(FlushModeType.COMMIT).setParameter("key", key).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}