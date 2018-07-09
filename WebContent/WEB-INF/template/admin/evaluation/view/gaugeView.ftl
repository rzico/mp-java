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
    <title>Gauge查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if gauge??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if gauge.id??]
            <td>${gauge.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if gauge.createDate??]
            <td><span id="createDate">${gauge.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if gauge.modifyDate??]
            <td><span id="modifyDate">${gauge.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">推广佣金：</th>
            [#if gauge.brokerage??]
            <td>${gauge.brokerage}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">测评简介：</th>
            [#if gauge.content??]
            <td>${gauge.content}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] gauge.deleted??]
            [#if gauge.deleted == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">分销佣金：</th>
            [#if gauge.distribution??]
            <td>${gauge.distribution}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">测评人数：</th>
            [#if gauge.evaluation??]
            <td>${gauge.evaluation}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">原价：</th>
            [#if gauge.marketPrice??]
            <td>${gauge.marketPrice}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">测评须知：</th>
            [#if gauge.notice??]
            <td>${gauge.notice}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">现价：</th>
            [#if gauge.price??]
            <td>${gauge.price}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">常模修订说明：</th>
            [#if gauge.revisionNote??]
            <td>${gauge.revisionNote}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">副标题：</th>
            [#if gauge.subTitle??]
            <td>${gauge.subTitle}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">主标题：</th>
            [#if gauge.title??]
            <td>${gauge.title}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">常模类型：</th>
            [#if types??]
                [#list types as type]
                [#if type.id == gauge.type]
                    <td>${type.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">用户类型：</th>
            [#if types??]
                [#list userTypes as userType]
                [#if userType.id == gauge.userType]
                    <td>${userType.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Product：</th>
            [#if types??]
                [#list products as product]
                [#if product.id == gauge.product]
                    <td>${product.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Thumbnail：</th>
            [#if gauge.thumbnail??]
            <td>${gauge.thumbnail}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">GaugeCategory：</th>
            [#if types??]
                [#list gaugeCategorys as gaugeCategory]
                [#if gaugeCategory.id == gauge.gaugeCategory]
                    <td>${gaugeCategory.name}</td>
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