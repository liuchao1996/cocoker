package com.cocoker.aspect;

import com.cocoker.beans.UserInfo;
import com.cocoker.exception.AdminAuthorizeException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/9 6:50 PM
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class AdminAuthorizeAspect {

    @Pointcut("execution(public * com.cocoker.controller.Admin*.*(..))" +
            "&& !execution(public * com.cocoker.controller.AdminController.login(..))" +
            "&& !execution(public * com.cocoker.controller.AdminController.toLogin(..))"
    )
    public void verify() {
    }

    @Before("verify()")
    public void doVerify() {
        //   ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        log.info("===> 请求者的IP：" + request.getRemoteAddr());
        //如果要获取Session信息的话，可以这样写：
        HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        UserInfo admin = (UserInfo) session.getAttribute("admin");
        if(admin == null){
            log.warn("[登录校验] session中查不到token");
            throw  new AdminAuthorizeException();
        }
        if(admin.getYOtype() != 7){
            log.warn("[登录校验] 权限不是管理员");
            throw  new AdminAuthorizeException();
        }

    }
}
