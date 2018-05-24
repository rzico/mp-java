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
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="lib/Hui-iconfont/1.0.8/iconfont.css">

</head>
<body>
<!--<div class="top-bar">
    <a href="#">文集列表</a>&nbsp;>&nbsp;<a href="#">某某文集</a>&nbsp;>&nbsp;<a href="#">新建</a>
</div>-->

<div class="left_bar">
    <h4>段落合集</h4>
    <div class="phase">
    [#if article??]
        [#list article.templates as content]
            [#if content_index==0]
                <div name="${content_index}" class="title" style="position: relative;border-top: 1px solid #e7e7eb">
                    <h4 class="runoff">${content.content}</h4>
                    <div style="background-image: url('${content.thumbnail}');"></div>
                    <div class="shade" style="top: -57px;">
                        <!--<a><i class="Hui-iconfont Hui-iconfont-arrow1-top"></i></a>-->
                        <a name="down"><i class="Hui-iconfont">&#xe674;</i></a>
                        <a class="delete" style="float:right;"><i class="Hui-iconfont">&#xe6e2;</i></a>
                    </div>
                </div>
            [#else]
                <div name="${content_index}" style="position: relative;" class="title">
                    <p class="abstract runoff">${content.content}</p>
                    <img src="${content.thumbnail}"/>
                    <div class="shade">
                        <a name="up"><i class="Hui-iconfont">&#xe679;</i></a>
                        [#if content_has_next]
                            <a name="down"><i class="Hui-iconfont">&#xe674;</i></a>
                        [/#if]
                        <a class="delete" style="float:right;"><i class="Hui-iconfont">&#xe6e2;</i></a>
                    </div>
                </div>
            [/#if]
        [/#list]
    [#else]
        <div class="title" style="position: relative;border-top: 1px solid #e7e7eb">
            <h4 class="runoff"></h4>
            <div style="background-image: url('');"></div>
            <div class="shade" style="top: -57px;">
            </div>
        </div>
    [/#if]
        <div id="add" class="title">
            <a style="left: 50%;transform: translate(-50%,-50%);font-size: 42px"><i class="Hui-iconfont">
                &#xe600;</i></a>
        </div>
    </div>
</div>
<div class="middle_bar">
    <div class="titlel">
        <lu>
            <li class="botton"><a herf="#" title="打开源码" onclick="openit()"><i class="Hui-iconfont">&#xe6ee;</i></a></li>
            <li class="botton"><a herf="#"><i class="Hui-iconfont">&#xe66b;</i></a></li>
            <li class="botton"><a herf="#"><i class="Hui-iconfont">&#xe66c;</i></a></li>
            <li class="botton"><a herf="#"><i class="Hui-iconfont">&#xe6ec;</i></a></li>
            <li class="botton"><a herf="#" title="加粗" id="bold" onclick="setBold()"><i class="Hui-iconfont">&#xe6e7;</i></a>
            </li>
            <li class="botton"><a herf="#" title="倾斜" onclick="fn_italic()"><i class="Hui-iconfont">&#xe6e9;</i></a>
            </li>
            <li class="botton"><a herf="#" title="下划线" onclick="fn_change_selection()"><i
                    class="Hui-iconfont">&#xe6fe;</i></a></li>
            <li class="botton"><a herf="#" title="居中" onclick="fn_milled()"><i class="Hui-iconfont">&#xe70e;</i></a>
            </li>
            <li class="botton"><a herf="#" title="居左" onclick="fn_left()"><i class="Hui-iconfont">&#xe710;</i></a></li>
            <li class="botton"><a herf="#" title="居右" onclick="fn_right()"><i class="Hui-iconfont">&#xe711;</i></a></li>
            <!--<li class="botton"><a herf="#"><i class="Hui-iconfont">&#xe70f;</i></a></li>-->
        </lu>
    </div>
    <div class="editor" id="qseditor" contenteditable="true" maxlength="5000" placeholder="请输入内容(不超过5000字)...">

    </div>
</div>
<div class="right_bar">
    <h4>多媒体</h4>
    <div class="phase">
        <div class="item" style="border-top: 1px solid #e7e7eb;">
            <a href="#" class="tab"><i class="Hui-iconfont">&#xe685;</i>图片</a>
        </div>
        <div class="item">
            <a href="#" class="tab"><i class="Hui-iconfont">&#xe6e6;</i>视频</a>
        </div>
        <div class="item">
            <a href="#" class="tab"><i class="Hui-iconfont">&#xe6a5;</i>音乐</a>
        </div>
        <div class="item">
            <a href="#" class="tab"><i class="Hui-iconfont">&#xe620;</i>商品</a>
        </div>
    </div>
</div>
<div class="bottom_bar">
    <div class="middle-bar">
        <a id="back" style="float: left" class="wihte" href="#">返回</a>
        [#if templates??]
        <select id="selectTemplate">
            [#list templates as template]
            <option value="${template.id}">${template.name}</option>
            [/#list]
        </select>
        [/#if]
        [#if article??]
            <input id="title" type="text" value="${article.title}" />
        [#else]
            <input id="title" type="text" placeholder="请输入文章标题..." />
        [/#if]

        <a id="view" class="wihte" href="#">预览文章</a>
        <a id="save" href="#">保存文章</a>
    </div>
</div>
<div id="cover">
    <div class="popup">
        <a style="top: 0;float: right"><i class="Hui-iconfont">&#xe6a6; </i></a>
        <div style="width:100%;height: 90%;background: url('${base}/resources/admin/images/iphone.png') no-repeat 0px 0px;background-size: 100% 100%;padding-top: 23%;">
            <iframe id="viewIframe" style="margin-left: 5%;width: 88%;height:87%;" src="http://dev.1xx.me/"></iframe>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<!--<script src="https://code.jquery.com/jquery-1.4.2.min.js"></script>-->
<script type="text/javascript">

    //中间变量
    var middle = null;

    var templateId = null;
    var goodsId = null;
    [#if article??]
    var thumbnail = [#if article.thumbnail??]"${article.thumbnail}"[#else] null [/#if];
    var title = [#if article.title??]"${article.title}"[#else] null [/#if];
    var music = [#if article.music??]"{\"id\":\"${article.music.id}\",\"name\":\"${article.music.name}\"}"[#else] null [/#if];
    [#else]
    var thumbnail = null;
    var title = null;
    var music = null;
    [/#if]

    //监听编辑器（失败品）
    $($("#qseditor").focus(function () {
//        alert("1   "+document.queryCommandValue("ForeColor"));
//        alert("2   "+document.queryCommandValue("FontSize"));
//        alert("3   "+document.queryCommandValue("bold"));
//        alert("4   "+document.queryCommandValue("italic"));
        var cursurPosition = 0;
        var s = 1;
        if ($("#qseditor")[0].selectionStart) {//非IE
            cursurPosition = $("#qseditor").selectionStart;

            var color = document.queryCommandValue("ForeColor"); // 字体颜色
            var size = document.queryCommandValue("FontSize");  // 字体大小
            var bold = document.queryCommandValue("bold");  // 是否加粗
            var italic = document.queryCommandValue("italic");  // 是否是斜体

            if (s != cursurPosition) {
                alert(cursurPosition);
                alert(color);
                alert(size);
                alert(bold);
                alert(italic);
                s = cursurPosition;
            }
            //监听是否加粗了
            if (bold == "false") {
                $("#bold").css("border", "none");
            } else {
                $("#bold").css("border", "1px solid #99ccff");
            }

            //监听是否是斜体
            if (italic == "false") {
                $("[title='倾斜']").css("border", "none");
            } else {
                $("[title='倾斜']").css("border", "1px solid #99ccff");
            }
            alert(cursurPosition);
        } else {//IE
            try {
                var range = document.selection.createRange();
                range.moveStart("character", -$input.value.length);
                cursurPosition = range.text.length;

                var color = document.queryCommandValue("ForeColor"); // 字体颜色
                var size = document.queryCommandValue("FontSize");  // 字体大小
                var bold = document.queryCommandValue("bold");  // 是否加粗
                var italic = document.queryCommandValue("italic");  // 是否是斜体
                //监听是否加粗了
                if (bold == "false") {
                    $("#bold").css("border", "none");
                } else {
                    $("#bold").css("border", "1px solid #99ccff");
                }

                //监听是否是斜体
                if (italic == "false") {
                    $("[title='倾斜']").css("border", "none");
                } else {
                    $("[title='倾斜']").css("border", "1px solid #99ccff");
                }
                alert(cursurPosition);
            } catch (e) {
                cursurPosition = 0;
            }
        }
//        alert(cursurPosition);//打印当前索引

    }));

    //编辑器未激活事件
    $("#qseditor").blur(function () {
        if (middle != null) {
            middle.children().eq(0).html($(this).html());
        }
        $("#qseditor").focus();
    });

    //左菜单样式事件
    $("body").on("click", ".title", function () {
        $(".title").css("border", "1px solid #e7e7eb").css("border-top", "none");
        $(".phase").find(".title").first().css("border", "1px solid #e7e7eb");
        $(this).css("border", "1px solid #99ccff");
        $(this).prev().css("border-bottom", "none");

        middle = $(this);
        $("#qseditor").html($(this).children().eq(0).html());
    });

    //点击菜单框事件
    $(".item").click(function () {
        $(".item").css("border", "1px solid #e7e7eb").css("border-top", "none");
        $(".phase").find(".bottom").first().css("border", "1px solid #e7e7eb");
        $(this).css("border", "1px solid #99ccff");
        $(this).prev().css("border-bottom", "none");
    });

    //文本编辑器 功能按钮点击样式事件
    $(".botton").click(function () {
//            alert($(this).css("border-bottom-style"));
        if ($(this).css("border-bottom-style") == 'none') {
            $(this).css("border", "1px solid #99ccff");
        } else {
            $(this).css("border", "none");
        }
    });

    //预览遮罩打开事件
    $("#view").click(function () {
        $("#viewIframe").attr("src","${base}/usered/article1/articleview.jhtml?content="+JSON.stringify(getContent()));
        $("#cover").fadeIn("fast");
    });

    //预览遮罩关闭事件
    $(".popup").first().click(function () {
        $("#cover").fadeOut("fast");
    });

    //删除
    $("body").on("click", ".delete", function () {
        var parent = $(this).offsetParent().offsetParent();
        //删除第一个
        if (!parent.prev().is(".title")) {
            var nextbro = parent.next(".title");
            var txt = nextbro.children().eq(0).html();
            var imgurl = nextbro.children().eq(1).attr("src");
            parent.children().eq(0).html(txt);
            parent.children().eq(1).attr("style", "background-image: url('" + imgurl + "');");
            deleteListening(parent);
//            parent.next(".title").children().find("[name='up']").remove();
        } else if (parent.next().attr("id") == "add") {
            parent.prev().children().find("[name='down']").remove();
            deleteListening(parent.prev());
        }
        else {
            deleteListening(parent.prev());
        }
    });

    //删除监听
    function deleteListening(parent) {
        parent.next().remove();
        if (parent.nextAll().length == 1 && parent.prevAll().length == 0) {
            parent.children().find(".delete").remove();
            parent.children().find("[name='down']").remove();
            parent.children().find("[name='up']").remove();
        }
    }

    //上移动
    $("body").on("click", "[name='up']", function () {
        var parent = $(this).offsetParent().offsetParent();
        //移动第一个
        if (parent.prevAll().length == 1) {
            var prevbro = parent.prev(".title");
            var txt = prevbro.children().eq(0).html();
            var imgurl = prevbro.children().eq(1).attr("style");
            prevbro.children().eq(0).html(parent.children().eq(0).html());
            prevbro.children().eq(1).attr("style", imgurl.substring(0, 23) + parent.children().eq(1).attr("src") + imgurl.substring(imgurl.length - 3, imgurl.length));
            parent.children().eq(0).html(txt);
            parent.children().eq(1).attr("src", imgurl.substring(23, imgurl.length - 3));
        }
        else {
            var prevbro = parent.prev(".title");
            var txt = prevbro.children().eq(0).html();
            var imgurl = prevbro.children().eq(1).attr("src");
            prevbro.children().eq(0).html(parent.children().eq(0).html());
            prevbro.children().eq(1).attr("src", parent.children().eq(1).attr("src"));
            parent.children().eq(0).html(txt);
            parent.children().eq(1).attr("src", imgurl);
        }
    });

    //下移动
    $("body").on("click", "[name='down']", function () {
        var parent = $(this).offsetParent().offsetParent();
        //移动第一个
        if (!parent.prev().is(".title")) {
            var nextbro = parent.next(".title");
            var txt = parent.children().eq(0).html();
            var imgurl = parent.children().eq(1).attr("style");
            parent.children().eq(0).html(nextbro.children().eq(0).html());
            parent.children().eq(1).attr("style", imgurl.substring(0, 23) + nextbro.children().eq(1).attr("src") + imgurl.substring(imgurl.length - 3, imgurl.length));
            nextbro.children().eq(0).html(txt);
            nextbro.children().eq(1).attr("src", imgurl.substring(23, imgurl.length - 3));
        }
        else {
            var nextbro = parent.next(".title");
            var txt = nextbro.children().eq(0).html();
            var imgurl = nextbro.children().eq(1).attr("src");
            nextbro.children().eq(0).html(parent.children().eq(0).html());
            nextbro.children().eq(1).attr("src", parent.children().eq(1).attr("src"));
            parent.children().eq(0).html(txt);
            parent.children().eq(1).attr("src", imgurl);
        }
    });

    //添加
    $("#add").click(function () {

        var left_bar_item = "<div style=\"position: relative;\" class=\"title\">\n" +
                "            <p class=\"abstract runoff\"></p>\n" +
                "            <img src=\"null\"/>\n" +
                "            <div class=\"shade\">\n" +
                "                <a name=\"up\"><i class=\"Hui-iconfont\">&#xe679;</i></a>\n" +
                "                <a class=\"delete\" style=\"float:right;\"><i class=\"Hui-iconfont\">&#xe6e2;</i></a>\n" +
                "            </div>\n" +
                "        </div>";
        var down_item = "<a name=\"down\"><i class=\"Hui-iconfont\">&#xe674;</i></a>";

        var delete_item = "<a class=\"delete\" style=\"float:right;\"><i class=\"Hui-iconfont\">&#xe6e2;</i></a>";

        if ($(this).prev().children().eq(2).children().length == 0) {
            $(this).prev().children().eq(2).prepend(delete_item);
            $(this).prev().children().find(".delete").before(down_item);
        } else {
            $(this).prev().children().find(".delete").before(down_item);
        }

        $(this).before(left_bar_item);
    });

    //获取文章
    function getContent(){
        var articles = [];
        $(".left_bar").children().eq(1).children().each(function () {
            if ($(this).children().length != 3) {
                return false
            }
            var article = new Object();
            if ($(this).children().filter("img") != null) {
                article.mediaType = "image";
            } else if ($(this).children().filter("video") != null) {
                article.mediaType = "video";
            } else if ($(this).children().filter("audio") != null) {
                article.mediaType = "audio";
            } else {
                article.mediaType = "image";
            }

            //图片
            var oneimgurl = $(this).children().filter("div").attr("style");
            if (oneimgurl != null) {
                article.thumbnail = oneimgurl.substring(23, oneimgurl.length - 3);
                article.original = oneimgurl.substring(23, oneimgurl.length - 3);
            }
            //图片
            var imgurl = $(this).children().filter("img").attr("src");
            if (imgurl != null) {
                article.thumbnail = imgurl;
                article.original = imgurl;
            }

            //视频
            var videourl = $(this).children().filter("video").attr("src");
            if (videourl != null) {
                article.thumbnail = "";
                article.original = videourl;
            }

            var content = $(this).children().filter("p").html();
            if (content != null && content != "") {
                article.content = content;
            } else {
                var onecontent = $(this).children().filter("h4").html();
                if (onecontent != null && onecontent != "") {
                    article.content = onecontent;
                } else {
                    article.content = "";
                }
            }

            articles.push(article);
        });
        return articles;
    }

    //保存文章
    $("#save").click(function () {
        //拿取文章
        var articles=getContent();
//        alert(JSON.stringify(articles));
        //前台判定
        if (title == null || title == "") {
            alert("请输入文章标题");
//            layer.msg('请输入文章标题',{icon:2,time:1000});
            return;
        }

        if (thumbnail == null || thumbnail == "") {
            for (var i = 0; i < articles.length; i++) {
                if (articles[i].thumbnail != null && articles[i].thumbnail != "") {
                    thumbnail = articles[i].thumbnail;
                    break;
                }
            }
        }
        $.ajax({
            type: 'post',
            url: '/usered/article1/save.jhtml',
            dataType: 'json',
            data: {
                id: [#if article??]${article.id}[#else] null [/#if],
                templateId: templateId,
                goodsId: goodsId,
                content: JSON.stringify(articles),
                thumbnail: thumbnail,
                title: title,
                music: music
            },
            success: function (message) {
                if (message.type == "success") {
                    alert("保存成功");
//                    layer.msg('保存成功!',{icon:2,time:1000});
                } else {
                    alert("保存失败");
//                    layer.msg('保存失败!',{icon:2,time:1000});
                }
            },
            error: function (message) {
                alert("error");
//                layer.msg('error!',{icon:2,time:1000});
            }
        });
    });

    //返回
    $("#back").click(function () {
        $(window).attr('location', "${base}/usered/article1/orders.jhtml?id=${articleId}");
    });

    //监听选择框事件
    $("#selectTemplate").change(function (){
        templateId=$("#selectTemplate option:selected").val();
    });

    //监听文章标题
    $("#title").blur(function (){
        title=$(this).val();
    });

    /*
  *该function用来将选中的区块加粗
  */

    function setBold() {
        document.execCommand("Bold", false, null);
    }

    /*
   *该function用来将选中的区块加上不同的线条
   */

    function fn_change_selection() {

        document.execCommand('Underline');

    }

    /*
    *该function用来创建一个超链接
    */

    function fn_creatlink() {
        document.execCommand('CreateLink', false, prompt('请输入链接'));//弹出一个对话框输入URL
    }

    /*
    * 该function用来控制字体斜体
    * */
    function fn_italic() {
        document.execCommand('Italic');
    }

    /*
    * 该function用来控制段落居左
    * */
    function fn_left() {
        $("[title='居中']").parent().css("border", "none");
        $("[title='居右']").parent().css("border", "none");
        document.execCommand("JustifyLeft");
    }

    /*
    * 该function用来控制段落居右
    * */
    function fn_right() {
        $("[title='居左']").parent().css("border", "none");
        $("[title='居中']").parent().css("border", "none");
        document.execCommand("JustifyRight");
    }

    /*
    * 该function用来控制段落居中
    * */
    function fn_milled() {
        $("[title='居左']").parent().css("border", "none");
        $("[title='居右']").parent().css("border", "none");
        document.execCommand("JustifyCenter");
    }

    /*
    * 打开源码
    * */
    function openit() {
//                alert(document.getElementById("qseditor").contentEditable);
        if (document.getElementById("qseditor").contentEditable == "true") {
            var htmltxt = $("#qseditor").html();
//            alert(htmltxt);
            $("#qseditor").text(htmltxt);
            document.getElementById("qseditor").contentEditable = false;
        } else {
            var htmltxt = $("#qseditor").text();
            $("#qseditor").html(htmltxt);
            document.getElementById("qseditor").contentEditable = true;
        }
    }

</script>
</body>
</html>
</body>
</html>
