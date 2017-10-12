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
    <title>收款单查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if payment??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if payment.id??]
            <td>${payment.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if payment.createDate??]
            <td><span id="createDate">${payment.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if payment.modifyDate??]
            <td><span id="modifyDate">${payment.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">付款金额：</th>
            [#if payment.amount??]
            <td>${payment.amount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">到期时间：</th>
            [#if payment.expire??]
            <td><span id="expire">${payment.expire}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">备注：</th>
            [#if payment.memo??]
            <td>${payment.memo}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">方式：</th>
            [#if types??]
                [#list methods as method]
                [#if method.id == payment.method]
                    <td>${method.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">操作员：</th>
            [#if payment.operator??]
            <td>${payment.operator}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">付款日期：</th>
            [#if payment.paymentDate??]
            <td><span id="paymentDate">${payment.paymentDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">支付方式：</th>
            [#if payment.paymentMethod??]
            <td>${payment.paymentMethod}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">支付插件：</th>
            [#if payment.paymentPluginId??]
            <td>${payment.paymentPluginId}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">编号：</th>
            [#if payment.sn??]
            <td>${payment.sn}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">状态：</th>
            [#if types??]
                [#list statuss as status]
                [#if status.id == payment.status]
                    <td>${status.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">类型：</th>
            [#if types??]
                [#list types as type]
                [#if type.id == payment.type]
                    <td>${type.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">买家：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == payment.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">订单：</th>
            [#if types??]
                [#list orderss as orders]
                [#if orders.id == payment.orders]
                    <td>${orders.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">ArticleReward：</th>
            [#if types??]
                [#list articleRewards as articleReward]
                [#if articleReward.id == payment.articleReward]
                    <td>${articleReward.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Payee：</th>
            [#if types??]
                [#list payees as payee]
                [#if payee.id == payment.payee]
                    <td>${payee.name}</td>
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