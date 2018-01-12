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
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>用户名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.username}" placeholder="" id="username" name="username" readonly="readonly" style="background-color:#E6E6FA">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">真实姓名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.name}" placeholder="" id="name" name="name" [#if data.id=="1"] readonly="readonly" style="background-color:#E6E6FA"[/#if]>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">邮箱：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" placeholder="@" name="email" value="${data.email}" id="email">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">角色：</label>
                <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                    [#if roles??]
                        [#list roles as role]
                            <div class="check-box">
                                [#assign checkUp = "false"]
                                [#list data.roles as dataRole]
                                    [#if dataRole.id == role.id]
                                        [#assign checkUp = "true"]
                                    [/#if]
                                [/#list]
                                <label class=""><input type="checkbox"[#if checkUp == "true"] checked[/#if] value="${role.id}" name="roleIds" >${role.name}</label>
                            </div>
                        [/#list]
                    [/#if]
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">企业：</label>
                <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                    [#if enterprises??]
                        <select name="enterpriseId" class="select" style="background-color: #FFFFFF">
                            <option value="0"></option>
                            [#list enterprises as enterprise]
                                <option[#if data.enterprise?? && enterprise.id == data.enterprise.id] selected[/#if] value="${enterprise.id}">${enterprise.name}</option>
                            [/#list]
				</select>
                    [/#if]
				</span>
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
                <label class="form-label col-xs-4 col-sm-2">所在地：</label>
                <div class="formControls col-xs-8 col-sm-9">
                <span class="fieldSet">
                    <input type="hidden" id="areaId" name="areaId" value="${(data.area.id)!}" treePath="${(data.area.treePath)!}" />
                </span>
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
                <span>${data.lockedDate}</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">最后登录日期：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span>${data.loginDate}</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>登录失败次数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span>${data.loginFailureCount}</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">最后登录IP：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span>${data.loginIp}</span>
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
                        loginFailureCount:{
                            required:true,
                        },
                        password:{
                            required:true,
                            minlength:6,
                            maxlength:16

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
                            url: "${base}/admin/admin/update.jhtml" ,
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
