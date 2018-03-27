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
    <title>会员管理查看</title>
</head>
<body>
<div class="pd-20">
    <table class="table">
        <tbody>
        [#if member??]
        <tr>
            <th class="text-r" width="80">创建日期：</th>
            [#if member.createDate??]
            <td><span id="createDate">${member.createDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">修改日期：</th>
            [#if member.modifyDate??]
            <td><span id="modifyDate">${member.modifyDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">地址：</th>
            [#if member.address??]
            <td>${member.address}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">余额：</th>
            [#if member.balance??]
            <td>${member.balance}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">出生日期：</th>
            [#if member.birth??]
            <td><span id="birth">${member.birth}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">邮箱：</th>
            [#if member.email??]
            <td>${member.email}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">性别：</th>
            [#if types??]
                [#list genders as gender]
                [#if gender.id == member.gender]
                    <td>${gender.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">最后登录日期：</th>
            [#if member.loginDate??]
            <td><span id="loginDate">${member.loginDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">手机：</th>
            [#if member.mobile??]
            <td>${member.mobile}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">姓名：</th>
            [#if member.name??]
            <td>${member.name}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">密码：</th>
            [#if member.password??]
            <td>${member.password}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">电话：</th>
            [#if member.phone??]
            <td>${member.phone}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">用户名：</th>
            [#if member.username??]
            <td>${member.username}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">邮编：</th>
            [#if member.zipCode??]
            <td>${member.zipCode}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">所在地：</th>
            [#if types??]
                [#list areas as area]
                [#if area.id == member.area]
                    <td>${area.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">签名：</th>
            [#if member.autograph??]
            <td>${member.autograph}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">头像：</th>
            [#if member.logo??]
            <td><img src="${member.logo}"/></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">昵称：</th>
            [#if member.nickName??]
            <td>${member.nickName}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">职业：</th>
            [#if types??]
                [#list occupations as occupation]
                [#if occupation.id == member.occupation]
                    <td>${occupation.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">设备号：</th>
            [#if member.uuid??]
            <td>${member.uuid}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">设备环境：</th>
            [#if member.scene??]
            <td>${member.scene}</td>
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