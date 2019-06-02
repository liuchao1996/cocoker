package com.cocoker;

import com.cocoker.utils.DateUtil;
import com.cocoker.utils.MD5;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/4 12:20 PM
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CocokerApplicationTest {

    @Test
    public void contextLoads() {
//        System.out.println(MD5.md5("1234567890abcdefghijklmnopqrstuvwxyz", ""));
        DecimalFormat df = new DecimalFormat("#.00");
        String str = df.format(Double.valueOf("1"));
        System.out.println(str);
    }


    @Test
    public void testDate(){
        Date time = DateUtil.getTime(01, 00, 00);
        System.out.println(time);
    }

}