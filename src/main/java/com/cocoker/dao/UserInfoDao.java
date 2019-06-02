package com.cocoker.dao;

import com.cocoker.beans.UserInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/23 4:11 PM
 * @Version: 1.0
 */
public interface UserInfoDao extends JpaRepository<UserInfo,Integer>{
//    UserInfo findByOpenid(String openid);
    UserInfo findByYOpenid(String openid);

    List<UserInfo> findByYOid(String openid);

    List<UserInfo> findByYOidIn(List<String> openid);

    List<UserInfo> findByYOpenidIn(List<String> openid);

    @Query(value = "select * from tbl_userinfo WHERE y_username = :username and y_upwd = :pwd",nativeQuery = true)
    UserInfo login(@Param("username") String username,@Param("pwd") String pwd);

    @Query(value = "SELECT SUM(y_usermoney) as money FROM tbl_userinfo",nativeQuery = true)
    String findAllMoney();

    @Query(value = "SELECT SUM(tmoney) as money FROM tbl_recharge WHERE tstatus = '1' AND TO_DAYS(create_time) =TO_DAYS(NOW())",nativeQuery = true)
    String findDayAllMoney();

    @Query(value = "SELECT COUNT(*) a from tbl_userinfo",nativeQuery = true)
    String allUserCount();


    //根据nickname查找
    List<UserInfo> findByYNicknameLike(String str);

}
