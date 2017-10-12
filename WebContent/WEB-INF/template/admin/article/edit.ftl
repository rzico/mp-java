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
            <label class="form-label col-xs-4 col-sm-2">谁可见：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                [#if authoritys??]
                [#list authoritys as authority]
                    <div class="radio-box">
                        <input name="authority" type="radio" id="authority-${authority_index}" value="${authority.id}"[#if authority.id == data.authority] checked[/#if]>
                        <label for="authority-${authority_index}">${authority.name}</label>
                    </div>
                [/#list]
                [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否精选：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isPitch" id="isPitch" value="true"[#if data.isPitch?? && data.isPitch] checked[/#if]>
                    <input type="hidden" name="_isPitch" value="false" />
                    <label for="isPitch">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否投稿：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isPublish" id="isPublish" value="true"[#if data.isPublish?? && data.isPublish] checked[/#if]>
                    <input type="hidden" name="_isPublish" value="false" />
                    <label for="isPublish">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否评论：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isReview" id="isReview" value="true"[#if data.isReview?? && data.isReview] checked[/#if]>
                    <input type="hidden" name="_isReview" value="false" />
                    <label for="isReview">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否赞赏：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isReward" id="isReward" value="true"[#if data.isReward?? && data.isReward] checked[/#if]>
                    <input type="hidden" name="_isReward" value="false" />
                    <label for="isReward">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否显示：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isShow" id="isShow" value="true"[#if data.isShow?? && data.isShow] checked[/#if]>
                    <input type="hidden" name="_isShow" value="false" />
                    <label for="isShow">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>密码：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="password" class="input-text" value="" placeholder="" id="password" name="password">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">作者：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.author}" placeholder="" id="author" name="author">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">文章内容：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.content}" placeholder="" id="content" name="content">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">收藏数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.favorite}" placeholder="" id="favorite" name="favorite" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">阅读数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.hits}" placeholder="" id="hits" name="hits" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">点赞数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.laud}" placeholder="" id="laud" name="laud" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">类型：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                [#if mediaTypes??]
                [#list mediaTypes as mediaType]
                    <div class="radio-box">
                        <input name="mediaType" type="radio" id="mediaType-${mediaType_index}" value="${mediaType.id}"[#if mediaType.id == data.mediaType] checked[/#if]>
                        <label for="mediaType-${mediaType_index}">${mediaType.name}</label>
                    </div>
                [/#list]
                [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">背景音乐：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.music}" placeholder="" id="music" name="music">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">评论数：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.review}" placeholder="" id="review" name="review" onInput="intInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">标题：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.title}" placeholder="" id="title" name="title">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">文集：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if articleCatalogs??]
				<select name="articleCatalogId" class="select" style="background-color: #FFFFFF">
                    [#list articleCatalogs as articleCatalog]
					<option[#if data.articleCatalog?? && articleCatalog.id == data.articleCatalog.id] selected[/#if] value="${articleCatalog.id}">${articleCatalog.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">分类：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if articleCategorys??]
				<select name="articleCategoryId" class="select" style="background-color: #FFFFFF">
                    [#list articleCategorys as articleCategory]
					<option[#if data.articleCategory?? && articleCategory.id == data.articleCategory.id] selected[/#if] value="${articleCategory.id}">${articleCategory.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否删除：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="deleted" id="deleted" value="true"[#if data.deleted?? && data.deleted] checked[/#if]>
                    <input type="hidden" name="_deleted" value="false" />
                    <label for="deleted">&nbsp;</label>
                </div>
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
            <label class="form-label col-xs-4 col-sm-2">是否样例：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isExample" id="isExample" value="true"[#if data.isExample?? && data.isExample] checked[/#if]>
                    <input type="hidden" name="_isExample" value="false" />
                    <label for="isExample">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否置顶：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isTop" id="isTop" value="true"[#if data.isTop?? && data.isTop] checked[/#if]>
                    <input type="hidden" name="_isTop" value="false" />
                    <label for="isTop">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">位置：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.addr}" placeholder="" id="addr" name="addr">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">伟度：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.lat}" placeholder="" id="lat" name="lat" onInput="floatInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">经度：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.lng}" placeholder="" id="lng" name="lng" onInput="floatInit(this)">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>会员：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
                [#if members??]
				<select name="memberId" class="select" style="background-color: #FFFFFF">
                    [#list members as member]
					<option[#if data.member?? && member.id == data.member.id] selected[/#if] value="${member.id}">${member.name}</option>
                    [/#list]
				</select>
                [/#if]
				</span>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">缩例图：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.thumbnial}" placeholder="" id="thumbnial" name="thumbnial">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">模版：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
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
            <label class="form-label col-xs-4 col-sm-2">标题图1：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.image1}" placeholder="" id="image1" name="image1">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">标题图2：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.image2}" placeholder="" id="image2" name="image2">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">标题图3：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.image3}" placeholder="" id="image3" name="image3">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">标题图4：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.image4}" placeholder="" id="image4" name="image4">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">标题图5：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.image5}" placeholder="" id="image5" name="image5">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">标题图6：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.image6}" placeholder="" id="image6" name="image6">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">类型：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                [#if titleTypes??]
                [#list titleTypes as titleType]
                    <div class="radio-box">
                        <input name="titleType" type="radio" id="titleType-${titleType_index}" value="${titleType.id}"[#if titleType.id == data.titleType] checked[/#if]>
                        <label for="titleType-${titleType_index}">${titleType.name}</label>
                    </div>
                [/#list]
                [/#if]
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">是否草稿：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="check-box">
                    <input type="checkbox" name="isDraft" id="isDraft" value="true"[#if data.isDraft?? && data.isDraft] checked[/#if]>
                    <input type="hidden" name="_isDraft" value="false" />
                    <label for="isDraft">&nbsp;</label>
                </div>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">缩例图：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.thumbnail}" placeholder="" id="thumbnail" name="thumbnail">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">Votes：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${data.votes}" placeholder="" id="votes" name="votes">
            </div>
        </div>

		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-2">tags：</label>
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
                        member:{
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
                            url: "${base}/admin/article/update.jhtml" ,
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
