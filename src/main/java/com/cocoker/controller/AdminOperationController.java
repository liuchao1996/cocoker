package com.cocoker.controller;

import com.cocoker.VO.ResultVO;
import com.cocoker.beans.*;
import com.cocoker.enums.ResultEnum;
import com.cocoker.service.*;
import com.cocoker.utils.DateUtil;
import com.cocoker.utils.MD5;
import com.cocoker.utils.RandomUtil;
import com.cocoker.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.TimerTask;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/9 2:35 PM
 * @Version: 1.0
 */
@RestController
@RequestMapping("/admin/operation")
@Slf4j
public class AdminOperationController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private OrderService orderService;
    @Autowired
    public EchartsService echartsService;
    @Autowired
    private TempService tempService;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private BalanceService balanceService;

    @Autowired
    private TipService tipService;

    public static Integer EchartsConst = 0;

    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat df = new DecimalFormat("#.0000");
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    /**
     * 删除用户
     *
     * @param uid
     * @param session
     * @return
     */
    @GetMapping("/delUser")
    public ModelAndView delUser(@RequestParam("id") String uid, HttpSession session) {
        if (uid == null) {

        }
        userInfoService.delOne(Integer.valueOf(uid));
        return new ModelAndView("redirect:/admin/index");
    }

    /**
     * 锁用户
     *
     * @param id
     * @return
     */
    @PostMapping("/lock/{id}")
    public ResultVO lock(@PathVariable("id") String id) {
        if (id == null) {
            return ResultVOUtil.error(-1, ResultEnum.USER_NOT_EXIST.getMsg());
        }
        UserInfo result = userInfoService.findOne(Integer.valueOf(id));
        if (result == null) {
            return ResultVOUtil.error(-1, ResultEnum.USER_NOT_EXIST.getMsg());
        }
        result.setYUstatus(1);
        UserInfo save = userInfoService.save(result);
        if (save == null) {
            return ResultVOUtil.error(-1, ResultEnum.UPDATE_USER_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }


    /**
     * 解锁用户
     *
     * @param id
     * @return
     */
    @PostMapping("/unlock/{id}")
    public ResultVO unlock(@PathVariable("id") String id) {
        if (id == null) {
            return ResultVOUtil.error(-1, ResultEnum.USER_NOT_EXIST.getMsg());
        }
        UserInfo result = userInfoService.findOne(Integer.valueOf(id));
        if (result == null) {
            return ResultVOUtil.error(-1, ResultEnum.USER_NOT_EXIST.getMsg());
        }
        result.setYUstatus(0);
        UserInfo save = userInfoService.save(result);
        if (save == null) {
            return ResultVOUtil.error(-1, ResultEnum.UPDATE_USER_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }


    /**
     * 设置赢
     *
     * @param oid
     * @return
     */
    @PostMapping("/up/{oid}")
    public ResultVO up(@PathVariable("oid") Integer oid) {
        Order order = orderService.findByOid(oid);
        if (new Date().getTime() - 30 * 1000 > order.getCreateTime().getTime()) {
            return ResultVOUtil.error(-1, "超过30秒");
        }
        order.setResult("盈");
//        Double price = Double.valueOf(+RandomUtil.getRandomBet(10, 50));
        if (order.getDirection().equals("看涨")) {
            order.setOfinal(String.valueOf(Double.valueOf(order.getOindex()) + 0.0003));
        }
        if (order.getDirection().equals("看跌")) {
            order.setOfinal(String.valueOf(Double.valueOf(order.getOindex()) - 0.0003));
        }

        Echarts echarts1 = echartsService.findByCreateTime(format.format((order.getCreateTime().getTime()) + (1000 * 30)).toString());
        if (echarts1 == null) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        } else {
            if (order.getDirection().equals("看涨")) {
                echarts1.setPrice(String.valueOf(Double.valueOf(order.getOindex()) + 0.0003));
            }
            if (order.getDirection().equals("看跌")) {
                echarts1.setPrice(String.valueOf(Double.valueOf(order.getOindex()) - 0.0002));
            }
            Echarts es = echartsService.saveEcharts(echarts1);
            if (es == null) {
                return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
            }
        }
        Echarts echarts2 = echartsService.findByCreateTime(format.format((order.getCreateTime().getTime()) + (1000 * 29)).toString());
        if (echarts2 == null) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        } else {
            if (order.getDirection().equals("看涨")) {
                echarts2.setPrice(String.valueOf(Double.valueOf(order.getOindex()) + 0.0001));
            }
            if (order.getDirection().equals("看跌")) {
                echarts2.setPrice(String.valueOf(Double.valueOf(order.getOindex()) - 0.0001));
            }
            Echarts es = echartsService.saveEcharts(echarts2);
            if (es == null) {
                return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
            }
        }
        Echarts echarts3 = echartsService.findByCreateTime(format.format((order.getCreateTime().getTime()) + (1000 * 28)).toString());
        if (echarts3 == null) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        } else {
            if (order.getDirection().equals("看涨")) {
                echarts3.setPrice(String.valueOf(Double.valueOf(order.getOindex()) + 0.0003));
            }
            if (order.getDirection().equals("看跌")) {
                echarts3.setPrice(String.valueOf(Double.valueOf(order.getOindex()) - 0.0002));
            }
            Echarts es = echartsService.saveEcharts(echarts3);
            if (es == null) {
                return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
            }
        }

        Integer result = orderService.saveResult(order.getOfinal(), order.getResult(), "1", order.getOid().toString());
        if (result < 0) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        }
        //加余额
        Temp temp = new Temp();
        temp.setTMoney(order.getMoney().multiply(new BigDecimal(2)));
        temp.setTOpenid(order.getOpenid());
        temp.setCreateTime(order.getCreateTime());
        Temp r = tempService.save(temp);
        if (r == null) {
            log.error("【用户订单修改 加余额】增加失败,temp: {}", temp);
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }


    /**
     * 设置输
     *
     * @param oid
     * @return
     */
    @PostMapping("/down/{oid}")
    public ResultVO down(@PathVariable("oid") Integer oid) {
        Order order = orderService.findByOid(oid);
        if (new Date().getTime() - 30 * 1000 > order.getCreateTime().getTime()) {
            return ResultVOUtil.error(-1, "超过30秒");
        }
        order.setResult("亏");
        Double price = Double.valueOf(RandomUtil.getRandomBet(10, 50)) + Double.valueOf(RandomUtil.getRandomBet(10, 99));
        if (order.getDirection().equals("看涨")) {
            order.setOfinal(String.valueOf(Double.valueOf(order.getOindex()) - 0.0003));
        }
        if (order.getDirection().equals("看跌")) {
            order.setOfinal(String.valueOf(Double.valueOf(order.getOindex()) + 0.0002));
        }

        Echarts echarts1 = echartsService.findByCreateTime(format.format((order.getCreateTime().getTime()) + (1000 * 30)).toString());
        if (echarts1 == null) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        } else {
            if (order.getDirection().equals("看涨")) {
                echarts1.setPrice(String.valueOf(Double.valueOf(order.getOindex()) - 0.0003));
            }
            if (order.getDirection().equals("看跌")) {
                echarts1.setPrice(String.valueOf(Double.valueOf(order.getOindex()) + 0.0003));
            }
            Echarts es = echartsService.saveEcharts(echarts1);
            if (es == null) {
                return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
            }
        }

        Echarts echarts2 = echartsService.findByCreateTime(format.format((order.getCreateTime().getTime()) + (1000 * 29)).toString());
        if (echarts2 == null) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        } else {
            if (order.getDirection().equals("看涨")) {
                echarts2.setPrice(String.valueOf(Double.valueOf(order.getOindex()) - 0.0002));
            }
            if (order.getDirection().equals("看跌")) {
                echarts2.setPrice(String.valueOf(Double.valueOf(order.getOindex()) + 0.0002));
            }
            Echarts es = echartsService.saveEcharts(echarts2);
            if (es == null) {
                return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
            }
        }

        Echarts echarts3 = echartsService.findByCreateTime(format.format((order.getCreateTime().getTime()) + (1000 * 28)).toString());
        if (echarts3 == null) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        } else {
            if (order.getDirection().equals("看涨")) {
                echarts3.setPrice(String.valueOf(Double.valueOf(order.getOindex()) - 0.0001));
            }
            if (order.getDirection().equals("看跌")) {
                echarts3.setPrice(String.valueOf(Double.valueOf(order.getOindex()) + 0.0001));
            }
            Echarts es = echartsService.saveEcharts(echarts3);
            if (es == null) {
                return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
            }
        }
        Integer result = orderService.saveResult(order.getOfinal(), order.getResult(), "1", order.getOid().toString());
        if (result < 0) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        }
        //减掉余额
        List<Temp> temps = tempService.findByTOpenid(order.getOpenid());
        if (null == temps || temps.size() == 0) {
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        }
        List<Integer> a = temps.stream().map(e -> e.getTId()).collect(Collectors.toList());
        int num = tempService.delTemp(temps.stream().map(e -> e.getTId()).collect(Collectors.toList()));
        if (num < 0) {
            log.error("[更新订单删除余额] 删除失败，temps：{} ", temps);
            return ResultVOUtil.error(ResultEnum.UPDATE_ORDER_ERROR.getCode(), ResultEnum.UPDATE_ORDER_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }


    /**
     * 修改管理员密码
     *
     * @param session
     * @param oldPwd  旧密码
     * @param pwd1    新密码
     * @param pwd2    确认密码
     * @param uid     uid
     * @return
     */
    @PostMapping("/editAdmin")
    public ResultVO editAdmin(HttpSession session, @RequestParam("oldPwd") String oldPwd, @RequestParam("pwd1") String pwd1, @RequestParam("pwd2") String pwd2, @RequestParam("uid") String uid) {
        if (!pwd1.equals(pwd2)) {
            return ResultVOUtil.error(-1, "2次输入不一致");
        }
        UserInfo one = userInfoService.findOne(Integer.valueOf(uid));
        if (!MD5.md5(oldPwd, "").equals(one.getYUpwd())) {
            return ResultVOUtil.error(-1, "旧密码不对");
        }
        one.setYUpwd(MD5.md5(pwd2, ""));
        UserInfo save = userInfoService.save(one);
        if (save == null) {
            return ResultVOUtil.error(-1, "修改失败");
        }
        session.removeAttribute("admin");
        return ResultVOUtil.success();
    }

    /**
     * 设置E charts数据
     *
     * @return
     */
    @GetMapping("/setEchartsDatas")
    @ResponseBody
    public String setEchartsData() {
        DecimalFormat df = new DecimalFormat("#.0000");
        if (EchartsConst == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Timer timer = new Timer(true);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    timer.schedule(new TimerTask() {
                        public void run() {
                            echartsService.add(new Echarts(sdf.format(new Date().getTime() + 30000).toString(), df.format(Double.valueOf(RandomUtil.getRandomBetween(1, 10).toString()))));
//                            echartsService.add(new Echarts(sdf.format(new Date().getTime() + 30000).toString(), RandomUtil.getRandomBetween(1, 10).toString() + RandomUtil.getRandomBet(1, 5)));
                        }
                    }, 1000, 1000);
                }
            }).start();
            EchartsConst++;

            //定时清除Echarts
            Date date = DateUtil.getTime(00, 00, 00);//凌晨0点//第一次执行定时任务的时间
            //如果第一次执行定时任务的时间 小于当前的时间
            //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
            if (date.before(new Date())) {
                date = this.addDay(date, 1);
            }
            Timer timer = new Timer();
            TimingTask1 task = new TimingTask1(echartsService);
            //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
            timer.schedule(task, date, PERIOD_DAY);
        } else {
            return "只能设置一次";
        }
        return "ok";
    }


    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * 清除E charts
     *
     * @return
     */
    @GetMapping("/cleanEcharts")
    public String cleanEcharts() {
        Integer clean = echartsService.cleanEcharts();
        if (clean < 0) {
            return "error";
        }
        return "ok";
    }


    /**
     * 查询用户信息
     *
     * @param uid
     * @param map
     * @return
     */
    @PostMapping("/userInfo/{uid}")
    public ResultVO userInfo(@PathVariable("uid") Integer uid, Map<String, Object> map) {
        UserInfo one = userInfoService.findOne(uid);
        if (one == null) {
            return ResultVOUtil.error(-1, "没查到该用户");
        }
        List<Recharge> recharges = rechargeService.findByOpenid(one.getYOpenid());
        map.put("recharges", recharges);
        map.put("one", one);
        return ResultVOUtil.success(map);
    }


    @PostMapping("/editMoney")
    @Transactional
    public ResultVO editMoney(@RequestParam("uid") Integer uid, @RequestParam("umoney") String money, HttpSession session) {
        UserInfo one = userInfoService.findOne(uid);
        if (one == null) {
            return ResultVOUtil.error(-1, "没查到");
        }
        one.setYUsermoney(new BigDecimal(money));
        UserInfo save = userInfoService.save(one);
        if (save == null) {
            return ResultVOUtil.error(-1, "修改失败");
        }
        UserInfo admin = (UserInfo) session.getAttribute("admin");
        Balance b = new Balance();
        b.setUMoney(new BigDecimal(money));
//        b.setCreateTime(new Date());
        b.setUAdmin(admin.getYOpenid());
        b.setUOpenid(one.getYOpenid());
        b.setUUsername(one.getYNickname());
        balanceService.save(b);
        return ResultVOUtil.success();
    }

    @GetMapping("/rhyme")
    @ResponseBody
    public ResultVO grh() {
        List<Balance> all = balanceService.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("all", all);
        return ResultVOUtil.success(map);
    }

    @PostMapping("/search/{uName}")
    public ResultVO search(@PathVariable("uName") String uName, Map<String, Object> map) {
        List<UserInfo> userInfoList = userInfoService.findByNicknameLike("%" + uName + "%");
        if (null == userInfoList || userInfoList.size() == 0) {
            return ResultVOUtil.error(-1, "没查到");
        }
        map.put("userInfoList", userInfoList);
        return ResultVOUtil.success(userInfoList);
    }


    @PostMapping("/settip")
    @ResponseBody
    public String setTip(@RequestParam(value = "msg", required = false) String msg) {
        if (msg == "" || msg == null) {
            int i = tipService.updTip("");
            return "设置位空";
        }
        int i = tipService.updTip(msg);
        if (i > 0) {
            return "ok";
        }
        return "设置失败";
    }

}
