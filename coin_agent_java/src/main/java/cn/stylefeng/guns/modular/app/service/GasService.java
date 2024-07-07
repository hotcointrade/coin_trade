package cn.stylefeng.guns.modular.app.service;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.controller.market.Kline;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.e.entity.Lock;
import cn.stylefeng.guns.modular.e.entity.LockProfit;
import cn.stylefeng.guns.modular.e.entity.LockRecord;
import cn.stylefeng.guns.modular.e.service.LockProfitService;
import cn.stylefeng.guns.modular.e.service.LockRecordService;
import cn.stylefeng.guns.modular.e.service.LockService;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 锁仓挖矿服务
 */
@Service
@Slf4j
public class GasService extends Constant {

    @Resource
    private LockService lockService;
    @Resource
    private LockRecordService lockRecordService;
    @Resource
    private LockProfitService lockProfitService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    HomeService homeService;

    @Resource
    WalletService walletService;

    /**
     * 锁仓币种
     */
    private static final String LOCK_COIN = "MGE";
    //USDT
    private static final String USDT = "USDT";
    private static final String LOCK_COIN_SYMBOL = "MGE/USDT";
    private static final String KINE_ = "KINE:";

    public Result lockPage(String token) {

        Member member = (Member) redisUtil.get(token);

        Map<String, Object> map = new HashMap<>();
        Kline entity = (Kline) redisUtil.get(KINE_ + LOCK_COIN_SYMBOL + _NEW);
        if (entity == null)
            return fail(ApiStatus.BAD_REQUEST);
        map.put("price", F.me().getWallet(member, LOCK_COIN).getUsedPrice());
        map.put("lockList", lockService.list());
        map.put("close", entity.getClose());
        map.put("lockMinUsdt", F.me().cfg(LOCK_MIN_USDT));
        return success(map);
    }

    public Result lockList(String token, String status, Page page) {
        Member member = (Member) redisUtil.get(token);
        LockRecord query = new LockRecord();
        query.setMemberId(member.getMemberId()).setStatus(status).setDel("N");
        return success(lockRecordService.page(page, new QueryWrapper<>(query).orderByDesc(CREATE_TIME)));
    }


    public Result lockProfit(String token, Long lockRecordId, Page page) {
        Member member = (Member) redisUtil.get(token);

        LockProfit query = new LockProfit();
        query.setLockRecordId(lockRecordId)
                .setMemberId(member.getMemberId())
                .setDel("N");

        Map<String, Object> map = new HashMap<>();

        map.put("mgeTotal", lockProfitService.getBaseMapper().coinTotal(lockRecordId, "MGE"));
        map.put("fotTotal", lockProfitService.getBaseMapper().coinTotal(lockRecordId, "FOT"));
        map.put("page", lockProfitService.page(page, new QueryWrapper<>(query)));
        return success(map);
    }




    @Transactional(rollbackFor = Exception.class)
    public Result doLock(String token, Long lockId, BigDecimal number, String payPwd) {
        Member member = (Member) redisUtil.get(token);
        if (!homeService.isTruePayPwd(member, payPwd)) {
            return fail(ApiStatus.ERROR_PAY_PWD);
        }
        Wallet wallet = F.me().getWallet(member, LOCK_COIN);

        if (wallet.getUsedPrice().compareTo(number) < 0) {
            return fail(ApiStatus.WALLET_LESS);
        }
//        锁仓最低USDT价值
        BigDecimal lockMinUsdt = new BigDecimal(F.me().cfg(LOCK_MIN_USDT));
        Kline entity = (Kline) redisUtil.get(KINE_ + LOCK_COIN_SYMBOL + _NEW);
        float close = entity.getClose();

        //价值达标
        BigDecimal value = new BigDecimal(close).multiply(number);
        if (lockMinUsdt.compareTo(value) > 0) {
            return fail(ApiStatus.LOCK_USDT_LOW);
        }

        Lock lock = lockService.getById(lockId);
        if (lock == null) {
            return fail(ApiStatus.BAD_REQUEST);
        }

        Date now = new Date();

        //起始
        LocalDate nowZeroDate = DateTimeUtil.parseLocalDate(DateTimeUtil.dateToStr(now), DateTimeUtil.STANDARD_FORMAT);
        //结束
        LocalDate endZero = nowZeroDate.plusDays(lock.getDays());


        LockRecord lockRecord = new LockRecord();
        lockRecord.setMemberId(member.getMemberId())
                .setLockId(lockId)
                .setLockName(lock.getName())
                .setRate(lock.getRate())
                .setDays(lock.getDays())
                .setPrice(value)
                .setNumber(number)
                .setStartTime(DateTimeUtil.localDateToDate(nowZeroDate))
                .setEndTime(DateTimeUtil.localDateToDate(endZero))
                .setProfitCount(0L)
                .setStatus(LockRecordStatus.LOCKING.code)
                .setCreateUser(SYS_PLATFORM);
        lockRecordService.save(lockRecord);

        BigDecimal flowPrice = number;
        BigDecimal beforeUsed = wallet.getUsedPrice();
        BigDecimal afterUsed = beforeUsed.subtract(flowPrice);

        BigDecimal beforeLock = wallet.getLockedPrice();
        BigDecimal afterLock = beforeLock.add(flowPrice);

        wallet.setUsedPrice(afterUsed)
                .setLockedPrice(afterLock)
                .setUpdateUser(wallet.getMemberId());
        if (!walletService.updateById(wallet)) {
            throw new UpdateDataException(100);
        }

//        //可用流水 -
//        F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.LOCK,
//                flowPrice, LOCK_COIN, flowPrice, LOCK_COIN, BigDecimal.ZERO, LOCK_COIN,
//                ItemCode.USED, LOCK_COIN, null, null,
//                beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
//        //冻结流水 +
//        F.me().saveCashflow(member.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.LOCK,
//                flowPrice, LOCK_COIN, flowPrice, LOCK_COIN, BigDecimal.ZERO, LOCK_COIN,
//                ItemCode.LOCKED, LOCK_COIN, null, null,
//                beforeLock, afterLock, member.getMemberId(), member.getMemberId());

        return success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result continueLock(String token, Long lockRecordId, String payPwd) {
        Member member = (Member) redisUtil.get(token);
        if (!homeService.isTruePayPwd(member, payPwd)) {
            return fail(ApiStatus.ERROR_PAY_PWD);
        }
        LockRecord query = new LockRecord();
        query.setMemberId(member.getMemberId())
                .setLockRecordId(lockRecordId)
                .setStatus(LockRecordStatus.WAIT.code)
                .setDel("N");
        LockRecord lockRecord = lockRecordService.getOne(new QueryWrapper<>(query));
        if (lockRecord == null) {
            return fail(ApiStatus.BAD_REQUEST);
        }

        //价值刷新判断
        BigDecimal mgeClose=getClosePrice(LOCK_COIN_SYMBOL,KINE);
        BigDecimal lockMinUsdt=new BigDecimal(F.me().cfg(LOCK_MIN_USDT));
        //最新价值
        BigDecimal newValue=mgeClose.multiply(lockRecord.getNumber());
        if(newValue.compareTo(lockMinUsdt)<0)
        {
            return fail(ApiStatus.LOCK_USDT_LOW);
        }
        //继续投入锁仓 业务
        continueLockItem(lockRecord,newValue);
        return success();
    }
    /**
     * 获取币种最新价
     *
     * @param symbols 交易对
     * @param type    类型：现货:KINE 期货-永续:KINE_PERPETUAL
     * @return
     */
    private BigDecimal getClosePrice(String symbols, String type) {
        Kline map = (Kline) redisUtil.get(type + symbols + _NEW);
        return map == null ? null : new BigDecimal(map.getClose());
    }
    @Transactional(rollbackFor = Exception.class)
    public Result unLock(String token, Long lockRecordId) {
        Member member = (Member) redisUtil.get(token);
        LockRecord query = new LockRecord();
        query.setMemberId(member.getMemberId())
                .setLockRecordId(lockRecordId)
                .setDel("N");
        LockRecord lockRecord = lockRecordService.getOne(new QueryWrapper<>(query));
        if (lockRecord == null) {
            return fail(ApiStatus.BAD_REQUEST);
        }
        lockRecord.setStatus(LockRecordStatus.FINISH.code)
                .setUpdateUser(member.getMemberId());
        lockRecordService.updateById(lockRecord);
        unLockItem(lockRecord);

        return success();
    }

    public Result unLockTip(String token, Long lockRecordId) {
        Member member = (Member) redisUtil.get(token);
        LockRecord query = new LockRecord();
        query.setMemberId(member.getMemberId())
                .setLockRecordId(lockRecordId)
                .setStatus(LockRecordStatus.WAIT.code)
                .setDel("N");
        LockRecord lockRecord = lockRecordService.getOne(new QueryWrapper<>(query));
        if (lockRecord == null) {
            return fail(ApiStatus.BAD_REQUEST);
        }
        Map<String, Object> map = new HashMap<>();

        map.put("coin", member.getUnlockc());
        map.put("price", member.getUnlockc().equals(USDT) ? lockRecord.getPrice() : lockRecord.getNumber());

        return success(map);
    }

    /**
     * 赎回明细
     * @param lockRecord  锁仓记录
     */
    public void unLockItem(LockRecord lockRecord) {
        Member member=F.me().getMember(lockRecord.getMemberId());
        //赎回币种
        String receiveCoin=member.getUnlockc();
        //赎回数量
        BigDecimal receiveFlowPrice=receiveCoin.equals(USDT)? lockRecord.getPrice() : lockRecord.getNumber();

        //冻结数量
        BigDecimal lockFlowPrice=lockRecord.getNumber();

        //冻结账户更新
        Wallet lockWallet=F.me().getWallet(member,LOCK_COIN);
        BigDecimal beforeLock=lockWallet.getLockedPrice();
        BigDecimal afterLock=beforeLock.subtract(lockFlowPrice);
        //冻结流水 -
        lockWallet.setLockedPrice(afterLock)
                .setUpdateUser(SYS_PLATFORM);
        if(!walletService.updateById(lockWallet))
        {
            log.error("更新失败！{}",lockWallet.toString());
            throw new UpdateDataException(100);
        }
//        F.me().saveCashflow(member.getMemberId(),WalletBigType.WALLET,CashFlowOpEnum.FLOW_OUT,CashFlowTypeEnum.UNLOCK,
//                lockFlowPrice,LOCK_COIN,lockFlowPrice,LOCK_COIN,BigDecimal.ZERO,LOCK_COIN,
//                ItemCode.LOCKED,LOCK_COIN,null,null,
//                beforeLock,afterLock,member.getMemberId(),member.getMemberId());
        //赎回账户更新
        Wallet receiveWallet=F.me().getWallet(member,receiveCoin);
        BigDecimal beforeUsed=receiveWallet.getUsedPrice();
        BigDecimal afterUsed=beforeUsed.add(receiveFlowPrice);
        //可用流水 +
        receiveWallet.setUsedPrice(afterUsed)
                .setUpdateUser(SYS_PLATFORM);
        if(!walletService.updateById(receiveWallet))
        {
            log.error("更新失败！{}",lockWallet.toString());
            throw new UpdateDataException(100);
        }
//        F.me().saveCashflow(member.getMemberId(),WalletBigType.WALLET,CashFlowOpEnum.FLOW_IN,CashFlowTypeEnum.UNLOCK,
//                receiveFlowPrice,receiveCoin, receiveFlowPrice,receiveCoin, BigDecimal.ZERO,receiveCoin,
//                ItemCode.USED,receiveCoin,null,null,
//                beforeUsed,afterUsed,member.getMemberId(),member.getMemberId());
    }

    /**
     * 继续锁仓操作
     * @param lockRecord
     * @param newValue 最新价值
     */
    public void continueLockItem(LockRecord lockRecord,BigDecimal newValue) {

        //step 1:结束本单
        lockRecord.setStatus(LockRecordStatus.FINISH.code).setUpdateUser(SYS_PLATFORM);
        lockRecordService.updateById(lockRecord);
        //step 2:更新新单
        Lock lock = lockService.getById(lockRecord.getLockId());

        Date now = new Date();

        //起始
        java.time.LocalDate nowZeroDate = DateTimeUtil.parseLocalDate(DateTimeUtil.dateToStr(now), DateTimeUtil.STANDARD_FORMAT);
        //结束
        LocalDate endZero = nowZeroDate.plusDays(lock.getDays());


        LockRecord newLockRecord = new LockRecord();
        newLockRecord.setMemberId(lockRecord.getMemberId())
                .setLockId(lockRecord.getLockId())
                .setLockName(lock.getName())
                .setRate(lock.getRate())
                .setDays(lock.getDays())
                .setPrice(newValue)
                .setNumber(lockRecord.getNumber())
                .setStartTime(DateTimeUtil.localDateToDate(nowZeroDate))
                .setEndTime(DateTimeUtil.localDateToDate(endZero))
                .setProfitCount(0L)
                .setStatus(LockRecordStatus.LOCKING.code)
                .setCreateUser(SYS_PLATFORM);
        lockRecordService.save(newLockRecord);
    }
}
