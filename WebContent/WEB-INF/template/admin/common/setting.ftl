<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="Bookmark" href="/favicon.ico">
    <link rel="Shortcut Icon" href="/favicon.ico"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${base}/resources/admin/lib/html5shiv.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/lib/Hui-iconfont/1.0.8/iconfont.css"/>

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/style.css"/>

    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>版本设置</title>
</head>
<body>
<form action="" method="post" class="form form-horizontal" id="form-add">
[#if androidVersion??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">安卓版本：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="android" name="android" type="text" class="input-text" value="${androidVersion}" placeholder="">
        </div>
    </div>
[/#if]
[#if androidMinVersion??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">安卓Min版本：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="androidMin" name="androidMin" type="text" class="input-text" value="${androidMinVersion}" placeholder="">
        </div>
    </div>
[/#if]
[#if androidUrl]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">安卓版本URL：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="androidUrl" name="androidUrl" type="text" class="input-text" value="${androidUrl}" placeholder="">
        </div>
    </div>
[/#if]
[#if iosVersion??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">苹果版本：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="ios" name="ios" type="text" class="input-text" value="${iosVersion}" placeholder="">
        </div>
    </div>
[/#if]
[#if iosMinVersion??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">苹果Min版本：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="iosMin" name="iosMin" type="text" class="input-text" value="${iosMinVersion}" placeholder="">
        </div>
    </div>
[/#if]
[#if iosUrl??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">苹果URL：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="iosUrl" name="iosUrl" type="text" class="input-text" value="${iosUrl}" placeholder="">
        </div>
    </div>
[/#if]
[#if resourceVersion??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">服务器版本：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="resource" name="resource" type="text" class="input-text" value="${resourceVersion}" placeholder="">
        </div>
    </div>
[/#if]
[#if resourceUrl??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">服务器URL：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="resourceUrl" name="resourceUrl" type="text" class="input-text" value="${resourceUrl}" placeholder="">
        </div>
    </div>
[/#if]
[#if codeVersion??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">第三方版本：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="codeVersion" name="codeVersion" type="text" class="input-text" value="${codeVersion}" placeholder="">
        </div>
    </div>
[/#if]
[#if templateId??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">第三方模板ID：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="templateId" name="templateId" type="text" class="input-text" value="${templateId}" placeholder="">
        </div>
    </div>
[/#if]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2"></label>
        <div class="formControls col-xs-8 col-sm-9">
            <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
        </div>
    </div>
</form>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${base}/resources/admin/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/jquery.validation/1.14.0/messages_zh.js"></script>
<script type="text/javascript">
    $(function(){
        var $submit = $(":submit");
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });

        $("#form-add").validate({
            rules:{
                android:{
                    required:true,
                },
                androidMin:{
                    required:true,
                },
                androidUrl:{
                    required:true,
                },
                ios:{
                    required:true,
                },
                iosMin:{
                    required:true,
                },
                iosUrl:{
                    required:true,
                },
                resource:{
                    required:true,
                },
                resourceUrl:{
                    required:true,
                },
            },
            onkeyup:false,
            focusCleanup:true,
            success:"valid",
            ignore:"",
            submitHandler:function(form){
                var load = layer.msg('加载中', {
                    icon: 16
                    ,shade: 0.01
                });
                $(form).ajaxSubmit({
                    type: 'post',
                    url: "${base}/admin/settings/edit.jhtml?type=${type}" ,
                    beforeSend: function() {
                        $submit.prop("disabled", true);
                    },
                    success: function(message){
                        layer.close(load);
                        if(message.type ==  "success"){
//                                    关闭当前页面
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.closeWindow(index, '修改成功');
                        }else{
                            $submit.prop("disabled", false);
                            layer.msg('修改失败!',{icon:2,time:1000});
                        }
                    },
                    error: function(XmlHttpRequest, textStatus, errorThrown){
                        $submit.prop("disabled", false);
                        layer.close(load);
                        layer.msg('error!',{icon:2,time:1000});
                    }
                });
            }
        });
    });
    /*关闭页面*/
    function closeWindow(index, msg) {
        layer.close(index);
        layer.msg(msg, {icon: 1, time: 1000});
    }
</script>
<script type="text/javascript" src="${base}/resources/admin/lib/jquery.ISelect/jquery.lSelect.js"></script>
</body>
</html>