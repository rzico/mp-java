package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Live;
import net.wit.entity.LiveGift;
import net.wit.entity.Member;

/**
 * @ClassName: LiveGiftService
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */

public interface LiveGiftService extends BaseService<LiveGift, Long> {
	Page<LiveGift> findPage(Date beginDate, Date endDate, Pageable pageable);

	void add(LiveGift gift, Member member, Live live) throws Exception;

}