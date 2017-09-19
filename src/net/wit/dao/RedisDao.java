package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Redis;
import net.wit.entity.Tag;

import java.util.Date;


/**
 * @ClassName: TagDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

public interface RedisDao extends BaseDao<Redis, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Tag>
	 */
	Page<Redis> findPage(Date beginDate, Date endDate, Pageable pageable);
	/**
	 * 根据用户名查找会员
	 * @param key (忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Redis findKey(String key);
}