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
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>内容：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.content}" placeholder="" id="content" name="content">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>是否显示：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isShow" id="isShow" value="true"[#if data.isShow?? && data.isShow] checked[/#if]>
                    <input type="hidden" name="_isShow" value="false" />
                    <label for="isShow">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>收件人已读：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="readed" id="readed" value="true"[#if data.readed?? && data.readed] checked[/#if]>
                    <input type="hidden" name="_readed" value="false" />
                    <label for="readed">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">缩例图：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.thumbnial}" placeholder="" id="thumbnial" name="thumbnial">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>标题：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.title}" placeholder="" id="title" name="title">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>类型：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                [#if types??]
                [#list types as type]
                    <div class="radio-box">
                        <input name="type" type="radio" id="type-${type_index}" value="${type.id}"[#if type.id == data.type] checked[/#if]>
                        <label for="type-${type_index}">${type.name}</label>
                    </div>
                [/#list]
                [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">提供者：</label>
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
            <label class="form-label col-xs-4 col-sm-2">订阅者：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if receivers??]
				<select name="receiverId" class="select" style="background-color: #FFFFFF">
                    [#list receivers as receiver]
					<option[#if data.receiver?? && receiver.id == data.receiver.id] selected[/#if] value="${receiver.id}">${receiver.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>是否删除：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="deleted" id="deleted" value="true"[#if data.deleted?? && data.deleted] checked[/#if]>
                    <input type="hidden" name="_deleted" value="false" />
                    <label for="deleted">&nbsp;</label>
                </div>
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
                var $submit = $(":submit");
                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                $("#form-update").validate({
                    rules:{
                        content:{
                            required:true,
                        },
                        title:{
                            required:true,
                        },
                        type:{
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
                            url: "${base}/admin/message/update.jhtml" ,
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
