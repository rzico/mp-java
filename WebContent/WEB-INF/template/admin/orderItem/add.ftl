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
    <form action="" method="post" class="form form-horizontal" id="form-add">
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>商品名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="name" name="name">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>数量：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="quantity" value="" placeholder="" id="quantity" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>已退货数量：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="returnQuantity" value="" placeholder="" id="returnQuantity" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>已发货数量：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="shippedQuantity" value="" placeholder="" id="shippedQuantity" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>商品编号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="sn" name="sn">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">商品规格：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="spec" name="spec">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>订单：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if orderss??]
				<select name="ordersId" class="select" style="background-color: #FFFFFF">
                    [#list orderss as orders]
					<option value="${orders.id}">${orders.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">产品：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if products??]
				<select name="productId" class="select" style="background-color: #FFFFFF">
                    [#list products as product]
					<option value="${product.id}">${product.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"></label>
            <div class="formControls col-xs-8 col-sm-9">
                <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
            </div>
        </div>

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
        <script type="text/javascript" src="${base}/resources/admin/js/wx.js"></script>

        <script type="text/javascript">
            $(function(){
                var $submit = $(":submit");
                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                $("#form-add").validate({
                    rules:{
                        isGift:{
                            required:true,
                        },
                        name:{
                            required:true,
                        },
                        price:{
                            required:true,
                        },
                        quantity:{
                            required:true,
                        },
                        returnQuantity:{
                            required:true,
                        },
                        shippedQuantity:{
                            required:true,
                        },
                        sn:{
                            required:true,
                        },
                        thumbnail:{
                            required:true,
                        },
                        weight:{
                            required:true,
                        },
                        orders:{
                            required:true,
                        },

                    },
                    onkeyup:false,
                    focusCleanup:true,
                    success:"valid",
                    ignore:"",
                    submitHandler:function(form){
                        var load = layer.msg('加载中', {
                            icon: 16
                            ,shade: 0.01
                        });
                        $(form).ajaxSubmit({
                            type: 'post',
                            url: "${base}/admin/orderItem/save.jhtml" ,
                            beforeSend: function() {
                               $submit.prop("disabled", true);
                            },
                            success: function(message){
                                layer.close(load);
                                if(message.type ==  "success"){
//                                    关闭当前页面
                                    var index = parent.layer.getFrameIndex(window.name);
                                    parent.add_row(message.data);
                                    parent.closeWindow(index, '添加成功');
                                }else{
                                    $submit.prop("disabled", false);
                                    layer.msg('添加失败!',{icon:2,time:1000});
                                }
                            },
                            error: function(XmlHttpRequest, textStatus, errorThrown){
                                $submit.prop("disabled", false);
                                layer.close(load);
                                layer.msg('error!',{icon:2,time:1000});
                            }
                        });
                    }
                });
            });
        </script>
</body>
</html>
