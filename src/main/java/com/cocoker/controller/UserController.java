package com.cocoker.controller;

import com.cocoker.VO.ResultVO;
import com.cocoker.beans.UserInfo;
import com.cocoker.config.ProjectUrl;
import com.cocoker.dto.UserDetailDTO;
import com.cocoker.enums.ResultEnum;
import com.cocoker.service.UserInfoService;
import com.cocoker.utils.QRCodeUtil;
import com.cocoker.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2018/12/24 9:31 PM
 * @Version: 1.0
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ProjectUrl projectUrl;

    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @ResponseBody
    @GetMapping("/get")
    public ResultVO getUserByOpenId(@RequestParam("openid") String openid) {
        UserInfo userInfo = userInfoService.findByOpenId(openid);
        if (userInfo == null) {
//            log.error("[用户查询] 查询失败 用户openid={}",openid);
            return ResultVOUtil.error(ResultEnum.USER_NOT_EXIST.getCode(), ResultEnum.USER_NOT_EXIST.getMsg());
        } else {
            return ResultVOUtil.success(new UserDetailDTO(userInfo.getYUsername(), userInfo.getYNickname(), userInfo.getYUpic(), userInfo.getYUsermoney(), "请不要攻击我，谢谢！"));
        }
    }

    @RequestMapping(value = "/qr", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] qr(@RequestParam("openid") String openid) throws Exception {
        String text = projectUrl.getQrUrl() + "/cocoker/wechat/authorize?upOpenid=" + openid + "&returnUrl=" + projectUrl.getReturnUrl()+ "/cocoker/coc"; // 随机生成验证码
        int width = 150; // 二维码图片的宽
        int height = 150; // 二维码图片的高
        String format = "png"; // 二维码图片的格式
        try {
            Thumbnails.of(new File("/Users/renhuiyong/Desktop/tg2.png")).size(414, 736)
                    .watermark(com.cocoker.enums.Positions.CUSTOM_UPPER, ImageIO.read(new File(QRCodeUtil.generateQRCode(text, width, height, format,   "/Users/renhuiyong/Desktop/qr2.png"))), 1f)
                    .outputQuality(0.9).toFile( "/Users/renhuiyong/Desktop/tgqr2.png");
        } catch (Exception e) {
            e.getMessage();
        }
//        try {
//            Thumbnails.of(new File(basePath + "/tg.png")).size(414, 736)
//                    .watermark(Positions.BOTTOM_CENTER, ImageIO.read(new File(QRCodeUtil.generateQRCode(text, width, height, format, basePath + "/qr.png"))), 1f)
//                    .outputQuality(0.9).toFile(basePath + "/tgqr.png");
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        File file = new File(  bastPath + "tgqr.png");
//        FileInputStream inputStream = new FileInputStream(file);
//        byte[] bytes = new byte[inputStream.available()];
//        inputStream.read(bytes, 0, inputStream.available());
//        return bytes;
        File file = new File(  "/Users/renhuiyong/Desktop/tgqr2.png");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
}
