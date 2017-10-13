// 娣诲姞Cookie
function addCookie(name, value, options) {
    if (arguments.length > 1 && name != null) {
        if (options == null) {
            options = {};
        }
        if (value == null) {
            options.expires = -1;
        }
        if (typeof options.expires == "number") {
            var time = options.expires;
            var expires = options.expires = new Date();
            expires.setTime(expires.getTime() + time * 1000);
        }
        document.cookie = encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires ? "; expires=" + options.expires.toUTCString() : "") + (options.path ? "; path=" + options.path : "") + (options.domain ? "; domain=" + options.domain : ""), (options.secure ? "; secure" : "");
    }
}

// 鑾峰彇Cookie
function getCookie(name) {
    if (name != null) {
        var value = new RegExp("(?:^|; )" + encodeURIComponent(String(name)) + "=([^;]*)").exec(document.cookie);
        return value ? decodeURIComponent(value[1]) : null;
    }
}

// 绉婚櫎Cookie
function removeCookie(name, options) {
    addCookie(name, null, options);
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

function intInit(obj){
    //鍏堟妸闈炴暟瀛楃殑閮芥浛鎹㈡帀锛岄櫎浜嗘暟瀛楀拰.
    obj.value = obj.value.replace(/[^\d]/g, "");
    //蹇呴』淇濊瘉绗竴涓负鏁板瓧鑰屼笉鏄�.
    obj.value = obj.value.replace(/^\./g, "");
    //淇濊瘉 鏁板瓧鏁存暟浣嶄笉澶т簬8浣�
    if(100000000<=parseFloat(obj.value))
        obj.value = "";
}
function floatInit(obj){
    //鍏堟妸闈炴暟瀛楃殑閮芥浛鎹㈡帀锛岄櫎浜嗘暟瀛楀拰.
    obj.value = obj.value.replace(/[^\d.]/g,"");
    //蹇呴』淇濊瘉绗竴涓负鏁板瓧鑰屼笉鏄�.
    obj.value = obj.value.replace(/^\./g,"");
    //淇濊瘉鍙湁鍑虹幇涓�涓�.鑰屾病鏈夊涓�.
    obj.value = obj.value.replace(/\.{2,}/g,".");
    //淇濊瘉.鍙嚭鐜颁竴娆★紝鑰屼笉鑳藉嚭鐜颁袱娆′互涓�
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    //淇濊瘉 鏁板瓧鏁存暟浣嶄笉澶т簬8浣�
    if(100000000<=parseFloat(obj.value))
        obj.value = "";
}
function percentInit(obj){
    //鍏堟妸闈炴暟瀛楃殑閮芥浛鎹㈡帀锛岄櫎浜嗘暟瀛楀拰.
    obj.value = obj.value.replace(/[^\d.]/g,"");
    //蹇呴』淇濊瘉绗竴涓负鏁板瓧鑰屼笉鏄�.
    obj.value = obj.value.replace(/^\./g,"");
    //淇濊瘉鍙湁鍑虹幇涓�涓�.鑰屾病鏈夊涓�.
    obj.value = obj.value.replace(/\.{2,}/g,".");
    //淇濊瘉.鍙嚭鐜颁竴娆★紝鑰屼笉鑳藉嚭鐜颁袱娆′互涓�
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    if(100<parseFloat(obj.value))
        obj.value = "";
    if(parseFloat(obj.value)<0)
        obj.value = "";
}
