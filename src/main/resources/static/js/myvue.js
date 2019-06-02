//vue
//        ====================================================
{
    function getQueryVariable(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] == variable) {
                return pair[1];
            }
        }
        return (false);
    }

    function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i].trim();
            if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
        }
        return "";
    }

    var app = new Vue({
        el: '#app',
        data: {},
        created() {
            // if (window.wx) {
            //     wx.ready(function () {
            //         wx.hideOptionMenu();
            //     })
            // }
            //
            //如果url里有openid, 设置进cookie
            var openid = getQueryVariable('openid');
            if (typeof openid !== 'undefined' && openid !== false) {
                var exp = new Date();
                exp.setTime(exp.getTime() + 3600 * 1000);//过期时间60分钟
                document.cookie = 'openid=' + openid + ";expires=" + exp.toGMTString() + ";path=/";
            }
            //获取openid
            if (getCookie('openid') == "" || getCookie('openid') == null) {
                location.href = "http://xf7w5e.top/cocoker/wechat/authorize?" + 'returnUrl=' + encodeURIComponent("abc");
            }
            //获取用户信息

            $.ajax({
                url: "/cocoker/user/get",
                data: {"openid": getCookie('openid')},
                success: function (result) {
                    if (result.code == 0) {
                        var user = result.data;
                        $('.userpic').attr("src", user.upic);
                        $('.nickname').text(user.nickname);
                        $('.money').text(user.usermoney);
                    }else {
                        window.location.href = "http://xf7w5e.top/cocoker/wechat/authorize?" + 'returnUrl=' + encodeURIComponent("abc");
                    }
                },
                error: function (result) {
                }
            });
        }
    })
}
//        ====================================================