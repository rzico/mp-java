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
[#if topic??]
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">小程序ID：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="id" name="id" type="text" readonly="readonly" class="input-text" value="${topic.id}" placeholder="">
        </div>
    </div>

    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">小程序名称：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="name" name="name" type="text" readonly="readonly" class="input-text" value="${topic.name}" placeholder="">
        </div>
    </div>

    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">小程序模板ID：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="templateId" name="templateId" type="text" class="input-text" placeholder="">
        </div>
    </div>

    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">小程序版本：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="version" name="version" type="text" class="input-text" placeholder="">
        </div>
    </div>

    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2">备注：</label>
        <div class="formControls col-xs-8 col-sm-9">
            <input id="userDesc" name="userDesc" type="text" class="input-text" placeholder="">
        </div>
    </div>
[#else]
服务器发生异常，请稍后重试
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
                    url: "${base}/admin/smallRange/upLoad.jhtml" ,
                    beforeSend: function() {
                        $submit.prop("disabled", true);
                    },
                    success: function(message){
                        layer.close(load);
                        if(message.type ==  "success"){
//                                    关闭当前页面
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.closeWindow(index, '提交成功');
                        }else{
                            $submit.prop("disabled", false);
                            layer.msg('提交失败!',{icon:2,time:1000});
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