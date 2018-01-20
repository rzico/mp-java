package net.wit.service.impl;

import net.wit.dao.WeixinUpDao;
import net.wit.service.WeixinUpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Eric on 2018/1/15.
 */
@Service("weixinUpServiceImpl")
public class WeixinUpServiceImpl implements WeixinUpService {

    @Resource(name = "weixinUpDaoImpl")
    private WeixinUpDao weixinUpDao;

    @Override
    @Transactional
    public String ArticleUpLoad(Long[] ids){
        return weixinUpDao.ArticleUpLoad(ids);
    }
}
