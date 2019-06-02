package com.cocoker.converter;

import com.cocoker.VO.ExchangeVO;
import com.cocoker.VO.PayReturnParamVO;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/12 1:21 PM
 * @Version: 1.0
 */
@Slf4j
public class ExchangeResult2Bean {
    public static ExchangeVO convert(String str){
        Gson gson = new Gson();
        ExchangeVO exchangeVO = new ExchangeVO();
        try {
            exchangeVO = gson.fromJson(str, new TypeToken<ExchangeVO>() {
            }.getType());
        }catch (Exception e){
            log.error("[对象转换错误] 提现订单结果 str  :{}",str);
            throw new CocokerException(ResultEnum.PARAM_ERROR);
        }
        return exchangeVO;
    }
}
