package com.cocoker.controller;

import com.cocoker.VO.ExchangeVO;
import com.cocoker.VO.ResultVO;
import com.cocoker.beans.Exchange;
import com.cocoker.beans.UserInfo;
import com.cocoker.config.ProjectUrl;
import com.cocoker.converter.ExchangeResult2Bean;
import com.cocoker.enums.ResultEnum;
import com.cocoker.service.ExchangeService;
import com.cocoker.service.UserInfoService;
import com.cocoker.utils.IpUtil;
import com.cocoker.utils.MD5;
import com.cocoker.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/16 12:03 PM
 * @Version: 1.0
 */
@Controller
@Slf4j
@RequestMapping("/admin/exchange")
public class AdminExchange {
    @Autowired
    private ProjectUrl projectUrl;
    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private UserInfoService userInfoService;

    private static RestTemplate restTemplate = new RestTemplate();


    @ResponseBody
    @PostMapping("/toexchange")
    public synchronized ResultVO exchangeList(@RequestParam("orderId") Integer orderId, @RequestParam("tixianid")String tixianid, Map<String, Object> hmap) {
        hmap.put("returnUrl", projectUrl.getReturnUrl() + "/cocoker/coc");
        Exchange exchange = exchangeService.findByTId(orderId);
        if (exchange == null) {
            log.info("【提现】 失败 订单不存在 orderId,{}", orderId);
            return ResultVOUtil.error(-1, ResultEnum.ORDER_NOT_EXIST.getMsg());
        }
        if (exchange.getTStatus() != 0) {
            log.info("【提现】 失败 订单已经提现 orderId,{}", orderId);
            return ResultVOUtil.error(-1, "勿重复点击");
        }
        String Url = projectUrl.getZeroMoney();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("lailu", "y");//来源【可选参数】
        map.add("mid", projectUrl.getZeroMoneyUID()); //商户uid
        map.add("jine", exchange.getTMoney());    //金额
        map.add("openid", exchange.getTExchangeOpenid());  //openid
//        Integer m = exchange.getTId() + 1392;
        map.add("tixianid", tixianid);////本地的提现id【要求唯一】字符串类型的数字，最大长度11位数,这里判断订单是否重复,不能用时间戳，跟本地表的id绑定,(不按照要求后果自负)
        map.add("mkey", MD5.md5(projectUrl.getZeroMoneyUID() + exchange.getTMoney() + exchange.getTExchangeOpenid(), projectUrl.getZeroMoneyKey())); //md5
        map.add("lx", "999");

        String result = restTemplate.postForObject(Url, map, String.class);

        ExchangeVO exchangeVO = ExchangeResult2Bean.convert(result);
        if (!exchangeVO.getO().equals("yes")) {
            log.info("【提现】 失败 返回信息 exchangeVO,{}", exchangeVO);
            return ResultVOUtil.error(-1, exchangeVO.getMsg());
        }
        //修改提现订单状态
        exchangeService.updOne(exchange.getTId().toString(),1);
        IpUtil.getIp();
//        log.info("【提现】------- 成功 返回信息 exchangeVO,{}", exchangeVO);
        return ResultVOUtil.success();
    }

    @ResponseBody
    @PostMapping("/noexchange")
    public ResultVO nochangeList(@RequestParam("orderId") Integer orderId, Map<String, Object> hmap) {
        hmap.put("returnUrl", projectUrl.getReturnUrl() + "/cocoker/coc");
        Exchange exchange = exchangeService.findByTId(orderId);
        if (exchange == null) {
            log.info("【拒绝提现】 失败 订单不存在 orderId,{}", orderId);
            return ResultVOUtil.error(-1, ResultEnum.ORDER_NOT_EXIST.getMsg());
        }
        UserInfo userinfo = userInfoService.findByOpenId(exchange.getTOpenid());
        if(userinfo == null){
            log.info("【拒绝提现】 失败 订单用户没找到 exchange,{}", exchange);
            return ResultVOUtil.error(-1, ResultEnum.UPDATE_USER_ERROR.getMsg());
        }
        if (exchange.getTStatus() != 0) {
            log.info("【拒绝提现】 失败 订单已经提现 orderId,{}", orderId);
            return ResultVOUtil.error(-1, "勿重复点击");
        }
        userinfo.setYUsermoney(userinfo.getYUsermoney().add(new BigDecimal(exchange.getTMoney())));
        UserInfo save = userInfoService.save(userinfo);
        if (save == null) {
            log.info("【拒绝提现】 失败 保存余额失败 userinfo,{}", userinfo);
            return ResultVOUtil.error(-1, ResultEnum.UPDATE_USER_ERROR.getMsg());
        }
        //修改提现订单状态2代表拒绝提现
        IpUtil.getIp();
        exchangeService.updOne(exchange.getTId().toString(),2);
        log.info("【拒绝提现】------- 拒绝成功 返回信息 user,{}", save);
        return ResultVOUtil.success();
    }

}
