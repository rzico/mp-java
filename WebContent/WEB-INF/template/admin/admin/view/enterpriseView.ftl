<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/html5shiv.js"></script>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/lib/Hui-iconfont/1.0.8/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/style.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>企业管理查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if enterprise??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if enterprise.id??]
            <td>${enterprise.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if enterprise.createDate??]
            <td><span id="createDate">${enterprise.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if enterprise.modifyDate??]
            <td><span id="modifyDate">${enterprise.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">企业名称：</th>
            [#if enterprise.name??]
            <td>${enterprise.name}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">结算比例：</th>
            [#if enterprise.brokerage??]
            <td>${enterprise.brokerage}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">类型：</th>
            [#if types??]
                [#list types as type]
                [#if type.id == enterprise.type]
                    <td>${type.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">城市：</th>
            [#if types??]
                [#list areas as area]
                [#if area.id == enterprise.area]
                    <td>${area.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>

        [#else]
         没有查找到数据
        [/#if]
        </tbody>
    </table>
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/wx.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
</body>
</html>