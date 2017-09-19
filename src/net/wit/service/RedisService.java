package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Redis;

import java.util.Date;

/**
 * @ClassName: RedisService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface RedisService extends BaseService<Redis, Long> {
	Page<Redis> findPage(Date beginDate, Date endDate, Pageable pageable);
	Redis findKey(String key);
	void put(String key,String value);
	void remove(String key);
}