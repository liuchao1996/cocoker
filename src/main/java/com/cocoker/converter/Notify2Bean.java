package com.cocoker.converter;

import com.cocoker.VO.ExchangeVO;
import com.cocoker.beans.PayNotifyBean;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/4/25 6:02 PM
 * @Version: 1.0
 */
@Slf4j
public class Notify2Bean {
    public static PayNotifyBean convert(String str){
        Gson gson = new Gson();
        PayNotifyBean payNotifyBean = new PayNotifyBean();
        try {
            payNotifyBean = gson.fromJson(str, new TypeToken<PayNotifyBean>() {
            }.getType());
        }catch (Exception e){
            log.error("[对象转换错误] 异步通知 str  :{}",str);
            throw new CocokerException(ResultEnum.PARAM_ERROR);
        }
        return payNotifyBean;
    }
}
