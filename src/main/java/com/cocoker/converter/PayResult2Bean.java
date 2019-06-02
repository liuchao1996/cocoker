package com.cocoker.converter;

import com.cocoker.VO.PayReturnParamVO;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/5 12:57 PM
 * @Version: 1.0
 */
@Slf4j
public class PayResult2Bean {

    public static PayReturnParamVO convert(String str){
        Gson gson = new Gson();
        PayReturnParamVO prp = new PayReturnParamVO();
        try {
            prp = gson.fromJson(str, new TypeToken<PayReturnParamVO>() {
            }.getType());
        }catch (Exception e){
            log.error("[对象转换错误] 充值订单结果 str  :{}",str);
            throw new CocokerException(ResultEnum.PARAM_ERROR);
        }
        return prp;
    }
}
