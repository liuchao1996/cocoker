package com.cocoker.utils;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/20 3:03 PM
 * @Version: 1.0
 */
public class MathUtil {
    private static final Double MONEY_RANGE = 0.01;
    public static Boolean equals(Double d1,Double d2){
        double abs = Math.abs(d1 - d2);
        if(abs < MONEY_RANGE){
            return true;
        }else{
            return false;
        }
    }
}
