package com.cocoker.service;

import com.cocoker.beans.Echarts;
import com.cocoker.beans.Temp;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/26 9:12 PM
 * @Version: 1.0
 */
public interface EchartsService {
    void add(Echarts e);

    List<Echarts> get100();

    String getCurrentData();

    Echarts find1();

    Echarts saveEcharts(Echarts echarts);

    Echarts findByCreateTime(String time);

    Integer updPriceByTime(String price, String createTime);

    Integer cleanEcharts();

    /**
     * 启动群发数据
     */
    void startEcharts();

}
