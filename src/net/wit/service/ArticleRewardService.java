package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleReward;
import net.wit.entity.Payment;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: ArticleRewardService
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */

public interface ArticleRewardService extends BaseService<ArticleReward, Long> {
	//保存并发起支付
	public Payment saveAndPayment(ArticleReward articleReward);
	Page<ArticleReward> findPage(Date beginDate,Date endDate, Pageable pageable);
}