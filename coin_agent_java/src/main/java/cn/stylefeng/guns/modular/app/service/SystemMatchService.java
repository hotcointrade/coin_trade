package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.controller.market.Depth;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.app.entity.RobotConfig;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.ThreadPoolUtil;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.service.SpotService;
import cn.stylefeng.guns.modular.e.entity.Match;
import cn.stylefeng.guns.modular.e.entity.MatchDetail;
import cn.stylefeng.guns.modular.e.service.MatchDetailService;
import cn.stylefeng.guns.modular.e.service.MatchService;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.service.CurrencyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 系统撮合订单服务
 */

@Service
@Slf4j
public class SystemMatchService extends Constant
{


    @Autowired
    MatchService matchService;

    @Autowired
    MatchDetailService matchDetailService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SpotService spotService;
    @Autowired
    RobotConfigService robotConfigService;


    //撮合订单

    /**
     * 撮合卖单
     * 用户卖10个 ，20元
     * 机器用户下一个买单
     * 机器用户 买 10个20元
     * @param match
     */
    @Transactional(rollbackFor = Exception.class)
    public void matchOrder(Match match){
       // redisUtil.publish("","");
        //TODO：带增加撮合的入口限制
        switch (match.getMatchType()){// 订单类型（买、卖）
            case "BUY": //如果是买单，拉取卖盘进行匹配,价格最优进行匹配
                buy2(match);
                break;
            case "SELL"://如果是卖单，拉取买盘进行匹配，价格最优进行匹配
                sell2(match);
                break;
        }
    }


    //买单 匹配 卖单 (USDT) 买入 (BTC)
    @Transactional(rollbackFor = Exception.class)
    public void buy(Match buyV){
        //  拉取卖盘  卖单单价<=买单单价 订单
        List<Match> matchList = matchService.getBaseMapper().getBestOrderList(buyV);
        //币种 账户处理
        String coin = buyV.getSymbols().split("/")[0];
        //交易本位币种 如： BTC/USDT  交易本位币种为:USDT
        String standardCoin = buyV.getSymbols().split("/")[1];
        //交易对
        String symbols = buyV.getSymbols();
        for (Match sell : matchList){
            Match query = new Match();
            query.setOrderNo(buyV.getOrderNo());
            Match buy = matchService.getOne(new QueryWrapper<>(query));
            //0.买单剩余量
            BigDecimal buyRest = buy.getWill().subtract(buy.getOk());
            //买单无剩余量，订单结束
            if (buyRest.compareTo(BigDecimal.ZERO) <= 0){
                log.info("-- 买单无剩余量，订单{}结束 --", buy.getMatchId());
                break;
            }
            //1.成交价即卖出价
            BigDecimal okUnit = sell.getUnit();
            //2.卖出单剩余量
            BigDecimal sellRest = sell.getWill().subtract(sell.getOk());
            //3.双方撮合成交数量（BTC)
            BigDecimal okNumber = buyRest.compareTo(sellRest) > 0 ? sellRest : buyRest;
            //双方撮合成交总价（USDT)
            BigDecimal okPrice = okNumber.multiply(okUnit);
            //4.订单成交后双方剩余量
            //买单成交后剩余量
            BigDecimal okBuyRest = buyRest.subtract(okNumber);
            //卖单成交后剩余量
            BigDecimal okSellRest = sellRest.subtract(okNumber);
            //5.买卖订单处理
            //5.1
            //买单消耗量 USDT
            buy.setFinishNumber(buy.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(buy.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM);
            //手续费扣除（手续费订单全部撮合完成进行扣除）
            BigDecimal feePrice = BigDecimal.ZERO; //买单,扣除BTC

            //买单手续费扣除
            //扣除手续费 (买单扣除BTC作为手续费）
            feePrice =okNumber.multiply(new BigDecimal(buy.getRate()));
            //买单有剩余量
            if (okBuyRest.compareTo(BigDecimal.ZERO) > 0){
                buy.setStatus(MatchStatus.PART_MATCH.code);
            }else{ //买单无剩余量，完结，手续费扣除
                buy.setStatus(MatchStatus.FINISH.code);

            }
            //买家流水金额（USDT)  USDT账户
            BigDecimal buyFlowPrice = okPrice;
            //买家流水量（BTC） BTC账户 扣除的手续费从到账BTC里面扣除
            BigDecimal buyFlowNumber = okNumber.subtract(feePrice);
            //买单更新
            if (!matchService.updateById(buy)){
                log.error("买单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }
            //5.2
            //卖单消耗
            sell.setFinishNumber(sell.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(sell.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM);
            BigDecimal usdtFeeNumber = BigDecimal.ZERO;//卖单,扣除USDT
            //卖单扣除手续费（扣除的为到账USDT）
            usdtFeeNumber = okPrice.multiply(new BigDecimal(sell.getRate()));
            //卖单有剩余量
            if (okSellRest.compareTo(BigDecimal.ZERO) > 0){
                sell.setStatus(MatchStatus.PART_MATCH.code);
            }else{ //卖单无剩余量，完结，手续费扣除
                sell.setStatus(MatchStatus.FINISH.code);

            }
            //卖家流水金额（USDT)  USDT账户
            BigDecimal sellFlowPrice = okPrice.subtract(usdtFeeNumber);
            //卖家流水量（BTC） BTC账户
            BigDecimal sellFlowNumber = okNumber;
            //卖单更新
            if (!matchService.updateById(sell)){
                log.error("卖单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }
            //6.生成成交明细订单
            //将对应订单形成关联（目前仅用于画k线，故不处理）
            MatchDetail matchDetail = new MatchDetail();
            matchDetail.setBuyId(buy.getMemberId())
                    .setBuyOrder(buy.getOrderNo())
                    .setSellId(sell.getMemberId())
                    .setSellOrder(sell.getOrderNo())
                    .setNumber(okPrice) //USDT
                    .setRemark(okNumber.toPlainString()) //到账BTC
                    .setUnit(okUnit)
                    .setSymbols(symbols)
                    .setCreateUser(SYS_PLATFORM);
            matchDetailService.save(matchDetail);

            Date now = new Date();
            long minTimes = DateTimeUtil.minuteTimeNotSec(now);
            redisUtil.lSet(String.format(KINE_MINUTE, symbols, minTimes), matchDetail, KLINE_TIME_OUT);
            //7.用户币币账户资产更新


            //7.1 买家账户更新

            //买家 USDT账户
            Currency buyCurrencyUsdt = F.me().getCurrency(buy.getMemberId(), standardCoin);
            //usdt冻结  变更前
            BigDecimal buyUsdtBeforeLock = buyCurrencyUsdt.getLockedPrice();
            //usdt 变更后
            BigDecimal buyUsdtAfterLock = buyUsdtBeforeLock.subtract(buyFlowPrice);
            buyCurrencyUsdt.setLockedPrice(buyUsdtAfterLock.compareTo(MIN_DATA_USDT) > 0 ? buyUsdtAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyUsdt))
                throw new UpdateDataException(100);
            // 买家 流水生成
            //USDT 流水
            // 冻结扣除
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowPrice, standardCoin, buyFlowPrice, standardCoin, BigDecimal.ZERO, standardCoin,
                    ItemCode.LOCKED, standardCoin, null, null,
                    buyUsdtBeforeLock, buyUsdtAfterLock,
                    buy.getMemberId(), sell.getMemberId());

            //买家 BTC账户
            Currency buyCurrencyBtc = F.me().getCurrency(buy.getMemberId(), coin);
            //btc可用 前
            BigDecimal buyBtcBeforeUsd = buyCurrencyBtc.getUsedPrice();
            BigDecimal buyBtcAfterUsd = buyBtcBeforeUsd.add(buyFlowNumber);
            buyCurrencyBtc.setUsedPrice(buyBtcAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyBtc))
                throw new UpdateDataException(100);
            //btc 流水 可用添加
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowNumber, coin, buyFlowNumber, coin,feePrice, coin,
                    ItemCode.USED, coin, null, null,
                    buyBtcBeforeUsd, buyBtcAfterUsd,
                    sell.getMemberId(), buy.getMemberId());


            //7.2 卖家账户更新
            //卖家USDT账户
            Currency sellCUsdt = F.me().getCurrency(sell.getMemberId(), standardCoin);
            //可用  USDT
            BigDecimal sellCUsdtBeforeUsd = sellCUsdt.getUsedPrice();
            BigDecimal sellCUsdtAfterUsd = sellCUsdtBeforeUsd.add(sellFlowPrice);
            sellCUsdt.setUsedPrice(sellCUsdtAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCUsdt))
                throw new UpdateDataException(100);

            //交易本位账户 账户添加
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowPrice, standardCoin, sellFlowPrice, standardCoin,usdtFeeNumber, standardCoin,
                    ItemCode.USED, standardCoin, null, null,
                    sellCUsdtBeforeUsd, sellCUsdtAfterUsd,
                    buy.getMemberId(), sell.getMemberId());

            //卖家BTC账户
            Currency sellCBtc = F.me().getCurrency(sell.getMemberId(), coin);

            //冻结 BTC 前
            BigDecimal sellBtcBeforeLock = sellCBtc.getLockedPrice();
            //冻结 BTC 后
            BigDecimal sellBtcAfterLock = sellBtcBeforeLock.subtract(sellFlowNumber);

            sellCBtc.setLockedPrice(sellBtcAfterLock.compareTo(MIN_DATA) > 0 ? sellBtcAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCBtc))
                throw new UpdateDataException(100);
            // 卖家 流水生成
            //冻结流出 交易币种冻结扣除
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowNumber, coin, sellFlowNumber, coin, BigDecimal.ZERO, coin,
                    ItemCode.LOCKED, coin, null, null,
                    sellBtcBeforeLock, sellBtcAfterLock,
                    sell.getMemberId(), buy.getMemberId());


            //8.当订单委托量完成，进行剩余量结算
            //8.1 买单 委托量完成，剩余USDT返还
            //订单完成了就跳出循环
            if (StringUtils.equals(buy.getStatus(), MatchStatus.FINISH.code)){
                //订单委托量成交完成后剩余USDT
                BigDecimal rest = buy.getTotalPrice().subtract(buy.getFinishNumber());
                if(rest.compareTo(BigDecimal.ZERO)>0){
                    Currency buyRestUsdtC = F.me().getCurrency(buy.getMemberId(), standardCoin);
                    BigDecimal beforeUsed = buyRestUsdtC.getUsedPrice();
                    BigDecimal afterUsed = beforeUsed.add(rest);
                    BigDecimal beforeLock = buyRestUsdtC.getLockedPrice();
                    BigDecimal afterLock = beforeLock.subtract(rest);
                    buyRestUsdtC.setUsedPrice(afterUsed)
                            .setLockedPrice(afterLock)
                            .setUpdateUser(SYS_PLATFORM);
                    if (!currencyService.updateById(buyRestUsdtC))
                        throw new UpdateDataException(100);
                    //USDT 流水 可用添加
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.USED, coin, null, null,
                            beforeUsed, afterUsed,
                            buy.getMemberId(), buy.getMemberId());
                    //USDT 冻结 减少
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.LOCKED, coin, null, null,
                            beforeLock, afterLock,
                            buy.getMemberId(), buy.getMemberId());

                }
                break;
            }
        }

    }

    //买单 匹配 卖单 (USDT) 买入 (BTC)
    @Transactional(rollbackFor = Exception.class)
    public void buy2(Match buyV){
        //  拉取卖盘  卖单单价<=买单单价 订单
        Match sell = createMatch(buyV);
        //非配置账号 走原来的逻辑
        if (sell == null) {
            sell(buyV);
            return;
        }
        //币种 账户处理
        String coin = buyV.getSymbols().split("/")[0];
        //交易本位币种 如： BTC/USDT  交易本位币种为:USDT
        String standardCoin = buyV.getSymbols().split("/")[1];
        //交易对
        String symbols = buyV.getSymbols();

            Match buy = buyV;
            //0.买单剩余量
            BigDecimal buyRest = buy.getWill().subtract(buy.getOk());

            //1.成交价即卖出价
            BigDecimal okUnit = sell.getUnit();
            //2.卖出单剩余量
            BigDecimal sellRest = sell.getWill().subtract(sell.getOk());
            //3.双方撮合成交数量（BTC)
            BigDecimal okNumber = buyRest.compareTo(sellRest) > 0 ? sellRest : buyRest;
            //双方撮合成交总价（USDT)
            BigDecimal okPrice = okNumber.multiply(okUnit);
            //4.订单成交后双方剩余量
            //买单成交后剩余量
            BigDecimal okBuyRest = buyRest.subtract(okNumber);
            //卖单成交后剩余量
            BigDecimal okSellRest = sellRest.subtract(okNumber);
            //5.买卖订单处理
            //5.1
            //买单消耗量 USDT
            buy.setFinishNumber(buy.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(buy.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM);
            //手续费扣除（手续费订单全部撮合完成进行扣除）
            BigDecimal feePrice = BigDecimal.ZERO; //买单,扣除BTC

            //买单手续费扣除
            //扣除手续费 (买单扣除BTC作为手续费）
            feePrice =okNumber.multiply(new BigDecimal(buy.getRate()));
            //买单有剩余量
            if (okBuyRest.compareTo(BigDecimal.ZERO) > 0){
                buy.setStatus(MatchStatus.PART_MATCH.code);
            }else{ //买单无剩余量，完结，手续费扣除
                buy.setStatus(MatchStatus.FINISH.code);

            }
            //买家流水金额（USDT)  USDT账户
            BigDecimal buyFlowPrice = okPrice;
            //买家流水量（BTC） BTC账户 扣除的手续费从到账BTC里面扣除
            BigDecimal buyFlowNumber = okNumber.subtract(feePrice);
            //买单更新
            if (!matchService.updateById(buy)){
                log.error("买单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }
            //5.2
            //卖单消耗
            sell.setFinishNumber(sell.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(sell.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM);
            BigDecimal usdtFeeNumber = BigDecimal.ZERO;//卖单,扣除USDT
            //卖单扣除手续费（扣除的为到账USDT）
            usdtFeeNumber = okPrice.multiply(new BigDecimal(sell.getRate()));
            //卖单有剩余量
            if (okSellRest.compareTo(BigDecimal.ZERO) > 0){
                sell.setStatus(MatchStatus.PART_MATCH.code);
            }else{ //卖单无剩余量，完结，手续费扣除
                sell.setStatus(MatchStatus.FINISH.code);

            }
            //卖家流水金额（USDT)  USDT账户
            BigDecimal sellFlowPrice = okPrice.subtract(usdtFeeNumber);
            //卖家流水量（BTC） BTC账户
            BigDecimal sellFlowNumber = okNumber;
            //卖单更新
            if (!matchService.updateById(sell)){
                log.error("卖单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }
            //6.生成成交明细订单
            //将对应订单形成关联（目前仅用于画k线，故不处理）
            MatchDetail matchDetail = new MatchDetail();
            matchDetail.setBuyId(buy.getMemberId())
                    .setBuyOrder(buy.getOrderNo())
                    .setSellId(sell.getMemberId())
                    .setSellOrder(sell.getOrderNo())
                    .setNumber(okPrice) //USDT
                    .setRemark(okNumber.toPlainString()) //到账BTC
                    .setUnit(okUnit)
                    .setSymbols(symbols)
                    .setCreateUser(SYS_PLATFORM);
            matchDetailService.save(matchDetail);

            Date now = new Date();
            long minTimes = DateTimeUtil.minuteTimeNotSec(now);
            redisUtil.lSet(String.format(KINE_MINUTE, symbols, minTimes), matchDetail, KLINE_TIME_OUT);
            //7.用户币币账户资产更新


            //7.1 买家账户更新

            //买家 USDT账户
            Currency buyCurrencyUsdt = F.me().getCurrency(buy.getMemberId(), standardCoin);
            //usdt冻结  变更前
            BigDecimal buyUsdtBeforeLock = buyCurrencyUsdt.getLockedPrice();
            //usdt 变更后
            BigDecimal buyUsdtAfterLock = buyUsdtBeforeLock.subtract(buyFlowPrice);
            buyCurrencyUsdt.setLockedPrice(buyUsdtAfterLock.compareTo(MIN_DATA_USDT) > 0 ? buyUsdtAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyUsdt))
                throw new UpdateDataException(100);
            // 买家 流水生成
            //USDT 流水
            // 冻结扣除
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowPrice, standardCoin, buyFlowPrice, standardCoin, BigDecimal.ZERO, standardCoin,
                    ItemCode.LOCKED, standardCoin, null, null,
                    buyUsdtBeforeLock, buyUsdtAfterLock,
                    buy.getMemberId(), sell.getMemberId());

            //买家 BTC账户
            Currency buyCurrencyBtc = F.me().getCurrency(buy.getMemberId(), coin);
            //btc可用 前
            BigDecimal buyBtcBeforeUsd = buyCurrencyBtc.getUsedPrice()==null?BigDecimal.ZERO:buyCurrencyBtc.getUsedPrice();
            BigDecimal buyBtcAfterUsd = buyBtcBeforeUsd.add(buyFlowNumber);
            buyCurrencyBtc.setUsedPrice(buyBtcAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyBtc))
                throw new UpdateDataException(100);
            //btc 流水 可用添加
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowNumber, coin, buyFlowNumber, coin,feePrice, coin,
                    ItemCode.USED, coin, null, null,
                    buyBtcBeforeUsd, buyBtcAfterUsd,
                    sell.getMemberId(), buy.getMemberId());


            //7.2 卖家账户更新
            //卖家USDT账户
            Currency sellCUsdt = F.me().getCurrency(sell.getMemberId(), standardCoin);
            //可用  USDT
            BigDecimal sellCUsdtBeforeUsd = sellCUsdt.getUsedPrice();
            BigDecimal sellCUsdtAfterUsd = sellCUsdtBeforeUsd.add(sellFlowPrice);
            sellCUsdt.setUsedPrice(sellCUsdtAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCUsdt))
                throw new UpdateDataException(100);

            //交易本位账户 账户添加
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowPrice, standardCoin, sellFlowPrice, standardCoin,usdtFeeNumber, standardCoin,
                    ItemCode.USED, standardCoin, null, null,
                    sellCUsdtBeforeUsd, sellCUsdtAfterUsd,
                    buy.getMemberId(), sell.getMemberId());

            //卖家BTC账户
            Currency sellCBtc = F.me().getCurrency(sell.getMemberId(), coin);

            //冻结 BTC 前
            BigDecimal sellBtcBeforeLock = sellCBtc.getLockedPrice()==null?BigDecimal.ZERO:sellCBtc.getLockedPrice();
            //冻结 BTC 后
            BigDecimal sellBtcAfterLock = sellBtcBeforeLock.subtract(sellFlowNumber);

            sellCBtc.setLockedPrice(sellBtcAfterLock.compareTo(MIN_DATA) > 0 ? sellBtcAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCBtc))
                throw new UpdateDataException(100);
            // 卖家 流水生成
            //冻结流出 交易币种冻结扣除
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowNumber, coin, sellFlowNumber, coin, BigDecimal.ZERO, coin,
                    ItemCode.LOCKED, coin, null, null,
                    sellBtcBeforeLock, sellBtcAfterLock,
                    sell.getMemberId(), buy.getMemberId());


            //8.当订单委托量完成，进行剩余量结算
            //8.1 买单 委托量完成，剩余USDT返还
            //订单完成了就跳出循环
            if (StringUtils.equals(buy.getStatus(), MatchStatus.FINISH.code)){
                //订单委托量成交完成后剩余USDT
                BigDecimal rest = buy.getTotalPrice().subtract(buy.getFinishNumber());
                if(rest.compareTo(BigDecimal.ZERO)>0){
                    Currency buyRestUsdtC = F.me().getCurrency(buy.getMemberId(), standardCoin);
                    BigDecimal beforeUsed = buyRestUsdtC.getUsedPrice();
                    BigDecimal afterUsed = beforeUsed.add(rest);
                    BigDecimal beforeLock = buyRestUsdtC.getLockedPrice();
                    BigDecimal afterLock = beforeLock.subtract(rest);
                    buyRestUsdtC.setUsedPrice(afterUsed)
                            .setLockedPrice(afterLock)
                            .setUpdateUser(SYS_PLATFORM);
                    if (!currencyService.updateById(buyRestUsdtC))
                        throw new UpdateDataException(100);
                    //USDT 流水 可用添加
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.USED, coin, null, null,
                            beforeUsed, afterUsed,
                            buy.getMemberId(), buy.getMemberId());
                    //USDT 冻结 减少
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.LOCKED, coin, null, null,
                            beforeLock, afterLock,
                            buy.getMemberId(), buy.getMemberId());

                }
            }
        //}

    }


    //卖单 匹配 买单
    @Transactional(rollbackFor = Exception.class)
    public void sell(Match sellEV) {


        //  拉取买盘  卖单单价 <= 买单单价 订单
        List<Match> matchList = matchService.getBaseMapper().getBestOrderList(sellEV);


        //交易对示例：BTC/USDT

        //币种 账户处理
        String coin = sellEV.getSymbols().split("/")[0];

        //交易本位币种 如： BTC/USDT  交易本位币种为:USDT
        String standardCoin = sellEV.getSymbols().split("/")[1];


        //交易对
        String symbols = sellEV.getSymbols();
        for (Match buy : matchList){//买盘列表
            Match query = new Match();
            query.setOrderNo(sellEV.getOrderNo());
            Match sell = matchService.getOne(new QueryWrapper<>(query));
            //0.卖单剩余量
            BigDecimal sellNewRest = sell.getWill().subtract(sell.getOk());
            //卖单无剩余量，订单结束
            if (sellNewRest.compareTo(BigDecimal.ZERO) <= 0){
                log.info("-- 卖单无剩余量，订单{}结束 --", sell.getMatchId());
                break;
            }

            //1.成交价即卖出价
            BigDecimal okUnit = sell.getUnit();
            //2.买单剩余量
            BigDecimal buyNewRest = buy.getWill().subtract(buy.getOk());
            //3.双方撮合成交数量（BTC)
            BigDecimal okNumber = buyNewRest.compareTo(sellNewRest) > 0 ? sellNewRest : buyNewRest;
            //双方撮合成交总价（USDT)
            BigDecimal okPrice = okNumber.multiply(okUnit);
            //4.订单成交后双方剩余量
            //买单成交后剩余量
            BigDecimal okBuyRest = buyNewRest.subtract(okNumber);
            //卖单成交后剩余量
            BigDecimal okSellRest = sellNewRest.subtract(okNumber);
            //5.买卖订单处理
            //5.1
            //买单消耗量 USDT
            buy.setFinishNumber(buy.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(buy.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM)
            ;

            //手续费扣除（手续费订单全部撮合完成进行扣除）
            BigDecimal feePrice = BigDecimal.ZERO; //买单,扣除BTC
            //扣除手续费 (买单手续费USDT）
            feePrice = okNumber.multiply(new BigDecimal(buy.getRate()));
            //买单有剩余量
            if (okBuyRest.compareTo(BigDecimal.ZERO) > 0){
                buy.setStatus(MatchStatus.PART_MATCH.code);
            } else{ //买单无剩余量，完结，手续费扣除
                buy.setStatus(MatchStatus.FINISH.code);

            }
            //买家流水金额（USDT)  USDT账户
            BigDecimal buyFlowPrice = okPrice;
            //买家流水量（BTC） BTC账户
            BigDecimal buyFlowNumber = okNumber.subtract(feePrice);
            //买单更新
            if (!matchService.updateById(buy))
            {
                log.error("买单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }
            //5.2
            //卖单消耗
            BigDecimal usdtFeeNumber = BigDecimal.ZERO;//卖单,扣除USDT手续费
            sell.setFinishNumber(sell.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(sell.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM);
            usdtFeeNumber = okPrice.multiply(new BigDecimal(sell.getRate()));
            //卖单有剩余量
            if (okSellRest.compareTo(BigDecimal.ZERO) > 0){
                sell.setStatus(MatchStatus.PART_MATCH.code);
            }else{ //卖单无剩余量，完结，手续费扣除
                sell.setStatus(MatchStatus.FINISH.code);
            }
            //卖家流水金额（USDT)  USDT账户
            BigDecimal sellFlowPrice = okPrice.subtract(usdtFeeNumber);
            //卖家流水量（BTC） BTC账户
            BigDecimal sellFlowNumber = okNumber;
            //卖单更新
            if (!matchService.updateById(sell)){
                log.error("卖单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }


            //6.生成成交明细订单
            //将对应订单形成关联（目前仅用于画k线，故不处理）
            MatchDetail matchDetail = new MatchDetail();
            matchDetail.setBuyId(buy.getMemberId())
                    .setBuyOrder(buy.getOrderNo())
                    .setSellId(sell.getMemberId())
                    .setSellOrder(sell.getOrderNo())
                    .setNumber(okPrice) //USDT
                    .setRemark(okNumber.toPlainString()) //到账BTC
                    .setUnit(okUnit)
                    .setSymbols(symbols)
                    .setCreateUser(SYS_PLATFORM);
            matchDetailService.save(matchDetail);

            Date now = new Date();
            long minTimes = DateTimeUtil.minuteTimeNotSec(now);
            redisUtil.lSet(String.format(KINE_MINUTE, symbols, minTimes), matchDetail, KLINE_TIME_OUT);


            //7.用户币币账户资产更新


            //7.1 买家账户更新

            //买家 USDT账户
            Currency buyCurrencyUsdt = F.me().getCurrency(buy.getMemberId(), standardCoin);

            //usdt冻结  变更前
            BigDecimal buyUsdtBeforeLock = buyCurrencyUsdt.getLockedPrice();
            //usdt 变更后
            BigDecimal buyUsdtAfterLock = buyUsdtBeforeLock.subtract(buyFlowPrice);
            buyCurrencyUsdt.setLockedPrice(buyUsdtAfterLock.compareTo(MIN_DATA_USDT) > 0 ? buyUsdtAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyUsdt))
                throw new UpdateDataException(100);
            // 买家 流水生成
            //USDT 流水
            // 冻结扣除
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowPrice, standardCoin, buyFlowPrice, standardCoin, BigDecimal.ZERO, standardCoin,
                    ItemCode.LOCKED, standardCoin, null, null,
                    buyUsdtBeforeLock, buyUsdtAfterLock,
                    buy.getMemberId(), sell.getMemberId());


            //买家 BTC账户
            Currency buyCurrencyBtc = F.me().getCurrency(buy.getMemberId(), coin);
            //btc可用 前
            BigDecimal buyBtcBeforeUsd = buyCurrencyBtc.getUsedPrice();
            BigDecimal buyBtcAfterUsd = buyBtcBeforeUsd.add(buyFlowNumber);

            buyCurrencyBtc.setUsedPrice(buyBtcAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyBtc))
                throw new UpdateDataException(100);

            //btc 流水 可用添加
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowNumber, coin, buyFlowNumber, coin,feePrice, coin,
                    ItemCode.USED, coin, null, null,
                    buyBtcBeforeUsd, buyBtcAfterUsd,
                    sell.getMemberId(), buy.getMemberId());


            //7.2 卖家账户更新

            //卖家USDT账户
            Currency sellCUsdt = F.me().getCurrency(sell.getMemberId(), standardCoin);
            //可用  USDT
            BigDecimal sellCUsdtBeforeUsd = sellCUsdt.getUsedPrice();
            BigDecimal sellCUsdtAfterUsd = sellCUsdtBeforeUsd.add(sellFlowPrice);
            sellCUsdt.setUsedPrice(sellCUsdtAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCUsdt))
                throw new UpdateDataException(100);


            //交易本位账户 账户添加
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowPrice, standardCoin, sellFlowPrice, standardCoin,usdtFeeNumber, standardCoin,
                    ItemCode.USED, standardCoin, null, null,
                    sellCUsdtBeforeUsd, sellCUsdtAfterUsd,
                    buy.getMemberId(), sell.getMemberId());


            //卖家BTC账户
            Currency sellCBtc = F.me().getCurrency(sell.getMemberId(), coin);

            //冻结 BTC 前
            BigDecimal sellBtcBeforeLock = sellCBtc.getLockedPrice();
            //冻结 BTC 后
            BigDecimal sellBtcAfterLock = sellBtcBeforeLock.subtract(sellFlowNumber);

            sellCBtc.setLockedPrice(sellBtcAfterLock.compareTo(MIN_DATA) > 0 ? sellBtcAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCBtc))
                throw new UpdateDataException(100);


            // 卖家 流水生成
            //冻结流出 交易币种冻结扣除
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowNumber, coin, sellFlowNumber, coin, BigDecimal.ZERO, coin,
                    ItemCode.LOCKED, coin, null, null,
                    sellBtcBeforeLock, sellBtcAfterLock,
                    sell.getMemberId(), buy.getMemberId());


            //8.当订单委托量完成，进行剩余量结算

            //8.1 买单 委托量完成，剩余USDT返还
            //订单完成了就跳出循环
            if (StringUtils.equals(buy.getStatus(), MatchStatus.FINISH.code))
            {
                //订单委托量成交完成后剩余USDT
                BigDecimal rest = buy.getTotalPrice().subtract(buy.getFinishNumber());

                if(rest.compareTo(BigDecimal.ZERO)>0)
                {
                    Currency buyRestUsdtC = F.me().getCurrency(buy.getMemberId(), standardCoin);
                    BigDecimal beforeUsed = buyRestUsdtC.getUsedPrice();
                    BigDecimal afterUsed = beforeUsed.add(rest);

                    BigDecimal beforeLock = buyRestUsdtC.getLockedPrice();
                    BigDecimal afterLock = beforeLock.subtract(rest);

                    buyRestUsdtC.setUsedPrice(afterUsed)
                            .setLockedPrice(afterLock)
                            .setUpdateUser(SYS_PLATFORM);
                    if (!currencyService.updateById(buyRestUsdtC))
                        throw new UpdateDataException(100);

                    //剩余返还
                    //USDT 流水 可用添加
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.USED, coin, null, null,
                            beforeUsed, afterUsed,
                            buy.getMemberId(), buy.getMemberId());
                    //USDT 冻结 减少
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.LOCKED, coin, null, null,
                            beforeLock, afterLock,
                            buy.getMemberId(), buy.getMemberId());
                }


            }
        }

    }
    //卖单 匹配 买单
    @Transactional(rollbackFor = Exception.class)
    public void sell2(Match sellEV) {


        //  拉取买盘  卖单单价 <= 买单单价 订单
        Match buy = createMatch(sellEV);
        //非配置账号 走原来的逻辑
        if (buy == null) {
            sell(sellEV);
            return;
        }

        //交易对示例：BTC/USDT

        //币种 账户处理
        String coin = sellEV.getSymbols().split("/")[0];

        //交易本位币种 如： BTC/USDT  交易本位币种为:USDT
        String standardCoin = sellEV.getSymbols().split("/")[1];


        //交易对
        String symbols = sellEV.getSymbols();
        //for (Match buy : matchList){//买盘列表
            //Match query = new Match();
            //query.setOrderNo(sellEV.getOrderNo());
            //Match sell = matchService.getOne(new QueryWrapper<>(query));
            Match sell = sellEV;
            //0.卖单剩余量
            BigDecimal sellNewRest = sell.getWill().subtract(sell.getOk());
            //卖单无剩余量，订单结束
            if (sellNewRest.compareTo(BigDecimal.ZERO) <= 0){
                log.info("-- 卖单无剩余量，订单{}结束 --", sell.getMatchId());
                return;
            }

            //1.成交价即卖出价
            BigDecimal okUnit = sell.getUnit();
            //2.买单剩余量
            BigDecimal buyNewRest = buy.getWill().subtract(buy.getOk());
            //3.双方撮合成交数量（BTC)
            BigDecimal okNumber = buyNewRest.compareTo(sellNewRest) > 0 ? sellNewRest : buyNewRest;
            //双方撮合成交总价（USDT)
            BigDecimal okPrice = okNumber.multiply(okUnit);
            //4.订单成交后双方剩余量
            //买单成交后剩余量
            BigDecimal okBuyRest = buyNewRest.subtract(okNumber);
            //卖单成交后剩余量
            BigDecimal okSellRest = sellNewRest.subtract(okNumber);
            //5.买卖订单处理
            //5.1
            //买单消耗量 USDT
            buy.setFinishNumber(buy.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(buy.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM)
            ;

            //手续费扣除（手续费订单全部撮合完成进行扣除）
            BigDecimal feePrice = BigDecimal.ZERO; //买单,扣除BTC
            //扣除手续费 (买单手续费USDT）
            feePrice = okNumber.multiply(new BigDecimal(buy.getRate()));
            //买单有剩余量
            if (okBuyRest.compareTo(BigDecimal.ZERO) > 0){
                buy.setStatus(MatchStatus.PART_MATCH.code);
            } else{ //买单无剩余量，完结，手续费扣除
                buy.setStatus(MatchStatus.FINISH.code);

            }
            //买家流水金额（USDT)  USDT账户
            BigDecimal buyFlowPrice = okPrice;
            //买家流水量（BTC） BTC账户
            BigDecimal buyFlowNumber = okNumber.subtract(feePrice);
            //买单更新
            if (!matchService.updateById(buy))
            {
                log.error("买单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }
            //5.2
            //卖单消耗
            BigDecimal usdtFeeNumber = BigDecimal.ZERO;//卖单,扣除USDT手续费
            sell.setFinishNumber(sell.getFinishNumber().add(okPrice)) //成交总价累计
                    .setOk(sell.getOk().add(okNumber)) //成交量累计
                    .setUpdateUser(SYS_PLATFORM);
            usdtFeeNumber = okPrice.multiply(new BigDecimal(sell.getRate()));
            //卖单有剩余量
            if (okSellRest.compareTo(BigDecimal.ZERO) > 0){
                sell.setStatus(MatchStatus.PART_MATCH.code);
            }else{ //卖单无剩余量，完结，手续费扣除
                sell.setStatus(MatchStatus.FINISH.code);
            }
            //卖家流水金额（USDT)  USDT账户
            BigDecimal sellFlowPrice = okPrice.subtract(usdtFeeNumber);
            //卖家流水量（BTC） BTC账户
            BigDecimal sellFlowNumber = okNumber;
            //卖单更新
            if (!matchService.updateById(sell)){
                log.error("卖单撮合异常，回滚。。。");
                throw new UpdateDataException(100);
            }


            //6.生成成交明细订单
            //将对应订单形成关联（目前仅用于画k线，故不处理）
            MatchDetail matchDetail = new MatchDetail();
            matchDetail.setBuyId(buy.getMemberId())
                    .setBuyOrder(buy.getOrderNo())
                    .setSellId(sell.getMemberId())
                    .setSellOrder(sell.getOrderNo())
                    .setNumber(okPrice) //USDT
                    .setRemark(okNumber.toPlainString()) //到账BTC
                    .setUnit(okUnit)
                    .setSymbols(symbols)
                    .setCreateUser(SYS_PLATFORM);
            matchDetailService.save(matchDetail);

            Date now = new Date();
            long minTimes = DateTimeUtil.minuteTimeNotSec(now);
            redisUtil.lSet(String.format(KINE_MINUTE, symbols, minTimes), matchDetail, KLINE_TIME_OUT);


            //7.用户币币账户资产更新


            //7.1 买家账户更新

            //买家 USDT账户
            Currency buyCurrencyUsdt = F.me().getCurrency(buy.getMemberId(), standardCoin);

            //usdt冻结  变更前
            BigDecimal buyUsdtBeforeLock = buyCurrencyUsdt.getLockedPrice();
            //usdt 变更后
            BigDecimal buyUsdtAfterLock = buyUsdtBeforeLock.subtract(buyFlowPrice);
            buyCurrencyUsdt.setLockedPrice(buyUsdtAfterLock.compareTo(MIN_DATA_USDT) > 0 ? buyUsdtAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyUsdt))
                throw new UpdateDataException(100);
            // 买家 流水生成
            //USDT 流水
            // 冻结扣除
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowPrice, standardCoin, buyFlowPrice, standardCoin, BigDecimal.ZERO, standardCoin,
                    ItemCode.LOCKED, standardCoin, null, null,
                    buyUsdtBeforeLock, buyUsdtAfterLock,
                    buy.getMemberId(), sell.getMemberId());


            //买家 BTC账户
            Currency buyCurrencyBtc = F.me().getCurrency(buy.getMemberId(), coin);
            //btc可用 前
            BigDecimal buyBtcBeforeUsd = buyCurrencyBtc.getUsedPrice();
            BigDecimal buyBtcAfterUsd = buyBtcBeforeUsd.add(buyFlowNumber);

            buyCurrencyBtc.setUsedPrice(buyBtcAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(buyCurrencyBtc))
                throw new UpdateDataException(100);

            //btc 流水 可用添加
            F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    buyFlowNumber, coin, buyFlowNumber, coin,feePrice, coin,
                    ItemCode.USED, coin, null, null,
                    buyBtcBeforeUsd, buyBtcAfterUsd,
                    sell.getMemberId(), buy.getMemberId());


            //7.2 卖家账户更新

            //卖家USDT账户
            Currency sellCUsdt = F.me().getCurrency(sell.getMemberId(), standardCoin);
            //可用  USDT
            BigDecimal sellCUsdtBeforeUsd = sellCUsdt.getUsedPrice();
            BigDecimal sellCUsdtAfterUsd = sellCUsdtBeforeUsd.add(sellFlowPrice);
            sellCUsdt.setUsedPrice(sellCUsdtAfterUsd).setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCUsdt))
                throw new UpdateDataException(100);


            //交易本位账户 账户添加
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowPrice, standardCoin, sellFlowPrice, standardCoin,usdtFeeNumber, standardCoin,
                    ItemCode.USED, standardCoin, null, null,
                    sellCUsdtBeforeUsd, sellCUsdtAfterUsd,
                    buy.getMemberId(), sell.getMemberId());


            //卖家BTC账户
            Currency sellCBtc = F.me().getCurrency(sell.getMemberId(), coin);

            //冻结 BTC 前
            BigDecimal sellBtcBeforeLock = sellCBtc.getLockedPrice();
            //冻结 BTC 后
            BigDecimal sellBtcAfterLock = sellBtcBeforeLock.subtract(sellFlowNumber);

            sellCBtc.setLockedPrice(sellBtcAfterLock.compareTo(MIN_DATA) > 0 ? sellBtcAfterLock : BigDecimal.ZERO)
                    .setUpdateUser(SYS_PLATFORM);
            if (!currencyService.updateById(sellCBtc))
                throw new UpdateDataException(100);


            // 卖家 流水生成
            //冻结流出 交易币种冻结扣除
            F.me().saveCashflow(sell.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                    sellFlowNumber, coin, sellFlowNumber, coin, BigDecimal.ZERO, coin,
                    ItemCode.LOCKED, coin, null, null,
                    sellBtcBeforeLock, sellBtcAfterLock,
                    sell.getMemberId(), buy.getMemberId());


            //8.当订单委托量完成，进行剩余量结算

            //8.1 买单 委托量完成，剩余USDT返还
            //订单完成了就跳出循环
            if (StringUtils.equals(buy.getStatus(), MatchStatus.FINISH.code))
            {
                //订单委托量成交完成后剩余USDT
                BigDecimal rest = buy.getTotalPrice().subtract(buy.getFinishNumber());

                if(rest.compareTo(BigDecimal.ZERO)>0)
                {
                    Currency buyRestUsdtC = F.me().getCurrency(buy.getMemberId(), standardCoin);
                    BigDecimal beforeUsed = buyRestUsdtC.getUsedPrice();
                    BigDecimal afterUsed = beforeUsed.add(rest);

                    BigDecimal beforeLock = buyRestUsdtC.getLockedPrice();
                    BigDecimal afterLock = beforeLock.subtract(rest);

                    buyRestUsdtC.setUsedPrice(afterUsed)
                            .setLockedPrice(afterLock)
                            .setUpdateUser(SYS_PLATFORM);
                    if (!currencyService.updateById(buyRestUsdtC))
                        throw new UpdateDataException(100);

                    //剩余返还
                    //USDT 流水 可用添加
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.USED, coin, null, null,
                            beforeUsed, afterUsed,
                            buy.getMemberId(), buy.getMemberId());
                    //USDT 冻结 减少
                    F.me().saveCashflow(buy.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST_REST,
                            rest, coin, rest, coin, BigDecimal.ZERO, coin,
                            ItemCode.LOCKED, coin, null, null,
                            beforeLock, afterLock,
                            buy.getMemberId(), buy.getMemberId());
                }


            }
        //}

    }

    /**
     * 机器人自动生成一个单子
     * @param match
     * @return
     */
    Match createMatch(Match match){
        QueryWrapper<RobotConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("symbol",match.getSymbols());
        //被后台照顾的MemberId
        wrapper.eq("user_id",match.getMemberId());
        wrapper.eq("status","0");
        wrapper.eq("trade_mode","1");
        RobotConfig one = robotConfigService.getOne(wrapper);
        //是配置的账号就撮合，没有配置的账号

        if(one ==null){//检查有没有 -1 全站撮合的
            QueryWrapper<RobotConfig> wrapper2 = new QueryWrapper<>();
            //wrapper2.eq("symbol",match.getSymbols());
            //被后台照顾的MemberId
            wrapper2.eq("user_id","-1");
            wrapper2.eq("status","0");
            wrapper2.eq("trade_mode","1");
            one = robotConfigService.getOne(wrapper2);
            if(one ==null){
                return  null;
            }
        }
        BigDecimal buyRest = match.getWill().subtract(match.getOk());
        BigDecimal maxValuee = one.getMaxValuee();
        if(buyRest.compareTo(maxValuee)>0){
            return null;
        }

        Match match1 = new Match();

        BeanUtil.copyProperties(match, match1);
        String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("M");
        //账号由后台控制，再加
        match1.setMemberId(one.getRobotUserId());
        match1.setCreateUser(one.getRobotUserId());
        if(match.getMatchType().equals(MatchType.SELL.code)){
            match1.setMatchType(MatchType.BUY.code);
        }
        if(match.getMatchType().equals(MatchType.BUY.code)){
            match1.setMatchType(MatchType.SELL.code);
        }


        //市价交易，市价交易，查询盘口，盘口有这个价格，才使用系统撮合
        //否则，等待买家撮合查询盘口，盘口有这个价格，才使用系统撮合，否则，等待买家撮合
        if (StrUtil.equals(match.getDealWay(), DealWay.LIMIT.code)){//市价，拉取最新价
            Spot spot = new Spot();
            String symbols = match.getSymbols();
            spot.setSymbol(symbols);
            spot = spotService.getOne(new QueryWrapper<>(spot));
            Depth o = null;
            //查询盘口
            if(("0").equals(spot.getType())){
                o= (Depth) redisUtil.get(DEPTH + symbols+ JIA);
            }else{
                o= (Depth) redisUtil.get(DEPTH + symbols);
            }

            List<List<BigDecimal>> asks =(List<List<BigDecimal>>)o.getAsks();
            List<List<BigDecimal>> bids =(List<List<BigDecimal>>)o.getBids();
            //找出最大，最小，小于最大，大于最小就撮合
            BigDecimal max = BigDecimal.ZERO;
            BigDecimal min = BigDecimal.ZERO;
            if(match.getMatchType().equals(MatchType.SELL.code)){//卖

                for (List<BigDecimal> ask : asks) {
                    BigDecimal b = ask.get(0);
                    //for (BigDecimal b : ask) {
                    if(b.compareTo(max)>0 || max.compareTo(BigDecimal.ZERO)==0) max =b;
                    if(min.compareTo(b)>0 || min.compareTo(BigDecimal.ZERO)==0) min = b;
                    //}

                }
            }
            if(match.getMatchType().equals(MatchType.BUY.code)){//买
                for (List<BigDecimal> bid : bids) {
                    BigDecimal b = bid.get(0);
                    //for (BigDecimal b : bid) {
                    if(b.compareTo(max)>0|| max.compareTo(BigDecimal.ZERO)==0) max =b;
                    if(min.compareTo(b)>0|| min.compareTo(BigDecimal.ZERO)==0) min = b;
                    //}

                }
            }
            //如果 单价<最小价格 或者 单价>最大价格 不需要往下执行了
            if(match.getUnit().compareTo(min)<0 || match.getUnit().compareTo(max)>0){
                return null;
            }
        }


        match1.setOrderNo(orderNo);
        match1.setStatus(MatchStatus.PAID.code);


        // if(match.getMemberId().equals(one.getUserId())){
        matchService.save(match1);
        return match1;
        //}

        //return null;
    }



//    public void mdData1hour()
//    {
//        //生成1hour
//        synchronized (this)
//        {
//            for (PlatCoinType platCoinType : PlatCoinType.values())
//            {
//                long start = System.currentTimeMillis();
//                String key = KINE + platCoinType.code + "_" + PeriodType.min_60.code;
//                String symbols = platCoinType.code;
//                Object obj = redisUtil.get(key);
//
//                if (obj == null)
//                {
//                    //为空,生成初始线
//                    List<Kline> list = new ArrayList<>();
//                    Kline entity = Kline.init(symbols);
//                    list.add(entity);
//                    redisUtil.set(key, list);
//                    return;
//                }
//
//                List<Kline> list = (List<Kline>) obj;
//                //list大小
//                if (list.size() == 0)
//                {
//                    Kline entity = Kline.init(symbols);
//                    list.add(entity);
//                    redisUtil.set(key, list);
//                    return;
//                }
//
//
//                SimpleDateFormat min = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
//
//                Date now = new Date();
//                //获取当前时间 的小时数
//                long nowStart = DateTimeUtil.strToDate(min.format(now)).getTime() / 1000;
//
//                long nowEnd = now.getTime() / 1000;
//
//                List<MatchDetail> todayList = matchDetailService.getBaseMapper().getMatchRangeTime(symbols, nowStart, nowEnd);
//
//                log.info("1hour k线 待刷新数据大小:{}", todayList.size());
//                if (todayList.size() == 0)
//                    return;
//
//
//                Kline kline = new Kline();
//                MatchDetail openMatch = todayList.get(0);//开盘
//                MatchDetail closeMatch = todayList.get(todayList.size() - 1);//收盘
//
//                MatchDetail minMatch = todayList.stream().min(Comparator.comparing(MatchDetail::getUnit)).get();
//                MatchDetail maxMatch = todayList.stream().max(Comparator.comparing(MatchDetail::getUnit)).get();
//                kline.setSymbol(symbols)
//                        .setOpen(openMatch.getUnit().floatValue())
//                        .setClose(closeMatch.getUnit().floatValue())
//                        .setLow(minMatch.getUnit().floatValue())
//                        .setHigh(maxMatch.getUnit().floatValue());
//                float amount = 0F;
//
//                for (int i = 0; i < todayList.size(); i++)
//                {
//                    //计算交易量
//                    MatchDetail entity = todayList.get(i);
//                    amount += entity.getNumber().floatValue();
//                }
//
//                kline.setId(nowStart)
//                        .setAmount(amount);
//                Collections.reverse(list);
//                if (list.get(list.size() - 1).getId() == nowStart)
//                {
//                    list.set(list.size() - 1, kline);
//                }
//                else
//                {
//                    list.add(kline);
//                }
//                Collections.reverse(list);
//
//                redisUtil.set(key, list);
//                log.info(">>> 1hour刷新 {}ms,", System.currentTimeMillis() - start);
//            }
//        }
//
//
//    }
//
//    public void test()
//    {
//
//
//        CountDownLatch latch = new CountDownLatch(4);
//
//
//        ThreadPoolUtil.execute(() ->
//        {
//            mdData1hour();
//            latch.countDown();
//        });
//
//        ThreadPoolUtil.execute(() ->
//        {
//            mdData4hour();
//            latch.countDown();
//        });
//
//
//        ThreadPoolUtil.execute(() ->
//        {
//            mdDay();
//            latch.countDown();
//        });
//
//        ThreadPoolUtil.execute(() ->
//        {
//            mdDepth(PlatCoinType.MGE_USDT);
//            latch.countDown();
//        });
//
//
//        try
//        {
//            latch.await();
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//
//
//    }

//
//    //日线k线画图
//    public void mdDay()
//    {
//        synchronized (this)
//        {
//            for (PlatCoinType platCoinType : PlatCoinType.values())
//            {
//                long start = System.currentTimeMillis();
//
//                String key = KINE + platCoinType.code + "_" + PeriodType.day_1.code;
//
//                String symbols = platCoinType.code;
//                Object obj = redisUtil.get(key);
//
//                if (obj == null)
//                {
//                    //为空,生成初始日线k线画图
//                    List<Kline> list = new ArrayList<>();
//                    Kline entity = Kline.init(symbols);
//                    list.add(entity);
//                    redisUtil.set(key, list);
//                    return;
//                }
//
//                List<Kline> list = (List<Kline>) obj;
//                //list大小
//                if (list.size() == 0)
//                {
//                    Kline entity = Kline.init(symbols);
//                    list.add(entity);
//                    redisUtil.set(key, list);
//                    return;
//                }
//
//                list = (List<Kline>) obj;
//                //获取今日的k线数据
//                List<MatchDetail> todayList = matchDetailService.getBaseMapper().getMatchList(symbols);
//                log.info("日 k线 待刷新数据大小:{}", todayList.size());
//                if (todayList.size() == 0)
//                    return;
//
//                Kline kline = new Kline();
//                MatchDetail openMatch = todayList.get(0);//开盘
//                MatchDetail closeMatch = todayList.get(todayList.size() - 1);//收盘
//
//                MatchDetail minMatch = todayList.stream().min(Comparator.comparing(MatchDetail::getUnit)).get();
//                MatchDetail maxMatch = todayList.stream().max(Comparator.comparing(MatchDetail::getUnit)).get();
//                kline.setSymbol(symbols)
//                        .setOpen(openMatch.getUnit().floatValue())
//                        .setClose(closeMatch.getUnit().floatValue())
//                        .setLow(minMatch.getUnit().floatValue())
//                        .setHigh(maxMatch.getUnit().floatValue());
//                float amount = 0F;
//
//                for (int i = 0; i < todayList.size(); i++)
//                {
//                    //计算交易量
//                    MatchDetail entity = todayList.get(i);
//                    if(entity.getRemark()!=null)
//                    {
//                        amount += Float.parseFloat(entity.getRemark());
//                    }
//
//                }
//                SimpleDateFormat min = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//                long now = DateTimeUtil.strToDate(min.format(new Date())).getTime() / 1000;
//
//                kline.setId(now)
//                        .setAmount(amount);
//                Collections.reverse(list);
//                if (list.get(list.size() - 1).getId() == now)
//                {
//                    list.set(list.size() - 1, kline);
//                }
//                else
//                {
//                    list.add(kline);
//                }
//                Collections.reverse(list);
//
//                redisUtil.set(key, list);
//
//                log.info(">>> 日线刷新 {}ms", System.currentTimeMillis() - start);
//            }
//        }
//
//    }
//
//    public void mdData4hour()
//    {
//        //生成4hour
//
//        for (PlatCoinType platCoinType : PlatCoinType.values())
//        {
//            long start = System.currentTimeMillis();
//            String key = KINE + platCoinType.code + "_" + PeriodType.hour_4.code;
//            String symbols = platCoinType.code;
//            Object obj = redisUtil.get(key);
//
//            if (obj == null)
//            {
//                //为空,生成初始线
//                List<Kline> list = new ArrayList<>();
//                Kline entity = Kline.init(symbols);
//                list.add(entity);
//                redisUtil.set(key, list);
//                return;
//            }
//
//            List<Kline> list = (List<Kline>) obj;
//            //list大小
//            if (list.size() == 0)
//            {
//                Kline entity = Kline.init(symbols);
//                list.add(entity);
//                redisUtil.set(key, list);
//                return;
//            }
//
//
//            SimpleDateFormat min = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
//
//            Date now = new Date();
//            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//            int value = currentHour % 4;
//            int startHour = 0;
//            int endHour = 0;
//            switch (value)
//            {
//                case 0:
//                    startHour = -4;
//
//                    break;//整4小时
//                case 1:
//                    startHour = -1;
//                    break;
//                case 2:
//                    startHour = -2;
//                    break;
//                case 3:
//                    startHour = -3;
//                    break;
//
//            }
//
//            //获取起始时间点
//            //获取当前时间 的小时数
//            long nowStart = DateTimeUtil.addTime(DateTimeUtil.strToDate(min.format(now)), startHour).getTime() / 1000;
//
//            long nowEnd = now.getTime() / 1000;
//
////            //获取当前时间 的小时数
////            long nowStart = DateTimeUtil.strToDate(min.format(now)).getTime() / 1000;
////
////            long nowEnd = DateTimeUtil.addTime(DateTimeUtil.strToDate(min.format(now)), startHour).getTime() / 1000;
//
//            List<MatchDetail> todayList = matchDetailService.getBaseMapper().getMatchRangeTime(symbols, nowStart, nowEnd);
//            log.info("4hour k线 待刷新数据大小:{}", todayList.size());
//            if (todayList.size() == 0)
//                return;
//
//            Kline kline = new Kline();
//            MatchDetail openMatch = todayList.get(0);//开盘
//            MatchDetail closeMatch = todayList.get(todayList.size() - 1);//收盘
//
//            MatchDetail minMatch = todayList.stream().min(Comparator.comparing(MatchDetail::getUnit)).get();
//            MatchDetail maxMatch = todayList.stream().max(Comparator.comparing(MatchDetail::getUnit)).get();
//            kline.setSymbol(symbols)
//                    .setOpen(openMatch.getUnit().floatValue())
//                    .setClose(closeMatch.getUnit().floatValue())
//                    .setLow(minMatch.getUnit().floatValue())
//                    .setHigh(maxMatch.getUnit().floatValue());
//            float amount = 0F;
//
//            for (int i = 0; i < todayList.size(); i++)
//            {
//                //计算交易量
//                MatchDetail entity = todayList.get(i);
//                amount += entity.getNumber().floatValue();
//            }
//
//            kline.setId(nowStart)
//                    .setAmount(amount);
//            Collections.reverse(list);
//            if (list.get(list.size() - 1).getId() == nowStart)
//            {
//                list.set(list.size() - 1, kline);
//            }
//            else
//            {
//                list.add(kline);
//            }
//            Collections.reverse(list);
//
//            redisUtil.set(key, list);
//            log.info(">>> 4hour刷新 {}ms ", System.currentTimeMillis() - start);
//        }
//
//    }
//
//    /**
//     * 代币深度数据生成
//     */
//    public void mdDepth(PlatCoinType coinType)
//    {
//
////        for (PlatCoinType coinType : PlatCoinType.values())
////        {
//        long start = System.currentTimeMillis();
//        Depth depth = new Depth();
//        String symbols = coinType.code;
//        String key = DEPTH + symbols;
//        //最近20条
//
//        //买入委托
//        Match buyQuery = new Match();
//        buyQuery.setMatchType(MatchType.BUY.code)
//                .setSymbols(symbols)
//                .setDel("N")
//        ;
//        List<Match> buyList = matchService.list(new QueryWrapper<>(buyQuery)
//                        .in("status", MatchStatus.PAID.code, MatchStatus.PART_MATCH.code)
//                        .orderByDesc(CREATE_TIME)
////                    .last("limit 20")
//        );
//
//        //买入委托
//        Match sellQuery = new Match();
//        sellQuery.setMatchType(MatchType.SELL.code)
//                .setSymbols(symbols)
//                .setDel("N")
//        ;
//        List<Match> sellList = matchService.list(new QueryWrapper<>(sellQuery)
//                        .in("status", MatchStatus.PAID.code, MatchStatus.PART_MATCH.code)
//                        .orderByDesc(CREATE_TIME)
////                    .last("limit 20")
//        );
//
//        //当前的所有买单 bids [price, quote volume]
//        List<List<BigDecimal>> bids = new ArrayList<>();
//
//        Map<BigDecimal, BigDecimal> buyMap = new HashedMap();
//        Set<BigDecimal> buySet = new HashSet<>();
//        //同单价数据合并
//        for (Match match : buyList)
//        {
//            //获取买单撮合成交量
//            List<MatchDetail> matchDetailList = matchDetailService.getBaseMapper().getMatchListByOrderNo(match.getOrderNo());
//
//            BigDecimal finishB=BigDecimal.ZERO;
//            for (MatchDetail matchDetail : matchDetailList)
//            {
//                finishB=finishB.add(new BigDecimal(matchDetail.getRemark()));
//            }
//            BigDecimal rest=match.getNumber().subtract(finishB);
//            if (buyMap.get(match.getUnit()) == null)
//            {
//                buyMap.put(match.getUnit(),rest);
//            }
//            else
//            {
//                BigDecimal total = buyMap.get(match.getUnit());
//                buyMap.put(match.getUnit(), total.add(rest));
//            }
//            buySet.add(match.getUnit());
//        }
//
//        for (BigDecimal unit : buySet)
//        {
//            List<BigDecimal> entity = new ArrayList<>();
//            entity.add(unit);
//            entity.add(buyMap.get(unit));
//            bids.add(entity);
//        }
//
//        //当前的所有卖单 asks [price, quote volume]
//        List<List<BigDecimal>> asks = new ArrayList<>();
//
//
//        Map<BigDecimal, BigDecimal> sellMap = new HashedMap();
//        Set<BigDecimal> sellSet = new HashSet<>();
//        //同单价数据合并
//        for (Match match : sellList)
//        {
//            if (sellMap.get(match.getUnit()) == null)
//            {
//                sellMap.put(match.getUnit(), match.getUnFinishNumebr());
//            }
//            else
//            {
//                BigDecimal total = sellMap.get(match.getUnit());
//                sellMap.put(match.getUnit(), total.add(match.getUnFinishNumebr()));
//            }
//            sellSet.add(match.getUnit());
//        }
//
//        for (BigDecimal unit : sellSet)
//        {
//            List<BigDecimal> entity = new ArrayList<>();
//            entity.add(unit);
//            entity.add(sellMap.get(unit));
//            asks.add(entity);
//        }
//        depth.setAsks(asks);
//        depth.setBids(bids);
//        //卖出委托
//        Integer version;
//        Depth old = (Depth) redisUtil.get(key);
//        if (old == null)
//        {
//            old = new Depth();
//            old.setVersion(0);
//        }
//        depth.setVersion(old.getVersion() + 1);
//        depth.setTs(Integer.parseInt(System.currentTimeMillis() / 1000 + ""));
//        redisUtil.set(key, depth);
//        log.info(">>>深度数据详情>>>> ");
//        log.info(">>>: {} ", depth.toString());
//        log.info(">>> 深度刷新 {}ms", System.currentTimeMillis() - start);
////        }
//    }


}
