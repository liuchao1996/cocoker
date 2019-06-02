package com.cocoker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/16 11:00 AM
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDTO {

    private String username;

    private String nickname;

    private String upic;

    private BigDecimal usermoney;

    private String msg;

}
