package com.cocoker.controller;

import com.alibaba.fastjson.JSONObject;
import com.cocoker.beans.Commission;
import com.cocoker.beans.Order;
import com.cocoker.beans.Tip;
import com.cocoker.config.ProjectUrl;
import com.cocoker.service.CommissionService;
import com.cocoker.service.OrderService;
import com.cocoker.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/23 12:08 PM
 * @Version: 1.0
 */
@Controller
public class HelloController {

    @Autowired
    private TipService tipService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private ProjectUrl projectUrl;


    @RequestMapping("/coc")
    public ModelAndView hello(HttpServletRequest request, Map<String, Object> map, @RequestParam(value = "openid", required = false) String openid) {

        String contextPath = request.getContextPath();
        map.put("returnUrl", projectUrl.getReturnUrl());

        List<Order> result1 = orderService.getByResult("盈");
        List<Order> result2 = orderService.findOrder20();
        List<JSONObject> result3 = commissionService.findCommissionTop300();
//        if(result2.size() == 5){
//            result2.remove(4);
//            result2.remove(3);
//        }
//        result1.addAll(result2);
//        if(result1 != null && result1.size()>0){
//            Collections.sort(result1);
//        }
        map.put("ods1", result1);
        map.put("ods2", result2);
        if (result3.size() > 3) {
            map.put("no1", result3.get(0));
            map.put("no2", result3.get(1));
            map.put("no3", result3.get(2));
            result3.remove(0);
            result3.remove(0);
            result3.remove(0);
        }else {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("y_upic","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2662493301,1276913271&fm=27&gp=0.jpg");
            jsonObject1.put("y_nickname","涐还能疼你多久");
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("y_upic","https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=316987360,369718959&fm=26&gp=0.jpg");
            jsonObject2.put("y_nickname","有没有丶未来");
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("y_upic","https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=231856455,1792556924&fm=26&gp=0.jpg");
            jsonObject3.put("y_nickname","研你一曲相思。");
            map.put("no1", jsonObject1);
            map.put("no2", jsonObject2);
            map.put("no3", jsonObject3);
        }

        map.put("ods3", result3);
        map.put("cOpenid", openid);
/*
        <!--最新订单-->
        <p class="newdd">最新订单</p>
        <table>
            <tr>
                <td>用户</td>
                <td>时间</td>
                <td>方向</td>
                <td>指数</td>
                <td>金额</td>
                <td>结果</td>
            </tr>

            <tr th:each=" order : ${ods}">
                <td><img class="tableimg" th:src="${order.opic}"></td>
                <td>[[${#dates.format(order.createTime,'yyyy-MM-dd hh:mm:ss')}]]</td>
                <td th:class="${order.direction =='看涨' ? 'red':'green'}">[[${order.direction}]]</td>
                <td>[[${order.oindex}]]</td>
                <td>[[${order.money}]]</td>
                <td th:class="${order.result =='盈' ? 'red':'green'}">[[${order.result}]]</td>
            </tr>
        </table>


        */
        return new ModelAndView("index", map);
    }

    @RequestMapping("/tococ")
    public ModelAndView tococ(HttpServletRequest request, Map<String, Object> map, @RequestParam(value = "openid", required = false) String openid) {
        return new ModelAndView("aqjc", map);
    }


//    @GetMapping("/btjq")
//    public ModelAndView btjq() {
//        return new ModelAndView("btjq");
//    }

    @ResponseBody
    @GetMapping("/num")
    public Integer getLineNum() {
        Random random = new Random();
        return random.nextInt(300) + 1000;
    }


    @ResponseBody
    @GetMapping("/tip")
    public Tip getTip() {
        return tipService.findFirst();
    }


    @GetMapping("/dispatch")
    public String dispatch() {

        return "";
    }

}
