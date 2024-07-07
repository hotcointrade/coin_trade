package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.stylefeng.guns.core.bipay.entity.Address;
import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.controller.market.*;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.entity.MemberRechargeAddress;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.bulletin.service.SendSMSExtService;
import cn.stylefeng.guns.modular.coin.entity.Futures;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import cn.stylefeng.guns.modular.coin.service.FuturesService;
import cn.stylefeng.guns.modular.coin.service.SpotService;
import cn.stylefeng.guns.modular.coin.service.SwapService;
import cn.stylefeng.guns.modular.com.service.SymbolService;
import cn.stylefeng.guns.modular.e.entity.*;
import cn.stylefeng.guns.modular.e.service.*;
import cn.stylefeng.guns.modular.fin.entity.*;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.service.*;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.entity.MiningOrderDetail;
import cn.stylefeng.guns.modular.mining.service.MiningOrderDetailService;
import cn.stylefeng.guns.modular.mining.service.MiningOrderService;
import cn.stylefeng.guns.modular.otc.entity.Bill;
import cn.stylefeng.guns.modular.otc.service.BillService;
import cn.stylefeng.guns.modular.otc.service.BuyService;
import cn.stylefeng.guns.modular.otc.service.SellService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 定时任务服务类
 */
@Service
public class JobService extends Constant {
    private Logger log = LoggerFactory.getLogger(JobService.class);


    private static final String LOCK_COIN_SYMBOL = "MGE/USDT";

//    @Autowired
//    HuoBiService huoBiService;
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SymbolService symbolService;

    @Autowired
    MatchService matchService;
    @Autowired
    CurrencyService currencyService;

    @Autowired
    ContractService contractService;


    @Autowired
    private FinMiningService finMiningService;
    @Autowired
    private MiningOrderService miningOrderService;
    @Autowired
    private MiningOrderDetailService miningOrderDetailService;
    @Autowired
    CompactService compactService;
    @Autowired
    FinFuturesService finFuturesService;
    @Autowired
    FuturesCompactService futuresCompactService;
    @Autowired
    FuturesLeverageService futuresLeverageService;

    @Autowired
    QuotesService quotesService;

    @Autowired
    MemberService memberService;


    @Resource
    CashflowService cashflowService;
    @Resource
    BillService billService;

    @Resource
    OtcService otcService;
    @Resource
    SendSMSExtService sendSMSExtService;

    @Resource
    SellService sellService;

    @Resource
    BuyService buyService;
    @Resource
    LegalService legalService;

    @Resource
    MatchDetailService matchDetailService;

    @Resource
    WalletService walletService;

    @Resource
    LockRecordService lockRecordService;

    @Resource
    LockAutoService lockAutoService;
    @Resource
    LockService lockService;
    @Resource
    LockProfitService lockProfitService;

    @Resource
    GasService gasService;

    @Resource
    private SwapService swapService;

    @Resource
    private FuturesService futuresService;

    @Resource
    private SpotService spotService;

    protected Random rand = new Random();
    @Resource
    private MemberRechargeAddressService memberRechargeAddressService;


    @Resource
    BiPayService biPayService;

    int number = 0;
    int count = 0;

    @Transactional(rollbackFor = Exception.class)
    public void timeOutOrder() {
        if (number++ > 1) {
            if (count++ > 3)
                number = 0;
            return;
        }
        number = 0;
        // log.info("> timeout order");
        //TIME_OUT
        int timeOut = Integer.valueOf(F.me().cfg("TIME_OUT"));
        Bill query = new Bill();
        query.setDel("N")
                .setStatus(BillStatus.WAIT.code)
        ;
        List<Bill> billList = billService.list(new QueryWrapper<>(query));

        for (Bill bill : billList) {
            long now = System.currentTimeMillis();
            long time = bill.getCreateTime().getTime() + timeOut * 60 * 1000;
            if (now <= time)
                continue;
            bill.setStatus(BillStatus.CANCEL.code)
                    .setCancelTime(new Date())
                    .setUpdateUser(SYS_PLATFORM);
            if (!billService.updateById(bill))
                throw new UpdateDataException(100);
            otcService.cancelItem(F.me().getMember(bill.getBuyMid()), bill);

            String smsTimeout = F.me().cfg("SMS_TIMEOUT");

            if (StrUtil.isNotBlank(smsTimeout) && otcService.smsIsOpen()) {
                Member sellMember = F.me().getMember(bill.getBuyMid());
                if (sellMember == null)
                    throw new UpdateDataException(100);
                //超时提醒
                String content = String.format(smsTimeout, bill.getOrderNo());
                sendSMSExtService.sendSms(content, sellMember.getAccount(), 1L);
            }
        }
    }


    /**
     * 合约止盈止损监听
     */
    @Transactional(rollbackFor = Exception.class)
    public void stopPl(String contractType) throws Exception {
        synchronized (this) {
            Compact compactQ = new Compact();
//            compactQ.setStatus(CompactStatus.N.code).setCoin(contractType);
            compactQ.setStatus(CompactStatus.N.code);
            List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
            for (Compact entity : compactList) {
                //提早出局未设置止盈止损，提高运行效率
                if (entity.getStopLoss() == null && entity.getStopProfit() == null)
                    continue;
                //获取最新行情价
                BigDecimal close = getClosePrice(entity.getSymbols(), KINE);
                //平仓价/开仓价
                BigDecimal outDivTrade = BigDecimal.ZERO;
                //杠杆*本金
                boolean flag = false;
                //总盈亏
                BigDecimal totalPlPrice = BigDecimal.ZERO;
                //当前平仓价
                BigDecimal currentUnit = BigDecimal.ZERO;
                //平仓类型
                ExitType exitType = null;
                //流水类型
                CashFlowTypeEnum cashFlowTypeEnum = null;
                //买入做多
                if (StrUtil.equals(entity.getCompactType(), CompactType.BUY.code)) {
                    //止盈
                    if (entity.getStopProfit() != null && close.compareTo(entity.getStopProfit()) >= 0) {
                        outDivTrade = entity.getStopProfit();
                        //做多盈亏 =(平仓价/开仓价)*持仓手数*每手的价值
                        totalPlPrice = (outDivTrade.subtract(entity.getTradePrice()))
                                .multiply(entity.getNumbers())
                                .multiply(new BigDecimal(entity.getHandNumber()));
                        flag = true;
                        exitType = ExitType.PROFIT;
                        currentUnit = entity.getStopProfit();
                        cashFlowTypeEnum = CashFlowTypeEnum.PROFIT;
                    }
                    //止损
                    if (entity.getStopLoss() != null && close.compareTo(entity.getStopLoss()) <= 0) {
                        outDivTrade = entity.getStopLoss();
                        totalPlPrice = (outDivTrade.subtract(entity.getTradePrice()))
                                .multiply(entity.getNumbers())
                                .multiply(new BigDecimal(entity.getHandNumber()));
                        flag = true;
                        exitType = ExitType.LOSS;
                        currentUnit = entity.getStopLoss();
                        cashFlowTypeEnum = CashFlowTypeEnum.LOSS;
                    }
                } else {//卖出做空
                    //止盈
                    if (entity.getStopProfit() != null && close.compareTo(entity.getStopProfit()) <= 0) {
                        outDivTrade = entity.getStopProfit();
                        flag = true;
                        totalPlPrice = (entity.getTradePrice().subtract(outDivTrade))
                                .multiply(entity.getNumbers())
                                .multiply(new BigDecimal(entity.getHandNumber()));
                        currentUnit = entity.getStopProfit();
                        exitType = ExitType.PROFIT;
                        cashFlowTypeEnum = CashFlowTypeEnum.PROFIT;
                    }
                    //止损
                    if (entity.getStopLoss() != null && close.compareTo(entity.getStopLoss()) >= 0) {
                        outDivTrade = entity.getStopLoss();
                        flag = true;
                        totalPlPrice = (entity.getTradePrice().subtract(outDivTrade))
                                .multiply(entity.getNumbers())
                                .multiply(new BigDecimal(entity.getHandNumber()));
                        currentUnit = entity.getStopLoss();
                        exitType = ExitType.LOSS;
                        cashFlowTypeEnum = CashFlowTypeEnum.LOSS;
                    }
                }


                if (!flag)
                    continue;


                //触发止盈止损动作，加锁
                redisUtil.set(STOP_LOCK + entity.getMemberId(), 1);

                //盈亏额度
                //计算平仓返还保证金金额
                BigDecimal returnPostionPrice = entity.getPositionPrice();
                //平仓价值数量
                BigDecimal handPrice = entity.getNumbers().multiply(new BigDecimal(entity.getHandNumber()));

                Swap swap = new Swap();
                swap.setSymbol(entity.getSymbols());
                swap.setDel("N");
                swap = this.swapService.getOne(new QueryWrapper<>(swap));
                //平仓手续费比例
                BigDecimal outFeeRate = swap.getCloseFeePrice().divide(new BigDecimal(100));
                //平仓手续费=平仓手数*每手价值数量*平仓价格*平仓手续费率
                BigDecimal outFee = entity.getNumbers()
                        .multiply(new BigDecimal(entity.getHandNumber()))
                        .multiply(currentUnit)
                        .multiply(outFeeRate);

                BigDecimal totalReturnPrice = returnPostionPrice.add(totalPlPrice).subtract(outFee);

                //配资
                entity.setExitPrice(currentUnit)
                        .setExitType(exitType.code)
                        .setStatus(CompactStatus.Y.code)
                        .setExitPositionPrice(entity.getPositionPrice())
                        .setExitTime(new Date())
                        .setTpl(totalPlPrice)
                        .setCloseNumber(entity.getNumbers())
                        .setPl(totalPlPrice)
                        .setCloseFeePrice(outFee)
                        .setHandPrice(handPrice)
                        .setUpdateUser(entity.getMemberId())
                ;

                compactService.updateById(entity);

                String coin = contractType;
                //配资账户
                Contract contract = F.me().getContract(entity.getMemberId(), contractType);
                BigDecimal usedPrice = contract.getUsedPrice().add(totalReturnPrice).compareTo(BigDecimal.ZERO) < 0
                        ? BigDecimal.ZERO : contract.getUsedPrice().add(totalReturnPrice);
                if (totalPlPrice.compareTo(BigDecimal.ZERO) > 0) {
                    F.me().saveCashflow(entity.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_IN, cashFlowTypeEnum,
                            totalPlPrice, coin, totalPlPrice, coin, outFee, coin,
                            ItemCode.USED, coin, null, "盈利",
                            contract.getUsedPrice(), usedPrice,
                            entity.getMemberId(), entity.getMemberId());
                } else if (totalPlPrice.compareTo(BigDecimal.ZERO) < 0) {
                    F.me().saveCashflow(entity.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_OUT, cashFlowTypeEnum,
                            BigDecimal.ZERO.subtract(totalPlPrice), coin, totalPlPrice, coin, outFee, coin,
                            ItemCode.USED, coin, null, "亏损",
                            contract.getUsedPrice(), usedPrice,
                            entity.getMemberId(), entity.getMemberId());
                }

                contract.setUsedPrice(usedPrice);
                contract.setUpdateUser(-1L);
                contract.setUpdateTime(new Date());
                int rows = contractService.updateWallet(contract);

                //log.info("止盈止损平仓:id:{} 单号：{},类型：{}", entity.getCompactId(), entity.getOrderNo(), entity.getExitType());

                refreshContractInfo(entity.getMemberId(), coin);
                //添加一个处理单位
                redisUtil.lSet(OP_CONTRACT_DATA, entity.getMemberId());

                //触发止盈止损动作，解锁
                redisUtil.del(STOP_LOCK + entity.getMemberId());
                if (rows <= 0)
                    throw new Exception("无法止盈止损平仓");
//                }
            }
        }
    }

    /**
     * 行情涨跌幅拉取
     *
     * @param symbols 交易对
     * @return
     */
    private BigDecimal getQuotes(String symbols) {
        Quotes quotes = new Quotes();
        quotes.setSymbols(symbols);
        return quotesService.getOne(new QueryWrapper<>(quotes)).getValue();
    }

//    /**
//     * 爆仓监听
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public void boom() throws Exception
//    {
//
//        synchronized (this)
//        {
//            for (ContractType contractType : ContractType.values())
//            {
//                //保证金率
//                BigDecimal contractMargin = new BigDecimal(PromotionFactory.me().getSysConfigValueByCode(CONTRACT_MARGIN)).divide(new BigDecimal(100));
//
//                //获取当前合约订单用户ID列表
//                List<Long> memberIdList = compactService.getBaseMapper().getMemberIdList();
//
//                for (Long memberId : memberIdList)
//                {
//                    //获取用户保证金率
//                    BigDecimal memberMargin = getMargin(memberId, contractType.getCode());
//
//                    if (memberMargin.compareTo(contractMargin) > 0)
//                        continue;
//
//                    //爆仓
//                    //爆仓 加锁
//                    redisUtil.lock(BOOM_LOCK + memberId, 1, 60);
//
//                    //获取用户所有合约记录
//
//                    Compact compactQ = new Compact();
//                    compactQ.setDel("N")
//                            .setMemberId(memberId)
//                            .setCoin(contractType.getCode())
//                            .setStatus(CompactStatus.N.code);
//                    List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
//
//                    String coin = contractType.getCode();
//                    //配资账户
//                    Contract entrust = PromotionFactory.me().getContract(memberId, contractType.getCode());
//                    //订单配资总量
//                    BigDecimal afterEntrustAdd = BigDecimal.ZERO;
//                    for (Compact entity : compactList)
//                    {
//                        //最新行情价
//                        BigDecimal close = getClosePrice(entity.getSymbols(), KINE_PERPETUAL);
//                        BigDecimal totalPlPrice = BigDecimal.ZERO;
//                        //平仓价/开仓价
//                        BigDecimal outDivTrade = close.divide(entity.getTradePrice(), 8, RoundingMode.DOWN);
//                        //杠杆*本金
//                        BigDecimal levMulNum = entity.getLeverRate().multiply(entity.getNumbers());
//                        //买涨
//                        if (StrUtil.equals(entity.getCompactType(), CompactType.BUY.code))
//                        {
//                            //做多盈亏 =[平仓价/(开仓价-1)]*杠杆*本金
//
//                            totalPlPrice = (outDivTrade.subtract(BigDecimal.ONE)).multiply(levMulNum);
//                        }
//                        //买跌
//                        if (StrUtil.equals(entity.getCompactType(), CompactType.SELL.code))
//                        {
//                            totalPlPrice = (BigDecimal.ONE.subtract(outDivTrade)).multiply(levMulNum);
//                        }
//
//                        //平仓手续费比例
//                        BigDecimal outFeeRate = new BigDecimal(PromotionFactory.me().getSysConfigValueByCode(OUT_FEE)).divide(new BigDecimal(100));
//
//                        //平仓手续费=本金*杠杆*平仓手续费比例
//
//                        BigDecimal outFee = levMulNum.multiply(outFeeRate);
//
//                        //配资手续费比例
//                        BigDecimal giveFeeRate = new BigDecimal(PromotionFactory.me().getSysConfigValueByCode(GIVE_FEE)).divide(new BigDecimal(100));
//
//
//                        //天数
//                        int days = DateTimeUtil.getDays(entity.getCreateTime(), new Date());
//                        //配资手续费=持仓配资*配资手续费比例*天数
//                        BigDecimal giveFee = entity.getGivePrice().multiply(giveFeeRate).multiply(new BigDecimal(days));
//
//                        BigDecimal totalFee = entity.getFee().add(outFee).add(giveFee);
//
//                        //保证金返还数量
//                        BigDecimal profitPosition = entity.getPositionPrice().subtract(totalFee).add(totalPlPrice);
//                        //配资
//                        BigDecimal profitGive = entity.getGivePrice();
//
//
//                        entity.setExitPrice(close)
//                                .setExitType(ExitType.FIXED.code)
//                                .setStatus(CompactStatus.Y.code)
//                                .setExitTime(new Date())
//                                .setTpl(totalPlPrice)
////                            .setPl(positionPlPrice)
////                            .setGivePl(givePlPrice)
//                                .setFee(totalFee)
//                                .setRemark("" + totalPlPrice)
//                                .setUpdateUser(entity.getMemberId())
//                        ;
//
//                        compactService.updateById(entity);
//
//
//                        afterEntrustAdd = afterEntrustAdd.add(profitGive);
//
//
//                        //盈利
//                        if (totalPlPrice.compareTo(BigDecimal.ZERO) >= 0)
//                        {
//                            PromotionFactory.me().saveCashflow(entity.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.FIXED,
//                                    totalPlPrice, coin, totalPlPrice, coin, totalFee, coin,
//                                    ItemCode.USED, coin, null, "盈利",
//                                    BigDecimal.ZERO, BigDecimal.ZERO,
//                                    SYS_PLATFORM, entity.getMemberId());
//                        }
//                        else
//                        {
//                            PromotionFactory.me().saveCashflow(entity.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.FIXED,
//                                    BigDecimal.ZERO.subtract(totalPlPrice), coin, totalPlPrice, coin, totalFee, coin,
//                                    ItemCode.USED, coin, null, "亏损",
//                                    BigDecimal.ZERO, BigDecimal.ZERO,
//                                    SYS_PLATFORM, entity.getMemberId());
//                        }
//
//
//                    }
//                    // 爆仓 解锁
//                    redisUtil.unLock(BOOM_LOCK + memberId);
//
//                    PromotionFactory.me().saveCashflow(memberId, WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.FIXED,
//                            afterEntrustAdd, coin, afterEntrustAdd, coin, BigDecimal.ZERO, coin,
//                            ItemCode.ENTRUST, coin, null, null,
//                            entrust.getEntrustPrice(), entrust.getEntrustPrice().add(afterEntrustAdd),
//                            SYS_PLATFORM, memberId);
//
//                    entrust
//                            .setGivePrice(entrust.getGivePrice().subtract(afterEntrustAdd))
//                            .setEntrustPrice(entrust.getEntrustPrice().add(afterEntrustAdd));
//
//                    int rows = contractService.updateWallet(entrust);
//
//                    if (rows <= 0)
//                        throw new Exception("爆仓：账户更新异常");
//
//                }
//            }
//        }
//    }

    //    /**
//     * 爆仓监听
//     */
    @Transactional(rollbackFor = Exception.class)
    @Async("boomTask")
    public void boom(String contractType) throws Exception {
        long start = System.currentTimeMillis();
        synchronized (this) {
            //保证金率
            BigDecimal contractMargin = new BigDecimal(F.me().getSysConfigValueByCode(CONTRACT_MARGIN)).divide(new BigDecimal(100));
            //获取当前合约订单用户ID列表
            List<Long> memberIdList = compactService.getBaseMapper().getMemberIdList();
            for (Long memberId : memberIdList) {
                //获取用户所有合约记录
                Compact compactQ = new Compact();
                compactQ.setDel("N")
                        .setMemberId(memberId)
                        .setCoin(contractType)
                        .setStatus(CompactStatus.N.code);
                List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
                String coin = contractType;
                //获取当前净值
                Contract currentContract = (Contract) redisUtil.get(String.format(CONTRACT_CODE, contractType) + memberId);
                for (Compact entity : compactList) {
                    BigDecimal lossPL = BigDecimal.ZERO;
                    if (redisUtil.get(PL + entity.getOrderNo()) != null) {
                        lossPL = (BigDecimal) redisUtil.get(PL + entity.getOrderNo());
                    }
//                    log.info("监听该订单号【"+entity.getOrderNo()+"】的可用余额与持仓保证金总和跟亏损比较"+
//                            currentContract.getUsedPrice().add(entity.getPositionPrice()).add(
//                                    lossPL.compareTo(BigDecimal.ZERO) <0
//                                            ? BigDecimal.ZERO  : lossPL)+
//                            ",可用余额："+currentContract.getUsedPrice()+",持仓保证金："+entity.getPositionPrice()+
//                            ",亏损额："+lossPL);
                    boolean isBoom = false;
                    boolean isConOpen = F.me().cfg(CONTRACT_OPEN).equals(Y);
                    BigDecimal ctrlFee = new BigDecimal(F.me().cfg(CONTRACT_FEE));
                    BigDecimal ctrlPrice = new BigDecimal(F.me().cfg(CONTRACT_MARKET_PRICE));
                    BigDecimal tradePrice = isConOpen ? ctrlFee.add(ctrlPrice) : entity.getTradePrice();

                    //计算维持保证金
                    BigDecimal marginPrice = entity.getNumbers().multiply(
                                    new BigDecimal(entity.getHandNumber()))
                            .multiply(tradePrice)
                            .multiply(contractMargin);

                    if (entity.getPositionPrice().compareTo(marginPrice) <= 0) {
                        log.info("监听该订单号【" + entity.getOrderNo() + "】的可用余额与持仓保证金总和跟亏损比较" +
                                currentContract.getUsedPrice().add(entity.getPositionPrice()).add(
                                        lossPL.compareTo(BigDecimal.ZERO) < 0
                                                ? BigDecimal.ZERO : lossPL) +
                                ",可用余额：" + currentContract.getUsedPrice() + ",持仓保证金：" + entity.getPositionPrice() +
                                ",亏损额：" + lossPL);
                        log.info("该订单号【" + entity.getOrderNo() + "】超过了维持保证金======此时的计算后的维持保证金为" + marginPrice +
                                ",可用余额：" + currentContract.getUsedPrice() + ",持仓保证金：" + entity.getPositionPrice());
                        isBoom = true;
                    }

                    //爆仓
                    //总盈亏
                    BigDecimal totalPlPrice = BigDecimal.ZERO;
                    //最新行情价
                    BigDecimal close = getClosePrice(entity.getSymbols(), KINE);
//                    log.info("该订单号【"+entity.getOrderNo()+"】，该币种【"+entity.getSymbols()+"】最新行情价为"+close);
                    //平仓价
                    BigDecimal outDivTrade = close;
                    //计算平仓返还保证金金额
                    BigDecimal returnPostionPrice = entity.getPositionPrice();
                    //平仓价值数量
                    BigDecimal handPrice = entity.getNumbers().multiply(new BigDecimal(entity.getHandNumber()));
                    //买涨
                    if (StrUtil.equals(entity.getCompactType(), CompactType.BUY.code)) {
                        //做多盈亏 =平仓
                        totalPlPrice = (outDivTrade.subtract(entity.getTradePrice())).multiply(handPrice);
                    }
                    //买跌
                    if (StrUtil.equals(entity.getCompactType(), CompactType.SELL.code)) {
                        totalPlPrice = (entity.getTradePrice().subtract(outDivTrade)).multiply(handPrice);
                    }

                    if (totalPlPrice.compareTo(BigDecimal.ZERO) >= 0) continue;

                    if (!isBoom && currentContract.getUsedPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        BigDecimal oldLossPrice = BigDecimal.ZERO;
                        BigDecimal subPrice = totalPlPrice;
                        if (redisUtil.get("OLD_LOSS_PL" + entity.getOrderNo()) != null) {
                            oldLossPrice = (BigDecimal) redisUtil.get("OLD_LOSS_PL" + entity.getOrderNo());
                        }
                        if (oldLossPrice.compareTo(BigDecimal.ZERO) == 0) {
                            if (subPrice.compareTo(BigDecimal.ZERO) >= 0) {
                                subPrice = BigDecimal.ZERO;
                            }
                        } else {
                            if (subPrice.compareTo(BigDecimal.ZERO) > 0 && oldLossPrice.compareTo(BigDecimal.ZERO) < 0) {
                                subPrice = subPrice.add(oldLossPrice);
                            } else if (subPrice.compareTo(BigDecimal.ZERO) < 0 && oldLossPrice.compareTo(BigDecimal.ZERO) > 0) {
                                subPrice = oldLossPrice.add(subPrice);
                            }
                        }

//                        log.info("该订单号【"+entity.getOrderNo()+"】======补差值："+subPrice+",老的盈亏额："+oldLossPrice+
//                                ",新盈亏额："+totalPlPrice+
//                                ",可用余额："+currentContract.getUsedPrice()+",持仓保证金："+entity.getPositionPrice());
                        if (subPrice.compareTo(BigDecimal.ZERO) < 0 && entity.getPositionPrice().add(subPrice).compareTo(BigDecimal.ZERO) <= 0) {
//                            log.info("该订单号【"+entity.getOrderNo()+"】持仓保证金不够扣除，进行爆仓======补差值："+subPrice+",老的盈亏额："+oldLossPrice+
//                                    ",新盈亏额："+totalPlPrice+
//                                    ",可用余额："+currentContract.getUsedPrice()+",持仓保证金："+entity.getPositionPrice());
                            isBoom = true;
                        }
                    }
                    if (isBoom) {
                        Swap swap = new Swap();
                        swap.setSymbol(entity.getSymbols());
                        swap.setDel("N");
                        swap = this.swapService.getOne(new QueryWrapper<>(swap));
                        //平仓手续费比例
                        BigDecimal outFeeRate = swap.getCloseFeePrice().divide(new BigDecimal(100));
                        //平仓手续费=平仓手数*每手价值数量*平仓价格*平仓手续费率
                        BigDecimal outFee = handPrice
                                .multiply(outDivTrade)
                                .multiply(outFeeRate);

                        BigDecimal totalReturnPrice = returnPostionPrice.add(totalPlPrice).subtract(outFee);

                        entity.setExitPrice(close)
                                .setExitType(ExitType.FIXED.code)
                                .setStatus(CompactStatus.Y.code)
                                .setExitTime(new Date())
                                .setTpl(totalPlPrice)
                                .setCloseNumber(entity.getNumbers())
                                .setPl(totalPlPrice)
                                .setExitPositionPrice(entity.getPositionPrice())
                                .setCloseFeePrice(outFee)
                                .setHandPrice(handPrice)
                                .setUpdateUser(entity.getMemberId())
                        ;
                        compactService.updateById(entity);
                        //配资账户
                        Contract entrust = F.me().getContract(memberId, contractType);
                        //更新余额
                        BigDecimal usedPrice = entrust.getUsedPrice().add(totalReturnPrice).compareTo(BigDecimal.ZERO) < 0
                                ? BigDecimal.ZERO : entrust.getUsedPrice().add(totalReturnPrice);
                        if (totalPlPrice.compareTo(BigDecimal.ZERO) > 0) {
                            F.me().saveCashflow(entity.getMemberId(),
                                    WalletBigType.CONTRACT,
                                    CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.FIXED,
                                    totalPlPrice, coin, totalPlPrice, coin, outFee, coin,
                                    ItemCode.USED, coin, null, "盈利",
                                    entrust.getUsedPrice(), usedPrice,
                                    entity.getMemberId(), entity.getMemberId());
                        } else if (totalPlPrice.compareTo(BigDecimal.ZERO) < 0) {
                            F.me().saveCashflow(entity.getMemberId(), WalletBigType.CONTRACT, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.FIXED,
                                    BigDecimal.ZERO.subtract(totalPlPrice), coin, totalPlPrice, coin, outFee, coin,
                                    ItemCode.USED, coin, null, "亏损",
                                    entrust.getUsedPrice(), usedPrice,
                                    entity.getMemberId(), entity.getMemberId());
                        }

                        entrust.setUsedPrice(usedPrice);
                        entrust.setUpdateUser(-1L);
                        entrust.setUpdateTime(new Date());
                        while (true) {
                            if (contractService.updateWallet(entrust) > 0) {
                                refreshContractInfo(entrust.getMemberId(), "USDT");
                                break;
                            }
                        }

                        redisUtil.del("OLD_LOSS_PL_SAVE");
                    }
                }

            }
        }
        // log.info("{}爆仓监听：{}ms", contractType, System.currentTimeMillis() - start);
//        }
    }

    /**
     * 计算用户保证金率
     *
     * @param memberId 用户id
     * @param code     币种
     * @return
     */
//    private BigDecimal getMargin(Long memberId, String code) {
//        //获取用户所有合约记录
//        Compact compactQ = new Compact();
//        compactQ.setDel("N")
//                .setMemberId(memberId)
//                .setEnabled("N") //未过期订单
//                .setCoin(code)
//                .setStatus(CompactStatus.N.code);
//        List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
//        //总盈亏
//        BigDecimal tpl = BigDecimal.ZERO;
//        //平仓手续费比例
//        BigDecimal outFeeRate = new BigDecimal(F.me().getSysConfigValueByCode(OUT_FEE)).divide(new BigDecimal(100));
//        //配资手续费比例
//        BigDecimal giveFeeRate = new BigDecimal(F.me().getSysConfigValueByCode(GIVE_FEE)).divide(new BigDecimal(100));
//        //总手续费
//        BigDecimal totalFee = BigDecimal.ZERO;
//        //总持仓
//        BigDecimal totalLock = BigDecimal.ZERO;
//        for (Compact compact : compactList) {
//
//            //杠杆*本金
//            BigDecimal levMulNum = compact.getLeverRate().multiply(new BigDecimal(compact.getNumbers()));
//            //总手续费
//            //平仓手续费=本金*杠杆*平仓手续费比例
//            BigDecimal outFee = levMulNum.multiply(outFeeRate);
//            //天数
//            int days = DateTimeUtil.getDays(compact.getCreateTime(), new Date());
//            //配资手续费=持仓配资*配资手续费比例*天数
//            BigDecimal giveFee = compact.getGivePrice().multiply(giveFeeRate).multiply(new BigDecimal(days));
//
//            //开仓手续费
////            BigDecimal contractFee = new BigDecimal(PromotionFactory.me().getSysConfigValueByCode(CONTRACT_FEE)).divide(new BigDecimal(100), 8, RoundingMode.DOWN);
//
//            //开仓手续费 =本金*杠杆*开仓手续费率
//            BigDecimal fee = compact.getFee();
//
//            totalFee = totalFee.add(fee).add(outFee).add(giveFee);
//
//            BigDecimal pl = (BigDecimal) redisUtil.get(PL + compact.getOrderNo());
//            if (pl != null)
//                tpl = tpl.add(pl);
//            totalLock = totalLock.add(compact.getPositionPrice()).add(compact.getGivePrice());
//        }
//
//
//        //保证金率=(资产+盈亏)/(持仓保证金+持仓配资)
//        //配资账户
//        Contract entrust = F.me().getContract(memberId, code);
//        //合约账户
//        Contract contractObj = (Contract) redisUtil.get(String.format(CONTRACT_CODE, code) + memberId);
//        if (contractObj == null)
//            return new BigDecimal(999999);
//
//
//        //持仓保证金+持仓配资
//        BigDecimal lockSum = (contractObj.getPositionPrice() == null ? BigDecimal.ZERO : contractObj.getPositionPrice()).add(
//                (entrust.getGivePrice() == null ? BigDecimal.ZERO : entrust.getGivePrice())
//        );
//
//
//        if (lockSum.compareTo(BigDecimal.ZERO) == 0)
//            return new BigDecimal(999999);
//
//        return ((contractObj.getWorthPrice().subtract(totalFee).subtract(entrust.getEntrustPrice()))).divide(lockSum, 8, RoundingMode.DOWN);
//    }


    /**
     * 盈亏监听
     */
    public void pl() {
        synchronized (this) {

            BigDecimal totalPlPrice = BigDecimal.ZERO;
            Compact compactQ = new Compact();
            compactQ.setStatus(CompactStatus.N.code);
            List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
            for (Compact entity : compactList) {
                //最新行情价
                BigDecimal close = getClosePrice(entity.getSymbols(), KINE);
                //close = close.add(getQuotes(entity.getSymbols()));
//
                BigDecimal outDivTrade = close;
                //杠杆*本金
//                BigDecimal levMulNum = entity.getLeverRate().multiply(entity.getNumbers());
                BigDecimal levMulNum = entity.getNumbers();
                //平仓价值数量
                BigDecimal handPrice = levMulNum.multiply(new BigDecimal(entity.getHandNumber()));
                //买涨
                if (StrUtil.equals(entity.getCompactType(), CompactType.BUY.code)) {
                    //做多盈亏 =平仓
                    BigDecimal tradeOutNum = (outDivTrade.subtract(entity.getTradePrice()));
                    totalPlPrice = tradeOutNum.multiply(handPrice);
                }
                //买跌
                if (StrUtil.equals(entity.getCompactType(), CompactType.SELL.code)) {
                    BigDecimal tradeOutNum = (entity.getTradePrice().subtract(outDivTrade));
                    totalPlPrice = tradeOutNum.multiply(handPrice);
                }

                redisUtil.set(PL + entity.getOrderNo(), totalPlPrice, 60);
            }
        }
    }


    //

    /**
     * 获取币种最新价
     *
     * @param symbols 交易对
     * @param type    类型：现货:KINE 期货-永续:KINE_PERPETUAL
     * @return
     */
    private BigDecimal getClosePrice(String symbols, String type) {
        Kline kline = redisUtil.getBean(type + symbols + _NEW, Kline.class);
        return kline == null ? BigDecimal.ZERO : new BigDecimal(kline.getClose());
    }


    public void writeCnyUsdtRedis() {
        redisUtil.set(CNY_USDT, new BigDecimal(F.me().getSysConfigValueByCode(CNY_USDT)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void entrust() throws Exception {
        synchronized (this) {
            //当前委托订单
            List<Compact> list = compactService.getBaseMapper().getEntrust();
            for (Compact dto : list) {
                //当前行情价
                //订单状态处理
                //限价卖出： 单价<=当前行情价 ，订单成交进入持仓
                if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)
                        && StrUtil.equals(dto.getCompactType(), CompactType.SELL.code)
                        && dto.getUnit().compareTo(getClosePrice(dto.getSymbols(), KINE)) <= 0) {
                    dto.setStatus(CompactStatus.N.code);
                }
                //限价买入：单价>=行情价，订单成交
                if (StrUtil.equals(dto.getDealWay(), DealWay.LIMIT.code)
                        && StrUtil.equals(dto.getCompactType(), CompactType.BUY.code)
                        && dto.getUnit().compareTo(getClosePrice(dto.getSymbols(), KINE)) >= 0) {
                    dto.setStatus(CompactStatus.N.code);
                }

                dto.setUpdateUser(SYS_PLATFORM);
                int rows = compactService.getBaseMapper().updateEntrust(dto);
                if (rows == 0) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("request", dto.toString());
//                    jsonObject.put("response", rows);
//                    PromotionFactory.me().saveWalletException("entrust", "合约 委托转持仓 更新异常", jsonObject);
                    throw new Exception("合约更新异常");
                }

            }

        }

    }

    private AtomicReference<Thread> sign = new AtomicReference<>();

    public void plTotal(String coin) {
        // log.info("{}盈亏监听", coin);
        synchronized (this) {
            //获取当前合约订单用户ID列表
            List<Long> memberIdList = compactService.getBaseMapper().getMemberIdList();
            for (Long memberId : memberIdList) {
                BigDecimal totalPlPrice = BigDecimal.ZERO;
                BigDecimal lossTotalPlPrice = BigDecimal.ZERO;
                Compact compactQ = new Compact();
                compactQ.setCoin(coin)
                        .setStatus(CompactStatus.N.code).setMemberId(memberId);
                List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
                for (Compact entity : compactList) {
                    BigDecimal pl = BigDecimal.ZERO;
                    //最新行情价
                    BigDecimal close = getClosePrice(entity.getSymbols(), KINE);
                    BigDecimal handPrice = (entity.getNumbers()).multiply(new BigDecimal(entity.getHandNumber()));
                    BigDecimal tradeOutNum;
                    //买涨
                    if (StrUtil.equals(entity.getCompactType(), CompactType.BUY.code)) {
                        //做多盈亏 =[平仓价/(开仓价-1)]*杠杆*本金
                        tradeOutNum = close.subtract(entity.getTradePrice());
                        pl = tradeOutNum.multiply(handPrice);
//                        log.info("用户【" + entity.getMemberId() + "】订单买涨当前行情：" + close + ",建仓：" + entity.getTradePrice() + "盈亏额：" + pl.toPlainString());
                    }
                    //买跌
                    if (StrUtil.equals(entity.getCompactType(), CompactType.SELL.code)) {
                        tradeOutNum = entity.getTradePrice().subtract(close);
                        pl = tradeOutNum.multiply(handPrice);
//                        log.info("用户【" + entity.getMemberId() + "】订单买跌当前行情：" + close + ",建仓：" + entity.getTradePrice() + "盈亏额：" + pl.toPlainString());
                    }

                    if (pl.compareTo(BigDecimal.ZERO) < 0) {
                        lossTotalPlPrice = lossTotalPlPrice.add(pl);
                    }
                    redisUtil.set(PL + entity.getOrderNo(), pl.setScale(4, RoundingMode.DOWN), EXPIRE_NORMAL);
                    totalPlPrice = totalPlPrice.add(pl);
                }
//                log.info("用户【" + memberId + "】总盈亏额：" + totalPlPrice.toPlainString());
                redisUtil.set(String.format(PL_TOTAL_, coin) + memberId, totalPlPrice.toPlainString());
                this.redisUtil.set(String.format(LOSS_PL_TOTAL_, coin) + memberId, lossTotalPlPrice.toPlainString());
            }
        }

    }

//    @Transactional(rollbackFor = Exception.class)
//    public void contractUpdateCoin() {
//        List<Swap> swapList = F.me().getSwaps("Y");
//        for (Swap swap : swapList) {
//            contractUpdateCoin(swap.getSymbol());
//        }
//    }


    /**
     * 账户刷新
     */
    @Transactional(rollbackFor = Exception.class)
    @Async("asyncTaskExecutor")
    public void contractUpdateCoin(String contractType) {

        String code = contractType;
        //获取当前合约订单用户ID列表
        boolean lock = redisUtil.lock(CONTRACT_UPDATE_LOCK, 1, 60);
        if (!lock) {
            //log.info("合约刷新-未获取锁");
            return;
        }

        //止盈止损及爆仓处理用户队列
        List many = redisUtil.lGet(OP_CONTRACT_DATA, 0, -1);

        for (Object o : many) {
            Long id = (Long) redisUtil.lPop(OP_CONTRACT_DATA);
            refreshContractInfo(id, code);
        }

        List<Long> coMembers = compactService.getBaseMapper().getMidsByInAndN();
        for (Long memberId : coMembers) {
            refreshContractInfo(memberId, code);
        }

        redisUtil.unLock(CONTRACT_UPDATE_LOCK);

    }


    /**
     * 刷新合约用户账户
     *
     * @param memberId 用户
     * @param code     币种
     */
    public void refreshContractInfo(Long memberId, String code) {
        //配资资产
        Contract entrustObj = F.me().getContract(memberId, code);
        if (entrustObj == null)
            return;
        //合约账户
        Contract contractObj = Contract.init();
        if (redisUtil.get(String.format(CONTRACT_CODE, code) + memberId) == null) {
            redisUtil.set(String.format(CONTRACT_CODE, code) + memberId, contractObj);
        }
        contractObj = (Contract) redisUtil.get(String.format(CONTRACT_CODE, code) + memberId);
        if (contractObj == null)
            return;

        BigDecimal plTotalNow = BigDecimal.ZERO;

        if (redisUtil.get(String.format(PL_TOTAL_, code) + memberId) != null) {
            plTotalNow = new BigDecimal(redisUtil.get(String.format(PL_TOTAL_, code) + memberId).toString());
        }
        BigDecimal lossTotalNow = BigDecimal.ZERO;
        if (this.redisUtil.get(String.format(LOSS_PL_TOTAL_, code) + memberId) != null) {
            lossTotalNow = new BigDecimal(this.redisUtil.get(String.format(LOSS_PL_TOTAL_, code) + memberId).toString());
        }
        //持仓保证金计算
        BigDecimal positionTotal = getPositionTotal(memberId, code);
        //当前个人委托保证金
        BigDecimal totalPositionIN = compactService.getBaseMapper().getPositionTotalStatus(memberId, "IN", code);
        //当前盈亏
        BigDecimal nowPl = plTotalNow.setScale(4, BigDecimal.ROUND_DOWN);
        //如果无持仓合约订单，情况当前盈亏及历史盈亏
        if (notCompactOrder(memberId, code)) {
            nowPl = BigDecimal.ZERO;
            redisUtil.del(String.format(PL_TOTAL_, code) + memberId);
            redisUtil.del(String.format(LOSS_PL_TOTAL_, code) + memberId);
        }
        //可用保证金计算  做显示用
        BigDecimal usePrice = entrustObj.getUsedPrice().add(lossTotalNow);
        //净值/权益账户：未实现盈亏 +可用
        BigDecimal nowWorthPrice = positionTotal
                .add(totalPositionIN)
                .add(entrustObj.getUsedPrice())
                .add(nowPl).setScale(4, BigDecimal.ROUND_DOWN);

        contractObj
                .setWorthPrice(nowWorthPrice)
                .setEntrustPrice(totalPositionIN)
                .setPositionPrice(positionTotal.setScale(4, BigDecimal.ROUND_DOWN))
                .setNoPl(nowPl)
                .setUsedPrice(usePrice.setScale(4, BigDecimal.ROUND_DOWN));
        if (contractObj.getUsedPrice().compareTo(BigDecimal.ZERO) <= 0) {
            if (redisUtil.get("OLD_LOSS_PL_SAVE") == null) {
                redisUtil.set("OLD_LOSS_PL_SAVE", "Y");
                Compact compactQ = new Compact();
                compactQ.setCoin("USDT")
                        .setStatus(CompactStatus.N.code).setMemberId(memberId);
                List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
                for (Compact entity : compactList) {
                    BigDecimal lossPL = BigDecimal.ZERO;
                    if (redisUtil.get(PL + entity.getOrderNo()) != null) {
                        lossPL = (BigDecimal) redisUtil.get(PL + entity.getOrderNo());
                    }
                    redisUtil.set("OLD_LOSS_PL" + entity.getOrderNo(), lossPL);
                }
            }
        } else {
            redisUtil.del("OLD_LOSS_PL_SAVE");
            Compact compactQ = new Compact();
            compactQ.setCoin("USDT")
                    .setStatus(CompactStatus.N.code).setMemberId(memberId);
            List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
            for (Compact entity : compactList) {
                BigDecimal lossPL = BigDecimal.ZERO;
                if (redisUtil.get(PL + entity.getOrderNo()) != null) {
                    lossPL = (BigDecimal) redisUtil.get(PL + entity.getOrderNo());
                }
                redisUtil.del("OLD_LOSS_PL" + entity.getOrderNo());
            }
        }
        //log.info("币种：{} 用户id:{} 合约可用余额：{} 总亏损：{} 波动可用余额：{}", code, memberId,entrustObj.getUsedPrice(),lossTotalNow,contractObj.getUsedPrice());
        redisUtil.set(String.format(CONTRACT_CODE, code) + memberId, contractObj);

    }

    /**
     * 刷新合约用户账户
     *
     * @param memberId 用户
     * @param code     币种
     */
    public void refreshFinFuturesInfo(Long memberId, String code) {
        //配资资产
        FinFutures entrustObj = F.me().getFinFutures(memberId, code);
        if (entrustObj == null)
            return;
        //合约账户
        FinFutures contractObj = FinFutures.init();
        if (redisUtil.get(String.format(FINFUTURES_CODE, code) + memberId) == null) {
            redisUtil.set(String.format(FINFUTURES_CODE, code) + memberId, contractObj);
        }
        contractObj = (FinFutures) redisUtil.get(String.format(FINFUTURES_CODE, code) + memberId);
        if (contractObj == null)
            return;

        BigDecimal plTotalNow = BigDecimal.ZERO;

        if (redisUtil.get(String.format(PLF_TOTAL_, code) + memberId) != null) {
            plTotalNow = new BigDecimal(redisUtil.get(String.format(PLF_TOTAL_, code) + memberId).toString());
        }
        BigDecimal lossTotalNow = BigDecimal.ZERO;
        if (this.redisUtil.get(String.format(LOSS_PLF_TOTAL_, code) + memberId) != null) {
            lossTotalNow = new BigDecimal(this.redisUtil.get(String.format(LOSS_PLF_TOTAL_, code) + memberId).toString());
        }
        //持仓保证金计算
        BigDecimal positionTotal = getPositionTotal(memberId, code);
        //当前个人委托保证金
        BigDecimal totalPositionIN = compactService.getBaseMapper().getPositionTotalStatus(memberId, "IN", code);
        //当前盈亏
        BigDecimal nowPl = plTotalNow.setScale(4, BigDecimal.ROUND_DOWN);
        //如果无持仓合约订单，情况当前盈亏及历史盈亏
        if (notCompactOrder(memberId, code)) {
            nowPl = BigDecimal.ZERO;
            redisUtil.del(String.format(PLF_TOTAL_, code) + memberId);
            redisUtil.del(String.format(LOSS_PLF_TOTAL_, code) + memberId);
        }
        //可用保证金计算  做显示用
        BigDecimal usePrice = entrustObj.getUsedPrice().add(lossTotalNow);
        //净值/权益账户：未实现盈亏 +可用
        BigDecimal nowWorthPrice = positionTotal
                .add(totalPositionIN)
                .add(entrustObj.getUsedPrice())
                .add(nowPl).setScale(4, BigDecimal.ROUND_DOWN);

        contractObj
                .setWorthPrice(nowWorthPrice)
                .setEntrustPrice(totalPositionIN)
                .setPositionPrice(positionTotal.setScale(4, BigDecimal.ROUND_DOWN))
                .setNoPl(nowPl)
                .setUsedPrice(usePrice.setScale(4, BigDecimal.ROUND_DOWN));
        if (contractObj.getUsedPrice().compareTo(BigDecimal.ZERO) <= 0) {
            if (redisUtil.get("OLD_LOSS_PLF_SAVE") == null) {
                redisUtil.set("OLD_LOSS_PLF_SAVE", "Y");
                Compact compactQ = new Compact();
                compactQ.setCoin("USDT")
                        .setStatus(CompactStatus.N.code).setMemberId(memberId);
                List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
                for (Compact entity : compactList) {
                    BigDecimal lossPL = BigDecimal.ZERO;
                    if (redisUtil.get(PLF + entity.getOrderNo()) != null) {
                        lossPL = (BigDecimal) redisUtil.get(PLF + entity.getOrderNo());
                    }
                    redisUtil.set("OLD_LOSS_PLF" + entity.getOrderNo(), lossPL);
                }
            }
        } else {
            redisUtil.del("OLD_LOSS_PLF_SAVE");
            Compact compactQ = new Compact();
            compactQ.setCoin("USDT")
                    .setStatus(CompactStatus.N.code).setMemberId(memberId);
            List<Compact> compactList = compactService.list(new QueryWrapper<>(compactQ));
            for (Compact entity : compactList) {
                BigDecimal lossPL = BigDecimal.ZERO;
                if (redisUtil.get(PL + entity.getOrderNo()) != null) {
                    lossPL = (BigDecimal) redisUtil.get(PL + entity.getOrderNo());
                }
                redisUtil.del("OLD_LOSS_PLF" + entity.getOrderNo());
            }
        }
        //log.info("币种：{} 用户id:{} 合约可用余额：{} 总亏损：{} 波动可用余额：{}", code, memberId,entrustObj.getUsedPrice(),lossTotalNow,contractObj.getUsedPrice());
        redisUtil.set(String.format(FINFUTURES_CODE, code) + memberId, contractObj);

    }

    //仓位保证金数据结算


    /**
     * 仓位保证金数据结算
     *
     * @param memberId
     * @param coin     币种
     * @return
     */
    private BigDecimal getFixPrice(Long memberId, String coin) {

        //转入总和
        //BigDecimal inTotal = cashflowService.getBaseMapper().getBalance(memberId, CashFlowTypeEnum.CONVERT_IN.getCode(), coin);
        //转出总额
        //BigDecimal outTotal = cashflowService.getBaseMapper().getBalance(memberId, CashFlowTypeEnum.CONVERT_OUT.getCode(), coin);
        //平仓结算返还计算= 用户个人总持仓保证金+结算总盈亏-总手续费
        //参数前已经加了该数据
        // BigDecimal handleTotal = compactService.getBaseMapper().getPositionTotalFix(memberId, coin);

        //当前个人持仓保证金
        BigDecimal totalPositionN = compactService.getBaseMapper().getPositionTotalStatus(memberId, "N", coin);

        //当前个人委托保证金
        BigDecimal totalPositionIN = compactService.getBaseMapper().getPositionTotalStatus(memberId, "IN", coin);

        return totalPositionN;
    }


    /**
     * 持仓保证金计算
     *
     * @param memberId
     * @param code     币种
     * @return
     */
    private BigDecimal getPositionTotal(Long memberId, String code) {
        return compactService.getBaseMapper().getPositionTotal(memberId, code);
    }

    /**
     * 资产计算    资产（转入/转出/平仓结算资产）
     *
     * @param memberId id
     * @param code     币种
     * @return
     */
    private BigDecimal getBalance(Long memberId, String code) {
        //转入总和
        BigDecimal inTotal = cashflowService.getBaseMapper().getBalance(memberId, CashFlowTypeEnum.CONVERT_IN.getCode(), code);
        //转出总额
        BigDecimal outTotal = cashflowService.getBaseMapper().getBalance(memberId, CashFlowTypeEnum.CONVERT_OUT.getCode(), code);

        //平仓结算 总盈亏-总手续费
        BigDecimal handleTotal = compactService.getBaseMapper().handleTotal(memberId, code);

        return inTotal.subtract(outTotal).add(handleTotal);

    }


    //

    /**
     * 没有持仓订单
     *
     * @param memberId 用户id
     * @param coin     币种
     * @return
     */
    private boolean notCompactOrder(Long memberId, String coin) {
        int count = contractService.getBaseMapper().countN(memberId, coin);
        return count == 0 ? true : false;
    }


    @Autowired
    HomeService homeService;

    public void v() {
        //测试:提醒功能
        BigDecimal close = getClosePrice(BTC_USDT, KINE);

        if (redisUtil.get(WILL) != null) {
            BigDecimal will = new BigDecimal((String) redisUtil.get(WILL));
            if (close.compareTo(will) >= 0) {
                String phone = (String) redisUtil.get(WILL_P);
                homeService.getMsg("-1", "86", null, 1L, phone);
                redisUtil.del(WILL);
                redisUtil.del(WILL_P);
            }
        }

    }

    public void upContract() {
        List<Long> memberIdList = memberService.getBaseMapper().memberIdList();
        for (Long memberId : memberIdList) {
            refreshContractInfo(memberId, "USDT");
            //refreshFinFuturesInfo(memberId,"USDT" );
            // refreshContractInfo(memberId, "MGE");
        }
    }

    public void mdTicket() {

        long start = System.currentTimeMillis();
        String symbols = "MGE/USDT";
        String key = KINE + symbols + "_" + PeriodType.day_1.code;
        //获取
        Object object = redisUtil.get(key);
        if (object != null) {

            List<Kline> klines = (List) object;
            if (klines.size() > 0) {
//                log.info("起始：{}", start);
                Kline entity = klines.get(0);

                Kline kline = new Kline();
                kline.setSymbol(symbols)
                        .setClose(entity.getClose())
                        .setOpen(entity.getOpen())
                        .setHigh(entity.getHigh())
                        .setLow(entity.getLow());
//                log.info("MD实时行情:{}", kline.getClose());
                long end = System.currentTimeMillis();
//                log.info("结束：{}", end);
//                log.info("执行时间 ：{}ms", end - start);
                redisUtil.set(KINE + symbols + _NEW, kline);
            }
        }
        // log.info(">>> {}行情刷新 {}ms", symbols, System.currentTimeMillis() - start);
    }

    public void test() {
        mdDepth("MGE/USDT");
    }


    /**
     * MD代币深度数据生成
     */
    public void mdDepth(String coinType) {
        Depth depth = new Depth();
        String symbols = coinType;
        String key = DEPTH + symbols;
        //最近20条

        //买入委托
        Match buyQuery = new Match();
        buyQuery.setMatchType(MatchType.BUY.code)
                .setSymbols(symbols)
                .setDel("N")
        ;
        List<Match> buyList = matchService.list(new QueryWrapper<>(buyQuery)
                        .in("status", MatchStatus.PAID.code, MatchStatus.PART_MATCH.code)
                        .orderByDesc(CREATE_TIME)
//                    .last("limit 20")
        );
        //当前的所有买单 bids [price, quote volume]
        List<List<BigDecimal>> bids = new ArrayList<>();
        Map<BigDecimal, BigDecimal> buyMap = new HashedMap();
        Set<BigDecimal> buySet = new HashSet<>();
        //同单价数据合并
        for (Match match : buyList) {
            //获取买单撮合成交量
            BigDecimal rest = match.getWill().subtract(match.getOk());
            if (buyMap.get(match.getUnit()) == null) {
                buyMap.put(match.getUnit(), rest);
            } else {
                BigDecimal total = buyMap.get(match.getUnit());
                buyMap.put(match.getUnit(), total.add(rest));
            }
            buySet.add(match.getUnit());
        }

        for (BigDecimal unit : buySet) {
            List<BigDecimal> entity = new ArrayList<>();
            entity.add(unit);
            entity.add(buyMap.get(unit));
            bids.add(entity);
        }

        //卖出委托
        Match sellQuery = new Match();
        sellQuery.setMatchType(MatchType.SELL.code)
                .setSymbols(symbols)
                .setDel("N")
        ;
        List<Match> sellList = matchService.list(new QueryWrapper<>(sellQuery)
                        .in("status", MatchStatus.PAID.code, MatchStatus.PART_MATCH.code)
                        .orderByDesc(CREATE_TIME)
//                    .last("limit 20")
        );
        //当前的所有卖单 asks [price, quote volume]
        List<List<BigDecimal>> asks = new ArrayList<>();
        Map<BigDecimal, BigDecimal> sellMap = new HashedMap();
        Set<BigDecimal> sellSet = new HashSet<>();
        //同单价数据合并
        for (Match match : sellList) {
            BigDecimal rest = match.getWill().subtract(match.getOk());
            if (sellMap.get(match.getUnit()) == null) {
                sellMap.put(match.getUnit(), rest);
            } else {
                BigDecimal total = sellMap.get(match.getUnit());
                sellMap.put(match.getUnit(), total.add(rest));
            }
            sellSet.add(match.getUnit());
        }

        for (BigDecimal unit : sellSet) {
            List<BigDecimal> entity = new ArrayList<>();
            entity.add(unit);
            entity.add(sellMap.get(unit));
            asks.add(entity);
        }
        depth.setAsks(asks);
        depth.setBids(bids);
        //卖出委托
        Integer version;
        Depth old = redisUtil.getBean(key, Depth.class);
        if (old == null) {
            old = new Depth();
            old.setVersion(0L);
        }
        depth.setVersion(old.getVersion() + 1);
        depth.setTs(Long.valueOf(System.currentTimeMillis() / 1000 + ""));
        redisUtil.set(key, depth);
        // log.info(">>>深度数据详情>>>> ");
        // log.info(">>>: {} ", depth.toString());
        // log.info(">>> 深度刷新 {}ms", System.currentTimeMillis() - start);
//        }
    }

    //资产为负数信息刷新为01
    @Transactional(rollbackFor = Exception.class)
    public void finZero() {
        //币币列表
        List<Currency> currencyList = currencyService.getBaseMapper().zeroList();

        for (Currency currency : currencyList) {
            //  log.info("资产为负数信息刷新:{}", currency.getUsedPrice());
            if (currency.getUsedPrice().compareTo(BigDecimal.ZERO) < 0) {
                currency.setUsedPrice(BigDecimal.ZERO);
            }
            if (currency.getLockedPrice().compareTo(BigDecimal.ZERO) < 0) {
                currency.setLockedPrice(BigDecimal.ZERO);
            }
            if (currencyService.updateWallet(currency) <= 0)
                throw new UpdateDataException(100);
        }

        List<Wallet> walletList = walletService.getBaseMapper().zeroList();
        for (Wallet wallet : walletList) {
            //    log.info("资产为负数信息刷新:{}", wallet.getUsedPrice());
            if (wallet.getUsedPrice().compareTo(BigDecimal.ZERO) < 0) {
                wallet.setUsedPrice(BigDecimal.ZERO);
            }
            if (wallet.getLockedPrice().compareTo(BigDecimal.ZERO) < 0) {
                wallet.setLockedPrice(BigDecimal.ZERO);
            }
            if (!walletService.updateById(wallet))
                throw new UpdateDataException(100);
        }

    }

    @Transactional
    public void lockCalmAndAuto() {

        LockRecord query = new LockRecord();
        query.setStatus(LockRecordStatus.LOCKING.code)
                .setDel("N");
        List<LockRecord> lockRecordList = lockRecordService.list(new QueryWrapper<>(query));

        //当前时间戳
        long nowTimeStamp = DateTimeUtil.getZeroDate(new Date()).getTime() / 1000;

        //锁仓mge行情价
        BigDecimal lockMgeClose = new BigDecimal(F.me().cfg(LOCK_UNIT));

        for (LockRecord lockRecord : lockRecordList) {
            //收益
            BigDecimal profit = lockRecord.getPrice()
                    .multiply(new BigDecimal(lockRecord.getRate()).divide(new BigDecimal(100), 8, RoundingMode.DOWN))
                    .divide(lockMgeClose, 8, RoundingMode.DOWN);
            //静态收益
            calmProfit(lockRecord, lockMgeClose, profit, nowTimeStamp);
            //动态收益返利
            //TODO: 动态屏蔽 2020-09-28
            log.info("TODO: 动态屏蔽 2020-09-28");
            autoProfit(lockRecord, profit);
        }


    }

    /**
     * 动态收益返利
     *
     * @param lockRecord lockRecord
     * @param profit     收益数量
     */
    private void autoProfit(LockRecord lockRecord, BigDecimal profit) {
        Member member = F.me().getMember(lockRecord.getMemberId());
        String[] pids = member.getParentRefereeId().split("/");
        if (pids.length == 0 || StrUtil.isBlank(pids[0])) {
            return;
        }

        int count = 0;
        for (int i = 0; i < pids.length; i++) {
            if (count >= 3) {
                break;
            }
            if (StrUtil.isBlank(pids[i])) {
                continue;
            }
            Member upMember = F.me().getMember(Long.parseLong(pids[i]));
            if (upMember == null) {
                continue;
            }

            //获取 用户动态比例
            BigDecimal lockAutoRate = getLockAuto(upMember);
            count++;
            if (lockAutoRate.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            BigDecimal autoPrice = profit.multiply(lockAutoRate);
            String remark = String.format("返利基数：{} 动态比例：{}", profit, lockAutoRate);
            //
            //    genLockFlow(upMember, member.getMemberId(), autoPrice, CashFlowTypeEnum.LOCK_AUTO_PROFIT, remark);

        }


    }

    /**
     * 获取达标锁仓动态比例
     *
     * @param upMember member
     * @return 比例
     */
    private BigDecimal getLockAuto(Member upMember) {

        LockAuto lockAuto = standardLockAuto(upMember);
        if (lockAuto == null) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(lockAuto.getRate()).divide(new BigDecimal(100), 8, RoundingMode.DOWN);
        }
    }

    /**
     * 满足的动态收益条件
     *
     * @param upMember
     * @return 动态收益条件
     */
    private LockAuto standardLockAuto(Member upMember) {
        for (int i = 3; i > 0; i--) {
            LockAuto query = new LockAuto();
            query.setCode("A" + i)
                    .setDel("N");
            LockAuto lockAuto = lockAutoService.getOne(new QueryWrapper<>(query));
            if (lockAuto == null) {
                return null;
            }

            String[] lockIds = lockAuto.getRemark().split(",");
            BigDecimal sum = lockRecordService.getBaseMapper().autoStandardSum(upMember.getMemberId(), lockIds, LockRecordStatus.LOCKING.code);

            if (sum.compareTo(lockAuto.getPrice()) >= 0) {
                return lockAuto;
            }
        }
        return null;
    }


    private void calmProfit(LockRecord lockRecord, BigDecimal lockMgeClose, BigDecimal profit, long nowTimeStamp) {
        String mgeCoin = "MGE";
        String fotCoin = "FOT";
        long endTimeStamp = DateTimeUtil.getZeroDate(lockRecord.getEndTime()).getTime() / 1000;
        if (lockRecord.getProfitCount().intValue() + 1 == lockRecord.getDays().intValue() && nowTimeStamp == endTimeStamp) {
            lockRecord.setStatus(LockRecordStatus.WAIT.code);
        }
        lockRecord.setProfitCount(lockRecord.getProfitCount() + 1)
                .setUpdateUser(SYS_PLATFORM);
        lockRecordService.updateById(lockRecord);

        Member member = F.me().getMember(lockRecord.getMemberId());
        //生成锁仓静态收益记录
        LockProfit lockProfitMge = new LockProfit();
        lockProfitMge.setMemberId(member.getMemberId())
                .setLockRecordId(lockRecord.getLockRecordId())
                .setCoin(mgeCoin)
                .setNumber(profit)
                .setCreateUser(SYS_PLATFORM);
        lockProfitService.save(lockProfitMge);
        LockProfit lockProfitFot = new LockProfit();
        lockProfitFot.setMemberId(member.getMemberId())
                .setLockRecordId(lockRecord.getLockRecordId())
                .setCoin(fotCoin)
                .setNumber(profit)
                .setCreateUser(SYS_PLATFORM);
        lockProfitService.save(lockProfitFot);
        //生成流水
        //genLockFlow(member, SYS_PLATFORM, profit, CashFlowTypeEnum.LOCK_CALM_PROFIT, null);


    }

    /**
     * 生成锁仓相关流水
     *
     * @param member           返利的用户
     * @param fromId           来源
     * @param profit           收益
     * @param cashFlowTypeEnum 流水类型
     * @param remark           备注
     */
    private void genLockFlow(Member member, Long fromId, BigDecimal profit, CashFlowTypeEnum cashFlowTypeEnum, String remark) {
        String mgeCoin = "MGE";
        String fotCoin = "FOT";


        //MGE 钱包更新 进入可用
        Wallet mgeWallet = F.me().getWallet(member.getMemberId(), mgeCoin);
        BigDecimal mgeBeforeUse = mgeWallet.getUsedPrice();
        BigDecimal mgeAfterUse = mgeBeforeUse.add(profit);
        mgeWallet.setUsedPrice(mgeAfterUse)
                .setUpdateUser(SYS_PLATFORM);
        if (!walletService.updateById(mgeWallet)) {
            log.info("MGE静态收益更新异常");
            throw new UpdateDataException(100);
        }
        F.me().saveCashflow(mgeWallet.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, cashFlowTypeEnum,
                profit, mgeWallet.getType(), profit, mgeWallet.getType(), BigDecimal.ZERO, mgeWallet.getType(),
                ItemCode.USED, mgeWallet.getType(), null, remark,
                mgeBeforeUse, mgeAfterUse, fromId, mgeWallet.getMemberId());


        //FOT 钱包更新 进入冻结
        Wallet fotWallet = F.me().getWallet(member.getMemberId(), fotCoin);
        BigDecimal fotBeforeLock = fotWallet.getLockedPrice();
        BigDecimal fotAfterLock = fotBeforeLock.add(profit);
        fotWallet.setLockedPrice(fotAfterLock)
                .setGaslock(fotWallet.getGaslock().add(profit))
                .setUpdateUser(SYS_PLATFORM);
        if (!walletService.updateById(fotWallet)) {
            log.error("fot静态收益更新异常");
            throw new UpdateDataException(100);
        }
        F.me().saveCashflow(fotWallet.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, cashFlowTypeEnum,
                profit, fotWallet.getType(), profit, fotWallet.getType(), BigDecimal.ZERO, fotWallet.getType(),
                ItemCode.LOCKED, fotWallet.getType(), null, remark,
                fotBeforeLock, fotAfterLock, fromId, fotWallet.getMemberId());
    }

    @Transactional
    public void lockReceiveListen() {
        LockRecord query = new LockRecord();
        query.setStatus(LockRecordStatus.WAIT.code)
                .setDel("N");
        List<LockRecord> list = lockRecordService.list(new QueryWrapper<>(query));

        for (LockRecord lockRecord : list) {
            //相差天数
            int subDays = DateTimeUtil.periodDays(DateTimeUtil.dateToLocalDate(lockRecord.getEndTime()), DateTimeUtil.dateToLocalDate(new Date()));

            if (subDays != 1) {
                continue;
            }
            //价值刷新判断
            BigDecimal mgeClose = getClosePrice(LOCK_COIN_SYMBOL, KINE);
            BigDecimal lockMinUsdt = new BigDecimal(F.me().cfg(LOCK_MIN_USDT));
            //最新价值
            BigDecimal newValue = mgeClose.multiply(lockRecord.getNumber());
            if (newValue.compareTo(lockMinUsdt) < 0) {
                // 赎回
                gasService.unLockItem(lockRecord);
                continue;
            }
            //:继续投入锁仓 业务
            gasService.continueLockItem(lockRecord, newValue);
        }

    }

    //团队奖
    public void lockTeamListen() {

    }

//    public void updateMemberRechargeAddress() {
//        boolean open = false;
//        if (StrUtil.equals(F.me().cfg(CHAIN_OPEN), "Y")) {
//            open = true;
//        }
//        if (open) {
//            Member member1 = new Member();
//            member1.setDel("N");
//            member1.setStatus("Y");
//            List<Member> list = memberService.list(new QueryWrapper<>(member1).orderByDesc("CREATE_TIME"));
//            Spot spot = new Spot();
//            spot.setDel("N");
//            spot.setStatus("Y");
//            List<Spot> spotList = spotService.list(new QueryWrapper<>(spot));
//            for (Member member : list) {
//                for (Spot spot1 : spotList) {
//                    if (StringUtils.isNotBlank(spot1.getCode())) {
//                        String[] symbols = spot1.getSymbol().split("/");
//                        MemberRechargeAddress memberRechargeAddress = new MemberRechargeAddress();
//                        memberRechargeAddress.setMemberId(member.getMemberId());
//                        memberRechargeAddress.setCoin(symbols[0]);
//                        memberRechargeAddress = this.memberRechargeAddressService.getOne(new QueryWrapper<>(memberRechargeAddress));
//                        if (memberRechargeAddress == null) {
//                            if (StringUtils.isNotBlank(spot1.getCode()) && (!"EOS".equals(symbols[0]) || !"XRP".equals(symbols[0]))) {
//                                Address address = biPayService.createCoinAddress(spot1.getCode(), "", "");
//                                System.out.println(JSONObject.toJSON(address));
//                                if (address != null) {
//                                    memberRechargeAddress = new MemberRechargeAddress();
//                                    memberRechargeAddress.setMemberId(member.getMemberId());
//                                    memberRechargeAddress.setCoin(symbols[0]);
//                                    memberRechargeAddress.setAddress(address.getAddress());
//                                    memberRechargeAddress.setCreateTime(new Date());
//                                    this.memberRechargeAddressService.save(memberRechargeAddress);
//                                }
//                            }
//                            if ("EOS".equals(symbols[0]) || "XRP".equals(symbols[0])) {
//                                String eosXrp = F.me().getSysConfigValueByCode(symbols[0] + "_ADDRESS");
//                                if (StringUtils.isNotBlank(eosXrp)) {
//                                    int length = eosXrp.length();
//                                    String number = getFixLenthString(length);
//                                    MemberRechargeAddress memberRechargeAddress1 = new MemberRechargeAddress();
//                                    memberRechargeAddress1.setMemberId(member.getMemberId());
//                                    memberRechargeAddress1.setCoin(symbols[0]);
//                                    memberRechargeAddress1.setAddress(eosXrp);
//                                    memberRechargeAddress1.setRemark(number);
//                                    int count = this.memberRechargeAddressService.count(new QueryWrapper<>(memberRechargeAddress1));
//                                    while (true) {
//                                        if (count <= 0) {
//                                            memberRechargeAddress = new MemberRechargeAddress();
//                                            memberRechargeAddress.setMemberId(member.getMemberId());
//                                            memberRechargeAddress.setCoin(symbols[0]);
//                                            memberRechargeAddress.setAddress(eosXrp);
//                                            memberRechargeAddress.setRemark(number);
//                                            memberRechargeAddress.setCreateTime(new Date());
//                                            this.memberRechargeAddressService.save(memberRechargeAddress);
//                                            break;
//                                        }
//                                        number = getFixLenthString(length);
//                                        memberRechargeAddress1 = new MemberRechargeAddress();
//                                        memberRechargeAddress1.setMemberId(member.getMemberId());
//                                        memberRechargeAddress1.setCoin(symbols[0]);
//                                        memberRechargeAddress1.setAddress(eosXrp);
//                                        memberRechargeAddress1.setRemark(number);
//                                        count = this.memberRechargeAddressService.count(new QueryWrapper<>(memberRechargeAddress1));
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private static String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    public List<Long> getDateArr(Long now, Integer rec, int forNum) {

        List<Long> longs = new ArrayList<>();
        for (int i = 0; i < forNum; i++) {
            long recc = rand.nextInt(rec) + now;
            longs.add(recc);
        }
        Collections.sort(longs);
        Collections.reverse(longs);
        return longs;
    }

    @Transactional
    public void miningCheck(MiningOrder miningOrder) {
        Integer miningedDays = miningOrder.getMiningedDays();//挖矿天数
        Integer miningDays = miningOrder.getMiningDays();//持续
        Calendar instance = Calendar.getInstance();
        Long endTime = Long.valueOf(miningOrder.getEndTime());
        if (instance.getTimeInMillis() >= endTime || miningOrder.getCycleNumber() <= (miningDays + miningedDays)) {//到期结束
            miningOrder.setMiningStatus("2");
            if (miningOrder.getEnergyToBeConsumed().compareTo(BigDecimal.ZERO) > 0) {
                FinMining energyMining = F.me().getMining(miningOrder.getMemberId(), miningOrder.getFuelEnergyCoin());
                BigDecimal usedPrice1 = energyMining.getUsedPrice();
                energyMining.setUsedPrice(usedPrice1.add(miningOrder.getEnergyToBeConsumed()));
                //日志
                F.me().saveCashflow(miningOrder.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.MINING_ENERGY_RETURN,
                        miningOrder.getEnergyToBeConsumed(), miningOrder.getFuelEnergyCoin(), miningOrder.getEnergyToBeConsumed(),
                        miningOrder.getFuelEnergyCoin(), BigDecimal.ZERO, miningOrder.getFuelEnergyCoin(),
                        ItemCode.USED, miningOrder.getFuelEnergyCoin(), null, null,
                        usedPrice1, energyMining.getUsedPrice(), miningOrder.getMemberId(), miningOrder.getMemberId());
            }
        } else {//需要挖矿
            //损耗能源 ，当损耗能源不足，则停止 赋值状态

            BigDecimal energyLossNumber = miningOrder.getEnergyLossNumber();
            BigDecimal usedPrice = miningOrder.getEnergyToBeConsumed();

            //产能/日
            BigDecimal esCapacity = miningOrder.getEstimatedCapacity();
            BigDecimal esCaDay = esCapacity.divide(new BigDecimal(miningOrder.getCycleNumber()), 2);

            //静态化/日
            BigDecimal esStaticYield = miningOrder.getEstimatedStaticYield();
            BigDecimal esStDay = esStaticYield.divide(new BigDecimal(miningOrder.getCycleNumber()), 2);

            if (usedPrice.compareTo(energyLossNumber) < 0) {
                //当能源不够，钱包有能源则扣除
                FinMining energyMining = F.me().getMining(miningOrder.getMemberId(), miningOrder.getFuelEnergyCoin());
                BigDecimal usedPrice1 = energyMining.getUsedPrice();
                BigDecimal fuelEnergyNumber = miningOrder.getFuelEnergyNumber();
                if (usedPrice1.compareTo(fuelEnergyNumber) > 0) {//足够
                    energyMining.setUsedPrice(usedPrice1.subtract(fuelEnergyNumber));
                    energyMining.setUpdateUser(SYS_PLATFORM);
                    energyMining.setUpdateTime(new Date());
                    //扣除能源消耗
                    finMiningService.updateById(energyMining);
                    //挖矿天数增加
                    miningOrder.setMiningedDays(miningedDays + 1);
                    miningOrder.setEnergyToBeConsumed(fuelEnergyNumber.subtract(miningOrder.getEnergyLossNumber()));
                    //挖矿增加一次差能
                    miningOrder.setReceived(miningOrder.getReceived().add(esCaDay));
                    //日志
                    F.me().saveCashflow(miningOrder.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.MINING_ENERGY_LOSS,
                            fuelEnergyNumber, miningOrder.getFuelEnergyCoin(), fuelEnergyNumber,
                            miningOrder.getFuelEnergyCoin(), BigDecimal.ZERO, miningOrder.getFuelEnergyCoin(),
                            ItemCode.USED, miningOrder.getFuelEnergyCoin(), null, null,
                            usedPrice1, energyMining.getUsedPrice(), miningOrder.getMemberId(), miningOrder.getMemberId());

                } else {//挖矿增加一次静态化收益
                    miningOrder.setAvailable(miningOrder.getAvailable().add(esStDay));
                    miningOrder.setMiningDays(miningDays + 1);
                }

//                miningOrderService.updateById(miningOrder);
//                return;
            } else {
                //能源足够
                miningOrder.setMiningDaysprofit(miningOrder.getCurrentDaysprofit());
                miningOrder.setCurrentDaysprofit(BigDecimal.ONE);
                //扣除能源消耗
                miningOrder.setEnergyToBeConsumed(usedPrice.subtract(energyLossNumber));
                //增加产能
                miningOrder.setReceived(miningOrder.getReceived().add(esCaDay));
                //挖矿天数增加
                miningOrder.setMiningedDays(miningedDays + 1);
            }


        }

        if (instance.getTimeInMillis() >= endTime || miningOrder.getCycleNumber() <= (miningDays + miningedDays + 1)) {//到期结束
            miningOrder.setMiningStatus("2");
            if (miningOrder.getEnergyToBeConsumed().compareTo(BigDecimal.ZERO) > 0) {
                FinMining energyMining = F.me().getMining(miningOrder.getMemberId(), miningOrder.getFuelEnergyCoin());
                BigDecimal usedPrice1 = energyMining.getUsedPrice();
                energyMining.setUsedPrice(usedPrice1.add(miningOrder.getEnergyToBeConsumed()));
                //日志
                F.me().saveCashflow(miningOrder.getMemberId(), WalletBigType.MINING, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.MINING_ENERGY_RETURN,
                        miningOrder.getEnergyToBeConsumed(), miningOrder.getFuelEnergyCoin(), miningOrder.getEnergyToBeConsumed(),
                        miningOrder.getFuelEnergyCoin(), BigDecimal.ZERO, miningOrder.getFuelEnergyCoin(),
                        ItemCode.USED, miningOrder.getFuelEnergyCoin(), null, null,
                        usedPrice1, energyMining.getUsedPrice(), miningOrder.getMemberId(), miningOrder.getMemberId());
            }

        }

        miningOrder.setUpdateUser(SYS_PLATFORM);
        miningOrder.setUpdateTime(instance.getTime());
        miningOrderService.updateById(miningOrder);


    }
}
