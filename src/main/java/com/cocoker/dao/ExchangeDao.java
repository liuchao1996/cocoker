package com.cocoker.dao;

import com.cocoker.beans.Exchange;
import com.cocoker.beans.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/11 4:17 PM
 * @Version: 1.0
 */
public interface ExchangeDao extends JpaRepository<Exchange,Integer>{

    @Query(value = "select * from tbl_exchange where t_openid = :openid ORDER by t_id desc",nativeQuery = true)
    List<Exchange> findByTopenid(@Param("openid") String openid);

    Page<Exchange> findByTStatus(Integer status, Pageable pageable);

    Exchange findByTId(Integer orderId);

    //今日提现
    @Query(value = "SELECT SUM(t_money) as money FROM tbl_exchange WHERE t_status = '1' AND TO_DAYS(create_time) =TO_DAYS(NOW())",nativeQuery = true)
    String findDayExchangeAllMoney();
}
