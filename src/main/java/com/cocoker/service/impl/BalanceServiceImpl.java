package com.cocoker.service.impl;

import com.cocoker.beans.Balance;
import com.cocoker.dao.BalanceDao;
import com.cocoker.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/11 12:31 PM
 * @Version: 1.0
 */
@Service
public class BalanceServiceImpl implements BalanceService{
    @Autowired
    private BalanceDao balanceDao;

    @Override
    @Transactional
    public void save(Balance balance) {
        balanceDao.save(balance);
    }

    @Override
    public List<Balance> findAll() {
        return balanceDao.findAll();
    }
}
