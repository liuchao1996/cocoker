package com.cocoker.controller;

import com.cocoker.beans.Echarts;
import com.cocoker.service.EchartsService;
import com.cocoker.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/26 8:08 PM
 * @Version: 1.0
 */
@RestController
@RequestMapping("/data")
public class EchartsController {

    @Autowired
    private EchartsService echartsService;

//    @ResponseBody
//    @GetMapping("/getEchartsDatas")
//    public List<Map<String,String>> echartsData() {
////        List<Echarts> echarts = echartsService.get100();
////        List lists = new ArrayList<>();
////        lists = echarts.stream().map(e -> Arrays.asList(e.getCreateTime(), e.getPrice())).collect(Collectors.toList());
////        Collections.reverse(lists);
//        Map<String,String> map= new HashMap<>();
//        map.put("s1","k1");
//        List<Map<String,String>> lists = new ArrayList<>();
//        lists.add(map);
//        return lists;
//    }
    @ResponseBody
    @GetMapping("/getEchartsDatas")
    public List echartsData() {
        List<Echarts> echarts = echartsService.get100();
        List lists = new ArrayList<>();
        lists = echarts.stream().map(e -> Arrays.asList(e.getCreateTime(), e.getPrice())).collect(Collectors.toList());
        Collections.reverse(lists);
        return lists;
    }

    /**
     * 项目启动后自动群发数据
     */
    @PostConstruct
    public void startData(){
        echartsService.startEcharts();
    }

    @ResponseBody
    @GetMapping("/getEchartsData")
    public List getEchartsData() {
        Echarts echarts = echartsService.find1();
        if (echarts == null) {
            return null;
        }
        List lists = Arrays.asList(echarts.getCreateTime(), echarts.getPrice());
        return lists;
    }



    //获取当前时间 ps -30s
    @ResponseBody
    @GetMapping("/getCurrentData")
    public String getCurrentData() {
        return echartsService.getCurrentData();
    }
}
