package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GaugeRelation;
import net.wit.entity.GaugeResult;

import java.util.Date;


/**
 * @ClassName: GaugeRelationDao
 * @author 降魔战队
 * @date 2018-2-12 21:4:34
 */
 

public interface GaugeRelationDao extends BaseDao<GaugeRelation, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<GaugeResult>
	 */
	Page<GaugeRelation> findPage(Date beginDate, Date endDate, Pageable pageable);
}