$('.history').click(function () {
    window.location.href='/cocoker/history?openid='+ getCookie('openid');
});
$('.entry').click(function () {
    window.location.href='/cocoker/entry';
});
$('.modelinfo').click(function () {
    window.location.href='/cocoker/modelinfo';
});
$('.novicetips').click(function () {
    window.location.href='/cocoker/novicetips';
});
$('.profitmodel').click(function () {
    window.location.href='/cocoker/profitmodel';
});
$('.recharge').click(function () {
    window.location.href='/cocoker/recharge';
});
$('.exchange').click(function () {
    window.location.href='/cocoker/exchange';
});
$('.proxy').click(function () {
    window.location.href='/cocoker/proxy?openid='+ getCookie('openid');
});
$('.rechargehistory').click(function () {
    window.location.href='/cocoker/rechargehistory?openid='+ getCookie('openid');
});
$('.exchargehistory').click(function () {
    window.location.href='/cocoker/exchargehistory?openid='+ getCookie('openid');
});


//跳转
document.getElementById('recharge').addEventListener('tap', function() {
    mui.openWindow({
        url: '/cocoker/recharge', //通过URL传参
    })
});
//跳转
document.getElementById('exchange').addEventListener('tap', function() {
    mui.openWindow({
        url: '/cocoker/exchange', //通过URL传参
    })
});

