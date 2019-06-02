package com.cocoker.config;

import com.cocoker.filter.AntiSqlInjectionfilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/14 10:50 AM
 * @Version: 1.0
 */
@Configuration
public class MyServerConfig {
    @Bean
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AntiSqlInjectionfilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*"));
        return registrationBean;
    }
}
