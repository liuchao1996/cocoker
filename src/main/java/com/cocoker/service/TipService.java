package com.cocoker.service;

import com.cocoker.beans.Tip;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/5/5 8:58 AM
 * @Version: 1.0
 */
public interface TipService {
    Tip findFirst();

    int updTip(String msg);
}
