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
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>编号：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span>${data.sn}</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>申请人：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span> ${data.member.nickName}</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>开户名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span> ${data.name}</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>银行账号：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span> ${data.cardno}</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>银行名称：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span> ${data.bankname}</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>所属城市：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span> ${data.city}</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>提现类型：</label>
                <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                    <span> ${message("Transfer.Type."+data.type)}</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>当前状态：</label>
                <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                    <span> ${message("Transfer.Status."+data.status)}</span>
                </div>
            </div>
            <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">备注：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span>${data.memo}</span>
            </div>
        </div>

             <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">操作员：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span>${data.operator}</span>
                </div>
            </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"></label>
            <div class="formControls col-xs-8 col-sm-9">
                [#if data.status=="waiting"]
                    [@adminDirective]
                        [#if !(admin.role?contains("3"))||admin.role?contains("1")||admin.role?contains("2")]
                            <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交付款&nbsp;&nbsp;">
                        [/#if]
                    [/@adminDirective]
                [/#if]
                [#if data.status=="confirmed"]
                    <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;查询状态&nbsp;&nbsp;">
                [/#if]
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
                        amount:{
                            required:true,
                        },
                        sn:{
                            required:true,
                        },
                        status:{
                            required:true,
                        },
                        type:{
                            required:true,
                        },
                        member:{
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
                            url: "${base}/admin/transfer/update.jhtml" ,
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
                                    parent.closeWindow(index, message.content);
                                }else{
                                    $submit.prop("disabled", false);
                                    parent.toast(message.content,2);
                                }
                            },
                            error: function(XmlHttpRequest, textStatus, errorThrown){
                                $submit.prop("disabled", false);
                                layer.close(load);
                                parent.toast('执行失败',2);
                            }
                        });
                    }
                });
            });

            /*编辑*/
            function edit(id) {
                var w_1 = window.innerWidth * 0.3;
                var h_1 = window.innerHeight * 0.5;
                layer_show("手动转账", "../transfer/manualTransfer.jhtml?id="+id, w_1, h_1);
            }

            /*刷新数据*/
            function rewrite(data) {
                var index = parent.layer.getFrameIndex(window.name);
                parent.add_row(data);
                parent.closeWindow(index, '提交成功');
            }

            /*关闭页面*/
            function closeWindow(index, msg) {
                layer.close(index);
                layer.msg(msg, {icon: 1, time: 1000});
            }

        </script>
</body>
</html>
