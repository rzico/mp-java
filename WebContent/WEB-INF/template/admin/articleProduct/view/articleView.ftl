<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${base}/resources/admin/lib/html5shiv.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/lib/Hui-iconfont/1.0.8/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/style.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>文章管理查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if article??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if article.id??]
            <td>${article.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if article.createDate??]
            <td><span id="createDate">${article.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if article.modifyDate??]
            <td><span id="modifyDate">${article.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">谁可见：</th>
            [#if types??]
                [#list authoritys as authority]
                [#if authority.id == article.authority]
                    <td>${authority.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isPitch??]
            [#if article.isPitch == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isPublish??]
            [#if article.isPublish == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isReview??]
            [#if article.isReview == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isReward??]
            [#if article.isReward == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isShow??]
            [#if article.isShow == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">密码：</th>
            [#if article.password??]
            <td>${article.password}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">作者：</th>
            [#if article.author??]
            <td>${article.author}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">文章内容：</th>
            [#if article.content??]
            <td>${article.content}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">收藏数：</th>
            [#if article.favorite??]
            <td>${article.favorite}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">阅读数：</th>
            [#if article.hits??]
            <td>${article.hits}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">点赞数：</th>
            [#if article.laud??]
            <td>${article.laud}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">类型：</th>
            [#if types??]
                [#list mediaTypes as mediaType]
                [#if mediaType.id == article.mediaType]
                    <td>${mediaType.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">背景音乐：</th>
            [#if article.music??]
            <td>${article.music}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">评论数：</th>
            [#if article.review??]
            <td>${article.review}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题：</th>
            [#if article.title??]
            <td>${article.title}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">文集：</th>
            [#if types??]
                [#list articleCatalogs as articleCatalog]
                [#if articleCatalog.id == article.articleCatalog]
                    <td>${articleCatalog.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">分类：</th>
            [#if types??]
                [#list articleCategorys as articleCategory]
                [#if articleCategory.id == article.articleCategory]
                    <td>${articleCategory.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.deleted??]
            [#if article.deleted == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">所在地：</th>
            [#if types??]
                [#list areas as area]
                [#if area.id == article.area]
                    <td>${area.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isExample??]
            [#if article.isExample == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isTop??]
            [#if article.isTop == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">位置：</th>
            [#if article.addr??]
            <td>${article.addr}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">伟度：</th>
            [#if article.lat??]
            <td>${article.lat}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">经度：</th>
            [#if article.lng??]
            <td>${article.lng}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == article.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">缩例图：</th>
            [#if article.thumbnial??]
            <td>${article.thumbnial}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">模版：</th>
            [#if types??]
                [#list templates as template]
                [#if template.id == article.template]
                    <td>${template.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题图1：</th>
            [#if article.image1??]
            <td>${article.image1}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题图2：</th>
            [#if article.image2??]
            <td>${article.image2}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题图3：</th>
            [#if article.image3??]
            <td>${article.image3}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题图4：</th>
            [#if article.image4??]
            <td>${article.image4}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题图5：</th>
            [#if article.image5??]
            <td>${article.image5}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题图6：</th>
            [#if article.image6??]
            <td>${article.image6}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">类型：</th>
            [#if types??]
                [#list titleTypes as titleType]
                [#if titleType.id == article.titleType]
                    <td>${titleType.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] article.isDraft??]
            [#if article.isDraft == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">缩例图：</th>
            [#if article.thumbnail??]
            <td>${article.thumbnail}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Votes：</th>
            [#if article.votes??]
            <td>${article.votes}</td>
            [/#if]
        </tr>

        [#else]
         没有查找到数据
        [/#if]
        </tbody>
    </table>
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/wx.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
</body>
</html>