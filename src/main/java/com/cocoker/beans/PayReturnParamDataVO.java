package com.cocoker.beans;

import lombok.Data;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/5/1 1:26 PM
 * @Version: 1.0
 */
@Data
public class PayReturnParamDataVO {

    private String amount;

    private String order_num;

    private String pay_url;
}
