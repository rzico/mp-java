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
            <label class="form-label col-xs-4 col-sm-2">地址：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="address" name="address">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值0：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue0" name="attributeValue0">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值1：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue1" name="attributeValue1">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值2：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue2" name="attributeValue2">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值3：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue3" name="attributeValue3">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值4：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue4" name="attributeValue4">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值5：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue5" name="attributeValue5">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值6：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue6" name="attributeValue6">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值7：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue7" name="attributeValue7">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值8：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue8" name="attributeValue8">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值9：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="attributeValue9" name="attributeValue9">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">出生日期：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="" id="birth" name="birth" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">邮箱：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" placeholder="@" name="email" value="" id="email">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">性别：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                [#if genders??]
                [#list genders as gender]
                    <div class="radio-box">
                        <input name="gender" type="radio" id="gender-${gender_index}" value="${gender.id}">
                        <label for="gender-${gender_index}">${gender.name}</label>
                    </div>
                [/#list]
                [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>是否启用：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isEnabled" id="isEnabled" value="true">
                    <input type="hidden" name="_isEnabled" value="false" />
                    <label for="isEnabled">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>是否锁定：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isLocked" id="isLocked" value="true">
                    <input type="hidden" name="_isLocked" value="false" />
                    <label for="isLocked">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">锁定日期：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="" id="lockedDate" name="lockedDate" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">最后登录日期：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="" id="loginDate" name="loginDate" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">连续登录失败次数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="loginFailureCount" value="" placeholder="" id="loginFailureCount" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">最后登录IP：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="loginIp" name="loginIp">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">手机：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="mobile" name="mobile">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">姓名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="name" name="name">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>密码：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="password" class="input-text" value="" placeholder="" id="password" name="password">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">电话：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="phone" name="phone">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">积分：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="point" value="" placeholder="" id="point" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">注册IP：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="registerIp" name="registerIp">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">SafeKeyCreate：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="" id="safeKeyCreate" name="safeKeyCreate" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">SafeKeyExpire：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="" id="safeKeyExpire" name="safeKeyExpire" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">SafeKeyValue：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="safeKeyValue" name="safeKeyValue">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>用户名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="username" name="username">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">邮编：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="zipCode" name="zipCode">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">所在地：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span class="fieldSet">
                    <input type="hidden" id="areaId" name="areaId" treePath="" />
                </span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">签名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="autograph" name="autograph">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">头像：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="logo" name="logo">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">昵称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="nickName" name="nickName">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">位置：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="addr" name="addr">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">伟度：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="lat" value="" placeholder="" id="lat" onInput="floatInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">经度：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="lng" value="" placeholder="" id="lng" onInput="floatInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">职业：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if occupations??]
				<select name="occupationId" class="select" style="background-color: #FFFFFF">
                    [#list occupations as occupation]
					<option value="${occupation.id}">${occupation.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">设备号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="uuid" name="uuid">
            </div>
        </div>

		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-2">tags：</label>
			<div class="formControls col-xs-8 col-sm-9 skin-minimal">
				[#if tags??]
				[#list tags as tag]
				<div class="check-box">
					<label class=""><input type="checkbox" value="${tag.id}" name="tagIds" >${tag.name}</label>
				</div>
				[/#list]
				[/#if]
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
                var $areaId = $("#areaId");
                $areaId.lSelect({
                    url: "${base}/admin/common/area.jhtml"
                });

                var $submit = $(":submit");
                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                $("#form-add").validate({
                    rules:{
                        balance:{
                            required:true,
                        },
                        username:{
                            required:true,
                            minlength:4,
                            maxlength:16

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
                            url: "${base}/admin/member/save.jhtml" ,
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
