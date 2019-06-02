package com.cocoker.service.impl;

import com.cocoker.beans.Echarts;
import com.cocoker.beans.Order;
import com.cocoker.beans.Temp;
import com.cocoker.beans.UserInfo;
import com.cocoker.dao.OrderDao;
import com.cocoker.dao.TempDao;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.cocoker.service.EchartsService;
import com.cocoker.service.OrderService;
import com.cocoker.service.TempService;
import com.cocoker.service.UserInfoService;
import com.cocoker.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/29 7:25 PM
 * @Version: 1.0
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TempService tempService;
    @Autowired
    private EchartsService echartsService;

    @Override
    public List<Order> findAllOrder(String openid) {
        return orderDao.findByOpenid(openid);
    }

    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat df = new DecimalFormat("#.0000");


    @Override
    @Transactional
    public void addOrder(String flag, String index, String money, String openid, String currentDate) {
        UserInfo userInfo = userInfoService.findByOpenId(openid);
        if (userInfo == null) {
            log.error("[创建订单] openid用户未找到，openid = {}", openid);
            throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
        }
        Order o = new Order();
        if (flag.equals("inc")) {
            o.setDirection("看涨");
        } else {
            o.setDirection("看跌");
        }

        Date d = null;
        //这里会有一个异常，所以要用try catch捕获异常
        try {
            d = sdf.parse(yMd.format(new Date()) + " " + currentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        o.setOindex(index);
        o.setMoney(new BigDecimal(money));
        o.setHandle("0");
        o.setCreateTime(d);
        String t1 = RandomUtil.getZD(Integer.valueOf(money));
        o.setResult(t1);
        o.setOpic(userInfo.getYUpic());
        //判断下
        if ((flag.equals("inc") && t1.equals("盈")) || (flag.equals("dec") && t1.equals("盈"))) {
            //加余额
            Temp temp = new Temp();
            temp.setTMoney(new BigDecimal(Integer.valueOf(money) * 2));
            temp.setTOpenid(openid);
            temp.setCreateTime(new Date());
            Temp r = tempService.save(temp);
            if (r == null) {
                log.error("【加余额】增加失败,temp: {}", temp);
                throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
            }
        }

        //赢
        if ((flag.equals("dec") && t1.equals("亏") || (flag.equals("inc") && t1.equals("盈")))) {
            String ofinal = String.valueOf((Double.valueOf(index) + RandomUtil.getRandomBetD(6,8)));
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0002), format.format((d.getTime() + (24 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0001), format.format((d.getTime() + (25 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0002), format.format((d.getTime() + (26 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0003), format.format((d.getTime() + (27 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0005), format.format((d.getTime() + (28 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0006), format.format((d.getTime() + (29 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal)), format.format((d.getTime() + (30 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0003), format.format((d.getTime() + (31 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0002), format.format((d.getTime() + (32 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0001), format.format((d.getTime() + (33 * 1000))).toString());
            RandomUtil.num = Integer.valueOf(df.format(Double.valueOf(ofinal) + 0.0001).substring(2));
//            RandomUtil.num = Integer.valueOf(df.format(Double.valueOf(ofinal) + 0.0001).substring(4));
            o.setOfinal(ofinal);
        } else {
            //输
            String ofinal = String.valueOf((Double.valueOf(index) - RandomUtil.getRandomBetD(6,8)));
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0002), format.format((d.getTime() + (24 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0001), format.format((d.getTime() + (25 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0002), format.format((d.getTime() + (26 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0003), format.format((d.getTime() + (27 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0005), format.format((d.getTime() + (28 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) + 0.0006), format.format((d.getTime() + (29 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal)), format.format((d.getTime() + (30 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0003), format.format((d.getTime() + (31 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0002), format.format((d.getTime() + (32 * 1000))).toString());
            echartsService.updPriceByTime(df.format(Double.valueOf(ofinal) - 0.0001), format.format((d.getTime() + (33 * 1000))).toString());
            RandomUtil.num = Integer.valueOf(df.format(Double.valueOf(ofinal) + 0.0001).substring(2));
//            RandomUtil.num = Integer.valueOf(df.format(Double.valueOf(ofinal) + 0.0001).substring(4));
            o.setOfinal(ofinal);
        }

        o.setOpenid(openid);
        o.setOnickname(userInfo.getYNickname());
        Integer save = orderDao.saveByAll(o.getOpenid(), o.getDirection(), o.getOindex(), df.format(Double.valueOf(o.getOfinal())), o.getMoney(), o.getResult(), sdf.format(o.getCreateTime()).toString(), o.getHandle(), o.getOnickname(), o.getOpic());
        if (save == null) {
            log.error("[创建订单] 创建订单失败，order = {}", o);
            throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
        }
    }

    @Override
    public List<Order> getByResult(String str) {
        return orderDao.getByResult(str);
    }

    @Override
    public Page<Order> findListPage(Pageable pageable) {
        return orderDao.findAll(pageable);
    }

    @Override
    @Transactional
    public Integer saveByAll(String openid, String direction, String oindex, String ofinal, BigDecimal money, String result, String createTime, String handle, String onickname, String opic) {
        return orderDao.saveByAll(openid, direction, oindex, ofinal, money, result, createTime, handle, onickname, opic);
    }

    @Override
    public Order findLastOrderByOpenid(String openid) {
        return orderDao.findLastOrderByOpenid(openid);
    }

    @Override
    public Order findByOid(Integer oid) {
        return orderDao.findByOid(oid);
    }

    @Override
    public List<Order> findOrder20() {
        return orderDao.findByLatest20();
    }

    @Override
    @Transactional
    public Integer saveResult(String ofinal, String result, String handle, String oid) {
        return orderDao.saveResult(ofinal, result, handle, oid);
    }
}

