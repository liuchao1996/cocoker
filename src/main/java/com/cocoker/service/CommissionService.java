package com.cocoker.service;

import com.alibaba.fastjson.JSONObject;
import com.cocoker.beans.Commission;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/8 2:03 PM
 * @Version: 1.0
 */
public interface CommissionService {

    String findByCOpenid(String openid);

    String findByCOpenidAndTime(String openid);

    String findByCOpenidAndTimeYesterday(String openid);

    Commission save(Commission commission);

    List<JSONObject> findCommissionTop300();

}
