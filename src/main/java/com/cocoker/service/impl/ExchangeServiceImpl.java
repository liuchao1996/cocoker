package com.cocoker.service.impl;

import com.cocoker.beans.Exchange;
import com.cocoker.beans.Recharge;
import com.cocoker.beans.UserInfo;
import com.cocoker.dao.ExchangeDao;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.cocoker.service.ExchangeService;
import com.cocoker.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/11 4:18 PM
 * @Version: 1.0
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Autowired
    private ExchangeDao exchangeDao;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public Exchange saveOne(String openid, String money,String exopenid) {
        UserInfo user = userInfoService.findByOpenId(openid);
        if (user == null) {
            throw new CocokerException(-1, ResultEnum.USER_NOT_EXIST.getMsg());
        }
        if(user.getYExchangeOpenid() == ""){
            user.setYExchangeOpenid(exopenid);
            userInfoService.save(user);
        }
        Exchange exchange = new Exchange();
        exchange.setTOpenid(openid);
        exchange.setTMoney(money);
        exchange.setTNickname(user.getYNickname());
        exchange.setTStatus(0);
        exchange.setTExchangeOpenid(exopenid);
        exchange.setCreateTime(new Date());
        return exchangeDao.save(exchange);
    }

    @Override
    public Exchange updOne(String id,Integer code) {
        Exchange one = exchangeDao.getOne(Integer.valueOf(id));
        one.setTStatus(code);
        Exchange e = exchangeDao.save(one);
        if (e == null) {
            throw new CocokerException(-1, ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        }
        return e;
    }

    @Override
    public List<Exchange> findByTopenid(String openid) {
        return exchangeDao.findByTopenid(openid);
    }

    @Override
    public Page<Exchange> findByTStatus(Integer status, Pageable pageable) {
        return exchangeDao.findByTStatus(status,pageable);
    }

    @Override
    public String findDayExchangeAllMoney() {
        return exchangeDao.findDayExchangeAllMoney();
    }

    @Override
    public Exchange findByTId(Integer id) {
        return exchangeDao.findByTId(id);
    }
}
