package com.cocoker.VO;

import com.cocoker.beans.PayReturnParamVOBean;
import lombok.Data;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/5 12:49 PM
 * @Version: 1.0
 */
@Data
public class PayReturnParamVO {

    private Integer code;

    private String msg;

    private PayReturnParamVOBean result;
}
