package com.cocoker.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cocoker.beans.Echarts;
import com.cocoker.dao.EchartsDao;
import com.cocoker.service.EchartsService;
import com.cocoker.utils.Mywebebsocket;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/26 9:12 PM
 * @Version: 1.0
 */
@Service
@Slf4j
public class EchartsServiceImpl implements EchartsService {
    @Autowired
    private EchartsDao echartsDao;


    @Autowired
    private Mywebebsocket mywebebsocket;

    @Override
    public void add(Echarts e) {
        echartsDao.save(e);
    }

    /**
     * 前台获取实时数据
     *
     * @return
     */
    @Override
    public List<Echarts> get100() {
        return echartsDao.find100();
    }

    private static List<Echarts> list = null;
    private static JSONObject jsonObject = new JSONObject();

    @Override
    public void startEcharts() {
        new Thread(() -> {
            try {
                synchronized (this) {

                    while (true) {

                        if (list == null) {
                            GetObj();
                        }
                        mywebebsocket.sendMsgToAll(jsonObject.toJSONString());
                        GetObj();
                        //等待一秒后再次获取数据群发给客户
                        wait(1000);
                    }
                }
            } catch (Exception e) {
                log.error("【前台获取实时数据错误】" + e.getMessage());
            }

        }).start();
    }

    /**
     * 设置数据格式
     */
    private void GetObj() {
        list = echartsDao.find100();
        List<String> timeList = new LinkedList<>();
        List<String> priceList = new LinkedList<>();

        for (int i = list.size() - 1; i > 0; i--) {
            timeList.add(list.get(i).getCreateTime());
            priceList.add(list.get(i).getPrice());
        }

        jsonObject.put("timeList", timeList);
        jsonObject.put("priceList", priceList);
    }

    @Override
    public String getCurrentData() {
        return echartsDao.getCurrentData();
    }

    @Override
    public Echarts find1() {
        return echartsDao.find1();
    }

    @Override
    public Echarts saveEcharts(Echarts echarts) {
        return echartsDao.save(echarts);
    }


    @Override
    public Echarts findByCreateTime(String time) {
        return echartsDao.findByCreateTime(time);
    }

    @Override
    @Transactional
    public Integer updPriceByTime(String price, String createTime) {
        return echartsDao.updPriceByTime(price, createTime);
    }

    @Override
    @Transactional
    public Integer cleanEcharts() {
        return echartsDao.cleanTable();
    }
}
