package com.cocoker.controller;

import com.cocoker.dao.EchartsDao;
import com.cocoker.service.EchartsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimerTask;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/5/6 1:59 PM
 * @Version: 1.0
 */
@Slf4j
public class TimingTask1 extends TimerTask {

    private EchartsService echartsService;

    @Override
    public void run() {
        Integer clean = echartsService.cleanEcharts();
        if (clean < 0) {
            log.info("[清除echarts数据] 成功 date : {}", new Date());
        } else {
            log.error("[清除echarts数据] 失败 date : {}", new Date());
        }
    }

    public TimingTask1(EchartsService es) {
        this.echartsService = es;
    }

    public TimingTask1() {
    }
}
