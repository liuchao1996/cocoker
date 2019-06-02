package com.cocoker.beans;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/11 4:14 PM
 * @Version: 1.0
 */
@Entity
@Table(name = "tbl_exchange")
@Data
public class Exchange {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer tId;

    private String tMoney;

    private String tOpenid;

    private String tExchangeOpenid;

    private String tNickname;

    private Integer tStatus;

    private Date createTime;

}
