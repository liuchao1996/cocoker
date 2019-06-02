package com.cocoker.controller;

import com.cocoker.VO.ResultVO;
import com.cocoker.beans.*;
import com.cocoker.dao.EchartsDao;
import com.cocoker.dto.UserDetailDTO;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.cocoker.service.*;
import com.cocoker.utils.ResultVOUtil;
import com.lly835.bestpay.rest.type.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/25 7:25 PM
 * @Version: 1.0
 */
@Controller
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private TempService tempService;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    EchartsDao dao;

    @GetMapping("get")
    @ResponseBody
    public Echarts get(){
        return dao.findByCreateTime("22:36:41");
    }

    @ResponseBody
    @RequestMapping("/order")
    public synchronized ResultVO order(@RequestParam("openid") String openid,
                                       @RequestParam("num") String num,
                                       @RequestParam("flag") String flag,
                                       @RequestParam("index") String index,
                                       @RequestParam("currentDate") String currentDate) {

        //TODO
        log.info("openid,{} 金额,{} 方向,{} 指数,{} ", openid, num, flag, index);
        UserInfo userInfo = new UserInfo();
        try {
            userInfo = userInfoService.save(openid, num, flag, index, currentDate);
        } catch (CocokerException e) {
            log.error("[创建订单] 订单错误 e={}", e.getMessage());
            return ResultVOUtil.error(-1, e.getMessage());
        }
//        return ResultVOUtil.success(userInfo);
        return ResultVOUtil.success(new UserDetailDTO(userInfo.getYUsername(), userInfo.getYNickname(), userInfo.getYUpic(), userInfo.getYUsermoney(), "请别黑我，谢谢大哥们！"));
    }

    @ResponseBody
    @GetMapping("/order/info")
    @Transactional
    public ResultVO orderinfo(@RequestParam("openid") String openid) {

        List<Temp> temps = tempService.findByTOpenidAndTime(openid);
        UserInfo userInfo = new UserInfo();
        if (null != temps || temps.size() != 0) {
            UserInfo user = userInfoService.findByOpenId(openid);
            for (Temp t : temps) {
                user.setYUsermoney(user.getYUsermoney().add(t.getTMoney()));
            }
            int num = tempService.delTemp(temps.stream().map(e -> e.getTId()).collect(Collectors.toList()));
            if (num < 0) {
                log.error("[更新余额删除] 删除失败，temps：{} ", temps);
                return ResultVOUtil.error(-1, ResultEnum.UPD_USER_BLANCE_ERR.getMsg());
            }
            userInfo = userInfoService.save(user);
            if (userInfo == null) {
                log.error("[更新余额] 更新失败，user： {}", user);
                return ResultVOUtil.error(-1, ResultEnum.UPD_USER_BLANCE_ERR.getMsg());
            }
        }
        return ResultVOUtil.success(new UserDetailDTO(userInfo.getYUsername(), userInfo.getYNickname(), userInfo.getYUpic(), userInfo.getYUsermoney(), "请不要攻击我，谢谢！"));

    }

    @GetMapping("/order/orderDetail")
    @ResponseBody
    public ResultVO orderDetail(@RequestParam("openid") String openid) {
        Order order = orderService.findLastOrderByOpenid(openid);
        order.setHandle("别黑我,谢谢大佬。球球31778408");
        return ResultVOUtil.success(order);
    }


    @GetMapping("/history")
    public ModelAndView history(@RequestParam("openid") String openid, Map<String, Object> map) {
        List<Order> orders = orderService.findAllOrder(openid);
        map.put("orders", orders);
        return new ModelAndView("order", map);
    }

    @GetMapping("/entry")
    public ModelAndView entry() {
        return new ModelAndView("entry");
    }

    @GetMapping("/modelinfo")
    public ModelAndView modelinfo() {
        return new ModelAndView("modelinfo");
    }

    @GetMapping("/novicetips")
    public ModelAndView novicetips() {
        return new ModelAndView("novicetips");
    }

    @GetMapping("/profitmodel")
    public ModelAndView profitmodel() {
        return new ModelAndView("profitmodel");
    }

    @GetMapping("/recharge")
    public ModelAndView recharge() {
        return new ModelAndView("recharge");
    }

    @GetMapping("/rechargehistory")
    public ModelAndView rechargehistory(@RequestParam("openid") String openid, Map<String, Object> map) {
        List<Recharge> l = rechargeService.findByOpenid(openid);
        map.put("recharges", l);
        return new ModelAndView("rechargehistory", map);
    }

    @GetMapping("/exchargehistory")
    public ModelAndView exchargehistory(@RequestParam("openid") String openid, Map<String, Object> map) {
        List<Exchange> l = exchangeService.findByTopenid(openid);
        map.put("excharge", l);
        return new ModelAndView("exchargehistory", map);
    }

    @GetMapping("/exchange")
    public ModelAndView exchange() {
        return new ModelAndView("exchange");
    }

    @GetMapping("/proxy")
    public ModelAndView proxy(@RequestParam("openid") String openid, Map<String, Object> map) {

        UserInfo user = userInfoService.findByOpenId(openid);
        if (user == null) {
            return new ModelAndView("proxy", map);
        }
        Proxy proxy = new Proxy();
        map.put("user", user);

        //查一级
        List<UserInfo> leven1 = userInfoService.findByOid(openid);
        map.put("leven", leven1);
        map.put("flag", "1");
        //-----start
        //one
        List<UserInfo> one = userInfoService.findByOid(user.getYOpenid());
        if (null != one && one.size() > 0) {
            proxy.setOne(one.size());
            //tow
            List<UserInfo> tow = userInfoService.findByOidIn(one.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            if (null != tow && tow.size() > 0) {
                proxy.setTwo(tow.size());
                //three
                List<UserInfo> three = userInfoService.findByOidIn(tow.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                if (null != three && three.size() > 0) {
                    proxy.setThree(three.size());
                    //four
                    List<UserInfo> four = userInfoService.findByOidIn(three.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                    if (null != four && four.size() > 0) {
                        proxy.setFour(four.size());
                        //five
                        List<UserInfo> five = userInfoService.findByOidIn(four.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                        if (null != five && five.size() > 0) {
                            proxy.setFive(five.size());
                            //six
                            List<UserInfo> six = userInfoService.findByOidIn(five.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                            if (null != six && six.size() > 0) {
                                proxy.setSix(six.size());
                                //seven
                                List<UserInfo> seven = userInfoService.findByOidIn(six.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                                if (null != seven && seven.size() > 0) {
                                    proxy.setSeven(seven.size());
                                    //eight
//                                    List<UserInfo> eight = userInfoService.findByOidIn(seven.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//                                    if (null != eight && eight.size() > 0) {
//                                        proxy.setEight(eight.size());
//                                        //nine
//                                        List<UserInfo> nine = userInfoService.findByOidIn(eight.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//                                        if (null != nine && nine.size() > 0) {
//                                            proxy.setNine(nine.size());
//                                            //ten
//                                            List<UserInfo> ten = userInfoService.findByOidIn(nine.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//                                            if (null != ten && ten.size() > 0) {
//                                                proxy.setTen(ten.size());
//                                            }
//                                        }
//                                    }
                                }
                            }
                        }
                    }
                }

            }

        }

        //-----end
        map.put("proxy", proxy);
        return new ModelAndView("proxy", map);
    }

    @PostMapping("/proxyInfo")
    public ModelAndView proxyInfo(@RequestParam("openid") String openid,
                                  @RequestParam("leven") String leven,
                                  Map<String, Object> map) {

        UserInfo user = userInfoService.findByOpenId(openid);
        map.put("user", user);
        if (leven.equals("1")) {
            List<UserInfo> leven1 = userInfoService.findByOid(openid);
            map.put("leven", leven1);
            map.put("flag", "1");
        }
        if (leven.equals("2")) {
            List<UserInfo> leven1 = userInfoService.findByOid(openid);
            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            map.put("leven", leven2);
            map.put("flag", "2");
        }
        if (leven.equals("3")) {
            List<UserInfo> leven1 = userInfoService.findByOid(openid);
            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            map.put("leven", leven3);
            map.put("flag", "3");
        }
        if (leven.equals("4")) {
            List<UserInfo> leven1 = userInfoService.findByOid(openid);
            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven4 = userInfoService.findByOidIn(leven3.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            map.put("leven", leven4);
            map.put("flag", "4");
        }
        if (leven.equals("5")) {
            List<UserInfo> leven1 = userInfoService.findByOid(openid);
            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven4 = userInfoService.findByOidIn(leven3.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven5 = userInfoService.findByOidIn(leven4.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            map.put("leven", leven5);
            map.put("flag", "5");
        }
        if (leven.equals("6")) {
            List<UserInfo> leven1 = userInfoService.findByOid(openid);
            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven4 = userInfoService.findByOidIn(leven3.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven5 = userInfoService.findByOidIn(leven4.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven6 = userInfoService.findByOidIn(leven5.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            map.put("leven", leven6);
            map.put("flag", "6");
        }
        if (leven.equals("7")) {
            List<UserInfo> leven1 = userInfoService.findByOid(openid);
            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven4 = userInfoService.findByOidIn(leven3.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven5 = userInfoService.findByOidIn(leven4.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven6 = userInfoService.findByOidIn(leven5.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            List<UserInfo> leven7 = userInfoService.findByOidIn(leven6.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            map.put("leven", leven7);
            map.put("flag", "7");
        }
//        if (leven.equals("8")) {
//            List<UserInfo> leven1 = userInfoService.findByOid(openid);
//            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven4 = userInfoService.findByOidIn(leven3.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven5 = userInfoService.findByOidIn(leven4.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven6 = userInfoService.findByOidIn(leven5.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven7 = userInfoService.findByOidIn(leven6.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven8 = userInfoService.findByOidIn(leven7.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            map.put("leven", leven8);
//            map.put("flag", "8");
//        }
//        if (leven.equals("9")) {
//            List<UserInfo> leven1 = userInfoService.findByOid(openid);
//            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven4 = userInfoService.findByOidIn(leven3.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven5 = userInfoService.findByOidIn(leven4.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven6 = userInfoService.findByOidIn(leven5.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven7 = userInfoService.findByOidIn(leven6.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven8 = userInfoService.findByOidIn(leven7.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven9 = userInfoService.findByOidIn(leven8.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            map.put("leven", leven9);
//            map.put("flag", "9");
//        }
//        if (leven.equals("10")) {
//            List<UserInfo> leven1 = userInfoService.findByOid(openid);
//            List<UserInfo> leven2 = userInfoService.findByOidIn(leven1.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven3 = userInfoService.findByOidIn(leven2.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven4 = userInfoService.findByOidIn(leven3.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven5 = userInfoService.findByOidIn(leven4.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven6 = userInfoService.findByOidIn(leven5.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven7 = userInfoService.findByOidIn(leven6.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven8 = userInfoService.findByOidIn(leven7.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven9 = userInfoService.findByOidIn(leven8.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            List<UserInfo> leven10 = userInfoService.findByOidIn(leven9.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//            map.put("leven", leven10);
//            map.put("flag", "10");
//        }
        return new ModelAndView("proxy", map);
//        return new ModelAndView("proxyinfo", map);
    }

    @GetMapping("/proxyCount")
    @ResponseBody
    public Map<String, Integer> proxyCount(@RequestParam("openid") String openid) {
        Map<String, Integer> map = new HashMap<>();
        UserInfo user = userInfoService.findByOpenId(openid);
        //-----start
        //one
        if(user == null){
            map.put("proxyCount", 0);
            return map;
        }
        List<UserInfo> one = userInfoService.findByOid(user.getYOpenid());
        if (one != null && one.size() > 0) {
            map.put("proxyCount", one.size());
            //tow
            List<UserInfo> tow = userInfoService.findByOidIn(one.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
            if (null != tow && tow.size() > 0) {
                map.put("proxyCount", one.size() + tow.size());
                //three
                List<UserInfo> three = userInfoService.findByOidIn(tow.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                if (null != three && three.size() > 0) {
                    map.put("proxyCount", one.size() + tow.size() + three.size());
                    //four
                    List<UserInfo> four = userInfoService.findByOidIn(three.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                    if (null != four && four.size() > 0) {
                        map.put("proxyCount", one.size() + tow.size() + three.size() + four.size());
                        //five
                        List<UserInfo> five = userInfoService.findByOidIn(four.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                        if (null != five && five.size() > 0) {
                            map.put("proxyCount", one.size() + tow.size() + three.size() + four.size() + five.size());
                            //six
                            List<UserInfo> six = userInfoService.findByOidIn(five.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                            if (null != six && six.size() > 0) {
                                map.put("proxyCount", one.size() + tow.size() + three.size() + four.size() + five.size() + six.size());
                                //seven
                                List<UserInfo> seven = userInfoService.findByOidIn(six.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
                                if (null != seven && seven.size() > 0) {
                                    map.put("proxyCount", one.size() + tow.size() + three.size() + four.size() + five.size() + six.size() + seven.size());
                                    //eight
//                                    List<UserInfo> eight = userInfoService.findByOidIn(seven.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//                                    if (null != eight && eight.size() > 0) {
//                                        map.put("proxyCount", one.size() + tow.size() + three.size() + four.size() + five.size() + six.size() + seven.size() + eight.size());
//                                    }
//                                    //nine
//                                    List<UserInfo> nine = userInfoService.findByOidIn(eight.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//                                    if (null != nine && nine.size() > 0) {
//                                        map.put("proxyCount", one.size() + tow.size() + three.size() + four.size() + five.size() + six.size() + seven.size() + eight.size() + nine.size());
//                                    }
//                                    //ten
//                                    List<UserInfo> ten = userInfoService.findByOidIn(nine.stream().map(e -> e.getYOpenid()).collect(Collectors.toList()));
//                                    if (null != ten && ten.size() > 0) {
//                                        map.put("proxyCount", one.size() + tow.size() + three.size() + four.size() + five.size() + six.size() + seven.size() + eight.size() + nine.size() + ten.size());
//                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //-----end
//        map.put("proxyCount", one.size() + tow.size() + three.size() + four.size() + five.size() + six.size() + seven.size());
        return map;
    }

    @GetMapping("/historyCommission")
    @ResponseBody
    public Map<String, Object> historyCommission(@RequestParam("openid") String openid) {

        String commission = commissionService.findByCOpenid(openid);
        Map<String, Object> map = new HashMap<>();
        map.put("historyCommission", commission);
        return map;
    }

    @GetMapping("/historyCommissionByTime")
    @ResponseBody
    public Map<String, Object> historyCommissionByTime(@RequestParam("openid") String openid) {

        String commission = commissionService.findByCOpenidAndTime(openid);
        Map<String, Object> map = new HashMap<>();
        map.put("historyCommissionByTime", commission);
        return map;
    }

    @GetMapping("/historyCommissionByYesterday")
    @ResponseBody
    public Map<String, Object> historyCommissionByYesterday(@RequestParam("openid") String openid) {
        String commission = commissionService.findByCOpenidAndTimeYesterday(openid);
        Map<String, Object> map = new HashMap<>();
        map.put("historyCommissionByYesterday", commission);
        return map;
    }

    @GetMapping("test")
    public String String() {

        return "redirect:https://www.baidu.com/";
    }

}
