package com.cocoker.dao;

import com.cocoker.beans.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.SortDefault;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/4 12:15 PM
 * @Version: 1.0
 */
public interface RechargeDao extends JpaRepository<Recharge,Integer>{
    Recharge findByTorderid(String openid);

    Page<Recharge> findByTstatus(Integer status,Pageable pageable);

    @Query(value = "select * from tbl_recharge where topenid = :openid ORDER by tid desc",nativeQuery = true)
    List<Recharge> findByTopenid(@Param("openid") String openid);
}
