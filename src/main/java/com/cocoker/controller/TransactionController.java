package com.cocoker.controller;

import com.cocoker.VO.ExchangeVO;
import com.cocoker.VO.PayReturnParamVO;
import com.cocoker.VO.ResultVO;
import com.cocoker.beans.*;
import com.cocoker.config.ProjectUrl;
import com.cocoker.config.WechatAccountConfig;
import com.cocoker.converter.ExchangeResult2Bean;
import com.cocoker.converter.Notify2Bean;
import com.cocoker.converter.PayResult2Bean;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.cocoker.service.*;
import com.cocoker.utils.*;
import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.rest.type.Get;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.OutputBuffer;
import org.apache.catalina.connector.Response;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/3 3:45 PM
 * @Version: 1.0
 */
@RequestMapping("/transaction")
@Controller
@Slf4j
public class TransactionController {
    @Autowired
    private PayService payService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProjectUrl projectUrl;

    private static RestTemplate restTemplate = new RestTemplate();
    /**
     * 充值
     */
    @Autowired
    private RechargeService rechargeService;

    @PostMapping("/recharge")
    @ResponseBody
    public ModelAndView recharge(@RequestParam("openid") String openid, @RequestParam("money") String moeny, Map<String, Object> map) {
        PayReturnParamVO result = payService.create(openid, moeny);
        map.put("payUrl", result.getResult().getPay_url());
        return new ModelAndView("pay/create", map);
//        return result;
    }


    /**
     * 充值异步通知
     *
     * @return
     */
    @PostMapping(value = "/notifyurl")
//    public synchronized ModelAndView notifyurl(@RequestBody String s, HttpServletResponse response) throws UnsupportedEncodingException {
    public synchronized void notifyurl(PayNotifyBean bean, HttpServletResponse response, HttpServletRequest request) {
//        String decode = URLDecoder.decode(str, "UTF-8");
//        PayNotifyBean bean = Notify2Bean.convert(decode.substring(0, decode.lastIndexOf("=")));
        Recharge recharge = rechargeService.findRechargeByTopenid(bean.getMerchant_order_num());
        if (recharge == null) {
            log.error("[异步通知] 订单查询不到, bean {}", bean);
            throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
        }
        if (recharge.getTstatus() == 0) {
            UserInfo userInfo = userInfoService.findByOpenId(recharge.getTopenid());
//            if (!MathUtil.equals(recharge.getTmoney().doubleValue() , Double.valueOf(bean.getAmount()))) {
            if (Double.doubleToLongBits(Double.valueOf(recharge.getTmoney().toString()) * 6.79) != Double.doubleToLongBits(Double.valueOf(bean.getAmount()))) {
//            if (Double.doubleToLongBits(Double.valueOf(recharge.getTmoney().toString()) * 6.79) != Double.doubleToLongBits(Double.valueOf(bean.getTotal_fee()))) {
                log.error("[异步通知] 金额异常, recharge {} bean {}", recharge, bean);
                throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
            }
            userInfo.setYUsermoney(userInfo.getYUsermoney().add(new BigDecimal(Double.valueOf(bean.getAmount()) / 6.79)));
//            userInfo.setYUsermoney(userInfo.getYUsermoney().add(new BigDecimal((Double.valueOf(bean.getTotal_fee()) / 100) / 6.79)));
            UserInfo save = userInfoService.save(userInfo);
            recharge.setTstatus(1);
            recharge.setTyue(userInfo.getYUsermoney());
            recharge.setTsdpayno(bean.getOrder_num());
            recharge.setTsdorderno(bean.getMerchant_order_num());
            recharge.setTnickname(userInfo.getYNickname());
            Recharge result = rechargeService.save(recharge);
            if (result == null) {
                log.error("[异步通知] 更新订单信息失败, recharge {}", recharge);
                throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
            }
            if (save == null) {
                log.error("[异步通知] 更新余额失败, userInfo {}", userInfo);
                throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
            }
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write("success");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[异步通知] 通知商户回调失败, e {}", e);
            throw new CocokerException(ResultEnum.NOTICE_MERCHANT_ERROT);
        }
//        SpringMVCUtil.render("text/html","success",new String []{"encoding:utf-8"});
//        return new ModelAndView("redirect:".concat(projectUrl.getReturnUrl()).concat("/cocoker/coc"));
    }

    /**
     * 充值回调
     *
     * @return
     */
    @GetMapping("/returnurl")
    public ModelAndView returnurl() {
//        return new ModelAndView("redirect:http://rhyme.nat300.top/cocoker/coc");
        return new ModelAndView("redirect:".concat(projectUrl.getReturnUrl()).concat("/cocoker/coc"));

    }


    @GetMapping("/exchange")
    @Transactional
    @ResponseBody
    public synchronized ModelAndView exchange(@RequestParam("openid") String openid, @RequestParam("u") String u, Map<String, Object> hmap) {
        hmap.put("returnUrl", projectUrl.getReturnUrl() + "/cocoker/coc");

        String[] split = u.split("-----");
        String wxOpenid = split[1];
        String money = split[0];
        UserInfo user = userInfoService.findByOpenId(wxOpenid);

        //查订单
//        List<Order> allOrder = orderService.findAllOrder(wxOpenid);
//        if(null == allOrder || allOrder.size() == 0){
//            log.error("【提现】 没有订单 wxOpenid,{}", wxOpenid);
//            hmap.put("msg",",获取用户信息失败!");
//            return new ModelAndView("pay/error", hmap);
//        }

        if (user == null) {
            log.error("【提现】 找不到用户 wxOpenid,{}", wxOpenid);
            return new ModelAndView("pay/error", hmap);
        }
        //小于3大于1000禁用
        if (Double.valueOf(money) < 1 || Double.valueOf(money) >= 2000) {
            user.setYUstatus(2);
            userInfoService.save(user);
            log.error("【提现】 小于1大于2000禁用 , user: {}", user);
            return new ModelAndView("pay/error", hmap);
        }

        if (user.getYUstatus() != 0) {
            log.error("【提现】 状态不对 user,{}", user);
            return new ModelAndView("pay/error", hmap);
        }
        if (user.getYUsermoney().doubleValue() < Double.valueOf(money)) {
            log.error("【提现】 金额不足 user,{}", user);
            return new ModelAndView("pay/error", hmap);
        }

        Exchange exchange = exchangeService.saveOne(wxOpenid, money, openid);
        if (exchange == null) {
            log.error("【提现】 创建订单失败 exchange,{}", exchange);
            return new ModelAndView("pay/error", hmap);
        }


//        log.info("【提现】------- 提现信息提交 exchange,{}", exchange);
//1        return new ModelAndView("pay/success", hmap);

        String Url = projectUrl.getZeroMoney();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("lailu", "rhyme");//来源【可选参数】
        map.add("mid", projectUrl.getZeroMoneyUID()); //商户uid

        map.add("jine", Double.valueOf(money) * 6.3);    //金额
        map.add("openid", openid);  //openid
        Integer m = 1555162539 + exchange.getTId();
        map.add("tixianid", m.toString());////本地的提现id【要求唯一】字符串类型的数字，最大长度11位数,这里判断订单是否重复,不能用时间戳，跟本地表的id绑定,(不按照要求后果自负)
        map.add("mkey", MD5.md5(projectUrl.getZeroMoneyUID() + (Double.valueOf(money.toString()) * 6.3) + exchange.getTExchangeOpenid(), projectUrl.getZeroMoneyKey())); //md5
        map.add("lx", "999");

        String result = restTemplate.postForObject(Url, map, String.class);

        ExchangeVO exchangeVO = ExchangeResult2Bean.convert(result);
        if (!exchangeVO.getO().equals("yes")) {
            log.info("【提现】 失败 提现信息 result,{}", exchangeVO);
            return new ModelAndView("pay/error", hmap);
        }
        //修改提现订单状态
        exchangeService.updOne(exchange.getTId().toString(), 1);

        IpUtil.getIp();
        //修改用户余额
        user.setYUsermoney(user.getYUsermoney().subtract(new BigDecimal(money)));
        UserInfo save = userInfoService.save(user);
        if (save == null) {
            log.error("【提现】 user 保存失败 user,{}", user);
            return new ModelAndView("pay/error", hmap);
        }

        return new ModelAndView("pay/success", hmap);


    }

}
