<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/html5shiv.js"></script>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/lib/Hui-iconfont/1.0.8/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="http://cdn.rzico.com/weex/resources/h-ui.admin/css/style.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>题库管理查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if articleVoteOption??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if articleVoteOption.id??]
            <td>${articleVoteOption.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if articleVoteOption.createDate??]
            <td><span id="createDate">${articleVoteOption.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if articleVoteOption.modifyDate??]
            <td><span id="modifyDate">${articleVoteOption.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">题目：</th>
            [#if articleVoteOption.title??]
            <td>${articleVoteOption.title}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">文章：</th>
            [#if types??]
                [#list articles as article]
                [#if article.id == articleVoteOption.article]
                    <td>${article.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">VoteOptions：</th>
            [#if articleVoteOption.voteOptions??]
            <td>${articleVoteOption.voteOptions}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Author：</th>
            [#if types??]
                [#list authors as author]
                [#if author.id == articleVoteOption.author]
                    <td>${author.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Member：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == articleVoteOption.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Options：</th>
            [#if articleVoteOption.options??]
            <td>${articleVoteOption.options}</td>
            [/#if]
        </tr>

        [#else]
         没有查找到数据
        [/#if]
        </tbody>
    </table>
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="http://cdn.rzico.com/weex/resources/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/wx.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
</body>
</html>