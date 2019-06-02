package com.cocoker.enums;

import lombok.Getter;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/24 8:51 PM
 * @Version: 1.0
 */
@Getter
public enum UserStatusEnum {
    DEFAULT_USER_STATUS(0,"用户默认状态"),
    ;
    private Integer code;

    private String msg;

    UserStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
