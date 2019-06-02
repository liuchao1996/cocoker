package com.cocoker.dao;

import com.cocoker.beans.Echarts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/26 9:10 PM
 * @Version: 1.0
 */
public interface EchartsDao extends JpaRepository<Echarts,Integer>{


    @Query(value = "select * from tbl_echarts WHERE create_time < CONCAT((SELECT DATE_SUB(CURTIME(), interval 0 SECOND)))  ORDER BY eid desc LIMIT 100",nativeQuery = true)
    List<Echarts> find100();

    @Query(value = "select * from tbl_echarts WHERE create_time = CONCAT((SELECT DATE_SUB(CURTIME(), interval 0 SECOND))) ",nativeQuery = true)
    String getCurrentData();

    @Query(value = "select * from tbl_echarts WHERE create_time < CONCAT((SELECT DATE_SUB(CURTIME(), interval 0 SECOND)))  ORDER BY eid desc LIMIT 1",nativeQuery = true)
    Echarts find1();

    Echarts findByCreateTime(String time);

    @Query(value = "UPDATE tbl_echarts set price = :price WHERE create_time = :createTime",nativeQuery = true)
    @Modifying
    Integer updPriceByTime(@Param("price") String price,@Param("createTime")String createTime);


    @Query(value = "TRUNCATE tbl_echarts",nativeQuery = true)
    @Modifying
    Integer cleanTable();

}
