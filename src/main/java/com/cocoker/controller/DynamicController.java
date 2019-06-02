package com.cocoker.controller;

import com.cocoker.beans.Order;
import com.cocoker.beans.UserInfo;
import com.cocoker.service.OrderService;
import com.cocoker.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/3 9:40 AM
 * @Version: 1.0
 */
@RequestMapping("/dynamic")
@Controller
public class DynamicController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/getnew")
    @ResponseBody
    public List getDynamic(){
        List list = new ArrayList();
        List<Order> result = orderService.getByResult("盈");
        for (Order o : result){
            UserInfo u = userInfoService.findByOpenId(o.getOpenid());
            list.add("恭喜：<img  height='25px' width='25px' style='border-radius: 50%;padding-bottom:2px;' src=" + u.getYUpic() +"> "+ u.getYNickname() + " 获得 "+ (Integer.valueOf(o.getMoney().toString()) * 2) + " $ 美元");
        }
        return list;
    }
}
