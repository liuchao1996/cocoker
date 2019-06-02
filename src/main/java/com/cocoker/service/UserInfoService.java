package com.cocoker.service;

import com.cocoker.beans.UserInfo;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/24 8:07 PM
 * @Version: 1.0
 */
public interface UserInfoService {
    UserInfo findOne(Integer id);

    UserInfo save(String openid, String num, String flag, String index,String currentDate);

    UserInfo save(WxMpUser wxMpUser,String oid);

    UserInfo save(UserInfo userInfo);

    Page<UserInfo> findList(Pageable pageable);

    boolean delOne(Integer id);

    UserInfo login(String username,String pwd);

    UserInfo findByOpenId(String openid);

    Page<UserInfo> findWithPage(Pageable pageable);

    List<UserInfo> findByOid(String openid);

    List<UserInfo> findByOidIn(List<String> openid);

    List<UserInfo> findByOpenidIn(List<String> openid);

    String findAllMoney();

    String findDayAllMoney();

    String allUserCount();

    List<UserInfo> findByNicknameLike(String str);
}
