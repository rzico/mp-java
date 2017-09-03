package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Receiver;

/**
 * @ClassName: ReceiverService
 * @author 降魔战队
 * @date 2017-9-3 20:35:58
 */

public interface ReceiverService extends BaseService<Receiver, Long> {
	Page<Receiver> findPage(Date beginDate, Date endDate, Pageable pageable);
}