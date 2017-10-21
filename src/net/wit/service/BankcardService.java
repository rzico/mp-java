package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Bankcard;
import net.wit.entity.Member;

/**
 * @ClassName: BankcardService
 * @author 降魔战队
 * @date 2017-10-20 17:56:4
 */

public interface BankcardService extends BaseService<Bankcard, Long> {
	Page<Bankcard> findPage(Date beginDate, Date endDate, Pageable pageable);

	Bankcard findDefault(Member member);
}