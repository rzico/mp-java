package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.PluginConfig;


/**
 * @ClassName: PluginConfigDao
 * @author 降魔战队
 * @date 2017-9-3 21:54:59
 */
 

public interface PluginConfigDao extends BaseDao<PluginConfig, Long> {

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<PluginConfig>
	 */
	Page<PluginConfig> findPage(Date beginDate, Date endDate, Pageable pageable);


	/**
	 * 判断插件ID是否存在
	 *
	 * @param pluginId
	 *            插件ID
	 * @return 插件ID是否存在
	 */
	boolean pluginIdExists(String pluginId);

	/**
	 * 根据插件ID查找插件配置
	 *
	 * @param pluginId
	 *            插件ID
	 * @return 插件配置，若不存在则返回null
	 */
	PluginConfig findByPluginId(String pluginId);

}