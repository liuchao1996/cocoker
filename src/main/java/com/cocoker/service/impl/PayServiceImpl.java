package com.cocoker.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cocoker.VO.PayReturnParamVO;
import com.cocoker.beans.Recharge;
import com.cocoker.config.WechatAccountConfig;
import com.cocoker.converter.PayResult2Bean;
import com.cocoker.dao.RechargeDao;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.cocoker.service.PayService;
import com.cocoker.utils.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/4 3:50 PM
 * @Version: 1.0
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private RechargeDao rechargeDao;

    @Autowired
    private WechatAccountConfig accountConfig;

    private static final DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public synchronized PayReturnParamVO create(String openid, String money) {
        String mid = accountConfig.getCustomeridY();
        String key = accountConfig.getCustomerKey();
        String payUrl = accountConfig.getPayUrl();
        String orderId = KeyUtil.genUniqueKey();
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("merchant_no", mid);
        map.add("version", "1.0");
        map.add("payment_type", "WxPay-JSPay");
        map.add("merchant_data", "mylc");
        map.add("merchant_order_num", orderId);
        String m = df.format(Double.valueOf(money));
        Double d = Double.valueOf(df.format(Double.valueOf(m) * 6.79));
        map.add("amount", d.toString());
        map.add("notify_url", accountConfig.getNotifyurlY());
        map.add("return_url", accountConfig.getReturnurlY());
        String flag = "amount=" + d.toString() + "&merchant_no=" + mid + "&merchant_order_num=" + orderId + "&notify_url=" + accountConfig.getNotifyurlY() + "&payment_type=WxPay-JSPay&return_url=" + accountConfig.getReturnurlY() + "&version=1.0&key=" + key;
        map.add("sign", MD5.md5(flag, ""));
        String result = restTemplate.postForObject(payUrl, map, String.class);
        PayReturnParamVO prp = PayResult2Bean.convert(result);
        if (prp.getCode() != 200) {
            log.error("[创建支付链接] 失败");
            throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
        }
        Recharge recharge = new Recharge();
        recharge.setTopenid(openid);
//        recharge.setTyue();
        recharge.setTorderid(orderId);
        recharge.setTstatus(0);
        recharge.setTsdpayno(prp.getResult().getOrder_num());
        recharge.setCreateTime(new Date());
        recharge.setTmoney(new BigDecimal(money));
        rechargeDao.save(recharge);
        return prp;
    }


//    @Override
//    public synchronized PayReturnParamVO create(String openid, String money) {
////               String payUrl = "http://api.mycsc.com.cn/pay";
////               String payUrl = "http://rhyme.nat300.top/pay/topay";
//               String payUrl = "http://wegyoa.top/pay/topay";
//        String orderId = KeyUtil.genUniqueKey();
//        RestTemplate restTemplate = new RestTemplate();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
//        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
//        map.add("xid", 10031);
//        map.add("xddh", orderId);
//        map.add("xremakes", "remakes");
//        map.add("xmoney", money);
//        map.add("xnotifyurl", "http://rhyme.nat300.top/cocoker/transaction/notifyurl");
//        map.add("xreturnurl", "http://rhyme.nat300.top/cocoker/transaction/returnurl");
//        map.add("xpaytype", "wxgzh");
//        map.add("xip", request.getRemoteAddr());
//        //签名【md5(商务号+商户订单号+支付金额+异步通知地址+商户秘钥)】
//        String flag = "10031" + orderId + money + "http://rhyme.nat300.top/cocoker/transaction/notifyurl" + "QuaPDljGsKWXhScMgJf8";
//        map.add("xsign", MD5.md5(flag,""));
//
//        String result = restTemplate.postForObject(payUrl, map, String.class);
//        PayReturnParamVO prp = PayResult2Bean.convert(result);
//        if (prp == null || prp.getStatus().equals("0")) {
//            log.error("[创建支付链接] 失败");
//            throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
//        }
//        Recharge recharge = new Recharge();
//        recharge.setTopenid(openid);
////        recharge.setTyue();
//        recharge.setTorderid(orderId);
//        recharge.setTstatus(0);
//        recharge.setCreateTime(new Date());
//        recharge.setTmoney(new BigDecimal(money));
//        rechargeDao.save(recharge);
//        return prp;
//    }


    //zf
//    @Override
//    public synchronized PayReturnParamVO create(String openid, String money) {
//        String payUrl = "http://api.mycsc.com.cn/pay";
//        String orderId = KeyUtil.genUniqueKey();
//        RestTemplate restTemplate = new RestTemplate();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.TEXT_HTML);
////        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
//        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
//        map.add("fxid", 2019171);
//        map.add("fxddh", orderId);
//        map.add("fxdesc", "订单名称");
//        map.add("fxfee", money);
//        map.add("fxnotifyurl", accountConfig.getNotifyurlY());
//        map.add("fxbackurl", accountConfig.getReturnurlY());
//        map.add("fxpay", "wxgzh");
//        map.add("fxip", request.getRemoteAddr());
//        //签名【md5(商务号+商户订单号+支付金额+异步通知地址+商户秘钥)】
//        String flag = "2019171" + orderId + money + accountConfig.getNotifyurlY() + "xokwbaGmZQeJjxZeziRJxLAHvgINFBUZ";
//        map.add("fxsign", MD5.md5(flag,""));
//
//        String result = restTemplate.postForObject(payUrl, map, String.class);
//        PayReturnParamVO prp = PayResult2Bean.convert(result);
//        if (prp == null || prp.getStatus().equals("0")) {
//            log.error("[创建支付链接] 失败");
//            throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
//        }
//        Recharge recharge = new Recharge();
//        recharge.setTopenid(openid);
////        recharge.setTyue();
//        recharge.setTorderid(orderId);
//        recharge.setTstatus(0);
//        recharge.setCreateTime(new Date());
//        recharge.setTmoney(new BigDecimal(money));
//        rechargeDao.save(recharge);
//        return prp;
//    }
//
//
//    @Override
//    public synchronized PayReturnParamVO create(String openid, String money) {
//        String orderId = KeyUtil.genUniqueKey();
//        RestTemplate restTemplate = new RestTemplate();
//        Map<String, String> map = new HashMap<>();
//        map.put("customerid", accountConfig.getCustomeridY().toString());
////        map.put("total_fee", Integer.parseInt(money) * 100 + "");
//        Integer m = Integer.valueOf(money) * 100;
//        map.put("total_fee", m.toString());
//        map.put("sdorderno", orderId);
//        map.put("notifyurl", accountConfig.getNotifyurlY());
//        map.put("returnurl", accountConfig.getReturnurlY());
////        map.put("sign", MD5.md5("customerid=100045&total_fee=" + Integer.parseInt(money) * 100 + "" + "&sdorderno=" + orderId + "&51imQxuLMHEl9kCpmP4ToLQkEF9ugA7b",""));
//        map.put("sign", MD5.md5("customerid=" + accountConfig.getCustomeridY().toString() + "&total_fee=" + m.toString() + "&sdorderno=" + orderId + "&" + accountConfig.getCustomerKey().toString(), ""));
//        String result = restTemplate.postForObject(accountConfig.getPayUrl(), map, String.class);
//        PayReturnParamVO prp = PayResult2Bean.convert(result);
//        if (prp == null || prp.getCode().equals("0")) {
//            log.error("[创建支付链接] 失败");
//            throw new CocokerException(ResultEnum.CREATE_ORDER_ERROR);
//        }
//        Recharge recharge = new Recharge();
//        recharge.setTopenid(openid);
////        recharge.setTyue();
//        recharge.setTorderid(orderId);
//        recharge.setTstatus(0);
//        recharge.setCreateTime(new Date());
//        recharge.setTmoney(new BigDecimal(money));
//        rechargeDao.save(recharge);
//        return prp;
//    }
}
