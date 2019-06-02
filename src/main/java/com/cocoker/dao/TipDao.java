package com.cocoker.dao;

import com.cocoker.beans.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/5/5 8:57 AM
 * @Version: 1.0
 */
public interface TipDao extends JpaRepository<Tip,Integer>{

    @Query(value = "SELECT * FROM tbl_tip LIMIT 1",nativeQuery = true)
    Tip findFirst();
/*
*
* @Modifying
    @Query(value = "UPDATE tbl_order SET ofinal = :ofinal, result = :result, handle= :handle WHERE oid = :oid",nativeQuery = true)
    Integer saveResult(@Param("ofinal")String ofinal,@Param("result")String result,@Param("handle")String handle,@Param("oid")String oid);

*
* */
    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_tip SET tip_msg = :msg WHERE tip_id = 1",nativeQuery = true)
    Integer updTip(@Param("msg") String msg);

}
