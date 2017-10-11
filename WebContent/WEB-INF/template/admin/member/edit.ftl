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
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/lib/Hui-iconfont/1.0.8/iconfont.css" />

    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/css/wx.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->

    <link href="http://cdn.rzico.com/weex/resources/lib/webuploader/0.1.5/webuploader.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="page-container">
    <form action="" method="post" class="form form-horizontal" id="form-update">
        <input type="number" value="${data.id}" style="display:none" name="id">
        [#if data??]
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">地址：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.address}" placeholder="" id="address" name="address">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值0：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue0}" placeholder="" id="attributeValue0" name="attributeValue0">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值1：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue1}" placeholder="" id="attributeValue1" name="attributeValue1">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值2：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue2}" placeholder="" id="attributeValue2" name="attributeValue2">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值3：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue3}" placeholder="" id="attributeValue3" name="attributeValue3">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值4：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue4}" placeholder="" id="attributeValue4" name="attributeValue4">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值5：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue5}" placeholder="" id="attributeValue5" name="attributeValue5">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值6：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue6}" placeholder="" id="attributeValue6" name="attributeValue6">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值7：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue7}" placeholder="" id="attributeValue7" name="attributeValue7">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值8：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue8}" placeholder="" id="attributeValue8" name="attributeValue8">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">会员注册项值9：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.attributeValue9}" placeholder="" id="attributeValue9" name="attributeValue9">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">出生日期：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="${data.birth}" id="birth" name="birth" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">邮箱：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" placeholder="@" name="email" value="${data.email}" id="email">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">性别：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                [#if genders??]
                [#list genders as gender]
                    <div class="radio-box">
                        <input name="gender" type="radio" id="gender-${gender_index}" value="${gender.id}"[#if gender.id == data.gender] checked[/#if]>
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
                    <input type="checkbox" name="isEnabled" id="isEnabled" value="true"[#if data.isEnabled?? && data.isEnabled] checked[/#if]>
                    <input type="hidden" name="_isEnabled" value="false" />
                    <label for="isEnabled">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>是否锁定：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isLocked" id="isLocked" value="true"[#if data.isLocked?? && data.isLocked] checked[/#if]>
                    <input type="hidden" name="_isLocked" value="false" />
                    <label for="isLocked">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">锁定日期：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="${data.lockedDate}" id="lockedDate" name="lockedDate" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">最后登录日期：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="${data.loginDate}" id="loginDate" name="loginDate" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">连续登录失败次数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.loginFailureCount}" placeholder="" id="loginFailureCount" name="loginFailureCount" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">最后登录IP：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.loginIp}" placeholder="" id="loginIp" name="loginIp">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">手机：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.mobile}" placeholder="" id="mobile" name="mobile">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">姓名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.name}" placeholder="" id="name" name="name">
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
                <input type="text" class="input-text" value="${data.phone}" placeholder="" id="phone" name="phone">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">积分：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.point}" placeholder="" id="point" name="point" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">注册IP：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.registerIp}" placeholder="" id="registerIp" name="registerIp">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">SafeKeyCreate：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="${data.safeKeyCreate}" id="safeKeyCreate" name="safeKeyCreate" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">SafeKeyExpire：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="${data.safeKeyExpire}" id="safeKeyExpire" name="safeKeyExpire" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">SafeKeyValue：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.safeKeyValue}" placeholder="" id="safeKeyValue" name="safeKeyValue">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>用户名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.username}" placeholder="" id="username" name="username">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">邮编：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.zipCode}" placeholder="" id="zipCode" name="zipCode">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">所在地：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span class="fieldSet">
                    <input type="hidden" id="areaId" name="areaId" value="${(data.area.id)!}" treePath="${(data.area.treePath)!}" />
                </span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">签名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.autograph}" placeholder="" id="autograph" name="autograph">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">头像：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.logo}" placeholder="" id="logo" name="logo">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">昵称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.nickName}" placeholder="" id="nickName" name="nickName">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">位置：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.addr}" placeholder="" id="addr" name="addr">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">伟度：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.lat}" placeholder="" id="lat" name="lat" onInput="floatInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">经度：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.lng}" placeholder="" id="lng" name="lng" onInput="floatInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">职业：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if occupations??]
				<select name="occupationId" class="select" style="background-color: #FFFFFF">
                    [#list occupations as occupation]
					<option[#if data.occupation?? && occupation.id == data.occupation.id] selected[/#if] value="${occupation.id}">${occupation.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">设备号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.uuid}" placeholder="" id="uuid" name="uuid">
            </div>
        </div>

		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-2">tags：</label>
			<div class="formControls col-xs-8 col-sm-9 skin-minimal">
				[#if tags??]
				[#list tags as tag]
				<div class="check-box">
					[#assign checkUp = "false"]
					[#list data.tags as dataTag]
					[#if dataTag.id == tag.id]
					[#assign checkUp = "true"]
					[/#if]
					[/#list]
					<label class=""><input type="checkbox"[#if checkUp == "true"] checked[/#if] value="${tag.id}" name="tagIds" >${tag.name}</label>
				</div>
				[/#list]
				[/#if]
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
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery/1.9.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/layer/2.4/layer.js"></script>
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/h-ui/js/H-ui.min.js"></script>
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

        <!--请在下方写此页面业务相关的脚本-->
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/My97DatePicker/4.8/WdatePicker.js"></script>
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery.validation/1.14.0/validate-methods.js"></script>
        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery.validation/1.14.0/messages_zh.js"></script>

        <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery.ISelect/jquery.lSelect.js"></script>
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

                $("#form-update").validate({
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
                    submitHandler:function(form){
                        var load = layer.msg('加载中', {
                            icon: 16
                            ,shade: 0.01
                        });
                        $(form).ajaxSubmit({
                            type: 'post',
                            url: "${base}/admin/member/update.jhtml" ,
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
