<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
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
    <style>
        .center {
            text-align: center;
        }
    </style>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>

    <title>量表管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 量表管理 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px"
                                               href="javascript:location.replace(location.href);" title="刷新"><i
        class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="page-container">
    <div class=""> 日期范围：
        <input type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin"
               class="input-text Wdate" style="width:120px;">
        -
        <input type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax"
               class="input-text Wdate" style="width:120px;">

		[#if types??]
			<span class="select-box" style="background-color:#FFFFFF;width:100px;height:32px;">
				<select name="type" class="select" style="background-color: #FFFFFF">
					<option value="">常模类型</option>
					[#list types as type]
					<option value="${type.id}">${type.name}</option>
					[/#list]
				</select>
			</span>
		[/#if]
        [#if userTypes??]
			<span class="select-box" style="background-color:#FFFFFF;width:100px;height:32px;">
				<select name="userType" class="select" style="background-color: #FFFFFF;">
					<option value="">用户类型</option>
					[#list userTypes as userType]
					<option value="${userType.id}">${userType.name}</option>
					[/#list]
				</select>
			</span>
		[/#if]
    [#if statuss??]
        <span class="select-box" style="background-color:#FFFFFF;width:100px;height:32px;">
				<select name="status" class="select" style="background-color: #FFFFFF;">
					<option value="">发布状态</option>
                    [#list statuss as status]
                        <option value="${status.id}">${status.name}</option>
                    [/#list]
				</select>
			</span>
    [/#if]
        <input type="text" class="input-text" style="width:150px" placeholder="输入要查询的内容" id="searchValue" name="">
        <button type="submit" class="btn btn-success radius" id="" onclick="search();" name="">
            <i class="Hui-iconfont">&#xe665;</i> 查询
        </button>
    </div>
    <div class="cl pd-5 bg-1 bk-gray mt-20">
        <span class="l">
                <a href="javascript:;" onclick="add('首页 &gt; 量表管理 &gt; 新增','add.jhtml','','510')" class="btn btn-primary radius">
                <i class="Hui-iconfont">&#xe600;</i> 新增量表</a>
                <a href="javascript:;" onclick="delAll()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>
        </span>
    </div>
    <div class="mt-20">
        <table class="table table-border table-bordered table-hover table-bg table-sort">
            <thead style="width: 100%;">
            <tr class="text-c">
            </tr>
            </thead>
            <tbody style="width: 100%;">
            </tbody>
        </table>
    </div>
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${base}/resources/admin/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/h-ui.admin/js/H-ui.admin.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${base}/resources/admin/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/datatables/1.10.15/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
    var table;
    var isSelectAll = false;
    var idTitleChange = function(){
        if(!isSelectAll){
            $('tbody').find("input[name='ids']:checkbox").prop('checked', true);
        }else{
            $('tbody').find("input[name='ids']:checkbox").removeProp('checked');
        }
        isSelectAll = !isSelectAll;

    }
    $(function () {
        $('.table').DataTable({
            "bProcessing": true,
            "bServerSide": true,
            "sPaginationType": "full_numbers",
            "sAjaxSource": "${base}/admin/gauge/list.jhtml",
            "aaSorting": [[2, "desc"]],//默认第几个排序
            "bFilter": false, //过滤功能
            "bLengthChange": false, //改变每页显示数据数量
            language: {
                "sProcessing": "",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            "createdRow": function (row, data, dataIndex) {
                $(row).children('td').attr('style', 'text-align: center;')
                $(row).children('td').eq(4).attr('style', 'text-align: left;');
                $(row).children('td').eq(5).attr('style', 'text-align: left;');

            },
            "aoColumns": [
                {
                    "mData": "id", "bSortable": false,
                    "sClass": "center",
                    "sTitle": "<input type=\"checkbox\" onchange='idTitleChange();' id=\"idTitle\" value=\"\">",
                },
                {
                    "mData": "id",
                    "sTitle": "ID",
                    "sClass": "center"
                },
                {
                    "mData": "createDate",
                    "sTitle": "创建日期",
                    "sClass": "center"
                },
                {
                    "mData": "modifyDate",
                    "sTitle": "修改日期",
                    "sClass": "center"
                },
                {
                    "mData": "title",
                    "sTitle": "主标题",
                    "sClass": "center"
                },
                {
                    "mData": "subTitle",
                    "sTitle": "副标题",
                    "sClass": "center"
                },
                {
                    "mData": "marketPrice",
                    "sTitle": "原价",
                    "sClass": "center"
                },
                {
                    "mData": "price",
                    "sTitle": "现价",
                    "sClass": "center"
                },
                {
                    "mData": "brokerage",
                    "sTitle": "推广佣金",
                    "sClass": "center"
                },
                {
                    "mData": "distribution",
                    "sTitle": "分销佣金",
                    "sClass": "center"
                },
                {
                    "mData": "evaluation",
                    "sTitle": "测评人数",
                    "sClass": "center"
                },
                {
                    "mData": "type",
                    "sTitle": "常模类型",
                    "sClass": "center"
                },
                {
                    "mData": "userType",
                    "sTitle": "用户类型",
                    "sClass": "center"
                },
                {
                    "mData": "status",
                    "sTitle": "状态",
                    "sClass": "center"
                },
                {
                    "mData": "id",
                    "sTitle": "设置",
                    "sClass": "center"
                },
                {
                    "mData": "id",
                    "sTitle": "操作",
                    "sClass": "center"
                }
            ],
            "aoColumnDefs": [
                {
                    "aTargets": [0],
                    "mRender": function (data, display, row) {
                        if(data != null){
                            return "<input style='text-align: center;' type='checkbox' value='" + data + "' name='ids'>";
                        }else{
                            return "";
                        }
                    }
                }, 
                {
                    "aTargets": [2],
                    "mRender": function (data, display, row) {
                        return DateFormat(data, 'yyyy-MM-dd HH:mm:ss');
                    }
                },
                {
                    "aTargets": [3],
                    "mRender": function (data, display, row) {
                        return DateFormat(data, 'yyyy-MM-dd HH:mm:ss');
                    }
                },
                {
                    "aTargets": [11],
                    "mRender": function (data, display, row) {
                        if(data != null){
                        [#if types??]
                            [#list types as type]
                                if ("${type.id}" == data) {
                                    return "${type.name}";
                                }
                            [/#list]
                        [/#if]
                        }else{
                            return "";
                        }
                    }
                },
                {
                    "aTargets": [12],
                    "mRender": function (data, display, row) {
                        if(data != null){
                        [#if userTypes??]
                            [#list userTypes as userType]
                                if ("${userType.id}" == data) {
                                    return "${userType.name}";
                                }
                            [/#list]
                        [/#if]
                        }else{
                            return "";
                        }
                    }
                },
                {
                    "aTargets": [13],
                    "mRender": function (data, display, row) {
                        if(data != null){
                        [#if statuss??]
                            [#list statuss as status]
                                if ("${status.id}" == data) {
                                    return "${status.name}";
                                }
                            [/#list]
                        [/#if]
                        }else{
                            return "";
                        }
                    }
                },
                {
                    "aTargets": [14],
                    "mRender": function (data, display, row) {
                        if(data != null){
                            return  "<a title='题目' href='javascript:;' onclick=\"edit('首页 &gt; 量表管理 &gt; 题目','../gaugeQuestion/index.jhtml?gaugeId=" + data + "','200" + data + "','510')\" class=\"ml-5\" style='text-decoration:none'><i class='Hui-iconfont'>题目</i></a>" +
                                    "<a title='因子' href='javascript:;' onclick=\"edit('首页 &gt; 量表管理 &gt; 因子','../gaugeGene/index.jhtml?gaugeId=" + data + "','200" + data + "','510')\" class=\"ml-5\" style='text-decoration:none'><i class='Hui-iconfont'>因子</i></a>" +
                                    "<a title='结果' href='javascript:;' onclick=\"edit('首页 &gt; 量表管理 &gt; 结果','../gaugeResult/index.jhtml?gaugeId=" + data + "','200" + data + "','510')\" class=\"ml-5\" style='text-decoration:none'><i class='Hui-iconfont'>结果</i></a>"+
                                    "<a title='测谎' href='javascript:;' onclick=\"edit('首页 &gt; 量表管理 &gt; 测谎','detect.jhtml?id=" + data + "','200" + data + "','510')\" class=\"ml-5\" style='text-decoration:none'><i class='Hui-iconfont'>测谎</i></a>";
                        }else{
                            return "";
                        }
                    }

                },
                {
                    "aTargets": [15],
                    "mRender": function (data, display, row) {
                        if(data != null){
                            return  "<a title='编辑' href='javascript:;' onclick=\"edit('首页 &gt; 量表管理 &gt; 编辑','edit.jhtml?id=" + data + "','200" + data + "','510')\" class=\"ml-5\" style='text-decoration:none'><i class='Hui-iconfont'>&#xe6df;</i></a>" +
                                    "<a title='删除' href='javascript:;' onclick=\"del(this,'" + data + "')\" class='ml-5' style='text-decoration:none'><i class='Hui-iconfont'>&#xe6e2;</i></a>";
                        }else{
                            return "";
                        }
                    }

                },
                //{'bVisible': false, "aTargets": [ 3 ]} //控制列的隐藏显示
                {"orderable": false, "aTargets": [0, 14,15]}// 制定列不参与排序
            ],
            "fnServerData": function (sSource, aoData, fnCallback) {
                /*处理查询数据*/searchValue
                var _beginDate = $("#datemin").val();
                var _endDate   = $("#datemax").val();
                var _searchValue = $("#searchvalue").val();
                /*处理常量*/
                var _type =  $('select[name="type"]').val();
                var _userType =  $('select[name="userType"]').val();
                var _status =  $('select[name="status"]').val();
                var index = layer.msg('加载中', {
                    icon: 16
                    ,shade: 0.01
                });
                $.ajax({
                    url: sSource,//这个就是请求地址对应sAjaxSource
                    data: {
                        "aoData": JSON.stringify(aoData),
                        "beginDate":_beginDate,
                        "endDate":_endDate,
                        "type":_type,
                        "userType":_userType,
                        "status":_status,
                        "searchValue":_searchValue
                    },//这个是把datatable的一些基本数据传给后台,比如起始位置,每页显示的行数
                    type: 'get',
                    dataType: 'json',
                    async: false,
                    success: function (message) {
                        layer.close(index);
                        if (message.type == "success") {
                            fnCallback(message.data);//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
                        } else {
                            layer.msg('数据请求失败!', {icon: 1, time: 1000});
                        }
                    },
                    error: function (msg) {
                        layer.close(load);
                        layer.msg('数据请求失败!', {icon: 2, time: 1000});
                    }
                });
            }
        });
        table = $('.table').DataTable();
    });
//   表格自适应屏幕
    window.onresize = function(){
        $('.table').css('width','100%');
    }
    /*添加单行*/
    function add_row(data) {
        table.row.add(data).draw();
    }

    /*搜索*/
    function search(){
     table.ajax.reload();
    }
    /*添加*/
    function add(title, url, w, h) {
        var index = layer.open({
            type:2,
            title:title,
            content:url
        });
        layer.full(index);
    }
    /*查看*/
    function show(title, url, id, w, h) {
        layer_show(title, url, w, h);
    }
    /*编辑*/
    function edit(title, url, id, w, h) {
        var index = layer.open({
            type:2,
            title:title,
            content:url
        });
        layer.full(index);
    }
    /*提示框*/
    function toast(msg, icon) {
        layer.msg(msg, {icon: icon, time: 1000});
    }
    /*关闭页面*/
    function closeWindow(index, msg) {
        layer.close(index);
        layer.msg(msg, {icon: 1, time: 1000});
    }
    /*删除全部*/
    function delAll(){
        var url = "${base}/admin/gauge/delete.jhtml";
        var i = 0;
        $('input[type="checkbox"][name="ids"]:checked').each(
                function() {
                    $(this).parents("tr").addClass("selected");
                    if(i == 0){
                        url += "?ids="+$(this).val();
                    }else{
                        url += "&ids="+$(this).val();
                    }
                    i++;
                }
        );
        if(i < 1) {
            layer.msg('请选择要删除的数据!', {icon: 0, time: 1000});
            return;
        }
        layer.confirm('确认要删除吗？', function (index) {
            var load = layer.msg('加载中', {
                icon: 16
                ,shade: 0.01
            });
            $.ajax({
                type: 'POST',
                url: url ,
                dataType: 'json' ,
                success: function (data) {
                    layer.close(load);
                    if (data.type == "success") {
                        table.rows('.selected').remove().draw( false );
                        layer.msg('已删除!', {icon: 1, time: 1000});
                    } else {
                        layer.msg('删除失败!', {icon: 2, time: 1000});
                    }
                },
                error: function (data) {
                    layer.close(load);
                    console.log(data.msg);
                },
            });
        });
    }
    /*删除*/
    function del(obj, id) {
        layer.confirm('确认要删除吗？', function (index) {
            var load = layer.msg('加载中', {
                icon: 16
                ,shade: 0.01
            });
            $.ajax({
                type: 'POST',
                data: {
                    ids: id
                },
                url: '${base}/admin/gauge/delete.jhtml',
                dataType: 'json',
                success: function (data) {
                    layer.close(load);
                    if (data.type == "success") {
                        $(obj).parents("tr").addClass("del");
                        table.row('.del').remove().draw( false );
                        layer.msg('已删除!', {icon: 1, time: 1000});
                    } else {
                        layer.msg('删除失败!', {icon: 2, time: 1000});
                    }
                },
                error: function (data) {
                    console.log(data.msg);
                },
            });
        });
    }
	
    function DateFormat(timestamp, format) {
        var newDate = new Date();
        newDate.setTime(timestamp);
        var date = {
            "M+": newDate.getMonth() + 1,
            "d+": newDate.getDate(),
            "H+": newDate.getHours(),
            "h+": newDate.getHours(),
            "m+": newDate.getMinutes(),
            "s+": newDate.getSeconds(),
            "q+": Math.floor((newDate.getMonth() + 3) / 3),
            "S+": newDate.getMilliseconds()
        };
        if (/(y+)/i.test(format)) {
            format = format.replace(RegExp.$1, (newDate.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (var k in date) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1
                        ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
            }
        }
        return format;
    }
</script>
</body>
</html>