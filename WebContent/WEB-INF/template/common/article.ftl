<#list articles as article>
    <#if article.mediaType=='product'>
    <div>${article.content}</div>
        <#if article.original!="">
            <#if article.url!="">
            <div style="position: relative;width: 90%;height: 60px;line-height: 60px;background: #E9FBE4;box-shadow: 1px 2px 3px #E9FBE4;border: 1px solid #C9E9C0;border-radius: 4px;text-align: center;color: #0C7823;display: block;position: absolute;font-size:12px;margin-left:5%;">
                <span>嗨!点击我就可以去购买了呢！</span>
                <div style="position:absolute;left:30px;overflow:hidden;width:0;height:0;border-width:10px;border-style:solid dashed dashed dashed;bottom:-20px;border-color:#C9E9C0 transparent transparent transparent;display: block;"></div>
                <div style="bottom: -19px;border-color: #E9FBE4 transparent transparent transparent;    position: absolute;left: 30px;overflow: hidden;width: 0;height: 0;border-width: 10px;border-style: solid dashed dashed dashed;display: block;"></div>
            </div>
            <div>
                <a href="${article.url}" style="top:10px">
                    <image src="${article.original}"></image>
                </a>
            </div>
            <#else>
            <div>
                <image src="${article.original}"></image>
            </div>
            </#if>
        </#if>
    </#if>
    <#if article.mediaType=='image'>
    <div>${article.content}</div>
        <#if article.original!="">
        <div>
            <image src="${article.original}"></image>
        </div>
        </#if>
    </#if>
    <#if article.mediaType=='video'>
    <div>${article.content}</div>
        <#if article.original!="">
        <div>
            <video src="${article.original}"
                   width="100%"
                   height="240px"
                   style="object-fit:fill"
                   webkit-playsinline="true"
                   x-webkit-airplay="true"
                   playsinline="true"
                   x5-video-player-type="h5"
                   x5-video-orientation="h5"
                   x5-video-player-fullscreen="true"
                   preload="auto" controls="controls">
                加载失败！
            </video>
        </div>
        <div>
            <iframe frameborder="0" allowfullscreen="" class="video_iframe" data-vidtype="-1"
                    data-ratio="1.7666666666666666" data-w="848" data-src="${article.original}"></iframe>
        </div>
        </#if>
    </#if>
    <#if article.mediaType=='html'>
    <div>${article.content}</div>
    </#if>
    <#if article.mediaType=='audio'>
    <div>${article.content}</div>
        <#if article.original!="">
        <div>
            <audio id="bgmusic" src="${article.original}" controls="controls" autoplay preload loop controls>Your
                browser does not support the audio element.
            </audio>
        </div>
        </#if>
    </#if>
</#list>