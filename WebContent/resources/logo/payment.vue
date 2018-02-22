<template>
    <div class="wrapper bg" v-if="isShow" @onclick="close('error')">
        <div class="box">
            <div class="nav">
                <div class="flex1 flex-center" @click="close('error')">
                    <text class="close" :style="{fontFamily:'iconfont'}" >&#xe60a;</text>
                </div>
                <div class="flex4 flex-center">
                    <text class="caption" >确认付款</text>
                </div>
                <div class="flex1 flex-center">
                    <text class="help"  :style="{fontFamily:'iconfont'}" >&#xe613;</text>
                </div>
            </div>
            <div>
                <text class="currency">¥{{info.amount | currencyfmt}}</text>
            </div>
            <text class="button btn" value="确定付款" @click="comfrm()">确定付款</text>
            <div class="cell">
                <div class="flex-row flex-start">
                    <text class="ico" :style="{fontFamily:'iconfont'}">&#xe6b1;</text>
                    <text class="title ml10">付款详情</text>
                </div>
                <div class="flex-row flex-end">
                    <text class="sub_title" style="margin-right: 50px;">{{info.memo}}</text>
                </div>
            </div>
            <div class="cell" style="border-bottom-width: 0px;">
                <dropdown :title="title" :id="id" :items="items" @onchange="onchange" ></dropdown>
            </div>
        </div>
        <div class="box" v-if="isPwd">
            <div class="nav">
                <div class="flex1 flex-center">
                    <text class="close" :style="{fontFamily:'iconfont'}"  @click="close('error')">&#xe60a;</text>
                </div>
                <div class="flex4 flex-center">
                    <text class="caption" >支付密码</text>
                </div>
                <div class="flex1 flex-center">
                    <text class="help" :style="{fontFamily:'iconfont'}" >&#xe613;</text>
                </div>
            </div>
            <text class="hint sub_title">请输入支付密码</text>
            <!--6个验证码框-->
            <div class="inputTextBox" @click="getFocus()">
                <!--隐藏的输入框-->
                <input type="tel" ref="captchRef" v-model="captchaValue" maxlength="6" @input="captchaInput" autofocus="true" class="input" />
                <div  v-for="item in textList" class="inputDiv" >
                    <text class="inputText">{{item}}</text>
                </div>
            </div>
            <text class="updatePassword" @click="updatePassword()">忘记密码？点击重置密码。</text>
        </div>

    </div>
</template>
<style lang="less" src="../style/wx.less"/>
<style scoped>

    .bg {
        background-color:rgba(0,0,0,0.5);
    }

    .box {
        width:750px;
        height: 800px;
        position: fixed;
        flex-direction: column;
        align-items: center;
        bottom:0px;
        background-color: #fff;
    }

    .nav {
        flex-direction: row;
        width:750px;
        height:80px;
        background-color: #eee;
        border-bottom-width: 1px;
        border-bottom-color: #ccc;
        border-bottom-style: solid;
    }

    .cell {
        margin-top: 20px;
        margin-left: 20px;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        width: 730px;
        min-height:98px;
        border-bottom-width: 1px;
        border-bottom-color: #ccc;
        border-bottom-style: solid;
    }
    .caption {
        font-size: 38px;
        line-height: 60px;
        color:#999;
    }
    .close {
        font-size: 42px;
        line-height: 60px;
        color:#999;
    }

    .currency {
        font-size: 58px;
        line-height: 80px;
        margin-top: 20px;
        margin-bottom: 20px;
    }

    .help {
        font-size: 42px;
        line-height: 60px;
        color:#999;
    }

    .hint {
        margin-top: 50px;
    }
    .inputTextBox{
        margin-top: 20px;
        flex-direction: row;
        align-items: center;
        justify-content: center;
        width:530px;
        position: relative;
    }

    .inputText{
        border-width: 1px;
        border-style: solid;
        border-color: #999;
        width: 80px;
        height:80px;
        font-size: 30px;
        text-align: center;
        line-height: 80px;
    }
    .inputDiv{
        flex:1;
        align-items: center;
    }
    .input{
        opacity:0;
        width:530px;
        position: absolute;
        height: 80px;
        font-size: 85px;
    }

    .btn {
        margin-left:50px;
        margin-right:50px;
        position: absolute;
        width:650px;
        bottom:50px;
    }

    .updatePassword {
        margin-top: 60px;
        color:blue;
        line-height: 50px;
        height: 50px;
    }

</style>
<script>
    import { POST, GET } from '../assets/fetch';
    import utils from '../assets/utils';
    import dropdown from './dropdown.vue'
    const event = weex.requireModule('event');
    var globalEvent = weex.requireModule('globalEvent');
    var timer = null;
    export default {
        components: {
            dropdown
        },
        filters:{
            currencyfmt:function (value) {
                // 返回处理后的值
                if (value != null) {
                    if(value == 0){
                        return value;
                    }else{
                        var price = (Math.round(value * Math.pow(10,2)) / Math.pow(10,2)).toFixed(2);
                        return price;
                    }
                }
            }
        },
        data:function(){
            return{
                info:{memo:""},
                sn:"",
                isShow:false,
                isPwd:false,
                captchaValue:'',
                textList:['','','','','',''],
                title: "付款方式",
                items:[{id:0,name:"微信支付",ico:'&#xe659;',color:'green'},{id:2,name:"钱包余额",ico:'&#xe6ce;',color:'#ff5545'}],
                id: 0,
                lastCaptchaLength:0
            }
        },
        created() {
            utils.initIconFont();
        },
        methods:{
            onchange:function (id) {
                this.id = id;
            },
//            当用户输入数字时触发
            captchaInput:function (e) {
                var _this = this;
                _this.captchaValue = e.value;
//                判断删除还是输入  '大于' --> 删除
                if (_this.lastCaptchaLength > _this.captchaValue.length) {
                    _this.textList[_this.lastCaptchaLength-1] = '';
                    _this.lastCaptchaLength = _this.captchaValue.length;
                } else {
                    let a = _this.captchaValue;
                    let b = a.substr(a.length - 1, 1)
                    _this.textList[_this.captchaValue.length-1] = b;
                    _this.lastCaptchaLength = _this.captchaValue.length;
//                当用户输完验证码后进行系统验证
                    if (_this.lastCaptchaLength == 6) {
                        _this.balance(_this.captchaValue);
                    }
                 }
            },
//            点击验证框时使隐藏的input获取焦点；
            getFocus:function () {
                this.$refs['captchRef'].focus();
            },
            clearPwd() {
                this.textList = ['','','','','',''];
                this.captchaValue = '';
                this.lastCaptchaLength = 0;
            },
            show (sn) {
                var _this = this;
                _this.sn = sn;
                _this.isPwd = false;
                _this.clearPwd();
              GET("payment/view.jhtml?sn="+sn,function (res) {
                  _this.info = res.data;
                  _this.isShow = true;
              },function (err) {
                  event.toast(err.content);
              })
            },
            close (e) {
                var _this = this;
                if (timer!=null) {
                    clearInterval(timer);
                    timer = null;
                }
                globalEvent.removeEventListener("onResume");
                _this.$emit("notify",e);
                _this.isShow = false;

            },
            comfrm () {
                if (this.id == 2) {
                    this.isPwd = true;
                    return;
                }
                if (this.id == 1) {
                    this.payment("alipayH5Plugin");
                }
                if (this.id == 0) {
                    this.payment("weixinAppPlugin");
                }
            },
            balance(pwd) {
                var _this = this;
                event.encrypt(pwd,function (message) {
                       if (message.type=="success") {
                           POST("payment/submit.jhtml?sn="+_this.sn+"&paymentPluginId=balancePayPlugin&enPassword="+message.data).then(
                               function (data) {
                                   if (data.type=="success") {
                                     _this.clearPwd();
                                     if (timer!=null) {
                                         clearInterval(timer);
                                         timer=null;
                                     }
                                     timer = setInterval(function () {
                                         POST("payment/query.jhtml?sn="+_this.sn).then(
                                             function (data) {
                                                 if (data.type=="success") {
                                                     if (data.data=="0000") {
                                                         _this.close(utils.message("success","success"));
                                                     } else {
                                                         if (data.data=="0001") {
                                                             _this.close(utils.message("error","error"));
                                                         }
                                                     }
                                                 }

                                             },
                                             function (err) {

                                             }
                                         )
                                     },1000);
                                 } else {
                                     _this.clearPwd();
                                     event.toast(data.content);
                                 }
                               },
                               function (err) {
                                   _this.clearPwd();
                                  event.toast("网络不稳定");
                               }
                           )
                       } else {
                           _this.clearPwd();
                           event.toast(message.content);
                       }
                    }
                )
            },
            payment (plugId) {
                var _this = this;
                POST("payment/submit.jhtml?sn="+this.sn+"&paymentPluginId="+plugId).then(
                    function (data) {
                        if (data.type=="success") {
                            event.wxPay(data.data.mweb_url,function (e) {
                                globalEvent.addEventListener("onResume", function (e) {
                                    if (timer!=null) {
                                        clearInterval(timer);
                                        timer=null;
                                    }
                                    timer = setInterval(function () {
                                        POST("payment/query.jhtml?sn="+_this.sn).then(
                                            function (data) {
                                                if (data.type=="success") {
                                                    if (data.data=="0000") {
                                                        _this.close(utils.message("success","success"));
                                                    } else {
                                                        if (data.data=="0001") {
                                                            _this.close(utils.message("error","error"));
                                                        }
                                                    }
                                                }

                                            },
                                            function (err) {

                                            }
                                        )
                                    },1000);
                                });
                            })
                        } else {
                            event.toast(data.content);
                            _this.close("error");
                        }
                    },
                    function (err) {
                        event.toast("网络不稳定");
                    }
                )
            },
            updatePassword:function () {
                var _this = this;
                GET("weex/member/attribute.jhtml",
                    function (data) {
                        if (data.type=="success") {
                            if (data.data.bindMobile==true) {
                                event.openURL(utils.locate("view/member/password/captcha.js"),
                                    function (updated) {
                                    }
                                )
                            } else {
                                event.openURL(utils.locate("view/member/password/index.js"),
                                    function (updated) {
                                    }
                                )
                            }
                        } else {
                            event.toast(data.content);
                        }
                    },
                    function (err) {
                        event.toast("网络不稳定")
                    }
                )
            }

        }
    }
</script>
