
<aside class="Hui-aside">
[@adminDirective]
    <div class="menu_dropdown bk_2">

        <dl id="menu-member">

            [#if admin.type=="operate"||admin.type=="shop"||admin.type=="agent"]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 文章管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../article/index.jhtml" data-title="文章管理" href="javascript:;">文章管理</a></li>
                    </ul>
                </dd>
            [/#if]

            [#if admin.type=="operate"||admin.type=="shop"||admin.type=="agent"]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 红包管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../redPackage/index.jhtml" data-title="红包管理" href="javascript:;">红包管理</a></li>
                    </ul>
                </dd>
            [/#if]
            [#if (admin.type=="operate"||admin.type=="shop"||admin.type=="agent")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 商品管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../productCategory/index.jhtml" data-title="商品分类"
                               href="javascript:;">商品分类 </a></li>
                        <li><a data-href="../product/index.jhtml" data-title="商品管理" href="javascript:;">商品管理</a></li>
                    </ul>
                </dd>
            [/#if]
            [#if (admin.type=="operate"||admin.type=="shop")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 订单管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../order/index.jhtml" data-title="订单管理" href="javascript:;">订单管理</a></li>
                    </ul>
                </dd>
            [/#if]
            [#if (admin.type=="operate"||admin.type=="shop")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 账单管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../payment/index.jhtml" data-title="收款管理" href="javascript:;">收款管理</a></li>
                        <li><a data-href="../refunds/index.jhtml" data-title="退款管理" href="javascript:;">退款管理</a></li>
                        <li><a data-href="../recharge/index.jhtml" data-title="充值管理" href="javascript:;">充值管理</a></li>
                        <li><a data-href="../transfer/index.jhtml" data-title="提现管理" href="javascript:;">提现管理</a></li>
                    </ul>
                </dd>
            [/#if]
            [#if (admin.type=="operate"||admin.type=="shop")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 直播管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../live/index.jhtml" data-title="直播管理" href="javascript:;">直播管理</a></li>
                        <li><a data-href="../liveGift/index.jhtml" data-title="礼物管理" href="javascript:;">礼物管理</a></li>
                        <li><a data-href="../liveGiftExchange/index.jhtml" data-title="印票兑换" href="javascript:;">印票兑换</a></li>
                    </ul>
                </dd>
            [/#if]
            [#if (admin.type=="operate"||admin.type=="shop"||admin.type=="agent")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 客户管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
            [/#if]
            <dd>
                <ul>
                    [#if (admin.type=="operate"||admin.type=="agent")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                        <li><a data-href="../member/index.jhtml" data-title="用户管理" href="javascript:;">用户管理</a></li>
                    [/#if]
                    [#if (admin.type=="operate"||admin.type=="agent")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                        <li><a data-href="../enterprise/index.jhtml" data-title="合作伙伴" href="javascript:;">合作伙伴</a></li>
                    [/#if]
                    [#if (admin.type=="operate"||admin.type=="shop"||admin.type=="agent")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                        <li><a data-href="../topic/index.jhtml" data-title="专栏管理" href="javascript:;">专栏管理</a></li>
                    [/#if]
                    [#if (admin.type=="operate"||admin.type=="shop"||admin.type=="agent")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                        <li><a data-href="../smallRange/index.jhtml" data-title="小程序管理" href="javascript:;">小程序管理</a></li>
                    [/#if]
                [#--[#if (admin.type=="operate")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]--]
                [#--<li><a data-href="../topicCard/index.jhtml" data-title="卡包管理" href="javascript:;">卡包管理</a></li>--]
                [#--[/#if]--]
                    [#if (admin.type=="operate"||admin.type=="shop")&&(admin.role?contains("1")||admin.role?contains("2")||admin.role?contains("3"))]
                        <li><a data-href="../card/index.jhtml" data-title="会员卡管理" href="javascript:;">会员管理</a></li>
                    [/#if]
                </ul>
            </dd>
            [#if (admin.type=="operate"||admin.type=="agent"||admin.type=="shop")&&(admin.role?contains("1")||admin.role?contains("2"))]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 系统管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
            [/#if]
            <dd>
                <ul>
                    [#if (admin.type=="operate")&&(admin.role?contains("1"))]
                        <li><a data-href="../role/index.jhtml" data-title="角色管理" href="javascript:;">角色管理</a></li>
                    [/#if]
                    [#if (admin.type=="operate")&&(admin.role?contains("1")||admin.role?contains("2"))]
                        <li><a data-href="../tag/index.jhtml" data-title="标签管理" href="javascript:;">标签管理</a></li>
                        <li><a data-href="../articleCategory/index.jhtml" data-title="文章分类" href="javascript:;">文章分类</a>
                        </li>
                        <li><a data-href="../template/index.jhtml" data-title="模板管理" href="javascript:;">模板管理</a></li>
                    [/#if]
                    [#if (admin.type=="operate"||admin.type=="agent")&&(admin.role?contains("1")||admin.role?contains("2"))]
                        <li><a data-href="../category/index.jhtml" data-title="行业分类" href="javascript:;">行业分类</a></li>
                    [/#if]
                    [#if (admin.type=="operate")&&(admin.role?contains("1")||admin.role?contains("2"))]
                        <li><a data-href="../occupation/index.jhtml" data-title="职业分类" href="javascript:;">职业分类</a></li>
                    [/#if]
                    [#if (admin.type=="operate")&&(admin.role?contains("1"))]
                        <li><a data-href="../area/index.jhtml" data-title="行政区域" href="javascript:;">行政区域</a></li>
                        <li><a data-href="../smssend/index.jhtml" data-title="短信管理" href="javascript:;">短信管理</a></li>
                    [/#if]
                    [#if (admin.type=="operate"||admin.type=="agent"||admin.type=="shop")&&(admin.role?contains("1")||admin.role?contains("2"))]
                        <li><a data-href="../log/index.jhtml" data-title="日志管理" href="javascript:;">日志管理</a></li>
                        <li><a data-href="../message/index.jhtml" data-title="消息管理" href="javascript:;">消息管理</a></li>
                    [/#if]
                </ul>
            </dd>
        </dl>

    </div>
[/@adminDirective]
</aside>