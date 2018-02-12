<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="/lib/html5shiv.js"></script>
    <script type="text/javascript" src="/lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="/h-ui.admin/css/style.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>GaugeGene查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if gaugeGene??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if gaugeGene.id??]
            <td>${gaugeGene.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if gaugeGene.createDate??]
            <td><span id="createDate">${gaugeGene.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if gaugeGene.modifyDate??]
            <td><span id="modifyDate">${gaugeGene.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Orders：</th>
            [#if gaugeGene.orders??]
            <td>${gaugeGene.orders}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">因子：</th>
            [#if gaugeGene.name??]
            <td>${gaugeGene.name}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Gauge：</th>
            [#if types??]
                [#list gauges as gauge]
                [#if gauge.id == gaugeGene.gauge]
                    <td>${gauge.name}</td>
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
<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/wx.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
</body>
</html>