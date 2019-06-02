package com.cocoker.VO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/26 8:09 PM
 * @Version: 1.0
 */
@Data
public class EchartsData <T>{
    List<T> data = new ArrayList<>();
}
