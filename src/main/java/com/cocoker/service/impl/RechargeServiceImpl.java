package com.cocoker.service.impl;

import com.cocoker.beans.Recharge;
import com.cocoker.dao.RechargeDao;
import com.cocoker.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/5 2:53 PM
 * @Version: 1.0
 */
@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RechargeDao rechargeDao;

    @Override
    public Recharge findRechargeByTopenid(String openid) {
        return rechargeDao.findByTorderid(openid);
    }

    @Override
    public Recharge save(Recharge recharge) {
        return rechargeDao.save(recharge);
    }

    @Override
    public List<Recharge> findByOpenid(String openid) {
        return rechargeDao.findByTopenid(openid);
    }

    @Override
    public Page<Recharge> findByTstatus(Integer status, Pageable pageable) {
        return rechargeDao.findByTstatus(status,pageable);
    }
}
