<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="Bookmark" href="/favicon.ico" >
    <link rel="Shortcut Icon" href="/favicon.ico" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${base}/resources/admin/lib/html5shiv.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/lib/respond.min.js"></script>

    <![endif]-->
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/lib/Hui-iconfont/1.0.8/iconfont.css" />

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/css/wx.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->
    <link href="${base}/resources/admin/lib/webuploader/0.1.5/webuploader.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        body{
            overflow-x: hidden;
        }
        div{
            background-size:contain|cover;
            width:100%;
            height: auto;
        }
        div>image{
            width: 100%;
            height: auto;
        }
        div>img{
            width: 100%;
            height: auto;
        }
    </style>
</head>
<body>
[#if feedback??]
    [#if feedback.member??]
    <div class="row cl">
        <label class="form-label col-xs-2 col-sm-2">用户：</label>
        <div class="formControls col-xs-10 col-sm-9">
        <span class="select-box">
            [#if member??]
                <div>${member.name}</div>
            [/#if]
        </span>
        </div>
    </div>
    [/#if]
    [#if feedback.content??]
    <div class="row cl">
        <label class="form-label col-xs-2 col-sm-2">问题内容：</label>
        <div class="formControls col-xs-10 col-sm-9">
            <input type="text" class="input-text" value="${feedback.content}" readonly="readonly">
        </div>
    </div>
    [/#if]


<div class="row cl">
    <label class="form-label col-xs-12 col-sm-2">问题反馈图：</label>
</div>
<div class="row cl">
    [#if feedback.problemPictrue1??]
        <div class="formControls col-xs-4 col-sm-9">
            <img src="${feedback.problemPictrue1}"/>
        </div>
    [/#if]
    [#if feedback.problemPictrue2??]
        <div class="formControls col-xs-4 col-sm-9">
            <img src="${feedback.problemPictrue2}"/>
        </div>
    [/#if]
    [#if feedback.problemPictrue3??]
        <div class="formControls col-xs-4 col-sm-9">
            <img src="${feedback.problemPictrue3}"/>
        </div>
    [/#if]
</div>
<div class="row cl">
    [#if feedback.problemPictrue4??]
        <div class="formControls col-xs-4 col-sm-9">
            <img src="${feedback.problemPictrue4}"/>
        </div>
    [/#if]
    [#if feedback.problemPictrue5??]
        <div class="formControls col-xs-4 col-sm-9">
            <img src="${feedback.problemPictrue5}"/>
        </div>
    [/#if]
</div>
    [#if feedback.solve==false]
    <div class="row cl">
        <label class="form-label col-xs-2 col-sm-2">问题内容：</label>
        <div class="formControls col-xs-10 col-sm-9">
            <input type="text" class="input-text" id="recontent" name="recontent">
        </div>
    </div>
        <div class="formControls col-xs-10 col-sm-9">
            <input type="submit" class="btn btn-success radius">
        </div>
    [#else]
    <div class="row cl">
        <label class="form-label col-xs-2 col-sm-2">问题内容：</label>
        <div class="formControls col-xs-10 col-sm-9">
            <input type="text" class="input-text" value="${feedback.recontent}" readonly="readonly">
        </div>
    </div>
    [/#if]
[#else]
数据在来的路上丢失了，稍后重试
[/#if]
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

<script type="text/javascript" src="${base}/resources/admin/lib/jquery.ISelect/jquery.lSelect.js"></script>
</body>
</html>
