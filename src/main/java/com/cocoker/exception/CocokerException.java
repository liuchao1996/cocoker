package com.cocoker.exception;

import com.cocoker.enums.ResultEnum;
import lombok.Data;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/23 4:45 PM
 * @Version: 1.0
 */
@Data
public class CocokerException extends RuntimeException{
    private Integer code;

    public CocokerException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public CocokerException(Integer code,String msg) {
        super(msg);
        this.code = code;
    }
}
