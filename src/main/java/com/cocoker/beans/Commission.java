package com.cocoker.beans;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/8 2:00 PM
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "tbl_commission")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cId;

    private String cOpenid;

    private BigDecimal cMoney;

    private Date createTime;

}
