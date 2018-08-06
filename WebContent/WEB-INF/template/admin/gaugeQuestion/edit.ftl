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

            <input type="hidden" value="${gaugeId}" id="gaugeId" name="gaugeId">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>题目：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text"  value="${data.title}" placeholder="" id="title" name="title">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>题型：</label>
                <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                    [#if questionTypes??]
                        [#list questionTypes as questionType]
                            <div class="radio-box">
                                <input name="questionType" type="radio" id="type-${questionType_index}" value="${questionType.id}"[#if questionType.id == data.questionType] checked[/#if]>
                                <label for="questionType-${questionType_index}">${questionType.name}</label>
                            </div>
                        [/#list]
                    [/#if]
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>选项类型：</label>
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
                <label class="form-label col-xs-4 col-sm-2">选项：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <table class="table table-border table-bordered table-bg table-hover table-sort">
                        <thead>
                        <tr class="text-c">
                            <th width="100">名称</th>
                            <th>图片</th>
                            <th width="60">分数</th>
                            <th width="100">操作</th>
                        </tr>
                        </thead>
                        <tbody  id="option">
                        <tr class="text-c hidden" >
                            <td>
                                <input type="text" class="input-text" value="" placeholder="" id="name" name="name">
                            </td>
                            <td>
                                <div class="uploader-thum-container">
                                    <div id="fileList" class="uploader-list">
                                    </div>
                                    <div id="filePicker">选择图片</div>
                                    <input type="hidden" id="image" name="image">
                                </div>
                            </td>
                            <td class="text-c">
                                <input type="text" class="input-text" value="" placeholder="" id="score" name="score">
                            </td>
                            <td class="td-manage">
                                <a style="text-decoration:none" class="ml-5" onClick="del_opt(this)" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>
                            </td>
                        </tr>
                            [#list options as option]
                            <tr class="text-c" >
                                <td>
                                    <input type="text" class="input-text" value="${option.name}" placeholder="" id="name" name="name">
                                </td>
                                <td>
                                    <div class="uploader-thum-container">
                                        <div id="fileList${option_index}" class="uploader-list">
                                            <div class="file-item thumbnail">
                                                <img width="100px" height="100px" src="${option.image}"/>
                                                <div class="info"></div>
                                            </div>
                                        </div>
                                        <div id="filePicker${option_index}">选择图片</div>
                                        <input type="hidden" id="image" name="image">
                                    </div>
                                </td>
                                <td class="text-c">
                                    <input type="text" class="input-text" value="${option.score}"  placeholder="" id="score" name="score">
                                </td>
                                <td class="td-manage">
                                    <a style="text-decoration:none" class="ml-5" onClick="del_opt(this)" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>
                                </td>
                            </tr>
                            [/#list]
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"></label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input class="btn btn-primary radius" onClick="add_opt()" type="button" value="&nbsp;&nbsp;添加&nbsp;&nbsp;">
                </div>
            </div>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">随机长度：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="strLen" value="${data.strLen}" placeholder="" id="strLen" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">显示时长：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" name="stayTime" value="${data.stayTime}" placeholder="请输入秒数" id="stayTime" onInput="intInit(this)">
            </div>
        </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">排序：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="orders"  value="${data.orders}"  placeholder="" id="orders" onInput="intInit(this)">
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
            var $option = $("#option");
            var rw = ${options_length};
            $(function(){
                var $submit = $(":submit");
                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                $("#form-update").validate({
                    rules:{
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
                            url: "${base}/admin/gaugeQuestion/update.jhtml" ,
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

            for (var i=0;i<rw;i++) {
                var w = i+1;
                new $uploadpicture("fileList"+w,"filePicker"+w);
            }

            function add_opt() {
                var $row = $option.find("tr:eq(0)").clone().show();
                $row.removeClass("hidden");
                $row.appendTo($option);

                rw = rw +1;
                $row.find("#fileList").attr("id","fileList"+rw);
                $row.find("#filePicker").attr("id","filePicker"+rw);
                new $uploadpicture("fileList"+rw,"filePicker"+rw);
            };

            function del_opt(val) {
                var $this = val;
                $this.closest("tr").remove();
            };

        </script>
</body>
</html>
