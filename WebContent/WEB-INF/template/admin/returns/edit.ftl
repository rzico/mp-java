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
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/css/wx.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->

    <link href="${base}/resources/admin/lib/webuploader/0.1.5/webuploader.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="page-container">
    <form action="" method="post" class="form form-horizontal" id="form-update">
        <input type="number" value="${data.id}" style="display:none" name="id">
        [#if data??]
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>发货人地址：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.address}" placeholder="" id="address" name="address">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>地区名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.areaName}" placeholder="" id="areaName" name="areaName">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">物流公司：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.deliveryCorp}" placeholder="" id="deliveryCorp" name="deliveryCorp">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">备注：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.memo}" placeholder="" id="memo" name="memo">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">操作员：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.operator}" placeholder="" id="operator" name="operator">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">发货人电话：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.phone}" placeholder="" id="phone" name="phone">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>发货人：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.shipper}" placeholder="" id="shipper" name="shipper">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">配送方式：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.shippingMethod}" placeholder="" id="shippingMethod" name="shippingMethod">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>编号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.sn}" placeholder="" id="sn" name="sn">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">运单号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.trackingNo}" placeholder="" id="trackingNo" name="trackingNo">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">发货人邮编：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.zipCode}" placeholder="" id="zipCode" name="zipCode">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>买家：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if members??]
				<select name="memberId" class="select" style="background-color: #FFFFFF">
                    [#list members as member]
					<option[#if data.member?? && member.id == data.member.id] selected[/#if] value="${member.id}">${member.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>订单：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if orderss??]
				<select name="ordersId" class="select" style="background-color: #FFFFFF">
                    [#list orderss as orders]
					<option[#if data.orders?? && orders.id == data.orders.id] selected[/#if] value="${orders.id}">${orders.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>卖家：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if sellers??]
				<select name="sellerId" class="select" style="background-color: #FFFFFF">
                    [#list sellers as seller]
					<option[#if data.seller?? && seller.id == data.seller.id] selected[/#if] value="${seller.id}">${seller.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"></label>
            <div class="formControls col-xs-8 col-sm-9">
                <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;修改&nbsp;&nbsp;">
            </div>
        </div>
            [#else]
            查找失败
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

        <script type="text/javascript" src="${base}/resources/admin/lib/jquery.ISelect/jquery.lSelect.js"></script>
        <script type="text/javascript">
            $(function(){
                var $submit = $(":submit");
                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                $("#form-update").validate({
                    rules:{
                        address:{
                            required:true,
                        },
                        areaName:{
                            required:true,
                        },
                        freight:{
                            required:true,
                        },
                        shipper:{
                            required:true,
                        },
                        sn:{
                            required:true,
                        },
                        member:{
                            required:true,
                        },
                        orders:{
                            required:true,
                        },
                        seller:{
                            required:true,
                        },

                    },
                    onkeyup:false,
                    focusCleanup:true,
                    success:"valid",
                    submitHandler:function(form){
                        var load = layer.msg('加载中', {
                            icon: 16
                            ,shade: 0.01
                        });
                        $(form).ajaxSubmit({
                            type: 'post',
                            url: "${base}/admin/returns/update.jhtml" ,
                            beforeSend: function() {
                                $submit.prop("disabled", true);
                            },
                            success: function(message){
                                layer.close(load);
                                if(message.type ==  "success"){
//                                    关闭当前页面
                                    var index = parent.layer.getFrameIndex(window.name);
                                    parent.add_row(message.data);
                                    //关闭弹窗并提示
                                    parent.closeWindow(index, '修改成功');
                                }else{
                                    $submit.prop("disabled", false);
                                    parent.toast('修改失败',2);
                                }
                            },
                            error: function(XmlHttpRequest, textStatus, errorThrown){
                                $submit.prop("disabled", false);
                                layer.close(load);
                                parent.toast('修改失败',2);
                            }
                        });
                    }
                });
            });
        </script>
</body>
</html>
