package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tag;

/**
 * @ClassName: TagService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface TagService extends BaseService<Tag, Long> {
	Page<Tag> findPage(Date beginDate,Date endDate, Pageable pageable);
	List<Tag> findList(Tag.Type type);
}