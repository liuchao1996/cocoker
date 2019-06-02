package com.cocoker.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/16 2:32 PM
 * @Version: 1.0
 */
@Slf4j
public class IpUtil {

    public static String getIp(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        log.info("==> 请求者的IP：" + request.getRemoteAddr());
        return request.getRemoteAddr();
    }

}
