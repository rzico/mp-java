[#list articles as article]
    [#if article.mediaType=='image']
       <div>${article.content}</div>
       <image src="${article.original}"></image>
    [/#if]
[/#list]