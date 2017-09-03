package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity - 账单记录
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "xm_enterprise")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_enterprise_sequence")
public class Enterprise extends BaseEntity {
    /** 企业 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业名'")
    private String name;
}
