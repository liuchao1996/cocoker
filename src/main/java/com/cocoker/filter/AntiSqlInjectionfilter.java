package com.cocoker.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.LogRecord;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/14 10:43 AM
 * @Version: 1.0
 */
public class AntiSqlInjectionfilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //获得所有请求参数名
        Enumeration params = servletRequest.getParameterNames();
        String sql = "";
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
            //System.out.println("name===========================" + name + "--");
            //得到参数对应值
            String[] value = servletRequest.getParameterValues(name);
            for (int i = 0; i < value.length; i++) {
                sql = sql + value[i];
            }
        }
        //System.out.println("============================SQL"+sql);
        //有sql关键字，跳转到error.html
        if (sqlValidate(sql)) {
            throw new IOException("您发送请求中的参数中含有非法字符");
            //String ip = req.getRemoteAddr();
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
//    text: "http://192.168.10.101/cocoker/wechat/authorize?upOpenid=" + getCookie('openid') +  "&returnUrl=" + encodeURIComponent("http://5vp731.cn/cocoker/coc")
//oNHRR0b8geefswKLzBYaqWujw8Bg
    //效验
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|count|" +
                "mid|master|truncate|char|declare|like";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
