package com.cocoker.beans;

import com.cocoker.utils.serializer.DateFormatSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/4 12:13 PM
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "tbl_recharge")
public class Recharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;

    private BigDecimal tmoney;

    private String tnickname;

    private BigDecimal tyue;

    private String topenid;

    private Integer tstatus;

    private String torderid;

    //商户订单号
    private String tsdorderno;
    //平台订单号
    private String tsdpayno;

    @JsonSerialize(using = DateFormatSerializer.class)
    private Date createTime;

}
