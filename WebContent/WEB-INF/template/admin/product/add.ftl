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
    <style type="text/css">
        *{
            margin: 0;
            padding: 0;
        }
        .goodsbox-table{
            width: 100%;
            height: auto;
            background: #FFFFFF;
            border: 1px solid rgb(0,0,0);
            position: relative;
            left: 0px;
            top: 0px;
        }

        .goodsbox-tablechild{
            width: 700px;
            height: 144px;
            /*height: auto;*/
            background: #FFFFFF;
            border: 1px solid rgb(0,0,0);
        }

        .goodsbox-col1{
            width: 100%;
            height: 52px;
            background-color: #FFFFFF;
            border: 1px solid rgb(0,0,0);
            position:inherit;
            left:0;
        }

        .goodsbox-col2{
            width: 100%;
            height: 10px;
            background-color: #F0F8FF;
            border: 1px solid rgb(0,0,0);
            position:inherit;
            left:0;
        }

        .goodsbox-col3{
            width: 100%;
            height: auto;
            background-color: #FFFFFF;
            border: 1px solid rgb(0,0,0);
            position:inherit;
            left:0;
        }

        .goodsbox-col5{
            width: 700px;
            height: auto;
            background-color: #FFFFFF;
            border: 1px solid rgb(0,0,0);
            position:inherit;
            left:0;
        }

        .goodsbox-r1{
            float: left;
            width: 200px;
            height: 48px;
            border: 1px solid rgb(0,0,0);
            text-align: right;
            font-size: 26px;
            font-weight: 100px;
        }
        .goodsbox-r2{
            float: left;
            width: 500px;
            height: 48px;
            border: 1px solid rgb(0,0,0);
            text-align: left;
            font-size: 26px;
            font-weight: 100px;
        }
        .goodsbox-rchild1{
            float: left;
            width: 198px;
            height: 144px;
            /*height: auto;*/
            border: 1px solid rgb(0,0,0);
            position:inherit;
        }
        .goodsbox-rchild2{
            float: left;
            width: 452px;
            height: 144px;
            /* height: auto; */
            border: 1px solid rgb(0,0,0);
            position:inherit;
        }
        .goodsbox-rchild3{
            float: left;
            width: 42px;
            height: 144px;
            /* height: auto; */
            border: 1px solid rgb(0,0,0);
            position:inherit;
            font-size: 30px;
            color: red;

            display: -webkit-box;
            -webkit-box-orient: horizontal;
            -webkit-box-pack: center;
            -webkit-box-align: center;

            display: -moz-box;
            -moz-box-orient: horizontal;
            -moz-box-pack: center;
            -moz-box-align: center;

            display: -o-box;
            -o-box-orient: horizontal;
            -o-box-pack: center;
            -o-box-align: center;

            display: -ms-box;
            -ms-box-orient: horizontal;
            -ms-box-pack: center;
            -ms-box-align: center;

            display: box;
            box-orient: horizontal;
            box-pack: center;
            box-align: center;
        }
        .goodsbox-colchild1{
            width: 100%;
            height: 46px;
            border: 1px solid rgb(0,0,0);
        }
        .goodsbox-rchild6{
            float: left;
            width: 100px;
            height: 44px;
            border: 1px solid rgb(0,0,0);
            text-align: right;
            font-size: 26px;
            font-weight: 100px;
        }
        .goodsbox-rchild4{
            float: left;
            width: 173px;
            height: 44px;
            border: 1px solid rgb(0,0,0);
        }
        .goodsbox-rchild5{
            float: left;
            width: 348px;
            height: 44px;
            border: 1px solid rgb(0,0,0);
        }

    </style>

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
        <div id="table-1" class="goodsbox-table">
            <div class="goodsbox-col1">
                <div class="goodsbox-r1">商品名称</div>
                <div class="goodsbox-r2">
                    <input type="text" style="height:44px; font-size:20px;" class="input-text" value="" placeholder="请输入商品名称" id="name" name="name">
                </div>
            </div>
            <div class="goodsbox-col2">

            </div>
            <div class="goodsbox-col1">
                <div class="goodsbox-r1">单位</div>
                <div class="goodsbox-r2">
                    <input type="text" style="height:44px; font-size:20px;" class="input-text" value="" placeholder="个、件、袋等" id="unit" name="unit">
                </div>
            </div>
            <div class="goodsbox-col2">

            </div>
            <div id="insertdiv" class="goodsbox-col3">
                <div id="childtable-1" class="goodsbox-col5">
                    <div class="goodsbox-tablechild">
                        <div class="goodsbox-rchild1">
                            <div class="formControls col-xs-8 col-sm-9">
                                <div class="uploader-thum-container">
                                    <div id="fileList" class="uploader-list"></div>
                                    <div id="filePicker">选择图片</div>
                                    <input type="hidden" value="" id="thumbnail" name="thumbnail">
                                </div>
                            </div>
                        </div>
                        <div class="goodsbox-rchild2">
                            <div class="goodsbox-colchild1" id="goodsbox-cchild1">
                                <div class="goodsbox-rchild6">规格</div>
                                <div class="goodsbox-rchild4">
                                    <input type="text" style="height:44px; font-size:20px;" class="input-text" value="" placeholder="规格1" id="spec1" name="spec1">
                                </div>
                                <div class="goodsbox-rchild4">
                                    <input type="text" style="height:44px; font-size:20px;" class="input-text" value="" placeholder="规格2" id="spec2" name="spec2">
                                </div>
                            </div>
                            <div class="goodsbox-colchild1">
                                <div class="goodsbox-rchild6">价格</div>
                                <div class="goodsbox-rchild5">
                                    <input type="text" style="height:44px; font-size:20px;" class="input-text" value="" placeholder="给商品定个好价格" id="price" name="price">
                                </div>
                            </div>
                            <div class="goodsbox-colchild1">
                                <div class="goodsbox-rchild6">库存</div>
                                <div class="goodsbox-rchild5">
                                    <input type="text" style="height:44px; font-size:20px;" class="input-text" value="" placeholder="设置命理库存避免超卖" id="stock" name="stock">
                                    <input type="hidden" class="input-text" value="" id="productid" name="productid">
                                </div>
                            </div>
                        </div>
                        <div class="goodsbox-rchild3" id="abcd">
                            <i class='Hui-iconfont' onclick="deletetable(this)">&#xe631;</i>
                        </div>
                    </div>
                <div class="goodsbox-col2">
                </div>
                </div>
            </div>
            <div class="goodsbox-col1">
                <div class="goodsbox-r1"></div>
                <div class="goodsbox-r2">
                    <a class="btn btn-primary radius" style="font-size: 20px;" onclick="append('1')">添加商品规格</a>
                </div>
            </div>
            <div class="goodsbox-col2"></div>
            <div class="goodsbox-col1">
                <div class="goodsbox-r1">选择分类</div>
                <div class="goodsbox-r2">
                    <span class="select-box" style="height:44px; font-size:20px;">
                        [#if productCategorys??]
                            <select name="productCategoryId" class="select" style="background-color: #FFFFFF; font-size:20px;">
                                <option value="">请选择</option>
                                [#list productCategorys as productCategory]
                                    <option value="${productCategory.id}">${productCategory.name}</option>
                                [/#list]
                            </select>
                        [/#if]
                    </span>
                </div>
            </div>
            <div class="goodsbox-col2">
            </div>
            <div class="goodsbox-col1">
                <div class="goodsbox-r1">销售策略</div>
                <div class="goodsbox-r2">
                    <span class="select-box" style="height:44px; font-size:20px;">
                    [#if distributions??]
                        <select name="distributionId" class="select" style="background-color: #FFFFFF; font-size:20px;">
                            <option value="">请选择</option>
                            [#list distributions as distribution]
                                <option value="${distribution.id}">${distribution.name}</option>
                            [/#list]
                        </select>
                    [/#if]
                    </span>
                </div>
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
            $tab = $("#childtable-1").clone();
            $tableCount = 1;
            $IsShow = true;
            $product_list = [];
            $productTemplates = [];
            $jsons = '';

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
                        stock:{
                            required:true,
                        },
                        price:{
                            required:true,
                        },
                        unit:{
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
                        savePage();
                        $(form).ajaxSubmit({
                            type: 'post',
                            url: "${base}/admin/product/save.jhtml" ,
                            data: {
                                body: $jsons
                            },
                            beforeSend: function() {
                                $submit.prop("disabled", true);
                                //savePage();
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

            function append(id){
                if (($tableCount == 1) && ($IsShow.toString() == 'false')){
                    $("#goodsbox-cchild1").css("display","block");
                    $("#goodsbox-cchild1 div").css("display","block");
                    $("#goodsbox-cchild1 input").css("display","block");
                    $("#abcd").css("display","-webkit-box");
                    $IsShow = true;
                }else{
                    var $tabcopy = $tab.clone();
                    $tableCount = $tableCount + 1;
                    //$tableId = "childtable-"+Math.random(0-1000).toString();
                    //$tabcopy.attr("id",$tableId);
                    $tabcopy.find("i").attr("onclick","deletetable(this)");
                    $("#insertdiv").append($tabcopy);
                }
            }

            function deletetable(obj){
                if (($tableCount == 1) && ($IsShow)){
                    $("#goodsbox-cchild1 div").css("display","none");
                    $("#goodsbox-cchild1 input").css("display","none");
                    $("#goodsbox-cchild1").css("display","none");
                    //$("#spec2").css("display","none");
                    $("#abcd").css("display","none");
                    $IsShow = false;
                }else{
                    var $div1 = obj.parentNode.parentNode.parentNode;
                    $div1.remove();
                    $tableCount = $tableCount - 1;
                }
            }

            function savePage(){
                $productTemplates = [];
                $('.goodsbox-col3').children().each(function(index,element){
                    $productTemplates.push({
                    "productId": '',
                    "thumbnail":$(element).find("#thumbnail").val(),
                    "spec1":$(element).find("#spec1").val(),
                    "spec2":$(element).find("#spec2").val(),
                    "price":parseInt($(element).find("#price").val()),
                    "stock":parseInt($(element).find("#stock").val()),
                    });
                });

                let categoryTemplate = {
                    id:parseInt($('select[name="productCategoryId"]').val()),
                    name:$('select[name="productCategoryId"]').find("option:selected").text()
                }
                // 销售策略
                let distributionTemplate = {
                    id:parseInt($('select[name="distributionId"]').val()),
                    name:$('select[name="distributionId"]').find("option:selected").text()
                }
                let productData = {
                    id: '',
                    name:$('#name').val(),
                    unit:$('#unit').val(),
                    productCategory:categoryTemplate,
                    distribution:distributionTemplate,
                    products: $productTemplates,
                };
                $jsons = JSON.stringify(productData);

            }

        </script>
</body>
</html>
