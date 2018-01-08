<!--_meta 作为公共模版分离出去-->
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="Bookmark" href="/favicon.ico" >
    <link rel="Shortcut Icon" href="/favicon.ico" />
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
    <!--/meta 作为公共模版分离出去-->

    <title>基本设置</title>
</head>
<body>
<div class="page-container">
    <form action="" method="post" class="form form-horizontal" id="form-update">
    <!-- form class="form form-horizontal" id="form-article-add" -->
        [#if data??]
        <div class="cl pd-5 bg-1 bk-gray mt-20">
        [#if data.status == 'unpaid' ]<!-- 待付款 -->
            <button type="button" class="btn btn-success radius" id="confirmId" onclick="confirm(${data.id});" name="">
                <i class="Hui-iconfont">&#xe6e1;</i> 订单确认
            </button>
            <button type="button" class="btn btn-success radius" id="cancelId" onclick="cancel(${data.id});" name="">
                <i class="Hui-iconfont">&#xe706;</i> 关闭
            </button>
        [#elseif data.status == 'unshipped']<!-- 待发货 -->
            <button type="button" class="btn btn-success radius" id="shippingId" onclick="shipping(${data.id});" name="">
                <i class="Hui-iconfont">&#xe67a;</i> 发货
            </button>
            <button type="button" class="btn btn-success radius" id="cancelId" onclick="cancel(${data.id});" name="">
                <i class="Hui-iconfont">&#xe706;</i> 关闭
            </button>
        [#elseif data.status == 'shipped']<!-- 已发货 -->
            <button type="button" class="btn btn-success radius" id="" onclick="search();" name="">
                <i class="Hui-iconfont">&#xe678;</i> 退货
            </button>
        [#elseif data.status == 'refunding']<!-- 退货中 -->
            <button type="button" class="btn btn-success radius" id="" onclick="search();" name="">
                <i class="Hui-iconfont">&#xe665;</i> 同意退货
            </button>
        [#elseif data.status == 'completed']<!-- 已完成 -->

        [/#if]
        </div>
        <div class="mt-20"></div>
        <div id="tab-system" class="HuiTab">
            <div class="tabBar cl">
                <span>订单信息</span>
                <span>商品信息</span>
                <span>收款信息</span>
                <span>退款信息</span>
                <span>订单日志</span>
            </div>
            <div class="tabCon">
                <table class="table table-border table-bordered table-bg mt-20">
                    <tr>
                        <th class="text-r" width="10%">
                            订单编号:
                        </th>
                        <td width="30%">
                            ${data.sn}
                        </td>
                        <th class="text-r" width="10%">
                            创建日期:
                        </th>
                        <td width="50%">
                            ${data.createDate}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            订单状态:
                        </th>
                        <td>
                        [#if orderStatuss?? && data.orderStatus??]
                            [#list orderStatuss as orderStatus]
                                [#if "${orderStatus.id}" == data.orderStatus]
                                ${orderStatus.name}
                                [/#if]
                            [/#list]
                        [/#if]
                        </td>
                        <th class="text-r">
                            支付状态:
                        </th>
                        <td>
                            [#if paymentStatuss?? && data.paymentStatus??]
                                [#list paymentStatuss as paymentStatus]
                                    [#if "${paymentStatus.id}" == data.paymentStatus]
                                        ${paymentStatus.name}
                                    [/#if]
                                [/#list]
                            [/#if]
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            配送状态:
                        </th>
                        <td>
                            [#if shippingStatuss?? && data.shippingStatus??]
                            [#list shippingStatuss as shippingStatus]
                                [#if "${shippingStatus.id}" == data.shippingStatus]
                                    ${shippingStatus.name}
                                [/#if]
                            [/#list]
                            [/#if]
                        </td>
                        <th class="text-r">
                            用户名:
                        </th>
                        <td>
                            ${data.member.username}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            订单金额:
                        </th>
                        <td>
                            ${data.Amount}
                        </td>
                        <th class="text-r">
                            已付金额:
                        </th>
                        <td>
                            ${data.amountPaid}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            商品重量:
                        </th>
                        <td>
                            ${data.Weight}
                        </td>
                        <th class="text-r">
                            商品数量:
                        </th>
                        <td>
                            ${data.Quantity}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            使用优惠券:
                        </th>
                        <td>
                            [#if data.couponCode??]
                                <span class="label label-success radius">是</span>
                            [#else]
                                <span class="label label-success radius">否</span>
                            [/#if]
                        </td>
                        <th class="text-r">
                            优惠券折扣:
                        </th>
                        <td>
                            ${data.couponDiscount}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            调整金额:
                        </th>
                        <td>
                            ${data.offsetAmount}
                        </td>
                        <th class="text-r">
                            赠送积分:
                        </th>
                        <td>
                            ${data.point}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            运费:
                        </th>
                        <td>
                            ${data.freight}
                        </td>
                        <th class="text-r">
                            支付手续费:
                        </th>
                        <td>
                            ${data.fee}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            支付方式:
                        </th>
                        <td>
                            [#if paymentMethods?? && data.paymentMethod??]
                            [#list paymentMethods as paymentMethod]
                                [#if "${paymentMethod.id}" == data.paymentMethod]
                                    ${paymentMethod.name}
                                [/#if]
                            [/#list]
                            [/#if]
                        </td>
                        <th class="text-r">
                            配送方式:
                        </th>
                        <td>
                            [#if shippingMethods?? && data.shippingMethod??]
                            [#list shippingMethods as shippingMethod]
                                [#if "${shippingMethod.id}" == data.shippingMethod]
                                    ${shippingMethod.name}
                                [/#if]
                            [/#list]
                            [/#if]
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            收货人:
                        </th>
                        <td>
                            ${data.consignee}
                        </td>
                        <th class="text-r">
                            地区:
                        </th>
                        <td>
                            ${data.areaName}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            地址:
                        </th>
                        <td>
                            ${data.address}
                        </td>
                        <th class="text-r">
                            邮编:
                        </th>
                        <td>
                            ${data.zipCode}
                        </td>
                    </tr>
                    <tr>
                        <th class="text-r">
                            电话:
                        </th>
                        <td>
                            ${data.phone}
                        </td>
                        <th class="text-r">
                            附言:
                        </th>
                        <td>
                            ${data.memo}
                        </td>
                    </tr>
                </table>
            </div>
            <div class="tabCon">
                <table class="table table-border table-bordered table-bg">
                    <thead>
                    <tr class="text-c">
                        <th>商品编号</th>
                        <th>商品名称</th>
                        <th>商家</th>
                        <th>联系电话</th>
                        <th>商品价格</th>
                        <th>数量</th>
                        <th>已发货数量</th>
                        <th>已退货数量</th>
                        <th>小计</th>
                    </tr>
                    </thead>
                    <tbody>
                    [#if data.orderItems??]
                        [#list data.orderItems as orderItem]
                           <tr class="text-c">
                            <td>${orderItem.product.sn}</td>
                            <td>${orderItem.name}</td>
                            <td>
                                [#if orderItem.member??]
                                    ${orderItem.member.username}
                                [/#if]
                            </td>
                            <td>
                                [#if orderItem.member??]
                                    ${orderItem.member.mobile}
                                [/#if]
                            </td>
                            <td>${orderItem.price}</td>
                            <td>${orderItem.quantity}</td>
                            <td>${orderItem.shippedQuantity}</td>
                            <td>${orderItem.returnQuantity}</td>
                            <td>${orderItem.subtotal}</td>
                        </tr>
                        [/#list]
                    [/#if]

                    </tbody>
                </table>
            </div>
            <div class="tabCon">
                <table class="table table-border table-bordered table-bg">
                    <thead>
                    <tr class="text-c">
                        <th>编号</th>
                        <th>方式</th>
                        <th>支付方式</th>
                        <th>付款金额</th>
                        <th>状态</th>
                        <th>付款日期</th>
                    </tr>
                    </thead>
                    <tbody>
                    [#if data.payments??]
                        [#list data.payments as payment]
                        <tr class="text-c">
                            <td>${payment.sn}</td>
                            <td>
                                [#list shoukuanMethods as shoukuanMethod]
                                   [#if "${shoukuanMethod.id}" == payment.method]
                                        ${shoukuanMethod.name}
                                    [/#if]
                                [/#list]
                            </td>
                            <td>${payment.paymentMethod}</td>
                            <td>${payment.amount}</td>
                            <td>
                                [#list shoukuanStatuss as shoukuanStatus]
                                    [#if "${shoukuanStatus.id}" == payment.status]
                                        ${shoukuanStatus.name}
                                    [/#if]
                                [/#list]
                            </td>
                            <td>${payment.paymentDate}</td>
                        </tr>
                        [/#list]
                    [/#if]
                    </tbody>
                </table>
            </div>
            <div class="tabCon">
                <table class="table table-border table-bordered table-bg">
                    <thead>
                    <tr class="text-c">
                        <th>编号</th>
                        <th>方式</th>
                        <th>退款方式</th>
                        <th>退款金额</th>
                        <th>状态</th>
                        <th>退款日期</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="text-c">
                    [#if data.refunds??]
                        [#list data.refunds as refund]
                        <tr class="text-c">
                            <td>${refund.sn}</td>
                            <td>
                                [#list tuikuanMethods as tuikuanMethod]
                                    [#if "${tuikuanMethod.id}" == refund.method]
                                        ${tuikuanMethod.name}
                                    [/#if]
                                [/#list]
                            </td>
                            <td>${refund.paymentMethod}</td>
                            <td>${refund.amount}</td>
                            <td>
                                [#list tuikuanStatuss as tuikuanStatus]
                                    [#if "${tuikuanStatus.id}" == refund.status]
                                        ${tuikuanStatus.name}
                                    [/#if]
                                [/#list]
                            </td>
                            <td>${refund.paymentDate}</td>
                        </tr>
                        [/#list]
                    [/#if]
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="tabCon">
                <table class="table table-border table-bordered table-bg">
                    <thead>
                    <tr class="text-c">
                        <th>类型</th>
                        <th>操作员</th>
                        <th>内容</th>
                        <th>创建日期</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="text-c">
                    [#if data.orderLogs??]
                        [#list data.orderLogs as orderLog]
                        <tr class="text-c">
                            <td>
                                [#list logTypes as logType]
                                    [#if "${logType.id}" == orderLog.type]
                                        ${logType.name}
                                    [/#if]
                                [/#list]
                            </td>
                            <td>${orderLog.operator}</td>
                            <td>${orderLog.content}</td>
                            <td>${orderLog.createDate}</td>
                        </tr>
                        [/#list]
                    [/#if]
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        [/#if]
    </form>
</div>

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${base}/resources/admin/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/jquery.validation/1.14.0/messages_zh.js"></script>

<script type="text/javascript">
    $(function(){
        $("#tab-system").Huitab({
            index:0
        });

    });


    function cancel(id){
        var $cancelId = $("#cancelId");
        layer.confirm('确定要关闭吗?',function(index){
            var load = layer.msg('关闭中...',{
                icon:16,
                shade:0.01
            });
            $.ajax({
                type: 'post',
                data:{
                    orderId:id
                },
                url:'${base}/admin/order/cancel.jhtml',
                dataType:'json',
                beforeSend:function(){
                    $cancelId.prop("disabled",true);
                },
                success:function(message){
                    layer.close(load);
                    if (message.type == 'success'){
                        //关闭当前页面
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.add_row(message.data);
                        //关闭弹窗并提示
                        parent.closeWindow(index,'关闭成功!');
                    }else{
                        $cancelId.prop("disabled",false);
                        parent.toast('关闭失败!',2);
                    }
                },
                error: function(XmlHttpRequest, textStatus, errorThrown){
                    $cancelId.prop("disabled", false);
                    layer.close(load);
                    parent.toast('关闭失败!',2);
                }
            });
        });
    }

    function confirm(id){
        var $confirmId = $("#confirmId");
        layer.confirm('确定要订单确认吗?',function(index){
            var load = layer.msg('确认中...',{
                icon:16,
                shade:0.01
            });
            $.ajax({
                type: 'post',
                data:{
                    orderId:id
                },
                url:'${base}/admin/order/confirm.jhtml',
                dataType:'json',
                beforeSend:function(){
                    $confirmId.prop("disabled",true);
                },
                success:function(message){
                    layer.close(load);
                    if (message.type == 'success'){
                        //关闭当前页面
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.add_row(message.data);
                        //关闭弹窗并提示
                        parent.closeWindow(index,'订单确认成功!');
                    }else{
                        $confirmId.prop("disabled",false);
                        parent.toast('订单确认失败!',2);
                    }
                },
                error: function(XmlHttpRequest, textStatus, errorThrown){
                    $confirmId.prop("disabled", false);
                    layer.close(load);
                    parent.toast('订单确认失败!',2);
                }
            });
        });
    }

    function shipping(id){
        var $shippingId = $("#shippingId");
        layer.confirm('确定要发货吗?',function(index){
            var load = layer.msg('发货中...',{
                icon:16,
                shade:0.01
            });
            $.ajax({
                type: 'post',
                data:{
                    orderId:id
                },
                url:'${base}/admin/order/shipping.jhtml',
                dataType:'json',
                beforeSend:function(){
                    $shippingId.prop("disabled",true);
                },
                success:function(message){
                    layer.close(load);
                    if (message.type == 'success'){
                        //关闭当前页面
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.add_row(message.data);
                        //关闭弹窗并提示
                        parent.closeWindow(index,'发货成功!');
                    }else{
                        $shippingId.prop("disabled",false);
                        parent.toast('发货失败!',2);
                    }
                },
                error: function(XmlHttpRequest, textStatus, errorThrown){
                    $shippingId.prop("disabled", false);
                    layer.close(load);
                    parent.toast('发货失败!',2);
                }
            });
        });
    }

</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>
