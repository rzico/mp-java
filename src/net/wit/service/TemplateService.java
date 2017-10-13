package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Template;

/**
 * @ClassName: TemplateService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface TemplateService extends BaseService<Template, Long> {
	Page<Template> findPage(Date beginDate,Date endDate, Pageable pageable);
	Template findDefault(Template.Type type);
	List<Template> findList(Template.Type type);
}