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
    <title>订单管理查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if order??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if order.id??]
            <td>${order.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if order.createDate??]
            <td><span id="createDate">${order.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if order.modifyDate??]
            <td><span id="modifyDate">${order.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">地址：</th>
            [#if order.address??]
            <td>${order.address}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">已付金额：</th>
            [#if order.amountPaid??]
            <td>${order.amountPaid}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">地区名称：</th>
            [#if order.areaName??]
            <td>${order.areaName}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">收货人：</th>
            [#if order.consignee??]
            <td>${order.consignee}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">优惠券折扣：</th>
            [#if order.couponDiscount??]
            <td>${order.couponDiscount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">到期时间：</th>
            [#if order.expire??]
            <td><span id="expire">${order.expire}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">交易佣金：</th>
            [#if order.fee??]
            <td>${order.fee}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">运费：</th>
            [#if order.freight??]
            <td>${order.freight}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] order.isAllocatedStock??]
            [#if order.isAllocatedStock == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">锁定到期时间：</th>
            [#if order.lockExpire??]
            <td><span id="lockExpire">${order.lockExpire}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">买家留言：</th>
            [#if order.memo??]
            <td>${order.memo}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">调整金额：</th>
            [#if order.offsetAmount??]
            <td>${order.offsetAmount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">操作人：</th>
            [#if order.operator??]
            <td>${order.operator}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">订单状态：</th>
            [#if order.orderStatus??]
            <td>${order.orderStatus}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">支付状态：</th>
            [#if order.paymentStatus??]
            <td>${order.paymentStatus}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">邮编：</th>
            [#if order.phone??]
            <td>${order.phone}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">赠送积分：</th>
            [#if order.point??]
            <td>${order.point}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">积分抵扣：</th>
            [#if order.pointDiscount??]
            <td>${order.pointDiscount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">配送状态：</th>
            [#if order.shippingStatus??]
            <td>${order.shippingStatus}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">订单编号：</th>
            [#if order.sn??]
            <td>${order.sn}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">邮编：</th>
            [#if order.zipCode??]
            <td>${order.zipCode}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">地区：</th>
            [#if types??]
                [#list areas as area]
                [#if area.id == order.area]
                    <td>${area.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">优惠码：</th>
            [#if types??]
                [#list couponCodes as couponCode]
                [#if couponCode.id == order.couponCode]
                    <td>${couponCode.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">买方：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == order.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">卖方：</th>
            [#if types??]
                [#list sellers as seller]
                [#if seller.id == order.seller]
                    <td>${seller.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] order.deleted??]
            [#if order.deleted == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">付款方式：</th>
            [#if order.paymentMethod??]
            <td>${order.paymentMethod}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">配送方式：</th>
            [#if order.shippingMethod??]
            <td>${order.shippingMethod}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] order.isDistribution??]
            [#if order.isDistribution == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Promoter：</th>
            [#if types??]
                [#list promoters as promoter]
                [#if promoter.id == order.promoter]
                    <td>${promoter.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">分销佣金：</th>
            [#if order.rebateAmount??]
            <td>${order.rebateAmount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">退货时间：</th>
            [#if order.returnedDate??]
            <td><span id="returnedDate">${order.returnedDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">发货时间：</th>
            [#if order.shippingDate??]
            <td><span id="shippingDate">${order.shippingDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] order.isPartner??]
            [#if order.isPartner == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">股东分红：</th>
            [#if order.partnerAmount??]
            <td>${order.partnerAmount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Partner：</th>
            [#if types??]
                [#list partners as partner]
                [#if partner.id == order.partner]
                    <td>${partner.name}</td>
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