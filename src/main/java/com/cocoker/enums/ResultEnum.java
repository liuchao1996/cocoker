package com.cocoker.enums;

import lombok.Getter;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/23 4:49 PM
 * @Version: 1.0
 */
@Getter
public enum  ResultEnum {
    WECHAT_MP_ERROR(-1,"微信公众账号方面错误"),
    USER_NOT_EXIST(-2,"用户不存在"),
    PARAM_ERROR(-3,"参数错误"),
    MONEY_ERROR(-4,"可用余额不足，请充值"),
    USER_STATUS_ERROR(-5,"用户状态不正确"),
    CREATE_ORDER_ERROR(-6,"订单创建失败"),
    UPD_USER_BLANCE_ERR(-7,"用户余额更新失败"),
    UPDATE_USER_ERROR(-8,"更新用户失败"),
    UPDATE_ORDER_ERROR(-9,"更新订单失败"),
    ORDER_NOT_EXIST(-10,"订单不存在"),
    NOTICE_MERCHANT_ERROT(-11,"通知商户回调失败"),

    ;
    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
