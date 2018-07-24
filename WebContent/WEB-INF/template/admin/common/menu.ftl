<aside class="Hui-aside">
[@adminDirective]
    <div class="menu_dropdown bk_2">

        <dl id="menu-member">
                <dt><i class="Hui-iconfont">&#xe60d;</i> 内容管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../navigation/index.jhtml" data-title="导航管理" href="javascript:;">导航管理</a></li>
                        <li><a data-href="../articleCatalog/index.jhtml" data-title="文集分类" href="javascript:;">文集分类</a></li>
                        <li><a data-href="../article/index.jhtml" data-title="文章管理" href="javascript:;">文章管理</a></li>
                         [#if (admin.isManager)]
                        <li><a data-href="../gauge/index.jhtml" data-title="量表管理" href="javascript:;">量表管理</a></li>
                         [/#if]
                        <li><a data-href="../counselor/index.jhtml" data-title="专家团队" href="javascript:;">专家团队</a></li>
                        <li><a data-href="../course/index.jhtml" data-title="课程管理" href="javascript:;">课程管理</a></li>
                        <li><a data-href="../music/index.jhtml" data-title="音乐资料" href="javascript:;">音乐资料</a></li>
                    </ul>
                </dd>
                <dt><i class="Hui-iconfont">&#xe60d;</i> 商品管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../productCategory/index.jhtml" data-title="商品分类"
                               href="javascript:;">商品分类 </a></li>
                        <li><a data-href="../product/index.jhtml" data-title="商品管理" href="javascript:;">商品管理</a></li>
                    </ul>
                </dd>
                <dt><i class="Hui-iconfont">&#xe60d;</i> 订单管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../order/index.jhtml" data-title="销售订单" href="javascript:;">销售订单</a></li>
                        <li><a data-href="../evaluation/index.jhtml" data-title="心理测评" href="javascript:;">心理测评</a></li>
                        <li><a data-href="../courseOrder/index.jhtml" data-title="报名课程" href="javascript:;">报名课程</a></li>
                        <li><a data-href="../counselorOrder/index.jhtml" data-title="咨询预约" href="javascript:;">咨询预约</a></li>
                    </ul>
                </dd>
            [#if (admin.isManager)]
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
            [#if (admin.isManager)]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 直播管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
                <dd>
                    <ul>
                        <li><a data-href="../liveGift/index.jhtml" data-title="礼物管理" href="javascript:;">礼物管理</a></li>
                        <li><a data-href="../live/index.jhtml" data-title="房间管理" href="javascript:;">房间管理</a></li>
                        <li><a data-href="../liveGiftExchange/index.jhtml" data-title="金币兑换" href="javascript:;">金币兑换</a></li>
                    </ul>
                </dd>
            [/#if]
                <dt><i class="Hui-iconfont">&#xe60d;</i> 客户管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    [#if (admin.isManager)]
                       <li><a data-href="../member/index.jhtml" data-title="用户管理" href="javascript:;">用户管理</a></li>
                    [/#if]
                    [#if (admin.isManager)]
                        <li><a data-href="../enterprise/index.jhtml" data-title="合作伙伴" href="javascript:;">合作伙伴</a></li>
                    [/#if]
                    [#if (admin.isManager)]
                        <li><a data-href="../topic/index.jhtml" data-title="专栏管理" href="javascript:;">专栏管理</a></li>
                    [/#if]
                    <li><a data-href="../card/index.jhtml" data-title="会员管理" href="javascript:;">会员管理</a></li>
                    [#--<li><a data-href="../organization/index.jhtml" data-title="合作单位" href="javascript:;">合作单位</a></li>--]
                </ul>
            </dd>
               <dt><i class="Hui-iconfont">&#xe60d;</i> 系统管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
                </dt>
               <dd>
                   <ul>
                        <li><a data-href="../admin/index.jhtml" data-title="管理员" href="javascript:;">管理员</a></li>
             [#if (admin.isManager && admin.role?contains("1"))]
                        <li><a data-href="../role/index.jhtml" data-title="角色管理" href="javascript:;">角色管理</a></li>
                        <li><a data-href="../tag/index.jhtml" data-title="标签管理" href="javascript:;">标签管理</a></li>
                        <li><a data-href="../articleCategory/index.jhtml" data-title="文章分类" href="javascript:;">文章分类</a></li>
                        <li><a data-href="../gaugeCategory/index.jhtml" data-title="量表分类" href="javascript:;">量表分类</a></li>
                        <li><a data-href="../template/index.jhtml" data-title="模板管理" href="javascript:;">模板管理</a></li>
                        <li><a data-href="../category/index.jhtml" data-title="行业分类" href="javascript:;">行业分类</a></li>
                        <li><a data-href="../occupation/index.jhtml" data-title="职业分类" href="javascript:;">职业分类</a></li>
                        <li><a data-href="../area/index.jhtml" data-title="行政区域" href="javascript:;">行政区域</a></li>
                        <li><a data-href="../smssend/index.jhtml" data-title="短信管理" href="javascript:;">短信管理</a></li>
                        <li><a data-href="../log/index.jhtml" data-title="日志管理" href="javascript:;">日志管理</a></li>
                        <li><a data-href="../message/index.jhtml" data-title="消息管理" href="javascript:;">消息管理</a></li>
              [/#if]
                   </ul>
               </dd>
        </dl>

    </div>
[/@adminDirective]
</aside>