package com.cocoker.service;

import com.cocoker.beans.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/29 7:24 PM
 * @Version: 1.0
 */
public interface OrderService {
    List<Order> findAllOrder(String openid);

    void addOrder(String flag,String index,String money,String openid,String currentDate);

    List<Order> getByResult(String str);

    Page<Order> findListPage(Pageable pageable);

    Integer saveByAll(String openid, String direction, String oindex, String ofinal, BigDecimal money, String result, String createTime, String handle, String onickname,String opic
    );
    Order findLastOrderByOpenid(String openid);

    Order findByOid(Integer oid);

    List<Order> findOrder20();

    Integer saveResult(String ofinal,String result,String handle,String oid);

}
