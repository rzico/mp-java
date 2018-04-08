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
    <title>LiveTape查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if liveTape??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if liveTape.id??]
            <td>${liveTape.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if liveTape.createDate??]
            <td><span id="createDate">${liveTape.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if liveTape.modifyDate??]
            <td><span id="modifyDate">${liveTape.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">封面：</th>
            [#if liveTape.frontcover??]
            <td>${liveTape.frontcover}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">礼物数：</th>
            [#if liveTape.gift??]
            <td>${liveTape.gift}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">头像：</th>
            [#if liveTape.headpic??]
            <td>${liveTape.headpic}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">回放地址：</th>
            [#if liveTape.hlsPlayUrl??]
            <td>${liveTape.hlsPlayUrl}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">点赞数：</th>
            [#if liveTape.likeCount??]
            <td>${liveTape.likeCount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">位置：</th>
            [#if liveTape.location??]
            <td>${liveTape.location}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">昵称：</th>
            [#if liveTape.nickname??]
            <td>${liveTape.nickname}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">观看地址：</th>
            [#if liveTape.playUrl??]
            <td>${liveTape.playUrl}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">开始时间：</th>
            [#if liveTape.startTime??]
            <td><span id="startTime">${liveTape.startTime}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题：</th>
            [#if liveTape.title??]
            <td>${liveTape.title}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">在线数：</th>
            [#if liveTape.viewerCount??]
            <td>${liveTape.viewerCount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">LiveGroup：</th>
            [#if types??]
                [#list liveGroups as liveGroup]
                [#if liveGroup.id == liveTape.liveGroup]
                    <td>${liveGroup.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Member：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == liveTape.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
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