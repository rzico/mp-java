package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Xdict;

import java.util.Date;

/**
 * @ClassName: LiveAdminService
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */

public interface XdictService extends BaseService<Xdict, Long> {
	Page<Xdict> findPage(Date beginDate, Date endDate, Pageable pageable);
}