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
    <script type="text/javascript" src="${base}/resources/admin/lib/html5shiv.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/lib/respond.min.js"></script>
    <![endif]-->
    <link href="${base}/resources/admin/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/resources/admin/h-ui.admin/css/H-ui.login.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/resources/admin/h-ui.admin/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/resources/admin/lib/Hui-iconfont/1.0.8/iconfont.css" rel="stylesheet"
          type="text/css"/>
    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js"></script>
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
                    <input id="mobile" name="mobile" type="text" placeholder="手机号码" class="input-text size-L">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60e;</i></label>
                <div class="formControls col-xs-8">
                    <input id="captcha" name="captcha" type="text" placeholder="验证码" class="input-text size-L">
                </div>
            </div>
            <div class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <a id="captchaClick" href="javascript:;">获取验证码</a></div>
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
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rng.js"></script>
<script type="text/javascript"
        src="${base}/resources/admin/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript"
        src="${base}/resources/admin/lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript"
        src="${base}/resources/admin/lib/jquery.validation/1.14.0/messages_zh.js"></script>

<script type="text/javascript">
    $(function () {
        $("#captchaClick").click(function () {
            //获取验证码点击事件
            $.ajax({
                url: "${base}/admin/common/public_key.jhtml",
                type: "GET",
                cache: false,
                beforeSend: function () {
                    $(":submit").prop("disabled", true);
                },
                success: function (data) {
                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                    var enMobile = hex2b64(rsaKey.encrypt($.trim($("#mobile").val())))
                    $.ajax({
                        url: "${base}/weex/login/send_mobile.jhtml;",
                        type: "POST",
                        data: {
                            mobile: enMobile
                        },
                        cache: false,
                        success: function (message) {
                            $(":submit").prop("disabled", false);
                            if (message.type == "success") {
                                layer.msg(message.content, {icon: 1, time: 3000});
                            }
                        },
                        error: function (message) {
                            $(":submit").prop("disabled", false);
                            layer.msg(message.content, {icon: 1, time: 3000});
                        }
                    });
                },
                error: function (message) {
                    $(":submit").prop("disabled", false);
                    layer.msg(message.content, {icon: 1, time: 3000});
                }
            });
        });

        //确认登录
        $("#loginform").validate({
            rules: {
                mobile: "required",
                captcha: "required"
            },
            submitHandler: function(from){
                $.ajax({
                    url: "${base}/admin/common/public_key.jhtml",
                    type: "GET",
                    cache: false,
                    beforeSend: function () {
                        $(":submit").prop("disabled", true);
                    },
                    success: function (data) {
                        var rsaKey = new RSAKey();
                        rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                        var enCaptcha = hex2b64(rsaKey.encrypt($.trim($("#captcha").val())))
                        $.ajax({
                            url: "${base}/weex/login/captcha.jhtml;",
                            type: "POST",
                            data: {
                                captcha: enCaptcha
                            },
                            cache: false,
                            success: function (message) {
                                $(":submit").prop("disabled", false);
                                layer.msg(message.content, {icon: 1, time: 1000});
                                if (message.type == "success") {
                                    location.href = "${base}/usered/index/main1.jhtml";
                                }
                            },
                            error: function (message) {
                                $(":submit").prop("disabled", false);
                                layer.msg(message.content, {icon: 1, time: 3000});
                            }
                        });
                    },
                    error: function (message) {
                        $(":submit").prop("disabled", false);
                        layer.msg(message.content, {icon: 1, time: 3000});
                    }
                });
            }
        });
    });

</script>


</body>
</html>