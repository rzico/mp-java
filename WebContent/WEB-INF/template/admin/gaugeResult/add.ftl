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
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/lib/ueditor/1.4.3/xiumi-ue-v5.css" />

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
        <input type="hidden"value="${gaugeId}" id="gaugeId" name="gaugeId">

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">标题：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="title" name="title">
            </div>
        </div>


        [#if gauge.method=='combined']
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">水平：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    [#if gaugeGenes??]
                        [#list gaugeGenes as gaugeGene]
                            <input type="hidden" value="${gaugeGene.id}" id="genes" name="genes">
                            <label for="attr-${gaugeGene_index}">${gaugeGene.name}=</label>
                        <span class="select-box" style="background-color:#FFFFFF;width:100px;height:32px;">
			          	<select id="attr-${gaugeGene_index}" name="attrs" class="select" style="background-color: #FFFFFF">
					       <option value="任意">任意</option>
                           [#list gaugeGene.attributes as attribute]
                              <option value="${attribute}">${attribute}</option>
                           [/#list]
				        </select>
			            </span>
                        [/#list]
                    [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">得分：</label>
            <div class="formControls col-xs-8 col-sm-9">
            [#if gaugeGenes??]
                [#list gaugeGenes as gaugeGene]
                    [#if gaugeGene_index>0]
                    <label> > </label>
                    [/#if]
                    <span class="select-box" style="background-color:#FFFFFF;width:100px;height:32px;">
			          	<select id="scompare-${gaugeGene_index}" name="scompare" class="select" style="background-color: #FFFFFF">
                            [#list gaugeGenes as gene]
                                <option value="${gene.id}">${gene.name}</option>
                            [/#list]
				        </select>
			            </span>
                [/#list]
            [/#if]
            </div>
        </div>
        [#else]

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>表达式：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="" placeholder="" id="expr" name="expr">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"></label>
                <div class="formControls col-xs-8 col-sm-9">
                ${expr_txt}
                </div>
            </div>

        [/#if]

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>结果类型：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span class="select-box" style="background-color:#FFFFFF;width:100px;height:32px;">
				<select id="type" name="type" class="select" style="background-color: #FFFFFF">
					<option value="">结果类型</option>
                [#list types as type]
                    <option value="${type.id}">${type.name}</option>
                [/#list]
				</select>
			</span>
            </div>
        </div>

        <div id="chart" class="row cl hidden">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>图表类型：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span class="select-box" style="background-color:#FFFFFF;width:100px;height:32px;">
				<select name="chartType" class="select" style="background-color: #FFFFFF">
					<option value="">图表类型</option>
                [#list  chartTypes as chartType]
                    <option value="${chartType.id}">${chartType.name}</option>
                [/#list]
				</select>
			</span>
            </div>
        </div>


        <div id="html" class="row cl hidden">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>结果展示：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <script id="htmlContent"  name="htmlContent" type="text/plain" style="width:100%;height:400px;"></script>
            </div>
        </div>

        <div id="json" class="row cl hidden">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>结果展示：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea class="input-text" name="content" id="content" style="height:300px;width:100%;"></textarea>
            </div>
        </div>


                    <div id="image" class="row cl hidden">
                    <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>结果展示：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    <div class="uploader-thum-container">
                    <div id="fileList" class="uploader-list"></div>
                    <div id="filePicker">选择图片</div>
                    <input type="hidden" value="" id="imageContent" name="imageContent">
                    </div>
                    </div>
                    </div>

                    <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-2">排序：</label>
            <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="orders" value="" placeholder="" id="orders" onInput="intInit(this)">
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
                <script type="text/javascript" src="${base}/resources/admin/lib/ueditor/1.4.3/ueditor.config.js"></script>
                <script type="text/javascript" src="${base}/resources/admin/lib/ueditor/1.4.3/ueditor.all.min.js"> </script>
                <script type="text/javascript" src="${base}/resources/admin/lib/ueditor/1.4.3/lang/zh-cn/zh-cn.js"></script>

                <script type="text/javascript" src="${base}/resources/admin/js/uploader.js"></script>
                <script type="text/javascript" src="${base}/resources/admin/lib/ueditor/1.4.3/xiumi-ue-dialog-v5.js"></script>

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
                var ue = UE.getEditor('htmlContent');

                $("#form-add").validate({
                    rules:{
                        maxscore:{
                            required:true,
                        },
                        minscore:{
                            required:true,
                        },
                        type:{
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
                            url: "${base}/admin/gaugeResult/save.jhtml" ,
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

            $('#type').on('change', function(e) {
                var r = $(this).val();//人员类别PK
                if (r=='html') {
                    $("#json").addClass("hidden");
                    $("#image").addClass("hidden");
                    $("#chart").addClass("hidden");
                    $("#html").removeClass("hidden");
                } else
                if (r=='image'){
                    $("#chart").addClass("hidden");
                    $("#json").addClass("hidden");
                    $("#html").addClass("hidden");
                    $("#image").removeClass("hidden");
                    new $uploadpicture("fileList","filePicker");
                } else {
                    $("#chart").addClass("hidden");
                    $("#json").removeClass("hidden");
                    $("#html").addClass("hidden");
                    $("#image").addClass("hidden");
                }

                if (r=='echart') {
                    $("#chart").removeClass("hidden");
                }
            });

        </script>
</body>
</html>
