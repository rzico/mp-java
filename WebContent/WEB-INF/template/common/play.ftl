<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>opacity</title>
    <style>
        *{
            padding: 0;
            margin: 0;
        }
        body{
            padding: 50px;
        }
        .demo{
            padding: 25px;
            background:none;
        }
        .demo p{
            color: #FFFFFF;
        }
    </style>
</head>
<body onload="javascript: document.forms[0].submit();">

<div class="demo">
    <p>重定向...</p>
    <form action="${requestUrl}"[#if requestMethod??] method="${requestMethod}"[/#if][#if requestCharset?has_content] accept-charset="${requestCharset}"[/#if]>
      [#list parameterMap.entrySet() as entry]
        <input type="hidden" name="${entry.key}" value="${entry.value}" />
      [/#list]
    </form>
</div>

</html>