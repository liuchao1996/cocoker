package com.cocoker.controller;

import com.cocoker.beans.UserInfo;
import com.cocoker.config.ProjectUrl;
import com.cocoker.enums.ResultEnum;
import com.cocoker.exception.CocokerException;
import com.cocoker.service.UserInfoService;
import com.cocoker.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/23 4:44 PM
 * @Version: 1.0
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ProjectUrl projectUrl;



    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, @RequestParam(value = "upOpenid", required = false) String upOpenid) {
        String url = projectUrl.getOauth2buildAuthorizationUrl() + "/cocoker/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(projectUrl.getReturnUrl() + "/cocoker/coc&upOpenid=" + upOpenid));
//        log.info("[微信网页授权] 获取code, redirectUrl = {}",redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl) {
        String[] urls = returnUrl.split("&");
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        WxMpUser wxMpUser = new WxMpUser();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            log.error("[微信网页授权] {}", e);
            throw new CocokerException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();

        UserInfo u = userInfoService.findByOpenId(openId);
        if (u == null) {
            userInfoService.save(wxMpUser, urls[1].substring(9));
        }
        return "redirect:" + urls[0] + "?openid=" + openId;
    }
}
