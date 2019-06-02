package com.cocoker.service;

import com.cocoker.VO.PayReturnParamVO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/20 10:12 AM
 * @Version: 1.0
 */
public interface PayService {
    PayReturnParamVO create(String openid, String money);
//    PayReturnParamVO create(String openid, String money);

}
