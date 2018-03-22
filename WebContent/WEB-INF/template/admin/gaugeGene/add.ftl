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
    <form action="" method="post" class="form form-horizontal" id="form-add">
        <div class="row cl">
            <input type="hidden" value="${gaugeId}" id="gaugeId" name="gaugeId">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>因子：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="name" name="name">
            </div>
        </div>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>因子类型：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
            [#if ranks??]
                [#list ranks as rank]
                    <div class="radio-box">
                        <input name="rank" type="radio" id="rank-${rank_index}" value="${rank.id}" onClick="rank('${rank.id}')" >
                        <label for="rank-${rank_index}">${rank.name}</label>
                    </div>
                [/#list]
            [/#if]
            </div>
        </div>


        <div  id="rank1">
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">题目：</label>
            <div class="formControls col-xs-8 col-sm-9">
            [#if gaugeQuestions??]
                [#list gaugeQuestions as question]
                    <div class="check-box">
                        <label class=""><input type="checkbox" value="${question.id}" name="questions" >${question.orders}</label>
                    </div>
                [/#list]
            [/#if]
            </div>
        </div>



        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>计分类型：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
            [#if scoreTypes??]
                [#list scoreTypes as scoreType]
                    <div class="radio-box">
                        <input name="scoreType" type="radio" id="scoreType-${scoreType_index}" value="${scoreType.id}">
                        <label for="type-${scoreType_index}">${scoreType.name}</label>
                    </div>
                [/#list]
            [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">水平设置：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <table class="table table-border table-bordered table-bg table-hover table-sort">
                    <thead>
                    <tr class="text-c">
                        <th width="100">名称</th>
                        <th width="100">最小值(%)</th>
                        <th width="100">最大值(%)</th>
                    </tr>
                    </thead>
                    <tbody  id="option">
                    <tr class="text-c hidden" >
                        <td>
                            <input type="text" class="input-text" value="" placeholder="" id="sname" name="sname">
                        </td>
                        <td class="text-c">
                            <input type="text" class="input-text" value="" placeholder="" id="smin" name="smin"  onInput="floatInit(this)">
                        </td>
                        <td class="text-c">
                            <input type="text" class="input-text" value="" placeholder="" id="smax" name="smax"  onInput="floatInit(this)">
                        </td>
                        <td class="td-manage">
                            <a style="text-decoration:none" class="ml-5" onClick="del_opt(this)" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>
                        </td>
                    </tr>
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

        </div>
        <div class="row cl" id="rank2">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>表达式：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="name" name="name">
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

        <script type="text/javascript">
            var $option = $("#option");
            $(function(){
                var $submit = $(":submit");
                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                $("#form-add").validate({
                    rules:{
                        name:{
                            required:true,
                        },
                        questions:{
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
                            url: "${base}/admin/gaugeGene/save.jhtml" ,
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


            function add_opt() {
                var $row = $option.find("tr:eq(0)").clone().show();
                $row.removeClass("hidden");
                $row.appendTo($option);
            };

            function del_opt(val) {
                var $this = val;
                $this.closest("tr").remove();
            };

            function rank(val) {
                $("#rank1").addClass("hidden");
                $("#rank2").addClass("hidden");
                $("#"+val).removeClass("hidden");
            };

        </script>
</body>
</html>
