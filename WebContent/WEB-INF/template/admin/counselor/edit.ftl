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
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.name}" placeholder="" id="name" name="name">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">头像：</label>
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
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>电话：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.phone}" placeholder="" id="phone" name="phone">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>头街：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.speciality}" placeholder="多个用,号分隔" id="speciality" name="speciality">
            </div>
        </div>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>签名：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.autograph}" placeholder="" id="autograph" name="autograph">
            </div>
        </div>

             <div class="row cl">
                 <label class="form-label col-xs-4 col-sm-2">检签：</label>
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
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>简介：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content1" id="content1" style="height:200px;width:500px;">${data.content1}</textarea>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>教育经历：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content2" id="content2" style="height:200px;width:500px;">${data.content2}</textarea>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>工作经历：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content3" id="content3" style="height:200px;width:500px;">${data.content3}</textarea>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>行业执照：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content4" id="content4" style="height:200px;width:500px;">${data.content4}</textarea>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>治疗取向：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content5" id="content5" style="height:200px;width:500px;">${data.content5}</textarea>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>咨询经验：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content6" id="content6" style="height:200px;width:500px;">${data.content6}</textarea>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>擅长领域：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content7" id="content7" style="height:200px;width:500px;">${data.content7}</textarea>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>咨询方式与费用：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content8" id="content8" style="height:200px;width:500px;">${data.content8}</textarea>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>状态：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                [#if statuss??]
                [#list statuss as status]
                    <div class="radio-box">
                        <input name="status" type="radio" id="status-${status_index}" value="${status.id}"[#if status.id == data.status] checked[/#if]>
                        <label for="status-${status_index}">${status.name}</label>
                    </div>
                [/#list]
                [/#if]
            </div>
        </div>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">排序：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.orders}" placeholder="" id="orders" name="orders" onInput="intInit(this)">
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

                var ue = UE.getEditor('content');
                ue.ready(function() {//编辑器初始化完成再赋值
                    ue.setContent('${data.content}');
                });


                $("#form-update").validate({
                    rules:{
                        autograph:{
                            required:true,
                        },
                        logo:{
                            required:true,
                        },
                        name:{
                            required:true,
                        },
                        speciality:{
                            required:true,
                        },
                        status:{
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
                            url: "${base}/admin/counselor/update.jhtml" ,
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
