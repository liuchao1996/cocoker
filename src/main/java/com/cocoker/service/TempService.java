package com.cocoker.service;

import com.cocoker.beans.Temp;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/7 6:54 PM
 * @Version: 1.0
 */
public interface TempService {
    List<Temp> findByTOpenid(String openid);

    Temp save(Temp temp);

    List<Temp> findByTOpenidAndTime(String openid);

    int delTemp(List<Integer> ids);
}
