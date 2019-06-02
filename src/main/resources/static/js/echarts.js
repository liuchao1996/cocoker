//var myChart = echarts.init(document.getElementById('main'));

var currentMoney;
var currentDate;


//-----------------------------
//        myChart.setOption(option);
//        ---------------- - -------------------------


//==================================================
// websocket链接开始
//==================================================

var option = {
    title: {
//   text: 'Beijing AQI'
    },
    grid: {
        left: '1',
        right: '10%',
        bottom: '1%',
        top: '2%',
        containLabel: true
    },
    tooltip: {
        trigger: 'axis'
    },
    xAxis: {
        data: [],
        splitLine: {
            show: true,
            lineStyle: {
                type: 'dotted',
                color: 'rgba(88,88,88,.6)',
                width: 1,
            }
        },
        axisLine: {
            lineStyle: {
                color: 'rgba(88,88,88,1)',
                width: 0,
            }
        },
    },
    yAxis: [{
        min: 6.7870,
        // type:'dotted',
        max: 6.8030,
        splitArea: {
            show: false
        },
        splitLine: {
            show: true,
            lineStyle: {
                type: 'dotted',
                color: 'rgba(88,88,88,.6)',
                width: 1,

            }
        },
        axisLine: {
            lineStyle: {
                color: 'rgba(88,88,88,1)',
                width: 1,
                type: 'dotted'
            }
        },
    }],
    visualMap: {
        show: false,
        top: 10,
        right: 10,
        precision: 2,
        outOfRange: {
            color: '#fff'
        },
    },
    series: {
        name: '美元汇率',
        itemStyle: {
            normal: {
                color: '#f00',
                lineStyle: {
                    color: '#fff',
                    width: 1,//设置线条粗细
                }
            },

        },
        type: 'line',
        data: [],
        symbol: "none", //去掉小圆点
        // animation: false, //去掉动画
        animationDuration: 0,
        animationEasing: 'cubicInOut',
        animationDurationUpdate: 0,
        animationEasingUpdate: 'cubicInOut',
        showAllSymbol: true, //显示所有原点
        markLine: {
            symbol: 'none',
            silent: true,
            color: '#fff',
            symbolSize: 8,
            label: {
                normal: {
                    padding: [-30, 0, -30, 120],
                    show: true,
                    position: 'middle',
                    formatter: '{c}',
                },
            },
            lineStyle: {
                type: 'dotted',
            },
            data: [{
                silent: false,             //鼠标悬停事件  true没有，false有
                lineStyle: {               //警戒线的样式  ，虚实  颜色
                    type: "dotted",
                    color: "#FA3934",
                },
                yAxis: '',
            }, {
                yAxis: '',
            }
            ]
        }
    }
}

//初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));
//使用制定的配置项和数据显示图表
///myChart.setOption(option);

//var wsUrl = "/cocoker/websocket/1"; //+ getCookie('openid');
var urlPrfix = "ws://27.50.151.9/cocoker/websocket/" + getCookie('openid');
ws = new WebSocket(urlPrfix);

ws.onopen = function () {
    // Web Socket 已连接上，使用 send() 方法发送数据
    ws.send("发送数据");
    //alert("数据发送中...");
};
ws.onmessage = function (event) {
    var jdata = JSON.parse(event.data);
    var date = new Date();
    var hour = date.getHours();
    var minuts = date.getMinutes();
    var seconds = date.getSeconds();
    if (hour < 10) {
        hour = "0" + hour;
    }
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    if (minuts < 10) {
        minuts = "0" + minuts;
    }


    option.xAxis.data = jdata.timeList;
    var price = jdata.priceList;
    currentMoney = price[price.length - 1];
    currentMoney2 = price[price.length - 2];

    if (currentMoney < currentMoney2) {
        $("#c1").text(currentMoney).css({"color": "green"});
    } else {
        $("#c1").text(currentMoney).css({"color": "red"});
    }

    currentDate = jdata.timeList[jdata.timeList.length - 1];

    for (let i = 0; i < 30; i++) {
        jdata.timeList.push([hour + ":" + minuts + ":" + seconds]);
    }

    var data = {
        "value": price[price.length - 1],
        "symbol": "image://images/test2.png",
        "symbolSize": 8,
    }
    var last = price[price.length - 1];
    price[price.length - 1] = data;

    option.series.data = price;
    option.series.markLine.data[0].yAxis = last;//[{yAxis: price[price.length - 1],}, {yAxis: '',}];
    myChart.setOption(option);


}


ws.onclose = function (event) {
    //alert("退出")
}
//==================================================
// websocket结束
//==================================================


