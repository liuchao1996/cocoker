package com.cocoker.service.impl;

import com.cocoker.beans.Temp;
import com.cocoker.dao.TempDao;
import com.cocoker.service.TempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/7 6:55 PM
 * @Version: 1.0
 */
@Service
@Transactional
public class TempServiceImpl implements TempService{
    @Autowired
    private TempDao dao;
    @Override
    public List<Temp> findByTOpenid(String openid) {
        return dao.findByTOpenid(openid);
    }

    @Override
    public Temp save(Temp temp) {
        return dao.save(temp);
    }

    @Override
    public List<Temp> findByTOpenidAndTime(String openid) {
        return dao.findByTOpenidAndTime(openid);
    }

    @Override
    @Transactional
    public int delTemp(List<Integer> ids) {
        return dao.deleteByTIdIn(ids);
    }
}
