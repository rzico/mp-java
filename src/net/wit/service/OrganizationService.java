package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Organization;

/**
 * @ClassName: OrganizationService
 * @author 降魔战队
 * @date 2018-2-28 16:42:7
 */

public interface OrganizationService extends BaseService<Organization, Long> {
	Page<Organization> findPage(Date beginDate, Date endDate, Pageable pageable);
}