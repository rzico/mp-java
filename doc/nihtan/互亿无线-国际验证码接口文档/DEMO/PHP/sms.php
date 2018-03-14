<?php

//接口类型：互亿无线国际短信接口。
//账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
//注意事项：
//（1）调试期间，请仔细阅读接口文档；
//（2）请使用APIID（查看APIID请登录用户中心->国际短信->产品总览->APIID）及APIkey来调用接口；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

header("Content-type:text/html; charset=UTF-8");

function Post($curlPost,$url){
		$curl = curl_init();
		curl_setopt($curl, CURLOPT_URL, $url);
		curl_setopt($curl, CURLOPT_HEADER, false);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($curl, CURLOPT_NOBODY, true);
		curl_setopt($curl, CURLOPT_POST, true);
		curl_setopt($curl, CURLOPT_POSTFIELDS, $curlPost);
		$return_str = curl_exec($curl);
		curl_close($curl);
		return $return_str;
}
function xml_to_array($xml){
	$reg = "/<(\w+)[^>]*>([\\x00-\\xFF]*)<\\/\\1>/";
	if(preg_match_all($reg, $xml, $matches)){
		$count = count($matches[0]);
		for($i = 0; $i < $count; $i++){
		$subxml= $matches[2][$i];
		$key = $matches[1][$i];
			if(preg_match( $reg, $subxml )){
				$arr[$key] = xml_to_array( $subxml );
			}else{
				$arr[$key] = $subxml;
			}
		}
	}
	return $arr;
}

$target = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";
$mobile = '136xxxxxxxx';//手机号码
$post_data = "account=用户名&password=密码&mobile=".$mobile."&content=Your verification code is 1125";
//用户名是登录用户中心->国际短信->产品总览->APIID
//查看密码请登录用户中心->国际短信->产品总览->APIKEY
$gets =  xml_to_array(Post($post_data, $target));
if($gets['SubmitResult']['code']==2){
	echo '提交成功';
}
?>