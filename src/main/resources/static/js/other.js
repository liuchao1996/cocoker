var active = null,
    lastid, span;
mui(".mui-content").on("tap", "a", function () {
    var id = this.getAttribute("id");
    if (!active) {
        this.classList.add("active");
        if (id) {
            span = this.querySelector("span");
            span.classList.remove("mui-" + id);
            span.classList.add("mui-" + id + "-filled");
        }
        active = this;
    } else {
        active.classList.remove("active");
        if (lastid) {
            span.classList.remove("mui-" + lastid + "-filled");
            span.classList.add("mui-" + lastid);
        }

        this.classList.add("active");
        if (id) {
            span = this.querySelector("span");
            span.classList.remove("mui-" + id);
            span.classList.add("mui-" + id + "-filled");
        }

        active = this;
    }
    lastid = id;
});

//--------------------------------------------------------------------------
$(function () {


    //背景音乐
    // document.getElementById('notice').play();
    //历史佣金
    //历史
    $.ajax({
        url: "/cocoker/historyCommission",
        type: "GET",
        data: {"openid": getCookie('openid')},
        success: function (result) {
            if (result.historyCommission > 0) {
                $('.historyCommission').text(result.historyCommission);
            } else {
                $('.historyCommission').text(0);
            }

        }
    });
    //今天
    $.ajax({
        url: "/cocoker/historyCommissionByTime",
        type: "GET",
        data: {"openid": getCookie('openid')},
        success: function (result) {
            if (result.historyCommissionByTime > 0) {
                $('.historyCommissionByTime').text(result.historyCommissionByTime);
            } else {
                $('.historyCommissionByTime').text(0);
            }

        }
    });

    //昨天
    $.ajax({
        url: "/cocoker/historyCommissionByYesterday",
        type: "GET",
        data: {"openid": getCookie('openid')},
        success: function (result) {
            console.log(result);
            if (result.historyCommissionByYesterday > 0) {
                $('.historyCommissionByYesterday').text(result.historyCommissionByYesterday);
            } else {
                $('.historyCommissionByYesterday').text(0);
            }

        }
    });

    /**************************************时间格式化处理************************************/
    function dateFtt(fmt, date) { //author: meizz
        var o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    //下级总人数
    $.ajax({
        url: "/cocoker/proxyCount",
        type: "GET",
        data: {"openid": getCookie('openid')},
        success: function (result) {
            if (result.proxyCount > 0) {
                $('.proxyCount').text(result.proxyCount);
            } else {
                $('.proxyCount').text(0);
            }

        }
    });
    //在线人数
    lineNum();
    setInterval(lineNum, 10000);

    //点击数量m
    $('.btndiv button').click(function () {
        $('.btndiv button').removeAttr("check").removeClass('btnsdiv');
        $(this).attr("check", "check").addClass('btnsdiv');
        $('.yjbt').text($(this).text() * 2);
    });

    //刚进来看看有没有赢的订单未结算
    function checkOrder() {
        $.ajax({
            url: "/cocoker/order/info",
            data: {"openid": getCookie('openid')},
            type: "GET",
            success: function (result) {
                if (result.code == 0) {
                    $('.money').text(result.data.usermoney)
                }
            }
        });
    }

    setTimeout(checkOrder, 31 * 1000)

    //
    mui.init({
        swipeBack: true //启用右滑关闭功能
    });

    //提交订单
    $('.subBtn').click(function () {
        if ($('.btndiv button[check=check]').length < 1) {
            popup({type: 'tip', msg: "请选择数量", delay: 1500});
            return
        }


        //判断用户余额
        if($("#money").text() < $('.btndiv button[check=check]')[0].innerText ){
            popup({type: 'tip', msg: "余额不足请充值", delay: 1500});
            setTimeout(function(){
                location.href =  "/cocoker/recharge";
            },1500);

            return;
        }



        $.ajax({
            url: "/cocoker/order",
            data: {
                "openid": getCookie('openid'),
                "num": $('.btndiv button[check=check]')[0].innerText,
                "flag": flag,
                "index": currentMoney,
                "currentDate": currentDate
            },
            success: function (result) {
                //关闭选择框
                $('.goumoney').hide();

                if (result.code == 0) {


                    //弹窗
                    popup({type: 'success', msg: "订单提交成功", delay: 1000});

                    //增加一条新红线，显示投注时的线
                    setTimeout(function(){

                        option.series.markLine.data[1].yAxis = currentMoney;
                        myChart.setOption(option);

                    },1000);


                    //更新数据
                    var user = result.data;
                    $('.money').text(user.usermoney);



                    //30秒之后查询
                    setTimeout(function () {

                        //30秒后清除线
                        option.series.markLine.data[1].yAxis = '';
                        myChart.setOption(option);

                        $.ajax({
                            url: "/cocoker/order/info",
                            data: {"openid": getCookie('openid')},
                            type: "GET",
                            success: function (result) {
                                if (result.code == 0) {
                                    // popup({type: 'tip', msg: "竞猜结束", delay: 1500});
                                    $('.money').text(result.data.usermoney);
                                    //查询订单
                                    $.ajax({
                                        url: "/cocoker/order/orderDetail",
                                        data: {"openid": getCookie('openid')},
                                        type: "GET",
                                        success: function (result) {
                                            $('.jyfx').text(result.data.direction);
                                            $('.gmsj').text(result.data.createTime.substring(0, 10));
                                            $('.jyjg').text(result.data.oindex);
                                            $('.zzjg').text(result.data.ofinal);
                                            $('.tr').text(result.data.money);
                                            $('.fh').text(result.data.result == '盈' ? result.data.money * 2 : 0);
                                            $('#showMe').click();
                                        }
                                    });
                                }
                            }
                        });
                    }, 31 * 1000)
                } else {
                    popup({type: 'tip', msg: result.msg, delay: 1500});
                }
            }
        });

    });


//--------fun - -- - - ----------
    function lineNum() {
        $.ajax({
            url: "/cocoker/num",
            success: function (res) {
                $('.linenum').text(res);
            }
        })
    }
})
$(".newbtnparent button").on("tap", function (event) {
    event.stopPropagation();
});



