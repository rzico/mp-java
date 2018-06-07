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
    <div class="row cl">
        <label class="form-label col-xs-4 col-sm-2"></label>
        <div class="formControls col-xs-8 col-sm-9">
            <input type="text" class="input-text" value="" placeholder="请输入手机号或邮箱" id="mobilemail" name="mobilemail" style="width:54%;">
            <button type="submit" class="btn btn-success radius" id="" onclick="search();" name="">
                <i class="Hui-iconfont">&#xe665;</i> 查询
            </button>
        </div>
    </div>
    <form action="" method="post" class="form form-horizontal" id="form-update">
        <input type="text" class="input-text" value="" placeholder="" hidden="hidden" id="memberId" name="memberId">
        <input type="number" value="${data.id}" style="display:none" name="id">
        [#if data??]
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.name}" placeholder="" id="name" name="name">
            </div>
        </div>


            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">缩例图：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <div class="uploader-thum-container">
                        <div id="fileList" class="uploader-list">
                            [#if data.logo??]
                                <div class="file-item thumbnail">
                                    <img width="100px" height="100px" src="${data.logo}"/>
                                    <div class="info"></div>
                                </div>'
                            [/#if]
                        </div>
                        <div id="filePicker">选择图片</div>
                        <input type="hidden" value="${data.logo}" id="logo" name="logo">
                    </div>
                </div>
            </div>

         <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">QQ号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.qq}" placeholder="" id="qq" name="qq">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">微信号：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.wechat}" placeholder="" id="wechat" name="wechat">
            </div>
        </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">是否在线：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="checkbox" name="online" id="online" value="true"[#if data.online] checked[/#if]>
                    <input type="hidden" name="_online" value="false" />
                    <label for="isPublish">&nbsp;</label>
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">描述：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.description}" placeholder="" id="description" name="description">
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

<script type="text/javascript" src="${base}/resources/admin/lib/webuploader/0.1.5/webuploader.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/ueditor/1.4.3/ueditor.config.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/ueditor/1.4.3/ueditor.all.min.js"> </script>
<script type="text/javascript" src="${base}/resources/admin/lib/ueditor/1.4.3/lang/zh-cn/zh-cn.js"></script>

<script type="text/javascript" src="${base}/resources/admin/js/uploader.js"></script>
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
                        name:{
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
                            url: "${base}/admin/customService/update.jhtml" ,
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




            /* 搜索 */
            function search(){
                var url = "/admin/enterprise/getMemberInfo.jhtml?phone="+$("#mobilemail").val();

                layer.confirm("请确认手机号？",function(index){
                    var load = layer.msg("查询中..",{
                        icon:16,shade:0.01
                    });
                    $.ajax({
                        type:'get',
                        url:url,
                        dataType:'json',
                        success:function(data){
                            layer.close(load);
                            if(data.type == "success"){
                                $.each(data.data, function (infoIndex, info){
                                    if (info["id"] == "id"){
                                        $("#memberId").val(info["name"]);
                                    }
                                })
                                layer.msg('读取成功!',{icon:16,time:1000});
                            }else{
                                layer.msg('读取失败!',{icon:16,time:1000});
                            }
                        },
                        error:function(data){
                            layer.close(load);
                            layer.msg('读取失败!',{icon:16,time:1000});
                        },
                    });
                });
            }

        </script>
</body>
</html>
