package com.cocoker.utils;

import java.util.Random;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/26 8:36 PM
 * @Version: 1.0
 */
public class RandomUtil {
    public static Integer num = 7950;

    public static Double getRandomBetween(Integer min, Integer max) {
        Random r = new Random();
//        Integer i = r.nextInt(max - min + 1) + min;
//        return i;
        if (r.nextInt(10) % 2 == 0) {
            num += r.nextInt(7);
        } else {
            num -= r.nextInt(7);
        }
        if (num < 7900) {
            num = 7902;
        }
        if (num > 7999) {
            num = 7997;
        }
        //6.79
        return Double.valueOf("6." + num);
    }

    public static Integer getRandomBet(Integer min, Integer max) {
        Random r = new Random();
        Integer i = r.nextInt(max - min + 1) + min;
        return i;
    }


    public static Double getRandomBetD(Integer min, Integer max) {
        Random r = new Random();
        Integer i = r.nextInt(max - min + 1) + min;
        return Double.valueOf("0.000" + i);
    }

    public static String getZD(Integer num) {
        Random r = new Random();
        if (num == 5) {
            if (r.nextInt(100) < 30) {
                return "盈";
            } else {
                return "亏";
            }
        }


        if (num <= 10) {
            if (r.nextInt(100) < 20) {
                return "盈";
            } else {
                return "亏";
            }
        }

        if (num <= 20) {
            if (r.nextInt(100) < 1) {
                return "盈";
            } else {
                return "亏";
            }
        }

//        if(num<=30){
//            if(r.nextInt(100)<15){
//                return "盈";
//            }else{
//                return "亏";
//            }
//        }
//        if(num<=100){
//            if(r.nextInt(100)<1){
//                return "盈";
//            }else{
//                return "亏";
//            }
//        }
//        if(num<=500){
//            if(r.nextInt(100)<0){
//                return "盈";
//            }else{
//                return "亏";
//            }
//        }
//        if(num<=1000){
//            if(r.nextInt(100)<10){
//                return "盈";
//            }else{
//                return "亏";
//            }
//        }
//        if(num<=2000){
//            if(r.nextInt(100)<1){
//                return "盈";
//            }else{
//                return "亏";
//            }
//        }
        return "亏";

    }
}
