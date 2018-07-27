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
                <label class="form-label col-xs-4 col-sm-2">版主：</label>
                <div class="formControls col-xs-8 col-sm-9"> <span>
                ${data.member.nickName}
                    <input type="hidden" value="${data.member.id}" placeholder="" id="memberId" name="memberId">
 				</span>
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>全称：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${data.name}" placeholder="" id="name" name="name">
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
                <label class="form-label col-xs-4 col-sm-2">所在地：</label>
                <div class="formControls col-xs-8 col-sm-9">
                <span class="fieldSet">
                    <input type="hidden" id="areaId" name="areaId" value="${(data.area.id)!}" treePath="${(data.area.treePath)!}" />
                </span>
                </div>
            </div>


            <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">地址：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.address}" placeholder="" id="address" name="address">
            </div>
        </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">行业：</label>
                <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                    [#if categorys??]
                        <select name="categoryId" class="select" style="background-color: #FFFFFF">
                            [#list categorys as category]
                                <option[#if data.category?? && category.id == data.category.id] selected[/#if] value="${category.id}">${category.name}</option>
                            [/#list]
				</select>
                    [/#if]
				</span>
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>交易佣金：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="brokerage" value="${data.brokerage}" placeholder="" id="brokerage" onInput="floatInit(this)">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>收单佣金：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="paybill" value="${data.paybill}" placeholder="" id="paybill" onInput="floatInit(this)">
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
                <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>到期日：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss' })" value="${data.expire}" id="expire" name="expire" class="input-text Wdate" style="width:180px;">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">模版：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span class="select-box">
                        [#if templates??]
                             <select name="templateId" class="select" style="background-color: #FFFFFF">
                                   [#list templates as template]
                                       <option[#if data.template?? && template.id == data.template.id] selected[/#if] value="${template.id}">${template.name}</option>
                                   [/#list]
			                 </select>
                        [/#if]
				    </span>
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">小程序ID：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="config.appetAppId" value="${data.config.appetAppId}" placeholder="" id="appetAppId" onInput="floatInit(this)">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">小程序Serect：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="config.appetAppSerect" value="${data.config.appetAppSerect}" placeholder="" id="appetAppSerect" onInput="floatInit(this)">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">公众号ID：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="config.wxAppId" value="${data.config.wxAppId}" placeholder="" id="wxAppId" onInput="floatInit(this)">
                </div>
            </div>

            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-2">公众号Serect：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" name="config.wxAppSerect" value="${data.config.wxAppSerect}" placeholder="" id="wxAppSerect" onInput="floatInit(this)">
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
                        brokerage:{
                            required:true,
                        },
                        expire:{
                            required:true,
                        },
                        name:{
                            required:true,
                        },
                        shortName:{
                            required:true,
                        },
                        status:{
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
                            url: "${base}/admin/topic/update.jhtml" ,
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
