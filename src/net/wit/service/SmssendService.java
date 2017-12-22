package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Smssend;

/**
 * @ClassName: SmssendService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface SmssendService extends BaseService<Smssend, Long> {
	Page<Smssend> findPage(Date beginDate,Date endDate, Pageable pageable);
	/**发送短信*/
	public String smsSend(Smssend smssend);
	public String send(Member member, String mobile, String content);
}