<template>
  <div class="trademain w-full">
    <!-- <my-header class="header-main">
      <login-bar></login-bar>
    </my-header> -->
    <section class="page-global-exchange page">
      <!-- 币币主模块 -->
      <div class="content-top">
        <div class="cen-left">
          <div class="mod coin-switch">
            <div class="tabs coin-switch-tabs"
              style="padding: 8px 8px 8px 10px;display: block;height: auto;border-radius: 2px 2px 0 0;border: 0;">
              <div class="quotes">
                <span class="mock-a fav" @click="getCustomList()" :class="currentCoinId == 'opt' ? 'active' : ''">
                  <!---->
                  <em>{{ $t('home.huobi36') }}</em>
                </span>
                <span class="mock-a upper active" v-for="(item, index) in marketList" :key="index" v-show="index < 1"
                  @click="clickTab(item)">{{ item.symbol.split('/')[1] }}</span>
              </div>
              <!-- 币种搜索 -->
              <div class="actions-serch flex">
                <div class="input-box">
                  <input :placeholder="$t('home.huobi84')" maxlength="9" type="text" v-model="search" />
                  <i class="el-icon-search i_sos"></i>
                  <i class="hb_icon_clear" style="display:none"></i>
                </div>
                <button class="btnexch upper" @click="showCny()">
                  <i class="iconfont icon-yqfqiehuan"></i>{{ rateObj.rateName }}
                </button>
              </div>
            </div>

            <!-- 左侧币种列表 -->
            <div class="coin-switch-content coin_list">
              <template>
                <el-table :data="marketList" :default-sort="{ prop: 'date', order: 'descending' }" height="830"
                  header-align='right' @row-click='onTableRowClick' :empty-text="$t('bico.W160')">
                  <el-table-column prop="coin" :label="$t('home.huobi37')" sortable width="90">
                    <template slot-scope="scope">
                      <div class="left_coinname flex">
                        <img :src="scope.row.img"
                          style="width: 18px;height: 18px;margin-right: 10px;border-radius: 50px;background-color: rgb(0, 0, 0);position: relative;top: 7px;" />
                        {{ scope.row.symbol.split('/')[0] }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="price" :label="$t('home.huobi38')" sortable width="90" align="right">
                    <template slot-scope="scope">
                      <!-- <p class="currency" v-if="scope.row.symbol == form.region">{{ nowPrice }}</p> -->
                      <p class="currency">{{ scope.row.close }}</p>
                      <!-- <p class="currency" v-if="usdtorcny==1">
                        {{scope.row.symbol == form.region? nowPrice:scope.row.close.toFixed(scope.row.number) }}</p>
                      <p class="currency" v-else>{{ (scope.row.close*rateNumber).toFixed(scope.row.number) }}</p> -->
                    </template>
                  </el-table-column>
                  <el-table-column prop="rose" :label="$t('home.huobi39')" :formatter="formatter" width="100"
                    align="right">
                    <template slot-scope="scope">
                      <span :class="scope.row.rose >= '0' ? 'color_up' : 'color_down'" v-if="scope.row.rose >= 0">+{{
                  scope.row.rose.toFixed(2) }}%</span>
                      <span :class="scope.row.rose < '0' ? 'color_down' : 'color_up'" v-if="scope.row.rose < 0">{{
                  scope.row.rose.toFixed(2) }}%</span>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </div>
            <!-- 左侧币种列表 -->
          </div>
        </div>
        <div class="cen-right">
          <div class="cont-right">
            <div class="centerbox">
              <!-- K线部分 -->
              <div class="top_kline trading-chart">
                <div class="klinetop">
                  <div class="ticker coin_title">
                    <div class="coin_infobox flex_space">
                      <div class="coinone flex">
                        <h2 class="symbol-name">{{ form.region.split('/')[0] }}</h2>
                        <div style="margin-left: 4px;"></div>
                        <span  class="symbol-type"
                          style="display: inline-block;vertical-align: unset;height: 17px;margin-right: 0px;border-radius: 4px;top: 0px;font-size: 14px;">{{
                  $t('home.huobi41') }}
                          <div  class="tips"></div>
                        </span>
                        <div style="margin-left: 4px;"></div>
                        <span  class="symbol-type"
                          style="display: inline-block;vertical-align: unset;height: 17px;margin-right: 0px;border-radius: 4px;top: 0px;font-size: 14px;">{{
                  $t('home.huobi42') }}
                          <div class="tips"></div>
                        </span>
                        <div style="margin-left: 6px;"></div>
                        <span>{{ $t('home.huobi43') }}</span>
                        <div style="margin-left: 10px;text-align: left;padding: 5px 0;">
                          <p class="us_price" :class="rose >= '0' ? 'color_up' : 'color_down'">{{ nowPrice }}</p>
                          <p class="cny_price">≈ {{ nowPrice  || '0.00' }} <!-- CNY --></p>
                        </div>

                        <div class="top_mag">
                          <p class="tl_p">{{ $t('home.huobi44') }}</p>
                          <p :class="rose >= '0' ? 'color_up' : 'color_down'" v-if="rose >= 0">+{{ rose || '0.00' }}%
                          </p>
                          <p :class="rose < '0' ? 'color_down' : 'color_up'" v-if="rose < 0">{{ rose || '0.00' }}%</p>
                          <p v-show="!rose">0.00</p>
                        </div>
                        <div class="top_mag">
                          <p class="tl_p">{{ $t('home.huobi45') }}</p>
                          <p class="bt_p">{{ highForth || '-' }}</p>
                        </div>
                        <div class="top_mag">
                          <p class="tl_p">{{ $t('home.huobi46') }}</p>
                          <p class="bt_p">{{ lossForth || '-' }}</p>
                        </div>
                        <div class="top_mag">
                          <p class="tl_p">{{ $t('home.huobi47') }}</p>
                          <p class="bt_p">{{ amountPirce || '-' }} <!-- {{form.region||'-'}} --> </p>
                        </div>

                      </div>
                      <div class="set_cor">
                        <i class="iconfont icon-iconset0108"></i>
                      </div>
                    </div>

                  </div>
                </div>
                <!-- <div class="container-k" style="margin-top: -19px;"> -->
                <div style="margin-top: -19px;height:563px">
                  <!-- K线 -->
                  <TradeView ref='tradeView' :nowPrice="nowPrice" :symbolValue="form.region" :type="0"
                    :defaultfloatPrecision="interception" />
                </div>

              </div>
              <!-- input -->
              <div class="center_input">
                <div class="buysell_trad">
                  <template>
                    <el-tabs v-model="activeIndex" @tab-click="">
                      <el-tab-pane :label="$t('home.huobi48')" name="0">
                        <div class="mod-body">
                          <div class="single-panel limit bor-right">
                            <el-form :label-position="labelPosition" label-width="80px" :model="formAlign">
                              <el-form-item :label="$t('home.huobi49')">
                                <div class="flex">
                                  <el-input v-model="formAlign.price" size="small" maxlength="14"></el-input>
                                  <span class="r_name">{{ form.region.split('/')[1] }}</span>
                                </div>
                              </el-form-item>
                              <el-form-item :label="$t('home.huobi50')">
                                <div class="flex">
                                  <el-input v-model="formAlign.number" size="small"></el-input>
                                  <span class="r_name">{{ getCoin }}</span>
                                </div>
                              </el-form-item>
                            </el-form>
                            <!-- <div class="t_plan">
                              <div class="block">
                                <el-slider v-model="value2" :step="20" show-stops>
                                </el-slider>
                              </div>
                            </div> -->

                            <div class="ex_tips">
                              <!-- 下单百分比 -->
                              <div class="blockPecent">
                                <el-slider v-model="activeName" @change="handleClick" :step="20" show-stops>
                                </el-slider>
                                <div class="bondClass" style="color: #ffffff;">
                                  <p><span>{{ $t('transaction.userBlance') }}：{{ balanceNum }}USDT</span>
                                    <span style="text-align: right;margin-left: 49px">{{ $t('home.huobi51') }}：{{
                  nowPrice * formMark.number || '0.0000' }}
                                      {{ form.region.split('/')[1] }}</span>
                                  </p>

                                </div>
                              </div>
                              <!-- <p> <span>交易额</span> <em>{{formAlign.price*formAlign.number||'0.0000'}}
                                  {{form.region.split('/')[1]}}</em> </p> -->
                            </div>
                            <div class="ex_btn">
                              <el-button type="" v-if='!token'>
                                <router-link to="login" style="color: #b0b8db">{{ $t('verification.logoName')
                                  }}/</router-link>
                                <router-link to="register">{{ $t('verification.registerName') }}</router-link>
                                <!--<p>登录<em>或</em>注册</p>-->
                              </el-button>
                              <el-button type="" @click="buy_limitBtn()" v-else class="corbuy">
                                <p v-html="buyingLabel"></p>
                              </el-button>
                            </div>
                          </div>
                          <!-- <div class="single-panel limit">

                          </div> -->
                          <div class="single-panel limit">
                            <el-form :label-position="labelPosition" label-width="80px" :model="formAlign">
                              <el-form-item :label="$t('home.huobi52')">
                                <div class="flex">
                                  <el-input v-model="formAlign.price" size="small" maxlength="14"></el-input>
                                  <span class="r_name">{{ form.region.split('/')[1] }}</span>
                                </div>
                              </el-form-item>
                              <el-form-item :label="$t('home.huobi53')">
                                <div class="flex">
                                  <el-input v-model="formAlign.number" size="small"></el-input>
                                  <span class="r_name">{{ getCoin }}</span>
                                </div>
                              </el-form-item>
                            </el-form>
                            <!-- <div class="t_plan">
                              <div class="block">
                                <el-slider v-model="value2" :step="20" show-stops>
                                </el-slider>
                              </div>
                            </div> -->
                            <div class="ex_tips">
                              <!-- 下单百分比 -->
                              <div class="blockPecent">
                                <el-slider v-model="activeName" @change="handleClick" :step="20" show-stops>
                                </el-slider>
                                <div class="bondClass" style="color: #ffffff;">
                                  <p><span>{{ $t('transaction.userBlance') }}：{{ balanceNum }}USDT</span>
                                    <span style="text-align: right;margin-left: 49px">{{ $t('home.huobi51') }}：{{
                  nowPrice * formMark.number || '0.0000' }}
                                      {{ form.region.split('/')[1] }}</span>
                                  </p>

                                </div>
                              </div>
                              <!-- <p> <span>交易额</span> <em>{{formAlign.price*formAlign.number||'0.0000'}}
                                {{form.region.split('/')[1]}}</em> </p>-->
                            </div>
                            <div class="ex_btn">
                              <el-button type="" v-if='!token'>
                                <router-link to="login" style="color: #b0b8db">{{ $t('verification.logoName')
                                  }}/</router-link>
                                <router-link to="register">{{ $t('verification.registerName') }}</router-link>
                                <!--<p>登录<em>或</em>注册</p>-->
                              </el-button>
                              <el-button type="" @click="sellLimitBtn()" v-else class="corsell">
                                <p v-html="sellingLabel"></p>
                              </el-button>
                            </div>
                          </div>
                          <!-- end -->
                        </div>
                      </el-tab-pane>
                      <!-- 市价交易 -->
                      <el-tab-pane :label="$t('home.huobi54')" name="1">
                        <div class="mod-body">
                          <div class="single-panel limit bor-right">
                            <el-form :label-position="labelPosition" label-width="80px" :model="formMark">
                              <el-form-item :label="$t('home.huobi49')">
                                <div class="flex">
                                  <el-input size="small" :placeholder="$t('home.huobi55')" disabled></el-input>
                                  <span class="r_name"></span>
                                </div>
                              </el-form-item>
                              <el-form-item :label="$t('home.huobi50')">
                                <div class="flex">
                                  <el-input v-model="formMark.number" size="small"></el-input>
                                  <span class="r_name">{{ getCoin }}</span>
                                </div>
                              </el-form-item>
                            </el-form>
                            <!-- <div class="t_plan">
                              <div class="block">
                                <el-slider v-model="value2" :step="20" show-stops>
                                </el-slider>
                              </div>
                            </div> -->
                            <div class="ex_tips">
                              <!-- 下单百分比 -->
                              <div class="blockPecent">
                                <el-slider v-model="activeName" @change="handleClick" :step="20" show-stops>
                                </el-slider>
                                <div class="bondClass" style="color: #ffffff;">
                                  <p><span>{{ $t('transaction.userBlance') }}：{{ balanceNum }}USDT</span>
                                    <span style="text-align: right;margin-left: 49px">{{ $t('home.huobi51') }}：{{
                  nowPrice * formMark.number || '0.0000' }}
                                      {{ form.region.split('/')[1] }}</span>
                                  </p>

                                </div>

                              </div>
                              <!-- <p> <span>交易额</span> <em>{{nowPrice*formMark.number||'0.0000'}}
                                  {{form.region.split('/')[1]}}</em> </p> -->
                            </div>
                            <div class="ex_btn">
                              <el-button type="" v-if='!token'>
                                <router-link to="login" style="color: #b0b8db">{{ $t('verification.logoName')
                                  }}/</router-link>
                                <router-link to="register">{{ $t('verification.registerName') }}</router-link>
                                <!--<p>登录<em>或</em>注册</p>-->
                              </el-button>
                              <el-button type="" @click="buy_limitBtn()" v-else class="corbuy">
                                <p v-html="buyingLabel"></p>
                              </el-button>
                            </div>
                          </div>
                          <!-- <div class="single-panel limit">

                          </div> -->
                          <div class="single-panel limit">
                            <el-form :label-position="labelPosition" label-width="80px" :model="formMark">
                              <el-form-item :label="$t('home.huobi52')">
                                <div class="flex">
                                  <el-input v-model="formMark.name" size="small" :placeholder="$t('home.huobi56')"
                                    disabled>
                                  </el-input>
                                  <span class="r_name"></span>
                                </div>
                              </el-form-item>
                              <el-form-item :label="$t('home.huobi53')">
                                <div class="flex">
                                  <el-input v-model="formMark.number" size="small"></el-input>
                                  <span class="r_name">{{ getCoin }}</span>
                                </div>
                              </el-form-item>
                            </el-form>
                            <!-- <div class="t_plan">
                             <div class="block">
                                <el-slider v-model="value2" :step="20" show-stops>
                                </el-slider>
                              </div>
                            </div> -->

                            <div class="ex_tips">
                              <!-- 下单百分比 -->
                              <div class="blockPecent">

                                <el-slider v-model="activeName" @change="handleClick" :step="20" show-stops>
                                </el-slider>
                                <div class="bondClass" style="color: #ffffff;">
                                  <p><span>{{ $t('transaction.userBlance') }}：{{ balanceNum }}USDT</span>
                                    <span style="text-align: right;margin-left: 49px">{{ $t('home.huobi51') }}：{{
                  nowPrice * formMark.number || '0.0000' }}
                                      {{ form.region.split('/')[1] }}</span>
                                  </p>

                                </div>

                                <!--<el-tabs v-model="activeName" @tab-click="handleClick">-->
                                <!--<el-tab-pane v-for="item in pecentArr"  :key="item.id" :label="item.name" :name="item.value"></el-tab-pane>-->
                                <!---->
                                <!--</el-tabs>-->
                              </div>
                              <!-- <p> <span>交易额</span> <em>{{nowPrice*formMark.number||'0.0000'}}
                                  {{form.region.split('/')[1]}}</em> </p> -->
                            </div>

                            <div class="ex_btn">
                              <!-- 市价卖出 Btn -->
                              <el-button type="" v-if='!token'>
                                <router-link to="login" style="color: #b0b8db">{{ $t('verification.logoName')
                                  }}/</router-link>
                                <router-link to="register">{{ $t('verification.registerName') }}</router-link>
                                <!--<p>登录<em>或</em>注册</p>-->
                              </el-button>
                              <el-button type="" @click="sellLimitBtn()" v-else class="corsell">
                                <p v-html="sellingLabel"></p>
                              </el-button>
                            </div>
                          </div>
                          <!-- end -->
                        </div>
                      </el-tab-pane>
                      <!-- <el-tab-pane label="止盈止损" name="third">止盈止损</el-tab-pane> -->
                    </el-tabs>
                  </template>
                </div>
                <!-- inputend -->
              </div>

              <!-- 大盘历史end -->
            </div>
            <div class="coinrightbox">
              <!-- 大盘记录 成交记录 -->
              <div class="right-history">
                <div class="global-trades">
                  <div class="his_left with_com" v-show="hisSHow1 == 1">
                    <div class="dtop-title flex">
                      <p class="m-left-10 r_active" @click="hisTab(1)" v-show="hisSHow1 == 1">{{ $t('home.huobi57') }}
                      </p>
                      <div class="mod-head">
                        <div class="flex mod_red">
                          <div class="active">
                            <img src="../assets/trade/zhangtu.svg" alt="" srcset="">
                          </div>
                          <div>
                            <img src="../assets/trade/trade.28169321.svg" alt="" srcset="">
                          </div>
                          <div>
                            <img src="../assets/trade/sell.01ca9792.svg" alt="" srcset="">
                          </div>
                        </div>
                      </div>
                      <p class="m-left-10 hisr_none" @click="hisTab(2)" v-show="hisSHow2 == 2">{{ $t('home.huobi58') }}
                      </p>
                    </div>
                    <div class="mod order-books cur" v-if="hisSHow1 == 1">
                      <div class="mod-body">
                        <div class="mod-title">
                          <div class="mod-title">
                          <span class="price">{{ $t('home.huobi59') }} </span>
                          <span></span>
                          <span class="amount">{{ $t('home.huobi60') }}
                      
                          </span>
                                <!--  ({{ getCoin }}) -->
                          <!--  </span>
                         <span class="total"> -->
                            <!-- {{ $t('home.huobi61') }} (USDT) -->

                        </div>
                        </div>
                        <!-- <dl  >
                              <dt >
                              <span class="price" >价格 (USDT)</span>
                              <span class="amount" >数量 (LOL)</span>
                              <span class="total" >累计 (LOL)</span>
                            </dt>  -->
                        <!-- <dd class="now-pric bs" > -->
                          <div class="his_back">
                          <div class="list_sell">

                            <div class="list asks" v-if="sellList != ''">
                              <p data-ee="fillPrice" class="single-orderbook" v-for="(item, index) in sellList"
                                :key="index"
                                 style="transition-duration:1s"
                                :style="'background-size:' + parseInt((item[1] / sellMax) * 100) + '% 100%'">
                                <span class="price color-sell">{{ item[0] }}</span>
                                <span class="amount">{{ item[1] }}</span>
                                <!-- <span class="total">{{ index  + 1}}</span> -->
                              </p>
                            </div>
                            <!-- 暂无数据 -->
                            <div class="no-data txt-center" v-else>
                              <p v-text="$t('tip.noRecord')" class="line-h50"></p>
                            </div>
                          </div>

                          <div class="now-pric">
                            <div class="ticker-close">
                              <span class="color-down">
                                {{ nowPrice }}
                                <em class="estimate hide1440">≈ {{ nowPrice }} USD</em>
                              </span>
                              <a target="_self" href="/">
                                {{ $t('home.huobi62') }}
                              </a>
                            </div>
                          </div>

                          <div class="list_buy">
                            <div class="list bids" v-if="buyList != ''" >
                              <p data-ee="fillPrice" class="single-orderbook" v-for="(item, index) in buyList"
                                :key="index"
                                style="transition-duration: 500ms;"
                                :style="'background-size:' + parseInt((item[1] / buyMax) * 100) + '% 100%'">
                                <span class="price color-buy">{{ item[0] }}</span>
                                <span class="amount">{{ item[1] }}</span>
                                <!-- <span class="total">{{ 13 -  index }}</span> -->
                              </p>
                            </div>
                            <!-- 暂无数据 -->
                            <div class="no-data txt-center" v-else>
                              <p v-text="$t('tip.noRecord')" class="line-h50"></p>
                            </div>
                          </div>

                        </div>
                        <!-- <div class="ticker-close" >
                                <span class="color-down" >
                                        0.001991
                                        <em  class="estimate hide1440">≈ 0.0138 CNY</em>
                                </span>
                                <a target="_self" href="/zh-cn/orderbooks/?type=exchange&amp;s=lol_usdt" data-v-1b2949ff="" >
                                        更多
                                </a>
                              </div>  -->
                        <!-- 测试 隐藏了 -->
                        <div class="list asks" style="display:none">
                          <p data-ee="fillPrice" class="single-orderbook" style="background-size: 30% 100%;">
                            <span class="price color-sell">0.001998</span> <span class="amount">2031.7499</span>
                            <span class="total">2031.7499</span>
                          </p>
                        </div>
                        <div class="list bids" style="display:none">
                          <p data-ee="fillPrice" class="single-orderbook" style="background-size: 10% 100%;">
                            <span class="price color-buy">0.001981</span>
                            <span class="amount">247347.4188</span>
                            <span class="total">386852.6018</span>
                          </p>
                        </div>
                        <!-- </dd> -->
                        <!-- </dl> -->
                      </div>
                    </div>

                  </div>

                  <!-- 成交历史 实时成交栏目 大盘历史 -->
                  <div class="his_right with_com" v-show="hisSHow2 == 1">
                    <div class="dtop-title flex">
                      <p class="m-left-10 " @click="hisTab(1)" v-show="hisSHow1 == 2">{{ $t('home.huobi57') }}</p>
                      <p class="m-left-10 r_active" @click="hisTab(2)" v-show="hisSHow2 == 1">{{ $t('home.huobi58') }}
                      </p>
                    </div>

                    <div class="history_box">
                      <div class="content-rt-history trade-table">
                        <template>
                          <el-table :data='realList' :cell-style='myCellStyle' max-height='855'
                            :empty-text="$t('bico.W160')">
                            <el-table-column :label="$t('home.huobi63')">
                              <span slot-scope="scope">{{ scope.row.ts | dateFormat() }}</span>
                            </el-table-column>
                            <el-table-column :label="$t('home.huobi59')" width="100" align='right'>
                              <template slot-scope="scope">
                                <span :class="scope.row.direction == 'sell' ? 'color-success' : 'color-danger'">{{
                  (scope.row.price * 1).toFixed(interception) }}
                                </span>
                              </template>
                            </el-table-column>
                            <el-table-column :label="$t('home.huobi60')" width="100" align='right'>
                              <template slot-scope="scope">
                                {{ (scope.row.amount * 1).toFixed(6) }}
                              </template>
                            </el-table-column>
                          </el-table>
                        </template>
                      </div>
                    </div>

                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>


      <!-- 当前委托 -->
      <!-- 委托历史 (最近一周) -->
      <div class="bottombox-list">
        <!-- bottom-order -->
        <div class="bottomlist">
          <div class="content-center m-top-5">
            <div class="bot-title">
              <p class="m-left-10">{{ $t('home.huobi83') }} <router-link v-if="token" class="m-left-10"
                  style="color: #ffffff;" to="coinOrder">{{ $t('legal.record') }}</router-link></p>
            </div>
            <div class="histable-list">
              <template>

                <el-table :data="tableData" style="width: 100%" height="250" :empty-text="$t('bico.W160')">
                  <el-table-column prop="matchType" :label="$t('home.huobi64')" width="150">
                    <template slot-scope="scope">
                      <p v-text="scope.row.matchType == 'BUY' ? $t('coin.buy') : $t('coin.sell')"
                        :class="scope.row.matchType == 'BUY' ? 'color-buy' : 'color-sell'"></p>
                    </template>
                  </el-table-column>
                  <el-table-column prop="symbols" :label="$t('home.huobi65')" width="150"></el-table-column>
                  <el-table-column prop="unit" :label="$t('home.huobi66')" width="150"></el-table-column>
                  <el-table-column prop="willNumberB" :label="$t('home.huobi67')" width="150"></el-table-column>

                  <el-table-column prop='totalPrice' :label="$t('home.huobi68')" width="150">
                  </el-table-column>
                  <el-table-column prop='numbersB' :label="$t('home.huobi69')" width="150">
                  </el-table-column>
                  <el-table-column prop='avgUnit' :label="$t('home.huobi70')" width="150">
                  </el-table-column>
                  <el-table-column prop='numberFee' :label="$t('home.huobi71')" width="150">
                  </el-table-column>
                  <el-table-column prop='orderNo' :label="$t('home.huobi72')" width="250">
                  </el-table-column>
                  <el-table-column prop="createTime" :label="$t('home.huobi73')" width="150"></el-table-column>
                  <el-table-column prop="zip" :label="$t('home.huobi74')" width="">
                    <template slot-scope="scope">
                      <el-button @click="cancelEntrustFun(scope.row.matchId)" type="text" :data-id="scope.row.autoid"
                        size="small">
                        {{ $t('home.huobi75') }}</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </div>
          </div>
          <!-- 委托历史 (最近一周) -->
          <div class="content-bottom m-top-5">
            <div class="bot-title">
              <p class="m-left-10">{{ $t('home.huobi76') }}</p>
            </div>
            <div class="histable-list">
              <template>
                <el-table :data="tableData2" style="width: 100%" height="250" :empty-text="$t('bico.W160')">
                  <el-table-column prop="matchType" :label="$t('home.huobi64')" width="150">
                    <template slot-scope="scope">
                      <p v-text="scope.row.matchType == 'BUY' ? $t('coin.buy') : $t('coin.sell')"
                        :class="scope.row.matchType == 'BUY' ? 'color-buy' : 'color-sell'"></p>
                    </template>
                  </el-table-column>
                  <el-table-column prop="symbols" :label="$t('home.huobi65')" width="150"></el-table-column>
                  <el-table-column prop="unit" :label="$t('home.huobi66')" width="150"></el-table-column>
                  <el-table-column prop="willNumberB" :label="$t('home.huobi67')" width="150"></el-table-column>

                  <el-table-column prop='totalPrice' :label="$t('home.huobi68')" width="150">
                  </el-table-column>
                  <el-table-column prop='numbersB' :label="$t('home.huobi69')" width="150">
                  </el-table-column>
                  <el-table-column prop='avgUnit' :label="$t('home.huobi70')" width="150">
                  </el-table-column>
                  <el-table-column prop='numberFee' :label="$t('home.huobi71')" width="150">
                  </el-table-column>
                  <el-table-column prop='orderNo' :label="$t('home.huobi72')" width="250">
                  </el-table-column>
                  <el-table-column prop="createTime" :label="$t('home.huobi73')" width="150"></el-table-column>
                  <el-table-column prop="zip" :label="$t('home.huobi77')" width="">
                    <template slot-scope="scope">
                      <p v-if="scope.row.status == 'PAID'">{{ $t('home.huobi78') }}</p>
                      <p v-if="scope.row.status == 'PART_MATCH'">{{ $t('home.huobi79') }}</p>
                      <p v-if="scope.row.status == 'FINISH'">{{ $t('home.huobi80') }}</p>
                      <p v-if="scope.row.status == 'CANCEL_FINISH'">{{ $t('home.huobi81') }}</p>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </div>
          </div>
        </div>
      </div>
    </section>
    <Foot />
  </div>
</template>
<script>
// import mainCoinModel from "@/js/allCoinModel.js";
// import commTool from '@/assets/utils/commTool.js'
//import { Loading, Form } from "element-ui";
//import { randomString } from "@/assets/js/common.js";
//import { checkTradePassword } from "../../service/TradeService.js";
//import myStorage from '@/assets/js/myStorage';
import coinTab from "@/components/coinTab.vue";
import CoinOrder from '@/components/CoinOrder'
import {
  ticketApi,
  tradeListApi,
  coinInformApi,
  coinTrasctionApi,
  realListApi,
  coinListApi,
  cancelCoinApi
} from '@/api/getData'
import TradeView from '@/components/TradeViewCoin'
import codeStatus from '@/config/codeStatus'
import logosArr from '@/logoSrc/index.js'
import "@/assets/css2/common.css";
import Foot from '@/components/Foot'
export default {
  data() {
    return {
      currentCoinId: ',',
      // 大盘历史
      hisSHow1: 1,
      hisSHow2: 1,
      usdtorcny: 1,
      priceHisData: [], //
      selectLogoClassImgFlag: false,
      selectLogoClassImg: '',
      activeIndex: '1',//0限价 1市价
      sellIndex: 'BUY',
      goodPrice: 1,
      num: '',
      sliderValue: '',
      levarageLabel: '',

      form: {
        region: 'BTC/USDT'
      },
      marketList: [],
      coinArrAll: [],
      buyList: [],
      buyHigh: '',
      search: '',
      sellList: [],
      sellHigh: '',
      // wTimerList: null,
      // wRealList: null,
      nowPrice: '',
      highForth: '',
      lossForth: '',
      labelPosition: 'right',
      amountPirce: '',
      rose: '',
      cny: '',
      useBond: '0.00000000',
      needBond: 0,
      proportion: 0,
      getIndex: 0,
      cnyUsdt: '',
      // timeTimer: null,
      steps: 0,
      activeName: 0,
      hands: '',
      getPair: 0,
      pecentArr: [{
        name: '25%',
        value: "25"
      }, {
        name: '50%',
        value: "50"
      }, {
        name: '75%',
        value: "75"
      }, {
        name: '100%',
        value: "100"
      }],
      token: sessionStorage.getItem('userToken'),
      fee: '', //手续费比例
      balanceNum: '0.00000000', //可用数量
      isClick: false,
      changeValue: '',
      interception: '', //币种截取位数
      hitNum: 0,
      loading: false,
      selectPanKou: true,
      realFlag: true,
      minBuyNumber: '', //最小委托数量
      realList: [], //实时交易数据
      sellNum: 0.1,
      value2: 0,
      sellPrice: 0.00,
      buyPrice: 0.00,
      //现价对象
      formAlign: {
        price: '',
        number: 0,
        region: '',
        type: '',
        name: '',
      },
      //市价对象
      formMark: {
        number: '',
        name: '',
      },
      screenWidth: document.body.clientWidth, // 屏幕尺寸
      tableData: [],
      tableData2: [],
      rateObj: JSON.parse(sessionStorage.getItem('platFormRate')),
      rateNumber: sessionStorage.getItem("platFormRateNumber"),
    }
  },
  created() {
    //this.delivery_asn = this.$route.query.delivery_asn;
    var par = this.$route.query.symbol
    if (par != undefined && par != '') {
      this.form.region = par
      this.getIndex = 1
    }
    // var str  = commTool.getURLParams("symbol")
    console.log("参数*******************", this.$route.query.symbol)
    //getURLParams()
    // this.wTimerList = setInterval(function () {
    this.ticketFun()
    // }, 1000)
    // this.timeTimer = setInterval(function () {
    this.getDepth();
    // }, 1000)
    // this.wRealList = setInterval(function () {
    this.getRealList()
    // }, 1000)
    if (this.token) {
      this.recordFun();
      this.recordFun2();
    }

  },
  filters: {
    dateFormat: function (tick) {
      var data = new Date(Number(tick));
      var h = data.getHours() < 10 ? "0" + data.getHours() : data.getHours()
      var m = data.getMinutes() < 10 ? "0" + data.getMinutes() : data.getMinutes()
      var s = data.getSeconds() < 10 ? '0' + data.getSeconds() : data.getSeconds()
      return h + ":" + m + ":" + s;

    },
    numFormat11: function (tick) {
      var data = tick + '';
      var length = data.length
      var s = 11
      if (length >= s) {
        return data.substring(0, s)
      }
      if (data.indexOf('.') < 0) {
        data = data + '.'
      }
      for (var i = 0; i < s - length; i++) {
        data = data + '0'
      }
      return data
    },
    numFormat9: function (tick) {
      var data = tick + '';
      var length = data.length
      var s = 9
      if (length >= s) {
        return data.substring(0, s)
      }
      if (data.indexOf('.') < 0) {
        data = data + '.'
      }
      for (var i = 0; i < s - length; i++) {
        data = data + '0'
      }
      return data
    },
  },
  computed: {
    amountLabel() {
      return this.$t('table.num') + `(${this.getCoin})`;
    },
    buyingLabel() {
      return this.$t('coin.buy') + `&nbsp;${this.getCoin}`;
    },
    sellingLabel() {
      return this.$t('coin.sell') + `&nbsp;${this.getCoin}`;
    },
    priceLabel() {
      return this.$t('home.huobi38') + `(${this.getCoin})`;
    },
    totalLabel() {
      return this.$t('bico.W112') + `(${this.getCoin})`;
    },
    // 成交额
    volumeTrans() {
      return this.$t('bico.W113') + `(${this.getCoin})`;
    },
    // 已成交
    tradedTrans() {
      return this.$t('bico.W114') + `(${this.getCoin})`;
    },
    getCoin() {
      var arr = this.form.region;
      var coin = arr.split('/');

      return coin[0]
    },
    getNew() {
      return this.nowPrice
    },
  },
  watch: {
    search(newVal, oldVal) {

      if (newVal == '') {
        return
      }
      //console.log(newVal, oldVal)
      // var inputV = newVal
      // console.log("new",newVal)

      // this.coinArrAll = this.marketList
      // this.marketList =[]
      // for(var i=0;i<this.coinArrAll.length;i++){
      //   var str = this.coinArrAll[i].symbol.split('/')[0]
      //   if(str.indexOf(inputV) != -1){

      //     this.marketList.push(this.coinArrAll[i])
      //   }
      // }
    },
    'formAlign.number': (newVal, oldVal) => {
      console.log("newVal number.name:", newVal);
      if (isNaN(newVal)) {
        this.formAlign.number = 0.1;
        this.formMark.number = 0.1;
        return
      }
      if (newVal == '') {
        return
      }
      if (newVal.toString().indexOf('.') == newVal.length - 1) {
        return
      }
      this.changeNum(newVal)
      console.log("oldVal number.name:", oldVal);
    },
    'formMark.number': (newVal, oldVal) => {
      if (isNaN(newVal)) {
        this.formAlign.number = 0.1;
        this.formMark.number = 0.1;
        return
      }
      if (newVal == '') {
        return
      }
      if (newVal.toString().indexOf('.') == newVal.length - 1) {
        return
      }
      console.log("newVal number.name:", newVal);
      this.changeNum(newVal)
      console.log("oldVal number.name:", oldVal);
    },
    getNew(newValue) {
      if (this.activeIndex == '1') { //市价
        this.getBond();
      }
    },
    selectLogoClassImgFlag(n) {
      if (n) {
        this.selectLogoClassImg = this.marketList[0].symbolLogo
      }
    }
  },
  mounted() {
    this.informFun(); //页面信息
    if (this.token) {
      this.timer = setInterval(() => {
        this.recordFun()
      }, 1500)
      this.timer2 = setInterval(() => {
        this.recordFun2()
      }, 5000)
    }
    window.onresize = () => {
      return (() => {
        window.screenWidth = document.body.clientWidth;
        this.screenWidth = window.screenWidth;
        // console.log('--屏幕宽度判断右侧大盘历史是否折叠--'+this.screenWidth);
        if (this.screenWidth < 1500) {
          this.hisSHow2 = 2;
        } else {
          this.hisSHow1 = 1;
          this.hisSHow2 = 1;
        }
      })()
    }
  },
  methods: {
    symInputChange(val) {
      var inputV = val.target.value
      //console.log("new", val.target.value)
      this.coinArrAll = this.marketList
      this.marketList = []
      for (var i = 0; i < this.coinArrAll.length; i++) {
        var str = this.coinArrAll[i].symbol
        if (str.indexOf(inputV) != -1) {
          this.marketList.push(this.coinArrAll[i])
        }
      }
    },
    showCny() {
      if (this.usdtorcny == 1) {
        this.usdtorcny = 2;
      } else {
        this.usdtorcny = 1;
      }
    },
    //表格列点击
    getRowData(data) {
      console.log(data)
    },
    // 单列样式
    myCellStyle() {
      return "padding:0px 0 !important;border:none";
    },
    formatter(row, column) {
      return row.rose;
    },
    // 历史记录切换
    hisTab(val) {
      if (this.screenWidth < 1400) {
        //console.log(this.screenWidth);
        if (val == 1) {
          this.hisSHow1 = 1;
          this.hisSHow2 = 2;
        } else if (val == 2) {
          this.hisSHow1 = 2;
          this.hisSHow2 = 1;
        }
      } else {
        return
      }
    },
    //限价交易 限价买入
    // 01限价买入
    async recordFun() {
      var dataArr = new URLSearchParams();
      dataArr.set('type', 1);
      dataArr.set('current', 1);
      dataArr.set('size', 100);
      var res = await coinListApi(dataArr);
      this.tableData = [];
      if (res.success) {
        //this.page.total = Number(res.data.total);
        var obj = res.data.records;
        if (obj.length > 0) {
          obj.forEach(element => {

            element.avgUnit = Number(element.avgUnit).toFixed(4);
            element.numbersB = element.numbersB = '' ? '0.0000' : Number(element.numbersB).toFixed(4);
            // 交易总额 = 委托数量*委托价格
            element.totalPrice = (Number(element.unit) * Number(element.willNumberB)).toFixed(8);
            element.numbersU = element.numbersU = '' ? '0.0000' : Number(element.numbersU).toFixed(4);
            element.numberFee = Number(element.numberFee).toFixed(8);
            this.tableData.push(element);
          });
        }
      } else {
        clearInterval(this.timer)
      }
    },
    async recordFun2() {
      var dataArr = new URLSearchParams();
      dataArr.set('type', 5);
      dataArr.set('current', 1);
      dataArr.set('size', 100);
      var res = await coinListApi(dataArr);
      this.tableData2 = [];
      if (res.success) {
        // this.page.total = Number(res.data.total);
        var obj = res.data.records;
        if (obj.length > 0) {
          obj.forEach(element => {

            element.avgUnit = Number(element.avgUnit).toFixed(4);
            element.numbersB = element.numbersB = '' ? '0.0000' : Number(element.numbersB).toFixed(4);
            // 交易总额 = 委托数量*委托价格
            element.totalPrice = (Number(element.unit) * Number(element.willNumberB)).toFixed(8);
            element.numbersU = element.numbersU = '' ? '0.0000' : Number(element.numbersU).toFixed(4);
            element.numberFee = Number(element.numberFee).toFixed(8);
            if (this.tableData2.length < 6) {
              this.tableData2.push(element);
            }

          });
        }
      } else {
        clearInterval(this.timer2)
      }
    },
    cancelEntrustFun(id) { //撤销委托
      this.$confirm(this.$t('orderStatus.cancelTip'), {
        confirmButtonText: this.$t('orderStatus.cancelBtn'),
        cancelButtonText: this.$t('coin.think'),
        customClass: 'comfirmBox',
        type: 'warning'
      }).then(async () => {
        var dataArr = new URLSearchParams();
        dataArr.set('matchId', id);
        var res = await cancelCoinApi(dataArr);
        this.resultFun(res)
      }).catch(() => { });

    },
    async buy_limitBtn() {
      var data = this.activeIndex == '0' ? this.formAlign : this.formMark
      var way = '',
        price = '',
        num = '';
      if (this.activeIndex == '0') { //限价
        if (data.number == '') {
          this.$message.error(this.$t('legal.entrustNum'));
          return false;
        }
        if (Number(data.number) <= 0) {
          this.$message.error(this.$t('legal.entrustCount') + '0');
          return false;
        }
        if (Number(data.price) < 0) {
          this.$message.error(this.$t('legal.effectiveBig'));
          return false;
        }
        if (isNaN(data.price)) {
          this.$message.error(this.$t('legal.effectivePrice'));
          return false;
        }
        num = data.number
        way = 'LIMIT';
        price = data.price;
      } else { //市价
        if (data.number == '') {
          this.$message.error(this.$t('legal.entrustNum'));
          return false;
        }
        if (Number(data.number) <= 0) {
          this.$message.error(this.$t('legal.entrustCount') + '0');
          return false;
        }
        num = data.number
        way = 'MARKET'
        price = this.nowPrice;
      }
      this.sellIndex = "BUY"
      var dataArr = new URLSearchParams();
      dataArr.set('unit', price);
      dataArr.set('number', num);
      dataArr.set('matchType', this.sellIndex);
      dataArr.set('dealWay', way);
      dataArr.set('symbols', this.form.region);
      this.hitNum += this.hitNum + 1;
      if (this.hitNum == 1) {
        var res = await coinTrasctionApi(dataArr);
        //console.log(res)
        if (res.success) {
          this.$message({
            type: 'success',
            message: res.msg
          })
          this.informFun();
        } else {
          codeStatus(res.code, function (msg) {
            this.$message.error(msg);
          })
        }

        setTimeout(() => {
          this.hitNum = 0;
        }, 2000);
      }
    },
    // 02限价卖出
    async sellLimitBtn() {
      var data = this.activeIndex == '0' ? this.formAlign : this.formAlign
      var way = '',
        price = '',
        num = '';
      if (this.activeIndex == '0') { //限价
        if (data.number == '') {
          this.$message.error(this.$t('legal.entrustNum'));
          return false;
        }
        if (Number(data.number) <= 0) {
          this.$message.error(this.$t('legal.entrustCount') + '0');
          return false;
        }
        if (Number(data.price) < 0) {
          this.$message.error(this.$t('legal.effectiveBig'));
          return false;
        }
        if (isNaN(data.price)) {
          this.$message.error(this.$t('legal.effectivePrice'));
          return false;
        }
        num = data.number
        way = 'LIMIT';
        price = data.price;
      } else { //市价
        if (data.number == '') {
          this.$message.error(this.$t('legal.entrustNum'));
          return false;
        }
        if (Number(data.number) <= 0) {
          this.$message.error(this.$t('legal.entrustCount') + '0');
          return false;
        }
        num = data.number
        way = 'MARKET'
        price = this.nowPrice;
      }
      this.sellIndex = "SELL"
      var dataArr = new URLSearchParams();
      dataArr.set('unit', price);
      dataArr.set('number', num);
      dataArr.set('matchType', this.sellIndex);
      dataArr.set('dealWay', way);
      dataArr.set('symbols', this.form.region);
      this.hitNum += this.hitNum + 1;
      if (this.hitNum == 1) {
        var res = await coinTrasctionApi(dataArr);
        //console.log(res)
        if (res.success) {
          this.$message({
            type: 'success',
            message: res.msg
          })
          this.informFun();
        } else {
          codeStatus(res.code, function (msg) {
            this.$message.error(msg);
          })
        }

        setTimeout(() => {
          this.hitNum = 0;
        }, 2000);
      }

    },
    onTableRowClick(rowData) {
      // const link ='currencyTrade?symbol='+rowData.symbol;
      // this.$router.replace(link);
      // window.location.reload();
      var symbol = rowData.symbol;
      this.realList = []
      this.realFlag = true
      this.form.region = rowData.symbol;

      this.interception = Number(rowData.number)
      this.nowPrice = rowData.close.toFixed(this.interception)

      //this.nowPrice = rowData.close.toFixed(rowData.number) //this.interception
      this.informFun();
      var value = rowData.symbol
      var obj = value.split('-');
      this.hands = obj[1];
      this.getPair = 0;
      this.ticketFun();
      this.getRealList()
      this.getDepth(); //行情深度
      // this.$refs.tradeView.removeChart();
      // this.$refs.tradeView.loadChart();
      this.$refs.tradeView.setSymbols();
      let imgSrc = logosArr.filter(v => v.name == value);
      this.selectLogoClassImg = imgSrc[0].logo;
    },

    changEnOrReList(flag) {
      this.selectPanKou = flag;
    },

    //获取当前币种的实时交易数据
    // async getRealList2() {
    //   var value = ((this.form.region).split('/')).join('');
    //   var getValue = value.toLocaleLowerCase();
    //   $.ajax({
    //     type: 'GET',
    //     url: 'https://api.huobi.pr//market/trade?symbol=' + getValue,
    //     success: function (res) {

    //       if (res.status == 'ok') {
    //         var dataList = res.tick.data;
    //         var c = dataList.length
    //         var flag = false
    //         if (this.realList.length >= c) {
    //           var list = this.realList.slice(0, c)
    //           for (var i = 0; i < c; i++) {
    //             if (list[i].ts == dataList[i].ts) {
    //               flag = true
    //             }
    //           }
    //         }
    //         if (!flag) {
    //           this.realList = dataList.concat(this.realList)
    //           if (this.realList.length > 45) {
    //             this.realList = this.realList.slice(0, 45)
    //           }
    //           if (this.realList.length > 0) {
    //             this.realFlag = false
    //             this.nowPrice = (this.realList[0].price * 1).toFixed(this.interception)
    //           }
    //         }
    //       }
    //     },
    //     error: function (err) {
    //       console.log(err)
    //     }
    //   })
    // },
    //重复点击取消选择比例
    handleClick(tab, event) { //重复点击取消选择比例
      if (Number(this.activeName) == Number(this.changeValue)) {
        this.isClick = !(this.isClick);

        if (this.isClick) {
          this.activeName = '0';
        }
      } else {
        this.activeName = tab;
        this.isClick = false;
      }

      this.changeValue = tab;

      this.getBond();
    },
    //获取可用余额和手续费比例
    async informFun() {
      if (this.token) {
        var dataArr = new URLSearchParams();
        dataArr.set('type', this.form.region);
        dataArr.set('matchType', this.sellIndex);
        var res = await coinInformApi(dataArr);
        if (res.success) {
          var data = res.data;
          this.fee = (Number(data.feeRate) / 100).toFixed(8);
          this.balanceNum = (data.price).toFixed(8);
          this.minBuyNumber = Number(data.minBuyNumber)
        }
      }
    },

    //获取某个币种的行情
    // getTicketsDetail() {
    //   var value = ((this.form.region).split('/')).join('');
    //   var getValue = value.toLocaleLowerCase();
    //   $.ajax({
    //     type: 'GET',
    //     url: 'https://api.huobi.pr/market/detail?symbol=' + getValue,
    //     success: function (res) {

    //       if (res.status == 'ok') {
    //         var obj = res.tick;
    //         if (obj) {
    //           var index = this.interception;
    //           this.nowPrice = (obj.close).toFixed(index);
    //           this.highForth = (obj.high).toFixed(2);
    //           this.lossForth = obj.low;

    //           this.amountPirce = Math.floor(obj.amount);
    //           // this.cny = element.cny;
    //           if (this.getPair == 0) {
    //             this.goodPrice = (obj.close).toFixed(index)
    //             this.formAlign.price = (obj.close).toFixed(index)
    //           }
    //           this.roseFun();
    //         }
    //       }
    //     },
    //     error: function (err) {
    //       console.log(err)
    //     }
    //   })
    // },
    //获取一天24小时的行情，得出涨幅
    // roseFun() {
    //   var value = ((this.form.region).split('/')).join('');
    //   var getValue = value.toLocaleLowerCase();
    //   $.ajax({
    //     type: 'GET',
    //     url: 'https://api.huobi.pr/market/history/kline?period=1day&size=1&symbol=' + getValue,
    //     success: function (res) {
    //       if (res.status == 'ok') {
    //         var arr = res.data;
    //         if (arr.length > 0) {
    //           var obj = arr[0];
    //           // （收盘价-开盘价）/开盘价*100
    //           this.rose = (((obj.close - obj.open) / obj.open) * 100).toFixed(8);
    //         }
    //       }
    //     }
    //   })
    // },

    //选择币种
    selectCoin(value) { //选择币种
      this.form.region = value;
      this.informFun();
      var obj = value.split('-');
      this.hands = obj[1];
      this.getPair = 0;
      // this.getCoin();
      this.realList = []
      this.realFlag = true
      let imgSrc = logosArr.filter(v => v.name == value);
      this.selectLogoClassImg = imgSrc[0].logo;
    },
    //币币交易
    async submitFun() {
      if (this.num == '') {
        this.$message.error(this.$t('legal.entrustNum'));
        return false;
      }
      if (Number(this.num) <= 0) {
        this.$message.error(this.$t('legal.entrustCount') + '0');
        return false;
      }
      var way = '',
        price = '';
      if (this.activeIndex == '0') { //限价
        if (Number(this.goodPrice) < 0) {
          this.$message.error(this.$t('legal.effectiveBig'));
          return false;
        }

        if (isNaN(this.goodPrice)) {
          this.$message.error(this.$t('legal.effectivePrice'));
          return false;
        }

        way = 'LIMIT';

        price = this.goodPrice;
      } else { //市价
        way = 'MARKET'
        price = this.nowPrice;
      }

      if (Number(this.num) < this.minBuyNumber) {
        this.$message.error(this.$t('legal.minEntrustNum') + this.minBuyNumber);
        return false
      }

      var dataArr = new URLSearchParams();
      dataArr.set('unit', price);
      dataArr.set('number', this.num);
      dataArr.set('matchType', this.sellIndex);
      dataArr.set('dealWay', way);
      dataArr.set('symbols', this.form.region);
      this.hitNum += this.hitNum + 1;
      if (this.hitNum == 1) {
        var res = await coinTrasctionApi(dataArr);
        console.log(res)
        if (res.success) {
          this.$message({
            type: 'success',
            message: res.msg
          })
          this.informFun();
        } else {
          codeStatus(res.code, function (msg) {
            this.$message.error(msg);
          })
        }

        setTimeout(() => {
          this.hitNum = 0;
        }, 2000);
      }

    },
    //选择限价市价
    handleIndex() {

    },
    //选择买入卖出
    handleType() {
      this.informFun(); //根据买入卖出获取手续费
      this.getBond(); //根据买入卖出计算手数
    },
    //计算手续费
    changeNum(value) {
      console.log("ujkashyfdjkhjasdhfkja", value)
      var index = this.interception;
      //var getTxt = '/^[0-9]+(\.[0-9]{1,' + index + '})?$/';

      var patrn = /^[0-9][0-9]*([\\.][0-9]{1,6})?$/;
      //var getTxt = '/^([1-9]+[\\d]*(.[0-9]{1,' + index + '})?)$/'
      //var patrn = eval(getTxt);
      if (!(patrn.test(value))) {
        this.formAlign.number = '';
        this.formMark.number = '';
        this.useBond = '0.00';
        this.proportion = 0;
        return false;
      } else {
        var cnyPrice = Number(value) * Number(this.fee);
        this.useBond = (cnyPrice).toFixed(8);
      }
    },
    //根据比例计算手数
    getBond() {
      if (this.activeName != '0') {

        var price = ''
        if (this.activeIndex == '1') { //市价：以当前行情价为委托价
          price = Number(this.nowPrice);
        } else { //限价
          price = Number(this.formAlign.price)
        }

        var index = this.interception;
        var lastPrice = '';
        if (this.sellIndex == 'BUY') { //买入
          lastPrice = (Number(this.balanceNum) * (Number(this.activeName) / 100) / Number(price))
        } else { //卖出
          lastPrice = (Number(this.balanceNum) * (Number(this.activeName) / 100))
        }

        var str = String(lastPrice);
        var getStr = str.indexOf('.');
        var getValue = str.substring(0, getStr + index + 1)
        var valueNum = Number(getValue);

        this.formAlign.number = valueNum;
        this.formMark.number = valueNum;
        this.changeNum(valueNum)
      }
    },
    //加限价最优价格
    addPrice(value) {
      this.getBond();
    },
    //获取币种
    async ticketFun() {
      var data = new URLSearchParams();
      data.set('type', 0)
      var res = await ticketApi(data);
      var obj = res.data;
      this.marketList = [];
      obj.forEach((element, index) => {
        if (element.symbol != '') {
          if (this.form.region == element.symbol) {
            var index = Number(element.number);
            this.interception = index
            if (this.realFlag) {
              this.nowPrice = this.interception != '' ? (element.close).toFixed(this.interception) : (element
                .close);
            }
            this.highForth = element.high;
            this.lossForth = element.low;
            this.rose = ((element.rose)).toFixed(2);
            this.amountPirce = element.amount;

            this.cny = element.cny;

            if (this.getPair == 0) {
              this.goodPrice = element.close
              this.formAlign.price = element.close
            }
            this.interception = Number(element.number);
          }
          // if (this.search != '') {

          // }
          //var str1=this.search.toLocaleLowerCase()
          var str2 = this.search.toLocaleUpperCase()
          if (element.symbol.search(str2) != -1) {
            this.marketList.push(element);
          }

          this.selectLogoClassImgFlag = true;
        }


      });
      if (this.getIndex == 0) {
        var objArr = this.marketList[0];
        this.form.region = objArr.symbol;
      }
      this.getIndex = this.getIndex + 1;
      this.getPair = this.getPair + 1;
      this.startSubscription()


    },
    // //行情深度
    // async getDepth() {
    //   var dataArr = new URLSearchParams();
    //   dataArr.set('symbol', this.form.region);
    //   dataArr.set('type', '0');
    //   var res = await tradeListApi(dataArr);
    //   var buyArr = [], sellArr = [];
    //   var buyArrObj = res.data.bids; //买单
    //   var sellArrObj = res.data.asks; //卖单
    //   if (buyArrObj.length > 0) {
    //     buyArrObj.sort(function (x, y) {
    //       return y[0] - x[0];
    //     })
    //     for (var i in buyArrObj) {
    //       buyArr.push(buyArrObj[i]);
    //     }
    //     this.buyArr = [];
    //     //下
    //     this.buyArr = buyArr.slice(0, 20);

    //     var buyNum = buyArr.slice(0, 4);
    //     buyNum.sort(function (x, y) {
    //       return y[1] - x[1]
    //     })

    //     this.buyHigh = buyNum[0][1];
    //   }

    //   if (sellArrObj.length > 0) {
    //     sellArrObj.sort(function (x, y) {
    //       return x[0] - y[0];
    //     })
    //     for (var i in sellArrObj) {
    //       sellArr.push(sellArrObj[i]);
    //     }
    //     this.sellArr = [];
    //     //上
    //     var newArr = sellArr.slice(0, 20);
    //     newArr.sort(function (x, y) {
    //       return y[0] - x[0];
    //     })

    //     this.sellArr = newArr;
    //     var sellNum = newArr.slice(0, 4);
    //     sellNum.sort(function (x, y) {
    //       return y[1] - x[1]
    //     })
    //     this.sellHigh = sellNum[0][1];
    //     this.startSubDepth()
    //   }
    // },
    async getDepth() {//行情深度
      var dataArr = new URLSearchParams();
      dataArr.set('symbol', this.form.region);
      dataArr.set('type', '1');
      var res = await tradeListApi(dataArr);
      this.buyList = [];
      this.sellList = [];
      var buyArrObj = res.data.bids;//买单
      var sellArrObj = res.data.asks;//卖单


      this.buyMax = buyArrObj[0][1]
      for (var i in buyArrObj) {
        this.buyList.push(buyArrObj[i]);
      }
      this.buyList = this.buyList.slice(0, 13);
      this.buyList.sort(function (x, y) {
        return y[0] - x[0];
      })

      // 卖
      this.sellMax = sellArrObj[0][1]
      for (var i in sellArrObj) {
        this.sellList.push(sellArrObj[i]);
      }
      this.sellList = this.sellList.slice(0, 13);
      this.sellList.sort(function (x, y) {
        return y[0] - x[0];
      })
      // console.log(this.buyList);
      // console.log(this.sellList);
      this.startSubDepth()
    },
    //获取当前币种的实时交易数据
    async getRealList() {
      var dataArr = new URLSearchParams();
      dataArr.set('symbol', this.form.region);
      dataArr.set('type', '1');
      var res = await realListApi(dataArr);
      this.realList = JSON.parse(res.data);
      this.startHistoryTrade();
    },


    //盘口数据
    startSubDepth() {
      let data = { "event": "sub", "type": "depth", "symbol": this.form.region }
      this.$webSocket.addEvent(data)
      this.$webSocket.addDoOn("/depth", (session, message) => {
        var response = JSON.parse(message.dataAsString())
        // console.log(response);
        if (response.symbol == this.form.region) {


          var data = JSON.parse(response.data)
          this.buyList = [];
          this.sellList = [];
          //debugger
          var buyArr = []
          var buyArrObj = data.bids;//买单
          var sellArrObj = data.asks;//卖单

          buyArrObj.sort(function (x, y) {
            return y[1] - x[1];
          })
          for (var i in buyArrObj) {
            this.buyList.push(buyArrObj[i]);
          }
          this.buyMax = buyArrObj[0][1]
          this.buyList = this.buyList.slice(0, 13);
          this.buyList.sort(function (x, y) {
            return x[0] - y[0]
          })


          sellArrObj.sort(function (x, y) {
            return y[1] - x[1];
          })
          this.sellMax = sellArrObj[0][1]
          for (var i in sellArrObj) {
            this.sellList.push(sellArrObj[i]);
          }
          var newArr = this.sellList.slice(0, 13);
          newArr.sort(function (x, y) {
            return x[0] - y[0];
          })

          this.sellList = newArr;

        }
      });
    },
    // 实时订单订阅
    startHistoryTrade() {
      let data = {
        "event": "sub",
        "type": "historyTrade",
        "symbol": this.form.region
      }
      this.$webSocket.addEvent(data)
      this.$webSocket.addDoOn("/historyTrade", (session, message) => {
        let response = JSON.parse(message.dataAsString())
        let real = JSON.parse(response.data)
        var dataList = [real, ...this.realList];
        dataList.pop()
        this.realList = dataList;
      })
    },
    //开始订阅
    startSubscription() {
      this.$webSocket.addDoOn("/ticket", (session, message) => {
        var response = JSON.parse(message.dataAsString())
        // 列表数据
        var index = this.marketList.findIndex(item => {
          return item.symbol == response.symbol
        })

        var data = JSON.parse(response.data)
        if (index != -1) {
          this.marketList.splice(index, 1, data)
        }


        if (this.form.region == data.symbol) {
          // console.log("-----",this.nowPrice)
          if (this.realFlag) {
            this.nowPrice = Number(data.close).toFixed(2)
          }
        }
      })
    },
  },
  beforeDestroy() {
    // 取消订阅深度
    let data1 = { "event": "unSub", "type": "depth" }
    this.$webSocket.addEvent(data1)
    // 取消订阅实时订单
    let data2 = { "event": "unSub", "type": "historyTrade" }
    this.$webSocket.addEvent(data2)
  },
  components: {
    CoinOrder,
    TradeView,
    Foot,
    coinTab,
  }
}
</script>

<style lang="scss" scoped>
.trademain {
  min-width: 1560px;

  .header-main {
    .hd-body {
      background: #131625;
    }
  }

  * {
    margin: 0;

  }

  div {
    box-sizing: border-box;
  }

  .page {
    -webkit-box-flex: 1;
    -ms-flex: 1 0 auto;
    flex: 1 0 auto;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    width: 100%;
    margin: 0 auto;
    border-top: 1px solid transparent;
    background-color: #0d1123;
  }

  .el-table td,
  .el-table th.is-leaf {
    border: none !important;
    background: #171b2b !important;
  }

  .content-top {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
    -ms-flex-direction: row;
    flex-direction: row;
    -webkit-box-align: stretch;
    -ms-flex-align: stretch;
    align-items: stretch;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 100%;
    // min-width: 1366px;
    height: 900px;
    margin-top: 6px;

    .cen-left {
      min-width: 282px;
    }
  }

  // cen-right
  .cen-right {
    margin: 0 10px 0 8px;
    width: 100%;
    min-width: 1000px;

    .cont-right {
      display: flex;

      .centerbox {
        margin-right: 8px;
        width: calc(100% - 322px);
      }
    }
  }

}

.bottombox-list {
  padding: 10px 10px 0 0;
}



// k线部分
.trading-chart {
  .ticker {
    position: relative;
    height: 44px !important;
    // border-bottom: 1px solid;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
    -ms-flex-direction: row;
    flex-direction: row;
    border-radius: 2px 2px 0 0;
    white-space: normal;
    padding-right: 26px;
    font-weight: 400;

    .flex_space {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .coinone {
      align-items: center;

      .symbol-name {
        height: 44px;
        line-height: 44px;
        vertical-align: unset;
        white-space: nowrap;
        margin-right: 4px;
        font-size: 22px;
        font-weight: 600;
        position: relative;
        cursor: pointer;
        display: inline-block;
        margin-left: 8px;
        padding-left: 8px;
      }

      .us_price {
        font-size: 16px;
        line-height: 20px;
      }

      .cny_price {
        font-size: 12px;
        font-weight: 400;
        color: #61688a;
      }

      .tl_p {
        color: #61688a;
      }

      .bt_p {
        color: #b0b8db;
      }

      .top_mag {
        margin-left: 15px;
        font-size: 12px;

        p {
          line-height: 18px;
        }
      }

    }

    .set_cor {
      position: absolute;
      right: 15px;
      bottom: 10px;
      cursor: pointer;

      i {
        font-size: 20px;
      }

      &:hover {
        color: #357ce1;
        font-size: 18px;
      }
    }

    .symbol-type {
      margin-right: 16px;
      height: 16px;
      line-height: 16px;
      font-size: 12px;
      border-radius: 3px;
      padding: 0 4px;
      color: #357ce1;
      border: 1px solid rgba(53, 124, 225, 0.75);
      vertical-align: middle;
      position: relative;
      cursor: pointer;
      display: inline-block;
    }
  }

  .chart-actions {
    height: 24px;
    border-bottom: 1px solid;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    font-size: 12px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  // 图表 K线宽度 高度
  .container-k {
    display: flex;
    width: 100%;
    // width: 938px;
    // height: 455px;
    max-width: 950px;
    height: 634px;
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    -ms-flex-direction: column;
    flex-direction: column;

    img {
      width: 100%;
      height: 455px;
    }

    .w937 {
      width: 100%;
      height: 455px;
    }
  }
}

.center_input {
  background-color: #171b2b;
  margin-top: 6px;
  height: 287px;
}

.buysell_trad {
  .mod-body {
    height: calc(100%);
    position: relative;
    display: flex;
    flex-direction: row;
  }

  .single-panel {
    width: 100%;
    height: 100%;
  }

  .t_plan {
    padding: 0 30px;
  }

  .ex_tips {
    height: 80px;
    line-height: 20px;
    padding: 0 20px;

    span {
      font-size: 12px;
    }

    em {}
  }

  .ex_btn {
    padding: 0 20px;
    margin-top: 0px;

    button {
      width: 100%;
    }

    em {
      color: #b0b8db;
    }
  }

  .r_name {
    display: inline-block;
    width: 80px;
    text-align: center;
  }
}
</style>

<style lang="scss" scoped>
.right-history {
  box-sizing: border-box;
  padding-right: 0px;
}

.global-trades {
  display: flex;

  .with_com {
    flex: 1;
  }

  .his_left {
    min-width: 320px;
  }

  .his_right {
    min-width: 320px;
  }

  .dtop-title {
    height: 44px;
    line-height: 44px;
    color: #b0b8db;
    background: #131625;
    width: 100%;
    border-bottom: 1px solid #131625;
    border-radius: 2px 2px 0 0;

    p {
      width: 70px;
      text-align: center;
      cursor: pointer;
    }
  }

  .order-books {
    width: 100%;
    min-width: 310px;
    height: 100%;

    .mod-head {
      height: 36px;
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
      overflow: initial !important;
      position: relative;
      background: #171b2b;
      border-bottom: 1px solid #111217;
      box-sizing: border-box;
    }

    .mod-body {
      height: calc(100% - 36px);
      overflow: hidden;

      .mod-title {
        background-color: #171b2b;
        color: #b0b8db;
        position: relative;
        top: 0;
        line-height: 28px;
        margin-top: 0;
        z-index: 1;
        padding: 0 10px;
        display: flex;

        span {
          -webkit-box-flex: 1;
          -ms-flex: 1;
          flex: 1;
          text-align: right;
          white-space: nowrap;
          font-size: 14px;

          &.price {
            text-align: left;
            font-size: 14px;
          }
        }
      }

      dl {
        font-size: 12px;
        position: relative;
        height: 100%;

        dt {
          background-color: #171b2b;
          color: #61688a;
          position: relative;
          top: 0;
          line-height: 28px;
          margin-top: 0;
          z-index: 1;
          padding: 0 10px;
          display: flex;
        }

        dd {
          background-color: #171b2b;
          border: 1px solid #111217;
          border-right: none;
          border-left: none;
          position: absolute;
          top: calc(50% - 5px);
          left: 0;
          width: 100%;
        }

        span {
          -webkit-box-flex: 1;
          -ms-flex: 1;
          flex: 1;
          text-align: right;
          white-space: nowrap;

          &.price {
            text-align: left;
          }
        }

        p {
          color: #b0b8db;
          padding: 0 15px;
          line-height: 20px;
          height: 20px;
          display: flex;
          -ms-flex-negative: 0;
          flex-shrink: 0;
          cursor: pointer;
          background-position: 0;
          background-size: 0 0;
          background-repeat: no-repeat;
          -webkit-box-flex: 1;
          -ms-flex: 1;
          flex: 1;
          -webkit-transition: margin .5s;
          -o-transition: margin .5s;
          transition: margin .5s;
        }


      }

      // ----------
      .his_back {
        background: #171b2b;

        .single-orderbook {
          &:hover {
            background: #1e2237;
          }
        }
      }

      .list_buy,
      .list_sell {
        height: 398px;
        overflow: hidden;
      }

      .list {
        p {
          color: #b0b8db;
          font-size: 12px;
          padding: 0 15px;
          line-height: 20px;
          height: 20px;
          display: flex;
          -ms-flex-negative: 0;
          flex-shrink: 0;
          cursor: pointer;
          background-position: 0;
          background-size: 0 0;
          background-repeat: no-repeat;
          -webkit-box-flex: 1;
          -ms-flex: 1;
          flex: 1;
          -webkit-transition: margin .5s;
          -o-transition: margin .5s;
          transition: margin .5s;
        }

        .single-orderbook {
          justify-content: space-between;
        }

        span {
          -webkit-box-flex: 1;
          -ms-flex: 1;
          flex: 1;
          text-align: right;
          white-space: nowrap;

          &.price {
            text-align: left;
          }
        }
      }

      .now-pric {
        .ticker-close {
          color: #7085ac;
          padding: 0 16px;
          height: 32px;
          line-height: 32px;
          display: flex;
          justify-content: space-between;
          white-space: nowrap;
          border-top: 1px solid #111217;
          border-bottom: 1px solid #111217;

          span.color-down {
            color: #d74e5a;
            max-width: 230px;
            display: inline-block;
            white-space: normal;
            overflow: hidden;
            font-size: 14px;
          }
        }
      }

      .asks {
        display: flex;
        bottom: 0px;
        -webkit-box-orient: vertical;
        -webkit-box-direction: reverse;
        -ms-flex-direction: column-reverse;
        flex-direction: column-reverse;

        p {
          background-position: 100%;
          background-image: -webkit-gradient(linear, left top, left bottom, from(rgba(250, 82, 82, .1)), to(rgba(250, 82, 82, .1)));
          background-image: -o-linear-gradient(rgba(250, 82, 82, .1), rgba(250, 82, 82, .1));
          background-image: linear-gradient(rgba(250, 82, 82, .1), rgba(250, 82, 82, .1))
        }
      }

      .bids {
        display: flex;
        bottom: 0px;
        -webkit-box-orient: vertical;
        -webkit-box-direction: reverse;
        -ms-flex-direction: column-reverse;
        flex-direction: column-reverse;

        // top: 34px;
        p {
          background-position: 100%;
          background-image: -webkit-gradient(linear, left top, left bottom, from(rgba(18, 184, 134, .1)), to(rgba(18, 184, 134, .1)));
          background-image: -o-linear-gradient(rgba(18, 184, 134, .1), rgba(18, 184, 134, .1));
          background-image: linear-gradient(rgba(18, 184, 134, .1), rgba(18, 184, 134, .1))
        }

      }
    }
  }
}

.order-books .mod-body dl dd>div {
  position: absolute;
  width: 100%;
  left: 0;
}

.order-books .mod-body dl .now-pric {
  height: 35px
}

.order-books .mod-body dl .now-pric .ticker-close {
  height: 35px;
  padding: 0 16px;
  line-height: 35px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  white-space: nowrap
}

.his_right {
  margin-left: 8px;

  .history_box {
    min-height: 854px;
  }
}

.mod_red {
  display: flex;
  align-items: center;
  cursor: pointer;

  >div {
    margin: 0 0px 0 15px;
    border: 1px solid #383f66;
    padding: 3px 4px;
    border-radius: 2px;
    height: 24px;
    line-height: 22px;
    margin-top: 10px;

    img {
      width: 16px;
      height: 16px;
    }

    &:hover {
      border-color: #357ce1;
    }
  }

  .active {
    border-color: #357ce1;
  }
}

@media screen and (min-width:1200px) and (max-width: 1600px) {
  .global-trades {
    display: flex;
    padding-right: 8px;

    .with_com {
      flex: 1;
    }

    .his_left {
      min-width: 300px;
    }

    .his_right {
      min-width: 300px;
    }
  }

  .order-books {
    width: 288px;
  }

  .order-books .mod-body dl dt {
    line-height: 24px
  }

  .order-books .mod-body dl .now-pric {
    height: 32px
  }

  .order-books .mod-body dl .now-pric .ticker-close {
    height: 30px;
    line-height: 30px;
    padding-left: 8px
  }

  .order-books .mod-body dl dd .asks {
    bottom: 33px
  }

  // 响应式
  .right-history {
    width: 316px;
  }

  .his_right {
    margin-left: 0;
  }

  .r_active {
    border-top: 3px solid #357ce1;
    background: #171b2b;
  }

  .trademain .cen-right .cont-right .centerbox {
    margin-right: 8px;
    width: calc(100% - 600px);
  }



}

// bottom-order
.bot-title {
  height: 40px;
  line-height: 40px;
  color: #ffffff;
  background: #131625;
  width: 100%;
  border-bottom: 1px solid #131625;
  border-radius: 2px 2px 0 0;
}

.el-table {
  font-size: 12px;
}

.coin-switch {
  box-sizing: border-box;
  background: #131625;
  height: 100%;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  -webkit-box-flex: 1;
  -ms-flex: auto;
  flex: auto;
  padding-bottom: 0;

  .quotes {
    display: flex;
    min-height: 28px;

    span {
      cursor: pointer;
    }
  }

  .mock-a {
    color: #b0b8db;
    height: 20px;
    font-size: 12px;
    line-height: 22px;
    padding: 0 6px;
    margin: 0 4px 4px 0;
    border-radius: 2px;
    border-bottom: 0;
    text-transform: uppercase;

    &.active {
      color: #ffffff;
      background-color: #357ce1;
    }
  }

  .tabs {
    padding: 8px 8px 8px 10px;
    display: block;
    height: auto;
    border-radius: 2px 2px 0 0;
    border: 0;
  }

  // 搜索框
  .actions-serch {
    .input-box {
      height: 26px;
      position: relative;

      input {
        background: #171b2b;
        border-color: #383f66;
        color: #d2d6ec;
        padding-left: 28px;
        border: 1px solid #383f66;
        border-radius: 2px;
        font-size: 12px;
        width: 100%;
        height: 100%;

      }
    }

    .i_sos {
      position: absolute;
      left: 5px;
      top: 6px;
    }

    .btnexch {
      padding-left: 40px;
      font-size: 14px;

      i {
        font-size: 16px;
        margin-right: 5px;
      }
    }
  }

  // coin_list
  .coin_list {
    tr {
      font-size: 12px !important;
    }

    p,
    span {
      font-size: 12px;
    }

    .el-table .cell {
      p {
        text-align: inherit;
        color: #b0b8db;
      }

      .trad_active {
        color: #357ce1;
      }
    }

  }

  .left_coinname {
    .option {
      width: 24px;
    }

    .star_active {
      color: #357ce1;
    }
  }
}

@media screen and (max-width:1440px) {
  .coin-switch .mock-a {
    padding: 0 2px;
    height: 18px;
    line-height: 18px;
  }
}
</style>
<style lang="scss">
@import "@/assets/css/variable.scss";
@import "@/assets/css/element-ui-reset.scss";
@import "@/assets/theme/index.scss";
@import "@/assets/css/header/headerReset.scss";
@import "@/assets/css/header/headerDefault.scss";
</style>
