package com.cocoker.controller;

import com.cocoker.VO.ResultVO;
import com.cocoker.beans.*;
import com.cocoker.dto.UserDetailDTO;
import com.cocoker.service.*;
import com.cocoker.utils.MD5;
import com.cocoker.utils.RandomUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/6 11:09 AM
 * @Version: 1.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EchartsService echartsService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private ExchangeService exchangeService;


    public static Integer EchartsConst = 0;


    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login/index");
    }

    /**
     * 登录
     *
     * @param username
     * @param pwd
     * @return
     */
    @PostMapping("/toLogin")
    public ModelAndView toLogin(@RequestParam("username") String username, @RequestParam("pwd") String pwd, HttpSession session) {

        UserInfo userInfo = userInfoService.login(username, MD5.md5(pwd, ""));
        if (userInfo == null) {
            return new ModelAndView("login/index");
        }
        session.setAttribute("admin", userInfo);
        return new ModelAndView("redirect:index");
    }

    @GetMapping("/index")
    public ModelAndView index(@PageableDefault(sort = {"yUid"}, direction = Sort.Direction.DESC) Pageable pageable,
                              Map<String, Object> map) {
        Page<UserInfo> list = userInfoService.findList(pageable);
        String allMoney = userInfoService.findAllMoney();
        String dayAllIncMoney = userInfoService.findDayAllMoney();
        String allUserCount = userInfoService.allUserCount();
        String dayAllDecMoney = exchangeService.findDayExchangeAllMoney();
        map.put("users", list.getContent());
        //总余额
        map.put("allMoney", allMoney);
        //今日充值
        map.put("dayAllMoney", dayAllIncMoney);
        //所有用户数量
        map.put("allUserCount", allUserCount);
        //今日提现
        map.put("dayAllDecMoney",dayAllDecMoney);

        return new ModelAndView("admin/index", map);
    }


    @GetMapping("/ui-elements")
    public ModelAndView index(Map<String, Object> map,
                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (page < 0) {
            return new ModelAndView("admin/ui-elements", map);
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "oid");

        Page<Order> pages = orderService.findListPage(pageRequest);
        map.put("pages", pages);
        return new ModelAndView("admin/ui-elements", map);
    }

    @GetMapping("/tab-panel")
    public ModelAndView editAdmin() {
        return new ModelAndView("admin/tab-panel");
    }

    @GetMapping("/table")
    public ModelAndView table() {
        return new ModelAndView("admin/table");
    }

    @GetMapping("/form")
    public ModelAndView form() {
        return new ModelAndView("admin/form");
    }

    @GetMapping("/search")
    public ModelAndView search() {
        return new ModelAndView("admin/search");
    }


    @GetMapping("/editMoney")
    public ModelAndView editMoney() {
        return new ModelAndView("admin/empty");
    }

    /**
     * 查询所有充值成功的
     * @param map
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/recharge")
    public ModelAndView rechargeList(Map<String, Object> map,
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (page < 0) {
            return new ModelAndView("admin/recharge");
        }
        PageRequest request = PageRequest.of(page, size, Sort.Direction.DESC, "tid");
        //1代表值查询充值成功的
        Page<Recharge> rechargeList = rechargeService.findByTstatus(1, request);
        map.put("rechargeList", rechargeList);
        return new ModelAndView("admin/recharge");
    }

    /**
     * 查询所有提现成功的
     * @param map
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/exchange")
    public ModelAndView exchangeList(Map<String, Object> map,
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (page < 0) {
            return new ModelAndView("admin/recharge");
        }
        PageRequest request = PageRequest.of(page, size, Sort.Direction.DESC, "tId");
        //1代表值查询提现成功的
        Page<Exchange> exchangeList = exchangeService.findByTStatus(1, request);
        map.put("exchangeList", exchangeList);
        return new ModelAndView("admin/exchange");
    }

    @GetMapping("/waitexchange")
    public ModelAndView waitexchange(Map<String, Object> map,
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (page < 0) {
            return new ModelAndView("admin/waitrecharge");
        }
        PageRequest request = PageRequest.of(page, size, Sort.Direction.DESC, "tId");
        //0代表值查询提现未成功的
        Page<Exchange> exchangeList = exchangeService.findByTStatus(0, request);
        map.put("exchangeList", exchangeList);
        return new ModelAndView("admin/waitexchange");
    }
}
