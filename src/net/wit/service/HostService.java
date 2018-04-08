package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Host;
import net.wit.entity.Shipping;

import java.util.Date;

/**
 * @ClassName: HostService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface HostService extends BaseService<Host, Long> {
	Page<Host> findPage(Date beginDate, Date endDate, Pageable pageable);
}