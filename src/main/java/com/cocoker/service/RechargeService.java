package com.cocoker.service;

import com.cocoker.beans.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/5 2:52 PM
 * @Version: 1.0
 */
public interface RechargeService {
    Recharge findRechargeByTopenid(String openid);

    Recharge save(Recharge recharge);

    List<Recharge> findByOpenid(String openid);

    Page<Recharge> findByTstatus(Integer status,Pageable pageable);
}
