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
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->
    <link href="${base}/resources/admin/lib/webuploader/0.1.5/webuploader.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="page-container">
    <form action="" method="post" class="form form-horizontal" id="form-add">

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>主标题：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="title" name="title">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>副标题：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="subTitle" name="subTitle">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>缩例图：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <div class="uploader-thum-container">
                    <div id="fileList" class="uploader-list"></div>
                    <div id="filePicker">选择图片</div>
                    <input type="hidden" value="" id="thumbnail" name="thumbnail">
                </div>
            </div>
        </div>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">分类：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
            [#if gaugeCategorys??]
                <select name="gaugeCategoryId" class="select" style="background-color: #FFFFFF">
                    [#list gaugeCategorys as gaugeCategory]
                        <option value="${gaugeCategory.id}">${gaugeCategory.name}</option>
                    [/#list]
				</select>
            [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>常模类型：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
            [#if types??]
                [#list types as type]
                    <div class="radio-box">
                        <input name="type" type="radio" id="type-${type_index}" value="${type.id}">
                        <label for="type-${type_index}">${type.name}</label>
                    </div>
                [/#list]
            [/#if]
            </div>
        </div>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>用户类型：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
            [#if userTypes??]
                [#list userTypes as userType]
                    <div class="radio-box">
                        <input name="userType" type="radio" id="userType-${userType_index}" value="${userType.id}">
                        <label for="userType-${userType_index}">${userType.name}</label>
                    </div>
                [/#list]
            [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>测评简介：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <div class="uploader-thum-container">
                    <div id="contentFileList" class="uploader-list"></div>
                    <div id="contentFilePicker">选择图片</div>
                    <input type="hidden" value="" id="content" name="content">
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>测评人数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="evaluation" value="" placeholder="" id="evaluation" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>亮点介绍：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" style="width:100px;" name="spots" value="" placeholder="" id="spots1" >
                <input type="text" class="input-text" style="width:100px;" name="spots" value="" placeholder="" id="spots2" >
            </div>
        </div>

       <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>测评须知：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="notice" id="notice" style="height:100px;width:300px;"></textarea>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>常模修订说明：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    <textarea class="input-text" name="revisionNote" id="revisionNote" style="height:100px;width:300px;"></textarea>
            </div>
        </div>

                    <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>市场价：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="" placeholder="" id="price" name="price" onInput="floatInit(this)">
                    </div>
                    </div>

                    <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>销售价：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="" placeholder="" id="marketPrice" name="marketPrice" onInput="floatInit(this)">
                    </div>
                    </div>

                    <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>推广佣金（%）：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="0.6" placeholder="" id="brokerage" name="brokerage" onInput="percentInit(this)">
                    </div>
                    </div>

                    <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>分销佣金（%）：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="0.6" placeholder="" id="distribution" name="distribution" onInput="percentInit(this)">
                    </div>
                    </div>


                    <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-2">标签：</label>
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

                <script type="text/javascript" src="${base}/resources/admin/lib/webuploader/0.1.5/webuploader.min.js"></script>

                <script type="text/javascript" src="${base}/resources/admin/js/uploader.js"></script>

        <script type="text/javascript">
            $(function(){
                var $submit = $(":submit");
                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                new $uploadpicture("contentFileList","contentFilePicker");

                $("#form-add").validate({
                    rules:{
                        brokerage:{
                            required:true,
                        },
                        distribution:{
                            required:true,
                        },
                        evaluation:{
                            required:true,
                        },
                        marketPrice:{
                            required:true,
                        },
                        notice:{
                            required:true,
                        },
                        price:{
                            required:true,
                        },
                        revisionNote:{
                            required:true,
                        },
                        subTitle:{
                            required:true,
                        },
                        title:{
                            required:true,
                        },
                        type:{
                            required:true,
                        },
                        userType:{
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
                            url: "${base}/admin/gauge/save.jhtml" ,
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
