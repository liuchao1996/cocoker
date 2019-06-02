package com.cocoker.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/5/6 2:05 PM
 * @Version: 1.0
 */
public class DateUtil {

    public static Date getTime(Integer h, Integer m, Integer s) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, s);
        Date time = calendar.getTime();
        return time;
    }
}
