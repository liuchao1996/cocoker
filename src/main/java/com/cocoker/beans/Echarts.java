package com.cocoker.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/26 9:08 PM
 * @Version: 1.0
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "tbl_echarts")
public class Echarts {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer eid;

    private String price;

    private String createTime;


    public Echarts(String createTime,String price) {
        this.createTime = createTime;
        this.price = price;
    }
}
