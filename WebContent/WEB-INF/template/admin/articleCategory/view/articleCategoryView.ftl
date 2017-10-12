<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${base}/resources/admin/lib/html5shiv.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/lib/Hui-iconfont/1.0.8/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/style.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>文章分类查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if articleCategory??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if articleCategory.id??]
            <td>${articleCategory.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if articleCategory.createDate??]
            <td><span id="createDate">${articleCategory.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if articleCategory.modifyDate??]
            <td><span id="modifyDate">${articleCategory.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">排序：</th>
            [#if articleCategory.orders??]
            <td>${articleCategory.orders}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">层级：</th>
            [#if articleCategory.grade??]
            <td>${articleCategory.grade}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">名称：</th>
            [#if articleCategory.name??]
            <td>${articleCategory.name}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">页面描述：</th>
            [#if articleCategory.seoDescription??]
            <td>${articleCategory.seoDescription}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">页面关键词：</th>
            [#if articleCategory.seoKeywords??]
            <td>${articleCategory.seoKeywords}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">页面标题：</th>
            [#if articleCategory.seoTitle??]
            <td>${articleCategory.seoTitle}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">状态：</th>
            [#if types??]
                [#list statuss as status]
                [#if status.id == articleCategory.status]
                    <td>${status.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">TreePath：</th>
            [#if articleCategory.treePath??]
            <td>${articleCategory.treePath}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Parent：</th>
            [#if types??]
                [#list parents as parent]
                [#if parent.id == articleCategory.parent]
                    <td>${parent.name}</td>
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
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/wx.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
</body>
</html>