package com.cocoker.service;

import com.cocoker.beans.Balance;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/11 12:31 PM
 * @Version: 1.0
 */
public interface BalanceService {

    void save(Balance balance);

    List<Balance> findAll();

}
