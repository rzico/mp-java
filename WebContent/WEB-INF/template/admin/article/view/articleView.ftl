<!--_meta 作为公共模版分离出去-->
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
<#list articles as article>
    <#if article.mediaType=='product'>
    <div>${article.content}</div>
        <#if article.original!="">
            <#if article.url!="">
            <div style="position: relative;width: 90%;height: 60px;line-height: 60px;background: #E9FBE4;box-shadow: 1px 2px 3px #E9FBE4;border: 1px solid #C9E9C0;border-radius: 4px;text-align: center;color: #0C7823;display: block;position: absolute;font-size:12px;margin-left:5%;">
                <span>嗨!点击我就可以去购买了呢！</span>
                <div style="position:absolute;left:30px;overflow:hidden;width:0;height:0;border-width:10px;border-style:solid dashed dashed dashed;bottom:-20px;border-color:#C9E9C0 transparent transparent transparent;display: block;"></div>
                <div style="bottom: -19px;border-color: #E9FBE4 transparent transparent transparent;    position: absolute;left: 30px;overflow: hidden;width: 0;height: 0;border-width: 10px;border-style: solid dashed dashed dashed;display: block;"></div>
            </div>
            <div>
                <a href="${article.url}" style="top:10px">
                    <image src="${article.original}"></image>
                </a>
            </div>
            <#else>
            <div>
                <image src="${article.original}"></image>
            </div>
            </#if>
        </#if>
    </#if>
    <#if article.mediaType=='image'>
    <div>${article.content}</div>
        <#if article.original!="">
        <div>
            <image src="${article.original}"></image>
        </div>
        </#if>
    </#if>
    <#if article.mediaType=='video'>
    <div>${article.content}</div>
        <#if article.original!="">
        <div>
            <video src="${article.original}"
                   width="100%"
                   height="auto"
                   style="object-fit:fill"
                   webkit-playsinline="true"
                   x-webkit-airplay="true"
                   playsinline="true"
                   x5-video-player-type="h5"
                   x5-video-orientation="h5"
                   x5-video-player-fullscreen="true"
                   preload="auto" controls="controls">
                加载失败！
            </video>
        </div>
        </#if>
    </#if>
    <#if article.mediaType=='html'>
    <div>${article.content}</div>
    </#if>
    <#if article.mediaType=='audio'>
    <div>${article.content}</div>
        <#if article.original!="">
        <div>
            <audio id="bgmusic" src="${article.original}" controls="controls" autoplay preload loop controls>Your
                browser does not support the audio element.
            </audio>
        </div>
        </#if>
    </#if>
</#list>
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
