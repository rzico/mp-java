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
    <title>产品档案查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if product??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if product.id??]
            <td>${product.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if product.createDate??]
            <td><span id="createDate">${product.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if product.modifyDate??]
            <td><span id="modifyDate">${product.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">成本价：</th>
            [#if product.cost??]
            <td>${product.cost}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] product.deleted??]
            [#if product.deleted == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] product.isList??]
            [#if product.isList == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] product.isMarketable??]
            [#if product.isMarketable == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">市场价：</th>
            [#if product.marketPrice??]
            <td>${product.marketPrice}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">名称：</th>
            [#if product.name??]
            <td>${product.name}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">赠送积分：</th>
            [#if product.point??]
            <td>${product.point}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">销售价：</th>
            [#if product.price??]
            <td>${product.price}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">编号：</th>
            [#if product.sn??]
            <td>${product.sn}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">单位：</th>
            [#if product.unit??]
            <td>${product.unit}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">一级代理价：</th>
            [#if product.vip1price??]
            <td>${product.vip1price}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">二级代理价：</th>
            [#if product.vip2price??]
            <td>${product.vip2price}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">三级代理价：</th>
            [#if product.vip3price??]
            <td>${product.vip3price}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">重量：</th>
            [#if product.weight??]
            <td>${product.weight}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">商品：</th>
            [#if types??]
                [#list goodss as goods]
                [#if goods.id == product.goods]
                    <td>${goods.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">分类：</th>
            [#if types??]
                [#list productCategorys as productCategory]
                [#if productCategory.id == product.productCategory]
                    <td>${productCategory.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Member：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == product.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Orders：</th>
            [#if product.orders??]
            <td>${product.orders}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">规格1：</th>
            [#if product.spec1??]
            <td>${product.spec1}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">规格2：</th>
            [#if product.spec2??]
            <td>${product.spec2}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">缩略图：</th>
            [#if product.thumbnail??]
            <td>${product.thumbnail}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">已分配库存：</th>
            [#if product.allocatedStock??]
            <td>${product.allocatedStock}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">库存：</th>
            [#if product.stock??]
            <td>${product.stock}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Distribution：</th>
            [#if types??]
                [#list distributions as distribution]
                [#if distribution.id == product.distribution]
                    <td>${distribution.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">商品类型：</th>
            [#if types??]
                [#list types as type]
                [#if type.id == product.type]
                    <td>${type.name}</td>
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