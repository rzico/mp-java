<!--_meta 作为公共模版分离出去-->
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="Bookmark" href="/favicon.ico">
    <link rel="Shortcut Icon" href="/favicon.ico"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${base}/resources/admin/lib/html5shiv.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/lib/respond.min.js"></script>

    <![endif]-->
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/lib/Hui-iconfont/1.0.8/iconfont.css"/>

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/h-ui.admin/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/css/wx.css"/>

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin/css/style.css"/>

    <!--[if IE 6]>
    <script type="text/javascript" src="${base}/resources/admin/lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->
    <link href="${base}/resources/admin/lib/webuploader/0.1.5/webuploader.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">

    </style>
</head>
<body style="background-color:transparent">
[#if page??]
    [#if page.content??]
        [#list page.content as article]
        <div class="tag">
            <div class="tag top">
                <a>${article.title}</a>
                <img src="${article.thumbnail}"/>
                <a style="top:17%; font-size: 14px"></a>
            </div>
            <div class="tag top zz">
                <a style="top: 50%;" href="/usered/article1/open.jhtml?id=${article.id}"></a>
            </div>
            <div class="tag down">
                <a style="text-decoration:none; out-line: nonecolor;">更新于${article.modifyDate}</a>
                <a onclick="openEdit(${article.id})" style="text-decoration:none; out-line: nonecolor; width: 10%; left:10%;"><i class="Hui-iconfont">
                    &#xe60c;</i></a>
                <a href="javascript:;" name="${article.id}" style="text-decoration:none; out-line: nonecolor; width: 10%; left:10%;"><i class="Hui-iconfont">
                    &#xe695;</i></a>
            </div>
        </div>
        [/#list]
    [/#if]
    [#if page.pageable??]
    <div class="bottom_bar">
        <div class="middle-bar">
            <a id="next" href="#">下一页</a>
            <a id="back" class="wihte" href="#">返回</a>
            <a id="seach" href="#">搜索</a>
            <input style="float: right" type="text" id="seachValue" name="seachValue" placeholder="请输入文章名称....."/>
            <a id="up" style="float: left; left: 5%" href="#">上一页</a>
            <a id="newArticle" style="float: left; left: 5%" href="#">新建文章</a>
        </div>
    </div>
    [/#if]
[/#if]

<div id="cover">
    <div class="popup">
        <a style="top: 0;float: right"><i class="Hui-iconfont">&#xe6a6; </i></a>
        <div style="width:100%;height: 90%;background: url('${base}/resources/admin/images/iphone.png') no-repeat 0px 0px;background-size: 100% 100%;padding-top: 23%;">
            <!--<div style="margin: auto;width: 88%;height:60%;border: 1px solid #000000;"></div>-->
            <iframe id="view" style="margin-left: 5%;width: 88%;height:87%;" src="http://dev.1xx.me/"></iframe>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
    $(function () {
        $(".tag.top").hover(function () {
            $(this).next().css("z-index", "1000");
        }, function () {
//            $(this).next().css("z-index","-1000");
        });
        $(".tag.top.zz").hover(function () {
//            $(this).next().css("z-index","1000");
        }, function () {
            $(this).css("z-index", "-1000");
        })
    })

    //打开
    $("[href='javascript:;']").click(function () {
//        alert($(this).attr("name"));
        $("#view").attr("src","http://dev.1xx.me/#/t1001?id="+$(this).attr("name"));
        $("#cover").fadeIn("fast");
    });
    //关闭
    $(".popup").first().click(function () {
        $("#cover").fadeOut("fast");
    });
    [#if page.pageable??]
    //下一页
    $("#next").click(function () {
        var pageNum=Math.ceil(${page.total}/${page.pageable.pageSize});
        var start=${page.pageable.pageStart}+${page.pageable.pageSize};
        var draw=${page.pageable.draw}+1;
        if((pageNum-1)*8>${page.pageable.pageStart}){
            $(window).attr('location',"${base}/usered/article1/orders.jhtml?id=${id}&start="+start+"&draw="+draw);
        }else{
            alert('已经是最后一页');
        }
    });
    //上一页
    $("#up").click(function () {
        var start=${page.pageable.pageStart}-${page.pageable.pageSize};
        var draw=${page.pageable.draw}+1;
        if(${page.pageable.pageStart}!=0){
            $(window).attr('location',"${base}/usered/article1/orders.jhtml?id=${id}&start="+start+"&draw="+draw);
        }else{
            alert('这是第一页');
        }
    });
    //返回
    $("#back").click(function () {
        $(window).attr('location',"${base}/usered/index/main1.jhtml");
    });
    //搜索
    $("#seach").click(function () {
        $(window).attr('location',"${base}/usered/article1/orders.jhtml?id=${id}&seachValue="+$("#seachValue").val());
    });
    //新建文章
    $("#newArticle").click(function () {
        $(window).attr('location',"${base}/usered/article1/editView.jhtml?articleId=${id}");
    });
    //编辑文章
    function openEdit(id){
        $(window).attr('location',"${base}/usered/article1/editView.jhtml?articleId=${id}&id="+id);
    }
    [/#if]
</script>
</body>
</html>
