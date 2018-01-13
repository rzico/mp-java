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
        <table class="table table-border table-bordered table-bg mt-20">
            <thead>
            <tr>
                <th colspan="4" scope="col">商户资料</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <th class="text-r" width="10%">手机号：</th>
                <td  width="30%" colspan="3">
                    <div class="formControls col-xs-8 col-sm-9" >
                        <input type="text" class="input-text" value="" placeholder="" id="phone" name="phone" width="30%">
                    </div>
                    <button type="submit" class="btn btn-success radius" id="" onclick="search();" name="">
                        <i class="Hui-iconfont">&#xe665;</i> 查询
                    </button>
                </td>
            </tr>
            <tr>
                <th class="text-r" width="10%">商户编号：</th>
                <td width="30%">
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="merchantNo" name="merchantNo">
                    </div>
                </td>
                <th class="text-r" width="10%">行业类型：</th>
                <td width="50%">
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text" value="" placeholder="" id="industryType" name="industryType">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">商户名称：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="scompany" name="scompany">
                    </div>
                </td>
                <th class="text-r">商户姓名：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text" value="" placeholder="" id="merchantName" name="merchantName">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">营业执照：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="licenseNo" name="licenseNo">
                    </div>
                </td>
                <th class="text-r">身份证：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text" value="" placeholder="" id="idCard" name="idCard">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">开户银行：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="bankName" name="bankName">
                    </div>
                </td>
                <th class="text-r">所属企业：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="hidden" class="input-text" value="" placeholder="" id="enterprise" name="enterprise">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">支行名称：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="branchBankName" name="branchBankName">
                    </div>
                </td>
                <th class="text-r">所属店主：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="hidden" class="input-text" value="" placeholder="" id="owner" name="owner">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">银行卡号：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="cardNo" name="cardNo">
                    </div>
                </td>
                <th class="text-r">邮箱：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text" value="" placeholder="" id="email" name="email">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">银行省份：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="cardProvince" name="cardProvince">
                    </div>
                </td>
                <th class="text-r">经营省份：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text" value="" placeholder="" id="province" name="province">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">银行城市：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-12">
                        <input type="text" class="input-text" value="" placeholder="" id="cardCity" name="cardCity">
                    </div>
                </td>
                <th class="text-r">经营城市：</th>
                <td>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text" value="" placeholder="" id="city" name="city">
                    </div>
                </td>
            </tr>
            <tr>
                <th class="text-r">经营地址：</th>
                <td colspan="3">
                    <div class="formControls col-xs-8 col-sm-9">
                        <input type="text" class="input-text" value="" placeholder="" id="address" name="address">
                    </div>
                </td>
            </tr>

            </tbody>
        </table>

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
            $(function(){
                var $submit = $(":submit");

                $('.skin-minimal input').iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue',
                    increaseArea: '20%'
                });

                $("#form-add").validate({
                    rules:{
                        brokerage:{
                            required:true,
                        },
                        merchantName:{
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
                            url: "${base}/admin/merchant/save.jhtml" ,
                            beforeSend: function() {
                                alert(1);
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