package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GaugeRelation;
import net.wit.entity.GaugeResult;

import java.util.Date;

/**
 * @ClassName: GaugeRelationService
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */

public interface GaugeRelationService extends BaseService<GaugeRelation, Long> {
	Page<GaugeRelation> findPage(Date beginDate, Date endDate, Pageable pageable);
}