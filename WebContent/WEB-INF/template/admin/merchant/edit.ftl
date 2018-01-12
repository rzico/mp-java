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
            <label class="form-label col-xs-4 col-sm-2">经营地址：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.address}" placeholder="" id="address" name="address">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">开户银行：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.bankName}" placeholder="" id="bankName" name="bankName">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">支行名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.branchBankName}" placeholder="" id="branchBankName" name="branchBankName">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">银行城市：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.cardCity}" placeholder="" id="cardCity" name="cardCity">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">银行卡号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.cardNo}" placeholder="" id="cardNo" name="cardNo">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">银行省份：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.cardProvince}" placeholder="" id="cardProvince" name="cardProvince">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">经营城市：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.city}" placeholder="" id="city" name="city">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">邮箱：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" placeholder="@" name="email" value="${data.email}" id="email">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">身份证：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.idCard}" placeholder="" id="idCard" name="idCard">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">行业类型：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.industryType}" placeholder="" id="industryType" name="industryType">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">营业执照：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.licenseNo}" placeholder="" id="licenseNo" name="licenseNo">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>商户姓名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.merchantName}" placeholder="" id="merchantName" name="merchantName">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">商户编号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.merchantNo}" placeholder="" id="merchantNo" name="merchantNo">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">手机号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.phone}" placeholder="" id="phone" name="phone">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">经营省份：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.province}" placeholder="" id="province" name="province">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">商户名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.scompany}" placeholder="" id="scompany" name="scompany">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">唯一标识：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.userId}" placeholder="" id="userId" name="userId">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">Enterprise：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if enterprises??]
				<select name="enterpriseId" class="select" style="background-color: #FFFFFF">
                    [#list enterprises as enterprise]
					<option[#if data.enterprise?? && enterprise.id == data.enterprise.id] selected[/#if] value="${enterprise.id}">${enterprise.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">Owner：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if owners??]
				<select name="ownerId" class="select" style="background-color: #FFFFFF">
                    [#list owners as owner]
					<option[#if data.owner?? && owner.id == data.owner.id] selected[/#if] value="${owner.id}">${owner.name}</option>
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
                        brokerage:{
                            required:true,
                        },
                        merchantName:{
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
                            url: "${base}/admin/merchant/update.jhtml" ,
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
