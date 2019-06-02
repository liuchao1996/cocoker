package com.cocoker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/25 6:21 PM
 * @Version: 1.0
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "projecturl")
public class ProjectUrl {

    private String oauth2buildAuthorizationUrl;

    private String returnUrl;

    private String qrUrl;

    private String zeroMoney;

    private String zeroMoneyUID;

    private String zeroMoneyKey;

}
