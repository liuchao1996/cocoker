package com.cocoker.utils;

import java.util.Random;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/13 11:00 AM
 * @Version: 1.0
 */
public class KeyUtil {

    /**
     * 生成唯一主键
     * 格式： 时间加随机数
     *
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
            Integer number = random.nextInt(900000000) + 1000000000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
