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
    <title>管理员管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 管理员中心 <span class="c-gray en">&gt;</span> 管理员管理 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="page-container">
    <div class="text-c"> 日期范围：
        <input type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin" class="input-text Wdate" style="width:120px;">
        -
        <input type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax" class="input-text Wdate" style="width:120px;">
        <input type="text" class="input-text" style="width:250px" placeholder="输入要查询的内容" id="" name="">
        <button type="submit" class="btn btn-success radius" id="" name=""><i class="Hui-iconfont">&#xe665;</i> 搜管理员</button>
    </div>
    <div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"><a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>
		<a href="javascript:;" onclick="member_add('添加管理员','add.jhtml','','510')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加管理员</a></span> <span class="r">共有数据：<strong>[#if page][#if page.total??]${page.total}[/#if][/#if]</strong> 条</span> </div>
    <div class="mt-20">
        <table class="table table-border table-bordered table-hover table-bg table-sort">
            <thead>
            <tr class="text-c">
                <th width="25"><input type="checkbox" name="" value=""></th>
                <th width="80">ID</th>
                <th width="80">用户名</th>
                <th width="80">密码</th>
                <th width="80">性别</th>
                <th width="80">邮箱</th>
                <th width="80">姓名</th>
                <th width="80">部门</th>
                <th width="80">是否启用</th>
                <th width="80">是否锁定</th>
                <th width="80">连续登录失败次数</th>
                <th width="80">锁定日期</th>
                <th width="80">最后登录日期</th>
                <th width="100">最后登录IP</th>
                <th width="80">企业名称</th>
                <th width="80">角色</th>
                <th width="80">操作</th>
            </tr>
            </thead>
            <tbody>
            [#--[#if page]--]
            [#--[#if page.content??]--]
               [#--[#list page.content as item]--]

               [#--<tr class="text-c">--]
                   [#--<td><input type="checkbox" value="${item.id}" name=""></td>--]
                   [#--<td>${item.id}</td>--]
                   [#--<td>${item.username}</td>--]
                   [#--<td>${item.password}</td>--]
                   [#--需要转换--]
                   [#--[#if genders??]--]
                       [#--[#list genders as gender]--]
                       [#--[#if gender.id == item.gender]--]
                           [#--<td>${gender.name}</td>--]
                       [#--[/#if]--]
                       [#--[/#list]--]
                   [#--[/#if]--]
                   [#--<td>${item.email}</td>--]
                   [#--<td>${item.name}</td>--]
                   [#--<td>${item.department}</td>--]
                   [#--这里boolean类型 要家个 td-status--]
                   [#--<td class="td-status">--]
                       [#--[#if item.isEnabled == true]--]
                       [#--<span class="label label-success radius">是</span>--]
                           [#--[#else]--]
                        [#--<span class="label lable-error radius">否</span>--]
                       [#--[/#if]--]
                   [#--</td>--]
                   [#--<td class="td-status">--]
                       [#--[#if item.isLocked == true]--]
                           [#--<span class="label label-success radius">是</span>--]
                       [#--[#else]--]
                           [#--<span class="label lable-error radius">否</span>--]
                       [#--[/#if]--]
                   [#--</td>--]
                   [#--<td>${item.loginFailureCount}</td>--]
                   [#--<td>${item.lockedDate}</td>--]
                   [#--<td>${item.loginDate}</td>--]
                   [#--<td>${item.loginIp}</td>--]
               [#--需要转换--]
               [#--<td><u style="cursor:pointer" class="text-primary" onclick="member_show('${item.enterprise.name}','enterpriseView.jhtml?id=${item.enterprise.id}','1000${item_index}','360','400')">${item.enterprise.name}</u></td>--]
                   [#--<td>${item.enterprise}</td>--]
               [#--需要转换 ,已转换--]
                   [#--<td>--]
                       [#--[#if item.roles??]--]
                         [#--[#list item.roles as role]--]
                             [#--[#if role_index==0]${role.name}[#else],${role.name}[/#if]--]
                         [#--[/#list]--]
                       [#--[/#if]--]
                   [#--</td>--]
                   [#--<td class="td-manage">--]
                       [#--<a title="编辑" href="javascript:;" onclick="member_edit('编辑','edit.jhtml?id=${item.id}','200${item_index}','','510')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a>--]
                       [#--<a title="删除" href="javascript:;" onclick="member_del(this,'1')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>--]
               [#--</tr>--]
               [#--[/#list]--]
            [#--[/#if]--]
            [#--[/#if]--]
            </tbody>
        </table>
    </div>
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${base}/resources/admin/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
            $(function(){
                $('.table').dataTable({
                    "bProcessing": true,
                    "bServerSide": true,
                    "sPaginationType": "full_numbers",
                    "sAjaxSource":"${base}/admin/admin/list.jhtml",
                    "bAutoWidth": true,//自动宽度
//            "bPaginate":true,
            "aaSorting": [[ 1, "desc" ]],//默认第几个排序
//            "bStateSave": false,//状态保存
                    "bFilter": false, //过滤功能
                    "bLengthChange": false, //改变每页显示数据数量
                    "aoColumns": [
                        {"mData": null},
                        {"mData": "id", "bSortable": false},
                        {"mData": "username"},
                {"mData": "password"},
                {"mData": "gender",},
                {"mData": "email"},
                {"mData": "name"},
                {"mData": "department"},
                {"mData": "isEnabled"},
                {"mData": "isLocked"},
                {"mData": "loginFailureCount"},
                {"mData": "lockedDate"},
//                {"mData": "loginDate"},
//                {"mData": "loginIp"},
//                {"mData": "enterprise"},
//                {"mData": "roles"},
//                {"mData": "null", "bSortable": false}
                    ],
                    "aoColumnDefs": [
                        {
                            "aTargets" : [0],
                            "mRender" : function (data, display, row) {
                                return "<input type='checkbox' value='1' name=''>";
                            }
                        },
                        //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
                      {"orderable":false,"aTargets":[0]}// 制定列不参与排序
                    ],
                    "fnServerData":function ( sSource,aoData, fnCallback) {
//                alert(aoData);
                        $.ajax({
                            url : sSource,//这个就是请求地址对应sAjaxSource
                            data : {"aoData":JSON.stringify(aoData)},//这个是把datatable的一些基本数据传给后台,比如起始位置,每页显示的行数
                            type : 'get',
                            dataType : 'json',
                            async : false,
                            success : function(message) {
                                if( message.type == "success"){
                                    fnCallback(message.data);//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
                                }else{
                                    layer.msg('数据请求失败!',{icon:1,time:1000});
                                }
                            },
                            error : function(msg) {
                                layer.msg('数据请求失败!',{icon:1,time:1000});
                            }
                        });
                    }
                });

            });
    /*用户-添加*/
    function member_add(title,url,w,h){
        layer_show(title,url,w,h);
    }
    /*用户-查看*/
    function member_show(title,url,id,w,h){
        layer_show(title,url,w,h);
    }

    /*用户-编辑*/
    function member_edit(title,url,id,w,h){
        layer_show(title,url,w,h);
    }
    /*密码-修改*/
    function change_password(title,url,id,w,h){
        layer_show(title,url,w,h);
    }
    /*用户-删除*/
    function member_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
            $.ajax({
                type: 'POST',
                url: '',
                dataType: 'json',
                success: function(data){
                    $(obj).parents("tr").remove();
                    layer.msg('已删除!',{icon:1,time:1000});
                },
                error:function(data) {
                    console.log(data.msg);
                },
            });
        });
    }

</script>
</body>
</html>