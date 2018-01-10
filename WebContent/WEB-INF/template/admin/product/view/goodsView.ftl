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
    <title>商品管理查看</title>
</head>
<body>
<div class="pd-20">
<table class="table">
<tbody>
[#if goods??]
<tr>
    <th class="text-r" width="80">Id：</th>
    [#if goods.id??]
        <td>${goods.id}</td>
    [/#if]
</tr>
<tr>
    <th class="text-r" width="80">创建日期：</th>
    [#if goods.createDate??]
        <td><span id="createDate">${goods.createDate}</span></td>
    [/#if]
</tr>
<tr>
    <th class="text-r" width="80">修改日期：</th>
    [#if goods.modifyDate??]
        <td><span id="modifyDate">${goods.modifyDate}</span></td>
    [/#if]
</tr>
</tbody>
</table>
    [#if products??]
        <table class="table">
            <tbody>
            <col width="80" />
                [#list products as product]
                [#if product_index!=0]
                <col width="100" />
                [/#if]
                [/#list]
            <tr>
                <th class="text-r" width="80">商品图片：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block; width: 100px;"><img
                            src="${product.thumbnail}" height="50" width="50"></span></td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">商品编号：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block;width: 100px;">${product.sn}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">商品名称：</th>
                [#list products as product]
                    <td class="text-l">
                        <span class="text-c" style="display: block;width: 100px;">
                            <u style="cursor:pointer"
                               class="text-primary"
                               onclick="edit('${product.name}','/admin/product/edit.jhtml?id=${product.id}','${product.id}','360','400')">${product.name}</u></span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">商品规格1：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block;width: 100px;">${product.spec1}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">商品规格2：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block;width: 100px;">${product.spec2}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">销售价格：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block;width: 100px;">${product.price}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">一级代理价：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c"
                                             style="display: block;width: 100px;">${product.vip1Price}</span></td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">二级代理价：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c"
                                             style="display: block;width: 100px;">${product.vip2Price}</span></td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">三级代理价：</th>
                [#list products as product]
                    <td class="text-c"><span class="text-c"
                                             style="display: block;width: 100px;">${product.vip3Price}</span></td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">成本价：</th>
                [#list products as product]
                    <td class="text-c"><span class="text-c" style="display: block;width: 100px;">${product.cost}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">市场价：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c"
                                             style="display: block;width: 100px;">${product.marketPrice}</span></td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">库存：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block;width: 100px;">${product.stock}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">已分配库存：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c"
                                             style="display: block;width: 100px;">${product.allocatedStock}</span></td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">单位：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block;width: 100px;">${product.unit}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">重量：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c"
                                             style="display: block;width: 100px;">${product.weight}</span></td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">赠送积分：</th>
                [#list products as product]
                    <td class="text-l"><span class="text-c" style="display: block;width: 100px;">${product.point}</span>
                    </td>
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">是否上架：</th>
                [#list products as product]
                    [#if product.isMarketable==true]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">已上架</span></td>
                    [#else]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">未上架</span></td>
                    [/#if]
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">是否列出：</th>
                [#list products as product]
                    [#if product.isList==true]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">已列出</span></td>
                    [#else]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">未列出</span></td>
                    [/#if]
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">商品分类：</th>
                [#list products as product]
                [#if product.productCategory??]
                    <td class="text-l"><span class="text-c"
                                             style="display: block;width: 100px;">${product.productCategory.name}</span></td>
                [#else]
                <td class="text-l"><span class="text-c"
                                         style="display: block;width: 100px;">尚未分类</span></td>
                [/#if]
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">分销方式：</th>
                [#list products as product]
                    [#if product.distribution??]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">${product.distribution.name}</span></td>
                    [#else]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">尚未选择分销方式</span></td>
                    [/#if]
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">是否删除：</th>
                [#list products as product]
                    [#if product.deleted==true]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">已删除</span></td>
                    [#else]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">未删除</span></td>
                    [/#if]
                [/#list]
            </tr>
            <tr>
                <th class="text-r" width="80">所属标签：</th>
                [#list products as product]
                    [#if product.tags??]
                        <td class="text-l">
                            <span class="text-c"
                                  style="display: block;width: 100px;">
                                [#list tag as ta]
                                [#if ta.id==product.tags.tags]
                                [#if ta_index!=0],[/#if]
                                ${ta.name}
                                [/#if]
                            [/#list]
                            </span>
                        </td>
                    [#else]
                        <td class="text-l"><span class="text-c"
                                                 style="display: block;width: 100px;">未删除</span></td>
                    [/#if]
                [/#list]
            </tr>
            </tbody>
        </table>
    [/#if]
[#else]
    没有查找到数据
[/#if]
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/wx.js"></script>
<!--/_footer 作为公共模版分离出去-->
<script>
/*编辑*/
function edit(title, url, id, w, h) {
var index = layer.open({
type:2,
title:title,
content:url
});
layer.full(index);
}
</script>
<!--请在下方写此页面业务相关的脚本-->
</body>
</html>