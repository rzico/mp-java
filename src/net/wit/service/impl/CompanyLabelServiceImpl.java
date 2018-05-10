package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CompanyLabelDao;
import net.wit.entity.CompanyLabel;
import net.wit.service.CompanyLabelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Eric-Yang on 2018/5/10.
 */
@Service("companyLabelServiceImpl")
public class CompanyLabelServiceImpl extends BaseServiceImpl<CompanyLabel,Long> implements CompanyLabelService{

    @Resource(name = "companyLabelDaoImpl")
    private CompanyLabelDao companyLabelDao;

    @Resource(name = "companyLabelDaoImpl")
    public void setBaseDao(CompanyLabelDao companyLabelDao) {
        super.setBaseDao(companyLabelDao);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void save(CompanyLabel companyLabel) {
        super.save(companyLabel);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public CompanyLabel update(CompanyLabel companyLabel) {
        return super.update(companyLabel);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public CompanyLabel update(CompanyLabel companyLabel, String... ignoreProperties) {
        return super.update(companyLabel, ignoreProperties);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void delete(CompanyLabel companyLabel) {
        super.delete(companyLabel);
    }


    @Override
    public Page<CompanyLabel> findPage(Date beginDate, Date endDate, Pageable pageable) {
        return companyLabelDao.findPage(beginDate,endDate,pageable);
    }
}
