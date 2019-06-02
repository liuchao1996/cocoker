package com.cocoker.dao;

import com.cocoker.beans.Temp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/7 6:53 PM
 * @Version: 1.0
 */
public interface TempDao extends JpaRepository<Temp,Integer>{

    @Modifying
    int deleteByTIdIn(List<Integer> ids);


    List<Temp> findByTOpenid(String openid);

    @Query(value = "select * from tbl_temp where t_openid = :openid and create_time < CONCAT((SELECT DATE_SUB(NOW(), interval 30 SECOND)))",nativeQuery = true)
    List<Temp> findByTOpenidAndTime(@Param("openid") String openid);

}





