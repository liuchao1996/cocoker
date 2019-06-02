package com.cocoker.VO;

import lombok.Data;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/24 9:35 PM
 * @Version: 1.0
 */
@Data
public class ResultVO <T>{
    private Integer code;

    private String msg;

    private T data;
}
