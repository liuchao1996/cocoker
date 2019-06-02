package com.cocoker.utils;

public class Random2Util {

  private static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  private static final java.util.Random RANDOM = new java.util.Random();

  public static String getRandomStr(Integer len) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(RANDOM_STR.charAt(RANDOM.nextInt(RANDOM_STR.length())));
    }
    return sb.toString();
  }

}
