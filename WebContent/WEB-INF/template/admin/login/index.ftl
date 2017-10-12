<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="{$base}/resources/admin/lib/html5shiv.js"></script>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/respond.min.js"></script>
    <![endif]-->
    <link href="http://cdn.rzico.com/weex/resources/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css"/>
    <link href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/H-ui.login.css" rel="stylesheet" type="text/css"/>
    <link href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="http://cdn.rzico.com/weex/resources/lib/Hui-iconfont/1.0.8/iconfont.css" rel="stylesheet"
          type="text/css"/>
    <!--[if IE 6]>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>后台登录</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
</head>
<body>
<div class="header"></div>
<div class="loginWraper">
    <div class="loginBox">
        <form id="loginform" class="form form-horizontal" action="submit.jhtml" method="post">
            <div class="row cl">
                <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60d;</i></label>
                <div class="formControls col-xs-8">
                    <input id="username" name="username" type="text" placeholder="账户" class="input-text size-L">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60e;</i></label>
                <div class="formControls col-xs-8">
                    <input id="password" name="password" type="password" placeholder="密码" class="input-text size-L">
                </div>
            </div>
            <div class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <input id="captcha" name="captcha" class="input-text size-L" type="text" placeholder="验证码"
                           onblur="if(this.value==''){this.value='验证码:'}"
                           onclick="if(this.value=='验证码:'){this.value='';}" value="验证码:" style="width:150px;">
                    <img id="captchaImage" src="${base}/admin/common/captcha.jhtml?captchaId=${captchaId}"> <a
                        id="captchaClick" href="javascript:;">看不清，换一张</a></div>
            </div>
            <div class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <label for="rememberMe">
                        <input type="checkbox" name="rememberMe" id="rememberMe" value="">
                        使我保持登录状态</label>
                </div>
            </div>
            <div class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <input name="" type="submit" class="btn btn-success radius size-L"
                           value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;">
                    <input name="" type="reset" class="btn btn-default radius size-L"
                           value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;">
                </div>
            </div>
        </form>
    </div>
</div>

<div class="footer">Copyright ${setting.company} ${setting.siteName} v1.0</div>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/js/jsbn.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/js/base64.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/js/rsa.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/js/prng4.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/js/rng.js"></script>
<script type="text/javascript"
        src="http://cdn.rzico.com/weex/resources/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript"
        src="http://cdn.rzico.com/weex/resources/lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript"
        src="http://cdn.rzico.com/weex/resources/lib/jquery.validation/1.14.0/messages_zh.js"></script>

<script type="text/javascript">
    $(function () {
        var $loginForm = $("#loginform");
        var $username = $("#username");
        var $password = $("#password");
        var $rememberMe = $("#rememberMe");
        var $captcha = $("#captcha");
        var $captchaImage = $("#captchaImage");
        var $captchaClick = $("#captchaClick");
        var $submit = $(":submit");

        // 更换验证码
        $captchaClick.click(function () {
            $captchaImage.attr("src", "${base}/admin/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
        });

        // 表单验证、记住用户名
        $loginForm.validate({
            rules: {
                username: "required",
                password: "required",
                captcha: "required"
            },
            submitHandler: function (form) {
                $.ajax({
                    url: "${base}/admin/common/public_key.jhtml",
                    type: "GET",
                    cache: false,
                    beforeSend: function () {
                        $submit.prop("disabled", true);
                    },
                    success: function (data) {
                        var rsaKey = new RSAKey();
                        rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                        var enPassword = hex2b64(rsaKey.encrypt($password.val()));
                        $.ajax({
                            url: $loginForm.attr("action"),
                            type: "POST",
                            data: {
                                username: $username.val(),
                                enPassword: enPassword,
                                rememberMe: $rememberMe.is(':checked'),
                                captchaId: "${captchaId}",
                                captcha: $captcha.val()
                            },
                            cache: false,
                            success: function (message) {
                                $submit.prop("disabled", false);
                                if (message.type == "success") {
                                   [#if redirectUrl??]
                                      location.href = "${redirectUrl}";
                                   [#else]
                                      location.href = "${base}/admin/common/main.jhtml";
                                   [/#if]
                                } else {
                                    layer.msg(message.content, {icon: 1, time: 3000});
                                    $captcha.val("");
                                    $captchaImage.attr("src", "${base}/admin/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
                                }
                            },
                            error: function (message) {
                                $submit.prop("disabled", false);
                                layer.msg(message.content, {icon: 1, time: 3000});
                                $captcha.val("");
                                $captchaImage.attr("src", "${base}/admin/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
                            }
                        });
                    },
                    error: function (message) {
                        $submit.prop("disabled", false);
                        layer.msg(message.content, {icon: 1, time: 3000});
                        $captcha.val("");
                        $captchaImage.attr("src", "${base}/admin/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
                    }

                });
            }
        });
    });

</script>


</body>
</html>