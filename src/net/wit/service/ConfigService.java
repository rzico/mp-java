package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Config;

import java.util.Date;

/**
 * @ClassName: TopicService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface ConfigService extends BaseService<Config, Long> {
	Config find(String key);
	Page<Config> findPage(Date beginDate, Date endDate, Pageable pageable);
}