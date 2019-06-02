package com.cocoker.beans;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/11 12:27 PM
 * @Version: 1.0
 */
@Entity
@Table(name = "tbl_editbalance")
@Data
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uId;

    private BigDecimal uMoney;

    private String uAdmin;

    private String uUsername;

    private String uOpenid;

//    private Date createTime;
}
