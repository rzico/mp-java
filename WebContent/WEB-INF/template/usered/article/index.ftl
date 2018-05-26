<!--_meta 作为公共模版分离出去-->
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
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/css/wx.css"/>

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/css/style.css"/>

    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->
    <link href="${base}/resources/admin/lib/webuploader/0.1.5/webuploader.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">

    </style>
</head>
<body style="background-color:transparent">
<div class="tag">
    <div class="tag top">
        <a>全部文章</a>
        <img src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521811583471&di=e3660069eae6315c850438eb8d99ef05&imgtype=0&src=http%3A%2F%2Fpic15.nipic.com%2F20110803%2F7180732_211822337168_2.jpg"/>
        <a style="top:17%; font-size: 14px"></a>
    </div>
    <div class="tag top zz"><a style="top: 50%;" href="/usered/article1/orders.jhtml">打开文集</a></div>
    <div class="tag down"><a style="text-decoration:none; out-line: nonecolor;">更新于最新</a></div>
</div>
[#if articleCatalog??]
    [#list articleCatalog as article]
    <div class="tag">
        <div class="tag top">
            <a>${article.name}</a>
            <img src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521811583471&di=e3660069eae6315c850438eb8d99ef05&imgtype=0&src=http%3A%2F%2Fpic15.nipic.com%2F20110803%2F7180732_211822337168_2.jpg"/>
            <a style="top:17%; font-size: 14px"></a>
        </div>
        <div class="tag top zz"><a style="top: 50%;" href="/usered/article1/orders.jhtml?id=${article.id}">打开文集</a></div>
        <div class="tag down"><a style="text-decoration:none; out-line: nonecolor;">更新于${article.modifyDate}</a></div>
    </div>
    [/#list]
[/#if]

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
    $(function () {
        $(".tag.top").hover(function () {
            $(this).next().css("z-index", "1000");
        }, function () {
//            $(this).next().css("z-index","-1000");
        });
        $(".tag.top.zz").hover(function () {
//            $(this).next().css("z-index","1000");
        }, function () {
            $(this).css("z-index", "-1000");
        })
    })
</script>
</body>
</html>
