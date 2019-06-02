package com.cocoker.beans;

import com.cocoker.utils.serializer.DateFormatSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/29 7:19 PM
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "tbl_order")
public class Order implements Comparable<Order>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;

    private String openid;

    private String direction;

    private String oindex;

    private String ofinal;

    private BigDecimal money;

    private String result;

    @JsonSerialize(using = DateFormatSerializer.class)
    private Date createTime;

    private String handle;

    private String onickname;

    private String opic;

    @Override
    public int compareTo(Order o) {
        return this.getOpenid().compareTo(o.getOpenid());
    }
}
