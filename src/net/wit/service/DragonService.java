package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Dragon;
import net.wit.entity.Host;

import java.util.Date;

/**
 * @ClassName: DragonService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface DragonService extends BaseService<Dragon, Long> {
	Page<Dragon> findPage(Date beginDate, Date endDate, Pageable pageable);
}