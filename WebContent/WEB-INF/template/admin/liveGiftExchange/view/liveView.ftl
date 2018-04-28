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
    <title>Live查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if live??]
        <tr>
            <th class="text-r" width="80">Id：</th>
            [#if live.id??]
            <td>${live.id}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if live.createDate??]
            <td><span id="createDate">${live.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if live.modifyDate??]
            <td><span id="modifyDate">${live.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">封面：</th>
            [#if live.frontcover??]
            <td>${live.frontcover}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">礼物数：</th>
            [#if live.gift??]
            <td>${live.gift}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">头像：</th>
            [#if live.headpic??]
            <td>${live.headpic}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">回放地址：</th>
            [#if live.hlsPlayUrl??]
            <td>${live.hlsPlayUrl}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">点赞数：</th>
            [#if live.likeCount??]
            <td>${live.likeCount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">位置：</th>
            [#if live.location??]
            <td>${live.location}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">昵称：</th>
            [#if live.nickname??]
            <td>${live.nickname}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">观看地址：</th>
            [#if live.playUrl??]
            <td>${live.playUrl}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">推流地址：</th>
            [#if live.pushUrl??]
            <td>${live.pushUrl}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">状态：</th>
            [#if types??]
                [#list statuss as status]
                [#if status.id == live.status]
                    <td>${status.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">标题：</th>
            [#if live.title??]
            <td>${live.title}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">在线数：</th>
            [#if live.viewerCount??]
            <td>${live.viewerCount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">LiveGroup：</th>
            [#if types??]
                [#list liveGroups as liveGroup]
                [#if liveGroup.id == live.liveGroup]
                    <td>${liveGroup.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">LiveTape：</th>
            [#if types??]
                [#list liveTapes as liveTape]
                [#if liveTape.id == live.liveTape]
                    <td>${liveTape.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Member：</th>
            [#if types??]
                [#list members as member]
                [#if member.id == live.member]
                    <td>${member.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">是否在线：</th>
            [#if live.online??]
            <td>${live.online}</td>
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