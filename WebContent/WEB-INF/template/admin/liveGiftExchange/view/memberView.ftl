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
            <th class="text-r" width="80">Id：</th>
            [#if member.id??]
            <td>${member.id}</td>
            [/#if]
        </tr>
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
            <th class="text-r" width="80">会员注册项值0：</th>
            [#if member.attributeValue0??]
            <td>${member.attributeValue0}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值1：</th>
            [#if member.attributeValue1??]
            <td>${member.attributeValue1}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值2：</th>
            [#if member.attributeValue2??]
            <td>${member.attributeValue2}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值3：</th>
            [#if member.attributeValue3??]
            <td>${member.attributeValue3}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值4：</th>
            [#if member.attributeValue4??]
            <td>${member.attributeValue4}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值5：</th>
            [#if member.attributeValue5??]
            <td>${member.attributeValue5}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值6：</th>
            [#if member.attributeValue6??]
            <td>${member.attributeValue6}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值7：</th>
            [#if member.attributeValue7??]
            <td>${member.attributeValue7}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值8：</th>
            [#if member.attributeValue8??]
            <td>${member.attributeValue8}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">会员注册项值9：</th>
            [#if member.attributeValue9??]
            <td>${member.attributeValue9}</td>
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
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] member.isEnabled??]
            [#if member.isEnabled == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">${TableComment}：</th>
            [#if] member.isLocked??]
            [#if member.isLocked == true]
            <span class=\"label label-success radius\">是</span>
            [#else]
            <span class=\"label label-success radius\">否</span>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">锁定日期：</th>
            [#if member.lockedDate??]
            <td><span id="lockedDate">${member.lockedDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">最后登录日期：</th>
            [#if member.loginDate??]
            <td><span id="loginDate">${member.loginDate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">连续登录失败次数：</th>
            [#if member.loginFailureCount??]
            <td>${member.loginFailureCount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">最后登录IP：</th>
            [#if member.loginIp??]
            <td>${member.loginIp}</td>
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
            <th class="text-r" width="80">积分：</th>
            [#if member.point??]
            <td>${member.point}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">注册IP：</th>
            [#if member.registerIp??]
            <td>${member.registerIp}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">SafeKeyCreate：</th>
            [#if member.safeKeyCreate??]
            <td><span id="safeKeyCreate">${member.safeKeyCreate}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">SafeKeyExpire：</th>
            [#if member.safeKeyExpire??]
            <td><span id="safeKeyExpire">${member.safeKeyExpire}</span></td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">SafeKeyValue：</th>
            [#if member.safeKeyValue??]
            <td>${member.safeKeyValue}</td>
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
            <td>${member.logo}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">昵称：</th>
            [#if member.nickName??]
            <td>${member.nickName}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">位置：</th>
            [#if member.addr??]
            <td>${member.addr}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">伟度：</th>
            [#if member.lat??]
            <td>${member.lat}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">经度：</th>
            [#if member.lng??]
            <td>${member.lng}</td>
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
        <tr>
            <th class="text-r" width="80">Topic：</th>
            [#if types??]
                [#list topics as topic]
                [#if topic.id == member.topic]
                    <td>${topic.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">推广二维码：</th>
            [#if member.qrcode??]
            <td>${member.qrcode}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">安全码：</th>
            [#if member.sign??]
            <td>${member.sign}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Promoter：</th>
            [#if types??]
                [#list promoters as promoter]
                [#if promoter.id == member.promoter]
                    <td>${promoter.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">冻结：</th>
            [#if member.freezeBalance??]
            <td>${member.freezeBalance}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">星级：</th>
            [#if types??]
                [#list vips as vip]
                [#if vip.id == member.vip]
                    <td>${vip.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">消费金额：</th>
            [#if member.amount??]
            <td>${member.amount}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">礼物：</th>
            [#if member.gift??]
            <td>${member.gift}</td>
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Agent：</th>
            [#if types??]
                [#list agents as agent]
                [#if agent.id == member.agent]
                    <td>${agent.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Operate：</th>
            [#if types??]
                [#list operates as operate]
                [#if operate.id == member.operate]
                    <td>${operate.name}</td>
                [/#if]
                [/#list]
            [/#if]
        </tr>
        <tr>
            <th class="text-r" width="80">Personal：</th>
            [#if types??]
                [#list personals as personal]
                [#if personal.id == member.personal]
                    <td>${personal.name}</td>
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