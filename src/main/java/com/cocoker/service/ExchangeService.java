package com.cocoker.service;

import com.cocoker.beans.Exchange;
import com.cocoker.beans.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/11 4:18 PM
 * @Version: 1.0
 */
public interface ExchangeService {

    Exchange saveOne(String openid,String money,String exopenid);

    Exchange updOne(String id,Integer code);

    List<Exchange>  findByTopenid(@Param("openid") String openid);

    Page<Exchange> findByTStatus(Integer status, Pageable pageable);

    String findDayExchangeAllMoney();

    Exchange findByTId(Integer id);
}
