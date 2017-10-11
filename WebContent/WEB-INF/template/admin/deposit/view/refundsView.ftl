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
    <title>退款单查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if refunds??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if refunds.id??]
            <td>${refunds.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if refunds.createDate??]
            <td><span id="createDate">${refunds.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if refunds.modifyDate??]
            <td><span id="modifyDate">${refunds.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">退款金额：</th>
            [#if refunds.amount??]
            <td>${refunds.amount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">备注：</th>
            [#if refunds.memo??]
            <td>${refunds.memo}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">方式：</th>
            [#if types??]
                [#list methods as method]
                [#if method.id == refunds.method]
                    <td>${method.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">操作员：</th>
            [#if refunds.operator??]
            <td>${refunds.operator}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">支付方式：</th>
            [#if refunds.paymentMethod??]
            <td>${refunds.paymentMethod}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">支付插件：</th>
            [#if refunds.paymentPluginId??]
            <td>${refunds.paymentPluginId}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">编号：</th>
            [#if refunds.sn??]
            <td>${refunds.sn}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">状态：</th>
            [#if types??]
                [#list statuss as status]
                [#if status.id == refunds.status]
                    <td>${status.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">类型：</th>
            [#if types??]
                [#list types as type]
                [#if type.id == refunds.type]
                    <td>${type.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == refunds.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Payment：</th>
            [#if types??]
                [#list payments as payment]
                [#if payment.id == refunds.payment]
                    <td>${payment.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">订单：</th>
            [#if types??]
                [#list orderss as orders]
                [#if orders.id == refunds.orders]
                    <td>${orders.name}</td>
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