



var marqueeContent = new Array();
marqueeContent[0] = '';
// getDynamic();
//滚动新闻
var marqueeInterval = new Array(); //定义一些常用而且要经常用到的变量
var marqueeId = 0;
var marqueeDelay = 2000;
var marqueeHeight = 25;

function initMarquee() {
    var str = marqueeContent[0];
    $(".dynamicContext").append('<div id=marqueeBox style="overflow:hidden;width: 100%;line-height: 23px; display: inline-block;font-size:14px;height:' + marqueeHeight + 'px" onmouseover="clearInterval(marqueeInterval[0])" onmouseout="marqueeInterval[0]=setInterval(\'startMarquee()\',marqueeDelay)"><div>' + str + '</div></div>');
    marqueeId++;
    marqueeInterval[0] = setInterval("startMarquee()", marqueeDelay);
}

function startMarquee() {
    var str = marqueeContent[marqueeId];
    marqueeId++;
    if (marqueeId >= marqueeContent.length) marqueeId = 0;
    if (marqueeBox.childNodes.length == 1) {
        var nextLine = document.createElement('DIV');
        nextLine.innerHTML = str;
        marqueeBox.appendChild(nextLine);
    } else {
        marqueeBox.childNodes[0].innerHTML = str;
        marqueeBox.appendChild(marqueeBox.childNodes[0]);
        marqueeBox.scrollTop = 0;
    }
    clearInterval(marqueeInterval[1]);
    marqueeInterval[1] = setInterval("scrollMarquee()", 20);
}

function scrollMarquee() {
    marqueeBox.scrollTop++;
    if (marqueeBox.scrollTop % marqueeHeight == (marqueeHeight - 1)) {
        clearInterval(marqueeInterval[1]);
    }
}
//取最新订单数据
// setInterval(getDynamic,30000);

function getDynamic() {
    $.ajax({
        url: "/cocoker/dynamic/getnew",
        success : function (result) {
            marqueeContent = result;
        },
        error : function (res) {

        }
    })
}
// initMarquee();