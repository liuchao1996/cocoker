package com.cocoker.beans;

import lombok.Data;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/5 2:29 PM
 * @Version: 1.0
 */
@Data
public class PayNotifyBean {

    private String amount;

    private String merchant_no;

    private String merchant_data;

    private String merchant_order_num;

    private String order_num;

    private String status;

    private String transaction_time;

    private String sign;

}
