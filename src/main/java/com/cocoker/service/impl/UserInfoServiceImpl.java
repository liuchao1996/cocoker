package com.cocoker.service.impl;

import com.cocoker.beans.Commission;
import com.cocoker.beans.UserInfo;
import com.cocoker.dao.UserInfoDao;
import com.cocoker.enums.ResultEnum;
import com.cocoker.enums.UserStatusEnum;
import com.cocoker.exception.CocokerException;
import com.cocoker.service.CommissionService;
import com.cocoker.service.OrderService;
import com.cocoker.service.UserInfoService;
import com.cocoker.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/24 8:11 PM
 * @Version: 1.0
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommissionService commissionService;

    @Override
    public UserInfo findOne(Integer id) {
        return userInfoDao.findById(id).get();
    }

    @Override
    @Transactional
    public UserInfo save(String openid, String num, String flag, String index, String currentDate) {
        if (openid == null || num == null || flag == null) {
            throw new CocokerException(ResultEnum.PARAM_ERROR);
        }
        UserInfo userInfo = userInfoDao.findByYOpenid(openid);
        if (userInfo == null) {
            throw new CocokerException(ResultEnum.USER_NOT_EXIST);
        }
        if (Double.valueOf(index) < 6.7 || Double.valueOf(index) > 6.8 || Integer.valueOf(num) < 1) {
            userInfo.setYUstatus(2);
            userInfoDao.save(userInfo);
            log.error("[< 提交订单信息异常----++++++++++++++_____+++++],index: {},money : {}", index, num);
            return null;
        }
        if (userInfo.getYUstatus() != UserStatusEnum.DEFAULT_USER_STATUS.getCode()) {
            throw new CocokerException(ResultEnum.USER_STATUS_ERROR);
        }
        if (userInfo.getYUsermoney().doubleValue() + 0.01 < Double.valueOf(num)) {
//        if (userInfo.getYUsermoney().doubleValue() + 0.01 < Double.valueOf(num) * 1.1) {
            throw new CocokerException(ResultEnum.MONEY_ERROR);
        }
        userInfo.setYUsermoney(userInfo.getYUsermoney().subtract(new BigDecimal(num).multiply(new BigDecimal(1))).setScale(2, BigDecimal.ROUND_HALF_UP));
//        userInfo.setYUsermoney(userInfo.getYUsermoney().subtract(new BigDecimal(num).multiply(new BigDecimal(1.1))).setScale(2, BigDecimal.ROUND_HALF_UP));
        UserInfo save = userInfoDao.save(userInfo);
        if (save == null) {
            throw new CocokerException(ResultEnum.MONEY_ERROR);
        }
        //下单
        orderService.addOrder(flag, index, num, openid, currentDate);


        //给代理加余额
        //one
        UserInfo one = userInfoDao.findByYOpenid(userInfo.getYOid());
        if (one != null) {
            //5%,3%,2%,1%,1%,0.5%,0.5  --13
            one.setYUsermoney(one.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.13) * 0.385)));
            userInfoDao.save(one);
            Commission c1 = new Commission();
            c1.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.385));
//            c1.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.3));
            c1.setCOpenid(one.getYOpenid());
            c1.setCreateTime(new Date());
            commissionService.save(c1);

            //two
            UserInfo tow = userInfoDao.findByYOpenid(one.getYOid());
            if (tow != null) {
                //3/13
                tow.setYUsermoney(tow.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.13) * 0.231)));
                userInfoDao.save(tow);
                Commission c2 = new Commission();
                c2.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.231));
//                c2.setCMoney(new BigDecimal((Double.valueOf(num) * 0.2) * 0.15));
                c2.setCOpenid(tow.getYOpenid());
                c2.setCreateTime(new Date());
                commissionService.save(c2);
                //three
                UserInfo three = userInfoDao.findByYOpenid(tow.getYOid());
                if (three != null) {
                    //2/13
                    three.setYUsermoney(three.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.13) * 0.154)));
                    userInfoDao.save(three);
                    Commission c3 = new Commission();
                    c3.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.154));
                    c3.setCOpenid(three.getYOpenid());
                    c3.setCreateTime(new Date());
                    commissionService.save(c3);
                    //four
                    UserInfo four = userInfoDao.findByYOpenid(three.getYOid());
                    if (four != null) {
                        //1/13
                        four.setYUsermoney(four.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.13) * 0.077)));
                        userInfoDao.save(four);
                        Commission c4 = new Commission();
                        c4.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.077));
                        c4.setCOpenid(four.getYOpenid());
                        c4.setCreateTime(new Date());
                        commissionService.save(c4);
                        //five
                        UserInfo five = userInfoDao.findByYOpenid(four.getYOid());
                        if (five != null) {
                            //1/13
                            five.setYUsermoney(five.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.13) * 0.077)));
                            userInfoDao.save(five);
                            Commission c5 = new Commission();
                            c5.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.077));
                            c5.setCOpenid(five.getYOpenid());
                            c5.setCreateTime(new Date());
                            commissionService.save(c5);
                            //six
                            UserInfo six = userInfoDao.findByYOpenid(five.getYOid());
                            if (six != null) {
                                //0,5/13
                                six.setYUsermoney(six.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.13) * 0.039)));
                                userInfoDao.save(six);
                                Commission c6 = new Commission();
                                c6.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.039));
                                c6.setCOpenid(six.getYOpenid());
                                c6.setCreateTime(new Date());
                                commissionService.save(c6);
                                //seven
                                UserInfo seven = userInfoDao.findByYOpenid(six.getYOid());
                                if (seven != null) {
                                    //0.5/13
                                    seven.setYUsermoney(seven.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.13) * 0.039)));
                                    userInfoDao.save(seven);
                                    Commission c7 = new Commission();
                                    c7.setCMoney(new BigDecimal((Double.valueOf(num) * 0.13) * 0.039));
                                    c7.setCOpenid(seven.getYOpenid());
                                    c7.setCreateTime(new Date());
                                    commissionService.save(c7);
                                    //eight
//                                    UserInfo eight = userInfoDao.findByYOpenid(seven.getYOid());
//                                    if (eight != null) {
//                                        //1/20
//                                        eight.setYUsermoney(eight.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.2) * 0.05)));
//                                        userInfoDao.save(eight);
//                                        Commission c8 = new Commission();
//                                        c8.setCMoney(new BigDecimal((Double.valueOf(num) * 0.2) * 0.05));
//                                        c8.setCOpenid(eight.getYOpenid());
//                                        c8.setCreateTime(new Date());
//                                        commissionService.save(c8);
//                                        //nine
//                                        UserInfo nine = userInfoDao.findByYOpenid(eight.getYOid());
//                                        if (nine != null) {
//                                            //1/20
//                                            nine.setYUsermoney(nine.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.2) * 0.01)));
//                                            userInfoDao.save(nine);
//                                            Commission c9 = new Commission();
//                                            c9.setCMoney(new BigDecimal((Double.valueOf(num) * 0.2) * 0.01));
//                                            c9.setCOpenid(nine.getYOpenid());
//                                            c9.setCreateTime(new Date());
//                                            commissionService.save(c9);
//                                            //ten
//                                            UserInfo ten = userInfoDao.findByYOpenid(nine.getYOid());
//                                            if (ten != null) {
//                                                //1/20
//                                                ten.setYUsermoney(ten.getYUsermoney().add(new BigDecimal((Double.valueOf(num) * 0.2) * 0.01)));
//                                                userInfoDao.save(ten);
//                                                Commission c10 = new Commission();
//                                                c10.setCMoney(new BigDecimal((Double.valueOf(num) * 0.2) * 0.01));
//                                                c10.setCOpenid(ten.getYOpenid());
//                                                c10.setCreateTime(new Date());
//                                                commissionService.save(c10);
//                                            }
//                                        }
//                                    }
                                }
                            }
                        }
                    }
                }
            }

        }


        return save;
    }

    @Override
    public Page<UserInfo> findList(Pageable pageable) {
        Page<UserInfo> all = userInfoDao.findAll(pageable);
        return all;
    }

    @Override
    public boolean delOne(Integer id) {
        userInfoDao.deleteById(id);
        return true;
    }


    @Override
    public UserInfo findByOpenId(String openid) {
        return userInfoDao.findByYOpenid(openid);
    }


    @Override
    public UserInfo save(WxMpUser wxMpUser, String oid) {
        UserInfo userInfo = new UserInfo();
        userInfo.setYOtype(UserStatusEnum.DEFAULT_USER_STATUS.getCode());
        userInfo.setYUstatus(UserStatusEnum.DEFAULT_USER_STATUS.getCode());
        userInfo.setYOid(oid);
        userInfo.setYUsermoney(new BigDecimal(0));
        userInfo.setYUpic(wxMpUser.getHeadImgUrl());
        userInfo.setYUsertype(UserStatusEnum.DEFAULT_USER_STATUS.getCode());
//        userInfo.setYUsermoney(new BigDecimal(2));
        userInfo.setYOpenid(wxMpUser.getOpenId());
        userInfo.setYNickname(wxMpUser.getNickname());
        userInfo.setYCreatetime(new Date());
        userInfoDao.save(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo save(UserInfo userInfo) {
        return userInfoDao.save(userInfo);
    }

    @Override
    public UserInfo login(String username, String pwd) {
        return userInfoDao.login(username, pwd);
    }

    @Override
    public Page<UserInfo> findWithPage(Pageable pageable) {
        return userInfoDao.findAll(pageable);
    }

    @Override
    public List<UserInfo> findByOid(String openid) {
        return userInfoDao.findByYOid(openid);
    }

    @Override
    public List<UserInfo> findByOidIn(List<String> openid) {
        return userInfoDao.findByYOidIn(openid);
    }

    @Override
    public List<UserInfo> findByOpenidIn(List<String> openid) {
        return userInfoDao.findByYOpenidIn(openid);
    }

    @Override
    public String findAllMoney() {
        return userInfoDao.findAllMoney();
    }

    @Override
    public String findDayAllMoney() {
        return userInfoDao.findDayAllMoney();
    }

    @Override
    public String allUserCount() {
        return userInfoDao.allUserCount();
    }

    @Override
    public List<UserInfo> findByNicknameLike(String str) {
        return userInfoDao.findByYNicknameLike(str);
    }
}
