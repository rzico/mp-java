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
    <title>优惠券查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if coupon??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if coupon.id??]
            <td>${coupon.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if coupon.createDate??]
            <td><span id="createDate">${coupon.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if coupon.modifyDate??]
            <td><span id="modifyDate">${coupon.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">优惠金额：</th>
            [#if coupon.amount??]
            <td>${coupon.amount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">使用起始日期：</th>
            [#if coupon.beginDate??]
            <td><span id="beginDate">${coupon.beginDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] coupon.deleted??]
            [#if coupon.deleted == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">使用结束日期：</th>
            [#if coupon.endDate??]
            <td><span id="endDate">${coupon.endDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">介绍：</th>
            [#if coupon.introduction??]
            <td>${coupon.introduction}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] coupon.isEnabled??]
            [#if coupon.isEnabled == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">使用条件：</th>
            [#if coupon.minimumPrice??]
            <td>${coupon.minimumPrice}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">名称：</th>
            [#if coupon.name??]
            <td>${coupon.name}</td>
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