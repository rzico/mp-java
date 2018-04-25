package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.LiveAdmin;

import java.util.Date;

/**
 * @ClassName: LiveAdminService
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */

public interface LiveAdminService extends BaseService<LiveAdmin, Long> {
	Page<LiveAdmin> findPage(Date beginDate, Date endDate, Pageable pageable);
}