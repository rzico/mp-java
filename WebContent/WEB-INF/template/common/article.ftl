<html>
<head>
    <title>${data.title}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <style type="text/css">
    .article {
        margin-top: 40px
    }

    
    .noMt {
        margin-top: 0
    }

    .article .bg {
        position: relative;
        width: 100%;
        z-index: -1;
        margin: 0 auto;
        background: #fff
    }

    h1, h2, h3, h4, h5 {
        font-weight: 300
    }

    .article .main {
        padding: 10px 0 0;
        width: 100%;
        box-sizing: border-box
    }
    .article .meta_title {
        font-size: 18px;
        font-weight: bold;
        text-align: left;
        line-height: 33px;
        padding: 0 0 6px;
        margin: 0 15px;
        white-space: pre-wrap;
        text-align: start;
        -webkit-box-orient: horizontal;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical
    }

    .article .meta, .article .meta_title {
        word-wrap: break-word;
        word-break: break-all
    }

    .article .meta {
        font-family: Helvetica Neue, Helvetica, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
        font-size: 16px;
        margin: 0 0 24px;
        line-height: 1.5;
        padding: 0 15px
    }

    .article .meta a {
        color: #333
    }

    .article .meta span {
        color: #8c8c8c;
        margin-right: 8px
    }
    .download_bar {
        position: fixed;
        z-index: 3000000000;
        left: 0;
        top: 0;
        width: 100%;
        height: 40px;
        text-shadow: none;
        background-color: hsla(0, 0%, 99%, .9);
        border-bottom: 1px solid rgba(0, 0, 0, .1)
    }

    .download_bar .title {
        float: left;
        white-space: nowrap;
        height: 100%
    }

    .download_bar .title .logo {
        width: 26px;
        height: 26px;
        position: absolute;
        left: 10px;
        top: 7px
    }

    .download_bar .download_btn {
        position: absolute;
        right: 5px;
        margin: 0px 10px 0px 0;
        vertical-align: middle;
        color: #333;
        padding: 2px;
        font-size: 12px;
        line-height: 12px;
        text-align: center;
        border-radius: 10px;
        /*border: 1px solid #666;*/
        z-index: 100003
    }

    .download_bar p {
        text-align: left;
        word-break: break-word;
        color: #333;
        padding-left: 42px;
        height: 100%;
        padding-top: 1px
    }

    .download_bar .close_btn {
        position: absolute;
        right: 10px;
        top: 0;
        z-index: 100002
    }

    .download_bar .close_btn .close_icon {
        display: block;
        position: relative;
        width: 16px;
        height: 16px
    }

    .download_bar .title .name {
        line-height: 39px;
        font-size: 18px;
        white-space: normal
    }

    body, html {
        color: #333;
        font-family: PingFangSC-Regular;
        font-size: 16px
    }

    * {
        margin: 0;
        padding: 0;
        font-style: normal;
        -webkit-box-sizing: border-box;
        box-sizing: border-box
    }

 </style>
</head>
<body ontouchstart="">
<div >
    <div data-v-25ce5c3e="" class="slideIn">
        <#--<div data-v-25ce5c3e="" id="download_bar" class="download_bar">-->
            <#--<div class="title"><img src="/resources/images/logo.png" alt="美心说" class="logo">-->
                <#--<p><span class="name">美心说</span> <span class="desc" style="line-height: 38px;">分享赚红包</span></p> <span-->
                        <#--href="javascript:;" id="download_btn" class="download_btn" style="top: 1px;"><img style="width:24px;height:24px" src="/resources/images/finger.png">马上分享</span></div>-->
            <#--<span class="close_btn" style="top: 10px;"><span-->
                    <#--class="close_icon iconfont icon-arrow-dropright"></span>-->
            <#--</span>-->
        <#--</div>-->
        <div data-v-25ce5c3e="" class="article">
            <div data-v-25ce5c3e="" class="main">
                <div data-v-25ce5c3e="" class="article_meta"><h1 class="meta_title">${data.title}</h1>
                    <div class="meta clearfix"><span>${data.createDate?string('yyyy-MM-dd hh:mm:ss')}</span> <span class="nickname"><a
                            class="toappuser">${data.author!}</a></span> <span></span></div>
                </div>
            </div>
        </div>
        <div style="padding:0px 15px">
            ${data.content}
        </div>
    </div> <!----></div>
</body>
</html>