//package com.cocoker.aspect;
//
//import com.cocoker.utils.IllegalStrFilterUtil;
//import com.cocoker.utils.getFieldsValue;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @Description:
// * @Author: y
// * @CreateDate: 2019/1/14 10:00 AM
// * @Version: 1.0
// */
//@Component
//@Aspect
//@Slf4j
//public class CheckInputParameterAspect {
//
//    // 存在SQL注入风险
//    private static final String IS_SQL_INJECTION = "输入参数存在SQL注入风险";
//
//    private static final String UNVALIDATED_INPUT = "输入参数含有非法字符";
//
//    private static final String ERORR_INPUT = "输入的参数非法";
//
//    /**
//     * 定义切入点:拦截controller层所有方法
//     */
////    @Pointcut("execution(public * com.cocoker.controller.Admin*.*(..))" +
//    @Pointcut("execution(public * com.cocoker.controller.*.*(..))")
//    public void params() {
//    }
//
//    /**
//     * 定义环绕通知
//     *
//     * @param joinPoint
//     * @throws Throwable
//     */
//    @Around("params()")
//    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        Object[] args = joinPoint.getArgs();// 参数
//        String str = String.valueOf(args);
//        if (!IllegalStrFilterUtil.sqlStrFilter(str)) {
//            log.info(IS_SQL_INJECTION);
//            log.info("当前调用接口-[" + request.getRequestURL() + "]");
//            new RuntimeException(ERORR_INPUT);
//        }
//        if (!IllegalStrFilterUtil.isIllegalStr(str)) {
//            log.info(UNVALIDATED_INPUT);
//            log.info("当前调用接口-[" + request.getRequestURL() + "]");
//            new RuntimeException(ERORR_INPUT);
//        }
//        Object result = joinPoint.proceed();
////        log.info("当前调用接口-[" + request.getRequestURL() + "]");
//        return result;
//    }
//
//}
