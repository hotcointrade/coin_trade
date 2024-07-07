package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.app.dto.e.ApiCurrencyDto;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.e.entity.Match;
import cn.stylefeng.guns.modular.e.service.MatchService;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.service.CurrencyService;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.search.parser.MValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Future;

@Service
@Slf4j
public class AsyncService extends Constant {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private Gson gson;

    @Autowired
    SystemMatchService systemMatchService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    MatchService matchService;


    /**
     * 币币交易  V2 优化版
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Async("currencyTask")
    public Future<Result> currencyV2(String token, ApiCurrencyDto dto) throws Exception {
        Member member = (Member) redisUtil.get(token);
        String feeType = dto.getSymbols().split("/")[0];
        //流水币种
        String coin;
        //用户币币账户
        Currency currency;
        //买单 USDT->
        if (dto.getMatchType().equals(MatchType.BUY.code)) {
            coin = dto.getSymbols().split("/")[1];
            currency = F.me().getCurrency(member.getMemberId(), coin); //USDT账户
        } else {
            coin = dto.getSymbols().split("/")[0];
            currency = F.me().getCurrency(member.getMemberId(), coin);//卖单 对应账户
            feeType = dto.getSymbols().split("/")[1];
        }

        //市价、限价
        BigDecimal unit = dto.getUnit();
        if (StrUtil.equals(dto.getDealWay(), DealWay.MARKET.code)) {//市价，拉取最新价
            unit = getClosePrice(dto.getSymbols(), KINE);
        }

        if (unit.compareTo(BigDecimal.ZERO) <= 0)
//            return fail(ApiStatus.MANY_OP);
            return new AsyncResult<>(fail(ApiStatus.MANY_OP));

        //手续费比例
        Spot spot = F.me().getSpot(dto.getSymbols());
        if (spot == null) {
            log.info("现货交易对{}不存在", dto.getSymbols());
            return new AsyncResult<>(fail(ApiStatus.BAD_REQUEST));
        }

        if (spot.getMinBuyNumber() != null && spot.getMinBuyNumber().compareTo(dto.getNumber()) > 0) {
            return new AsyncResult<>(fail(ApiStatus.MIN_BUY_NUMBER));
        }

        //例如：BTC/USDT
        // 买单-手续费计算的拉取BTC扣除 比例
        // 卖单-手续费计算的拉取USDT扣除 比例
        BigDecimal rate = spot.getSpotFee().divide(new BigDecimal(100), 8, RoundingMode.DOWN);
        //余额扣除
        BigDecimal flowPrice;
        //手续费
        BigDecimal fee;
        //手续费量（BTC）
        BigDecimal feeNumber;
        //买 :买入btc数量
        if (dto.getMatchType().equals(MatchType.BUY.code)) {
            //扣除USDT余额 （做转换，得到实际扣除USDT量）
            flowPrice = dto.getNumber().multiply(unit);
            //手续费 （转换，得到实际扣除USDT量手续费）
            fee = dto.getNumber().multiply(rate).multiply(unit);
            feeNumber = dto.getNumber().multiply(rate);
        } else {
            //卖：卖出BTC数量
            //扣除对应币种账户 （BTC数量）
            flowPrice = dto.getNumber();
            //手续费量（BTC数量）
            feeNumber = dto.getNumber().multiply(rate);
            rate = spot.getUsdtSpotFee().divide(new BigDecimal(100), 8, RoundingMode.DOWN);
            //手续费 （USDT）
            fee = dto.getNumber().multiply(rate).multiply(unit);
        }

        //可用<交易量
        if (currency.getUsedPrice().compareTo(flowPrice) < 0)
//            return fail(ApiStatus.WALLET_LESS);
            return new AsyncResult<>(fail(ApiStatus.WALLET_LESS));

        //买-USDT账户
        //卖-BTC账户
        BigDecimal beforeUsed = currency.getUsedPrice();
        BigDecimal afterUsed = currency.getUsedPrice().subtract(flowPrice);

        BigDecimal beforeLocked = currency.getLockedPrice();
        BigDecimal afterLocked = currency.getLockedPrice().add(flowPrice);


        //钱包更新
        currency.setUsedPrice(afterUsed)
                .setLockedPrice(afterLocked)
                .setUpdateUser(member.getMemberId());

        int rows = currencyService.updateWallet(currency);
        if (rows <= 0) {
            log.error("币币交易：币币钱包更新异常！！ {}");
//            return fail(ApiStatus.MANY_OP);
            return new AsyncResult<>(fail(ApiStatus.MANY_OP));
        }

        //生成委托单
        Match match = new Match();
        String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("M");
        BeanUtil.copyProperties(dto, match);
        match.setMemberId(member.getMemberId())
                .setOrderNo(orderNo)
                .setRate(rate + "")
                .setFee(fee)  //折合USDT （买入扣除USDT、卖出扣除BTC）
                .setFinishNumber(BigDecimal.ZERO) //已成交总价
                .setConvertNumber(BigDecimal.ZERO) //撮合到账数量
                .setTotalPrice(flowPrice)
                .setWill(dto.getNumber())
                .setActual(dto.getNumber().subtract(feeNumber))
                .setFeeNumber(feeNumber)
                .setOk(BigDecimal.ZERO) //成交量
                .setVersion(0L)
                .setStatus(MatchStatus.PAID.code)
                .setCreateUser(member.getMemberId());

        boolean isOk = matchService.save(match);
        if (!isOk)
            throw new UpdateDataException(100);
        //流水记录
        //可用 流出
        F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                flowPrice, coin, flowPrice, coin, fee, coin,
                ItemCode.USED, coin, null, null,
                beforeUsed, afterUsed,
                member.getMemberId(), member.getMemberId());
        //冻结 流入
        F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
                ItemCode.LOCKED, coin, null, null,
                beforeLocked, afterLocked,
                member.getMemberId(), member.getMemberId());

        String lockKey = "MATCH";
        //撮合锁
        redisUtil.lock(lockKey, 1, 5);
        //币币撮合
        systemMatchService.matchOrder(match);
        redisUtil.unLock(lockKey);



        //刷新行情及所有信息
//        systemMatchService.test();
//        return success(ApiStatus.OK_CURRENCY);
        // redisUtil.publish("index","MGE/USDT");


        if ("0".equals(spot.getType())) {
            redisUtil.publish("index", dto.getSymbols());
            redisUtil.publish("robotMatchSpot", dto.getSymbols());
            redisUtil.publish("robotMatch", match.getMatchType() + "," + match.getSymbols() + "," + match.getUnit().toString() + "," + match.getOrderNo());
        }

        return new AsyncResult<>(success(ApiStatus.OK_CURRENCY));

    }

    //private String getMatchString(Match match){
    //    StringBuffer stringBuffer = new StringBuffer();
    //    stringBuffer.append(match.getMatchType())
    //            .append(",")
    //            .append(match.getSymbols())
    //            .append(",")
    //    return match.getMatchType()+","+match.getSymbols()+","+match.getUnit().toString()+","+match.getOrderNo()
    //}


    /**
     * 币币交易  V2 优化版
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Async("currencyTask")
    public Future<Result> currencySysRobot(String token, ApiCurrencyDto dto) throws Exception {
        //用户信息
        Member member = (Member) redisUtil.get(token);
        String feeType = dto.getSymbols().split("/")[0];
        //流水币种
        String coin;
        //用户币币账户
        Currency currency;
        //买单 USDT->
        if (dto.getMatchType().equals(MatchType.BUY.code)) {
            coin = dto.getSymbols().split("/")[1];
            currency = F.me().getCurrency(member.getMemberId(), coin); //USDT账户
        } else {
            coin = dto.getSymbols().split("/")[0];
            currency = F.me().getCurrency(member.getMemberId(), coin);//卖单 对应账户
            feeType = dto.getSymbols().split("/")[1];
        }

        //市价、限价
        BigDecimal unit = dto.getUnit();
        if (StrUtil.equals(dto.getDealWay(), DealWay.MARKET.code)) {//市价，拉取最新价
            unit = getClosePrice(dto.getSymbols(), KINE);
        }

        if (unit.compareTo(BigDecimal.ZERO) <= 0)
//            return fail(ApiStatus.MANY_OP);
            return new AsyncResult<>(fail(ApiStatus.MANY_OP));

        //手续费比例
        Spot spot = F.me().getSpot(dto.getSymbols());
        if (spot == null) {
            log.info("现货交易对{}不存在", dto.getSymbols());
            return new AsyncResult<>(fail(ApiStatus.BAD_REQUEST));
        }

        if (spot.getMinBuyNumber() != null && spot.getMinBuyNumber().compareTo(dto.getNumber()) > 0) {
            return new AsyncResult<>(fail(ApiStatus.MIN_BUY_NUMBER));
        }

        //例如：BTC/USDT
        // 买单-手续费计算的拉取BTC扣除 比例
        // 卖单-手续费计算拉取USDT扣除 比例
        BigDecimal rate = spot.getSpotFee().divide(new BigDecimal(100), 8, RoundingMode.DOWN);
        //余额扣除
        BigDecimal flowPrice;
        //手续费
        BigDecimal fee;
        //手续费量（BTC）
        BigDecimal feeNumber;
        //买 :买入btc数量
        if (dto.getMatchType().equals(MatchType.BUY.code)) {
            //扣除USDT余额 （做转换，得到实际扣除USDT量）
            flowPrice = dto.getNumber().multiply(unit);
            //手续费 （转换，得到实际扣除USDT量手续费）
            fee = dto.getNumber().multiply(rate).multiply(unit);
            feeNumber = dto.getNumber().multiply(rate);

        } else {
            //卖：卖出BTC数量
            //扣除对应币种账户 （BTC数量）
            flowPrice = dto.getNumber();
            //手续费量（BTC数量）
            feeNumber = dto.getNumber().multiply(rate);
            rate = spot.getUsdtSpotFee().divide(new BigDecimal(100), 8, RoundingMode.DOWN);
            //手续费 （USDT）
            fee = dto.getNumber().multiply(rate).multiply(unit);
        }

        //可用<交易量
        if (currency.getUsedPrice().compareTo(flowPrice) < 0)
//            return fail(ApiStatus.WALLET_LESS);
            return new AsyncResult<>(fail(ApiStatus.WALLET_LESS));

        //买-USDT账户
        //卖-BTC账户
        BigDecimal beforeUsed = currency.getUsedPrice();
        BigDecimal afterUsed = currency.getUsedPrice().subtract(flowPrice);

        BigDecimal beforeLocked = currency.getLockedPrice();
        BigDecimal afterLocked = currency.getLockedPrice().add(flowPrice);


        //钱包更新
        currency.setUsedPrice(afterUsed)
                .setLockedPrice(afterLocked)
                .setUpdateUser(member.getMemberId());

        int rows = currencyService.updateWallet(currency);
        if (rows <= 0) {
            log.error("币币交易：币币钱包更新异常！！ {}");
//            return fail(ApiStatus.MANY_OP);
            return new AsyncResult<>(fail(ApiStatus.MANY_OP));
        }

        //生成委托单
        Match match = new Match();
        String orderNo = cn.stylefeng.guns.modular.base.util.RandomUtil.code("M");
        BeanUtil.copyProperties(dto, match);
        match.setMemberId(member.getMemberId())
                .setOrderNo(orderNo)
                .setRate(rate + "")
                .setFee(fee)  //折合USDT （买入扣除USDT、卖出扣除BTC）
                .setFinishNumber(BigDecimal.ZERO) //已成交总价
                .setConvertNumber(BigDecimal.ZERO) //撮合到账数量
                .setTotalPrice(flowPrice)
                .setWill(dto.getNumber())
                .setActual(dto.getNumber().subtract(feeNumber))
                .setFeeNumber(feeNumber)
                .setOk(BigDecimal.ZERO) //成交量
                .setVersion(0L)
                .setStatus(MatchStatus.PAID.code)
                .setCreateUser(member.getMemberId());

        boolean isOk = matchService.save(match);
        if (!isOk)
            throw new UpdateDataException(100);
        //流水记录
        //可用 流出
        F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.CURRENCY_ENTRUST,
                flowPrice, coin, flowPrice, coin, fee, coin,
                ItemCode.USED, coin, null, null,
                beforeUsed, afterUsed,
                member.getMemberId(), member.getMemberId());
        //冻结 流入
        F.me().saveCashflow(member.getMemberId(), WalletBigType.CURRENCY, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.CURRENCY_ENTRUST,
                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
                ItemCode.LOCKED, coin, null, null,
                beforeLocked, afterLocked,
                member.getMemberId(), member.getMemberId());

        String lockKey = "MATCH";
        //撮合锁
        redisUtil.lock(lockKey, 1, 5);
        //TODO:币币撮合 需要使用redis
        systemMatchService.matchOrder(match);

        //刷新行情及所有信息
//        systemMatchService.test();
//        return success(ApiStatus.OK_CURRENCY);
        // redisUtil.publish("index","MGE/USDT");

        redisUtil.unLock(lockKey);
        if ("0".equals(spot.getType())) {
            //测试位置
            redisUtil.publish("robotMatch", JSON.toJSON(match));
            redisUtil.publish("index", dto.getSymbols());
            redisUtil.publish("robotMatchSpot", dto.getSymbols());
        }
        return new AsyncResult<>(success(ApiStatus.OK_CURRENCY));

    }

    /**
     * 获取币种最新价
     *
     * @param symbols
     * @param type    现货:KINE 期货-永续:KINE_PERPETUAL
     * @return
     */
    private BigDecimal getClosePrice(String symbols, String type) {
        Kline map = redisUtil.getBean(type + symbols + _NEW, Kline.class);
        return map == null ? null : new BigDecimal(map.getClose());
    }

}
