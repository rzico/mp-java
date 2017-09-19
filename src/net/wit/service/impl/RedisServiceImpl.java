package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.dao.RedisDao;
import net.wit.entity.Member;
import net.wit.entity.Redis;
import net.wit.service.RedisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName: RedisDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("redisServiceImpl")
public class RedisServiceImpl extends BaseServiceImpl<Redis, Long> implements RedisService {
	@Resource(name = "redisDaoImpl")
	private RedisDao redisDao;

	@Resource(name = "redisDaoImpl")
	public void setBaseDao(RedisDao redisDao) {
		super.setBaseDao(redisDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Redis redis) {
		super.save(redis);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Redis update(Redis redis) {
		return super.update(redis);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Redis update(Redis redis, String... ignoreProperties) {
		return super.update(redis, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Redis redis) {
		super.delete(redis);
	}

	public Page<Redis> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return redisDao.findPage(beginDate,endDate,pageable);
	}

	public Redis findKey(String key) {
		Redis redis = null;
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			String sid = request.getSession().getId();
			String skey = sid+"#"+key;
			redis = redisDao.findKey(skey);
		}
		return redis;
	}

	public void put(String key,String value) {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			String sid = request.getSession().getId();
			String skey = sid+"#"+key;
			Redis redis = redisDao.findKey(skey);
			if (redis==null) {
				redis = new Redis();
				redis.setKey(skey);
				redis.setValue(value);
			}
			redisDao.persist(redis);
		}
	}

	public void remove(String key) {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			String sid = request.getSession().getId();
			String skey = sid+"#"+key;
			Redis redis = redisDao.findKey(skey);
			redisDao.remove(redis);
		}
	}

}