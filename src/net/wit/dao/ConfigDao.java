package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Config;
import net.wit.entity.Vip;

import java.util.Date;


/**
 * @ClassName: ConfigDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

public interface ConfigDao extends BaseDao<Config, Long> {

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Config>
	 */
	Page<Config> findPage(Date beginDate, Date endDate, Pageable pageable);

	Config find(String key);

}