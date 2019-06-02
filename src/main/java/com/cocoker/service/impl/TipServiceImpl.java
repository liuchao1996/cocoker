package com.cocoker.service.impl;

import com.cocoker.beans.Tip;
import com.cocoker.dao.TipDao;
import com.cocoker.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/5/5 8:58 AM
 * @Version: 1.0
 */
@Service
public class TipServiceImpl implements TipService{
    @Autowired
    private TipDao tipDao;

    @Override
    public Tip findFirst() {
        return tipDao.findFirst();
    }

    @Override
    public int updTip(String msg) {
        return tipDao.updTip(msg);
    }
}
