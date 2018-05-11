package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CompanyLabel;

import java.util.Date;

/**
 * Created by Eric-Yang on 2018/5/10.
 */
public interface CompanyLabelService extends BaseService<CompanyLabel,Long> {
    Page<CompanyLabel> findPage(Date beginDate, Date endDate, Pageable pageable);
}
