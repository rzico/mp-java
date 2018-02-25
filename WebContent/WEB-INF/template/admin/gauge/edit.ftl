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
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>主标题：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.title}" placeholder="" id="title" name="title">
                </div>
            </div>


            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>副标题：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.subTitle}" placeholder="" id="subTitle" name="subTitle">
                </div>
            </div>


            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>缩例图：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <div class="uploader-thum-container">
                        <div id="fileList" class="uploader-list">
                            [#if data.thumbnail??]
                                <div class="file-item thumbnail">
                                    <img width="100px" height="100px" src="${data.thumbnail}"/>
                                    <div class="info"></div>
                                </div>'
                            [/#if]
                        </div>
                        <div id="filePicker">选择图片</div>
                        <input type="hidden" value="${data.thumbnail}" id="thumbnail" name="thumbnail">
                    </div>
                </div>
            </div>


            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">分类：</label>
                <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                    [#if gaugeCategorys??]
                        <select name="articleCategoryId" class="select" style="background-color: #FFFFFF">
                            [#list gaugeCategorys as gaugeCategory]
                                <option [#if data.gaugeCategory?? && gaugeCategory.id == data.gaugeCategory.id] selected[/#if] value="${gaugeCategory.id}">${gaugeCategory.name}</option>
                            [/#list]
				</select>
                    [/#if]
				</span>
				</span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>常模类型：</label>
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
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>用户类型：</label>
                <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                    [#if userTypes??]
                        [#list userTypes as userType]
                            <div class="radio-box">
                                <input name="userType" type="radio" id="userType-${userType_index}" value="${userType.id}"[#if userType.id == data.userType] checked[/#if]>
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
                        <div id="contentFileList" class="uploader-list">
                            [#if data.content??]
                                <div class="file-item thumbnail">
                                    <img width="100px" height="100px" src="${data.content}"/>
                                    <div class="info"></div>
                                </div>'
                            [/#if]
                        </div>
                        <div id="contentFilePicker">选择图片</div>
                        <input type="hidden" value="${data.content}" id="content" name="content">
                    </div>
                </div>
            </div>


            <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>测评人数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.evaluation}" placeholder="" id="evaluation" name="evaluation" onInput="intInit(this)">
            </div>
        </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>亮点介绍：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    [#if data.spots?? && data.spots?size>0]
                        [#list data.spots as spot]
                            <input type="text" class="input-text" style="width:100px;" value="${spot}" name="spots" placeholder="" id="spots" >
                        [/#list]
                    [#else]
                        <input type="text" class="input-text" style="width:100px;" name="spots" value="" placeholder="" id="spots1" >
                        <input type="text" class="input-text" style="width:100px;" name="spots" value="" placeholder="" id="spots2" >
                    [/#if]
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>测评须知：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <textarea class="input-text" name="notice" id="notice" value="${data.notice}" style="height:100px;width:300px;">${data.notice}</textarea>
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>常模修订说明：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <textarea class="input-text" name="revisionNote" id="revisionNote" value="${data.revisionNote}" style="height:100px;width:300px;">${data.revisionNote}</textarea>
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>市场价：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.price}" placeholder="" id="price" name="price" onInput="floatInit(this)">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>销售价：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.marketPrice}" placeholder="" id="marketPrice" name="marketPrice" onInput="floatInit(this)">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>推广佣金（%）：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.brokerage}" placeholder="" id="brokerage" name="brokerage" onInput="percentInit(this)">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>分销佣金（%）：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.distribution}" placeholder="" id="distribution" name="distribution" onInput="percentInit(this)">
                </div>
            </div>


            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">标签：</label>
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

                $("#form-update").validate({
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
                    submitHandler:function(form){
                        var load = layer.msg('加载中', {
                            icon: 16
                            ,shade: 0.01
                        });
                        $(form).ajaxSubmit({
                            type: 'post',
                            url: "${base}/admin/gauge/update.jhtml" ,
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
