package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.websocket.WebSocketService;
import cn.stylefeng.guns.modular.app.common.ApiStatus;
import cn.stylefeng.guns.modular.app.dto.otc.*;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.entity.Payment;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.guns.modular.base.util.RandomUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
import cn.stylefeng.guns.modular.bulletin.service.SendSMSExtService;
import cn.stylefeng.guns.modular.coin.entity.Spot;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.service.LegalService;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import cn.stylefeng.guns.modular.otc.entity.*;
import cn.stylefeng.guns.modular.otc.service.*;
import cn.stylefeng.roses.core.util.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OtcService extends Constant {
    //状态：是
    public static final String YES = "1";
    //状态：否
    public static final String NO = "0";

    //状态：审核中
    public static final String CHECK = "2";

    public static final String BUY = "BUY";

    public static final String SELL = "SELL";

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MemberService memberService;

    @Autowired
    DepositService depositService;

    @Autowired
    HomeService homeService;

    @Autowired
    LegalService legalService;

    @Autowired
    BackService backService;

    @Autowired
    BuyService buyService;

    @Autowired
    SellService sellService;

    @Autowired
    BillService billService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    SendSMSExtService sendSMSExtService;
    @Autowired
    AppealService appealService;

    @Resource
    private WalletService walletService;

    public Result legalMgrPage(String token){
        Member member = (Member) redisUtil.get(token);
        Map<String, Object> map = new HashedMap();
        //昵称，为空则需要补填
        map.put("nickName", member.getNickName());
        map.put("realStatus", member.getRealStatus());
        //押金缴纳情况 0-待补缴 1-全部缴纳 2-未开通  3:押金退还审核中
        // 4.需要成为承兑商 5.已获得承兑商权限 6.承兑商申请审核中 7.承兑商审核失败
        switch (member.getOtc()){
            case "0":map.put("deposit",4);break;
            case "1":map.put("deposit",5);break;
            case "2":map.put("deposit",6);break;
            case "3":map.put("deposit",7);break;
        }

        Deposit deposit = getDeposit(member);
        if(member.getOtc().equals("1")){
            map.put("deposit",deposit == null ? "2" : getCheckBack(member) != null ? "3" : deposit.getStatus());
            map.put("USDT", F.me().cfg("OTC_BAIL_USDT"));
            map.put("usedPrice",F.me().getLegal(member.getMemberId(), "USDT").getUsedPrice());
            map.put("number",deposit != null ? deposit.getNumber() : F.me().cfg("OTC_BAIL_USDT"));
        }else{
            map.put("USDT", F.me().cfg("OTC_BAIL_USDT"));
            map.put("usedPrice",F.me().getLegal(member.getMemberId(), "USDT").getUsedPrice());
            map.put("number",F.me().cfg("OTC_BAIL_USDT"));
        }
        map.put("haveAppeal","N");
        if (member.getOtc() != null && "1".equals(member.getOtc())){
            map.put("haveAppeal","Y");
            if (member.getHasOtc() == null || "0".equals(member.getHasOtc())){
                member.setHasOtc("1");
                this.memberService.updateById(member);
                redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));

            }
        }else if (member.getHasOtc() != null && member.getHasOtc().equals("1")){
            map.put("haveAppeal","Y");
        }
        return success(map);
    }

    public Result nickName(String token, String nickName)
    {
        Member member = (Member) redisUtil.get(token);

        if (member.getNickName() != null && StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.BAD_REQUEST);
        Member query = new Member();
        query.setNickName(nickName.trim()).setDel("N");
        if (memberService.getOne(new QueryWrapper<>(query)) != null)
            return fail(ApiStatus.EXIST_NICK_NAME);

        member.setNickName(nickName.trim())
                .setUpdateUser(SYS_PLATFORM);
        memberService.updateById(member);

        redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));

        return success();
    }


    public Result depositPage(String token){
        Member member = (Member) redisUtil.get(token);
        if (isExistDeposit(member))
            return fail(ApiStatus.BAD_REQUEST);
        Map<String, Object> map = new HashedMap();
        map.put("USDT", F.me().cfg("OTC_BAIL_USDT"));
        map.put("usedPrice",F.me().getLegal(member.getMemberId(), "USDT").getUsedPrice());
        return success(map);
    }


    /**
     * 是否存在缴纳押金记录
     *
     * @param member
     * @return
     */
    private boolean isExistDeposit(Member member)
    {
        Deposit query = new Deposit();
        query.setDel("N")
                .setMemberId(member.getMemberId())
        ;
        return depositService.getOne(new QueryWrapper<>(query)) != null ? true : false;
    }

    /**
     * 获取缴纳押金记录 ，为空则不存在
     *
     * @param member
     * @return
     */
    public Deposit getDeposit(Member member)
    {
        Deposit query = new Deposit();
        query.setDel("N")
                .setMemberId(member.getMemberId())
        ;
        return depositService.getOne(new QueryWrapper<>(query));
    }


    @Transactional(rollbackFor = Exception.class)
    public Result deposit(String token, String type, String payPwd){
        String prefix = "OTC_BAIL_";
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        if (StrUtil.isBlank(member.getPayPassword()))
            return fail(ApiStatus.PAY_PWD_EMPTY);
        if (isExistDeposit(member))
            return fail(ApiStatus.EXIT_APPEAL_OTC);
        if (member.getPayPassword() == null)
            return fail(ApiStatus.PAY_PWD_EMPTY);
        if (!homeService.isTruePayPwd(member, payPwd))
            return fail(ApiStatus.ERROR_PAY_PWD);
        BigDecimal price = new BigDecimal(F.me().cfg(prefix + type));
        Legal legal = F.me().getLegal(member.getMemberId(), type);
        if (legal.getUsedPrice().compareTo(price) < 0)
            return fail(ApiStatus.WALLET_LESS);
        //押金记录
        Deposit deposit = new Deposit();
        deposit.setMemberId(member.getMemberId())
                .setNickName(member.getNickName())
                .setCoin(type)
                .setNumber(price)
                .setRemark(WithdrawStatusEnum.CHECK.getCode()) //审核中
                .setStatus("1") //全额缴纳-1   待补缴-0
                .setCreateUser(SYS_PLATFORM);
        depositService.save(deposit);

        member.setOtc("2").setUpdateUser(SYS_PLATFORM);
        memberService.updateById(member);
        redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
        BigDecimal beforeUsed = legal.getUsedPrice();
        BigDecimal afterUsed = legal.getUsedPrice().subtract(price);
        BigDecimal beforeLock = legal.getLockedPrice();
        BigDecimal afterLock = legal.getLockedPrice().add(price);
        legal.setUsedPrice(afterUsed)
                .setLockedPrice(afterLock)
                .setUpdateUser(SYS_PLATFORM);
        if (!legalService.updateById(legal))
            throw new UpdateDataException(100);
        //可用流水
        F.me().saveCashflow(member.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.DEPOSIT,
                price, type, price, type, BigDecimal.ZERO, type,
                ProConst.ItemCode.USED, type, null, null,
                beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
        //冻结流水
        F.me().saveCashflow(member.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.DEPOSIT,
                price, type, price, type, BigDecimal.ZERO, type,
                ProConst.ItemCode.LOCKED, type, null, null,
                beforeLock, afterLock, member.getMemberId(), member.getMemberId());
        try {
            WebSocketService.sendInfo(DEPOSITSTRING,SYSTEMSOCKETSENDNAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success();
    }


    public Result verifySwitch(String token){
//        String idcardOpen = F.me().cfg("IDCARD_OPEN");
//        if (idcardOpen.equals(NO))
//            return success();
        Member member = (Member) redisUtil.get(token);
        Map<String, Object> map = new HashMap<>();
        map.put("isOtc",member.getOtc());
        return  Result.success(map);
    }



 /*

        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================

                                        待同步接口



        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================
        ========================================================================================================================

     */


    public Result makeUpDepositPage(String token){
        Member member = (Member) redisUtil.get(token);

        Deposit deposit = getDeposit(member);
        if (deposit == null)
            return fail(ApiStatus.BAD_REQUEST);
        //状态 全额缴纳-1   待补缴-0
        if (deposit.getStatus().equals(YES))
            return fail(ApiStatus.BAD_REQUEST);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        //基准值
        BigDecimal otc = new BigDecimal(F.me().cfg("OTC_BAIL_" + deposit.getCoin()));
        //补缴金额
        BigDecimal makePrice = otc.subtract(deposit.getNumber());
        if (makePrice.compareTo(BigDecimal.ZERO) <= 0)
            return fail(ApiStatus.BAD_REQUEST);
        Map<String, Object> map = new HashedMap();
        map.put("coin", deposit.getCoin());
        map.put("number", makePrice);
        return success(map);
    }


    @Transactional(rollbackFor = Exception.class)
    public Result makeUpDeposit(String token, String payPwd)
    {
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        if (!homeService.isTruePayPwd(member, payPwd))
            return fail(ApiStatus.ERROR_PAY_PWD);

        //是否有退还审核订单
        if (getCheckBack(member) != null)
            return fail(ApiStatus.BACK_CHECK_EXIST);

        Deposit deposit = getDeposit(member);
        if (deposit == null)
            return fail(ApiStatus.BAD_REQUEST);
        //状态 全额缴纳-1   待补缴-0
        if (deposit.getStatus().equals(YES))
            return fail(ApiStatus.BAD_REQUEST);

        //基准值
        BigDecimal otc = new BigDecimal(F.me().cfg("OTC_BAIL_" + deposit.getCoin()));
//        BigDecimal otc = new BigDecimal(member.getDeposit());

        //补缴金额
        BigDecimal makePrice = otc.subtract(deposit.getNumber());
        if (makePrice.compareTo(BigDecimal.ZERO) <= 0)
            return fail(ApiStatus.BAD_REQUEST);

        Legal legal = F.me().getLegal(member.getMemberId(), deposit.getCoin());

        if (legal.getUsedPrice().compareTo(makePrice) < 0)
            return fail(ApiStatus.WALLET_LESS);

        deposit.setNumber(deposit.getNumber().add(makePrice))
                .setStatus(YES)   //状态 全额缴纳-1   待补缴-0
                .setUpdateUser(member.getMemberId());

        if (!depositService.updateById(deposit))
            throw new UpdateDataException(100);


        BigDecimal beforeUsed = legal.getUsedPrice();

        BigDecimal afterUsed = legal.getUsedPrice().subtract(makePrice);

        BigDecimal beforeLock = legal.getLockedPrice();
        BigDecimal afterLock = legal.getLockedPrice().add(makePrice);


        legal.setUsedPrice(afterUsed)
                .setLockedPrice(afterLock)
                .setUpdateUser(SYS_PLATFORM);
        if (!legalService.updateById(legal))
            throw new UpdateDataException(100);

        //挂单次数清除
        redisUtil.del(String.format(OTC_CANCEL, member.getMemberId()));

        //可用流水 -
        F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.MAKE_UP_PRICE,
                makePrice, deposit.getCoin(), makePrice, deposit.getCoin(), BigDecimal.ZERO, deposit.getCoin(),
                ItemCode.USED, deposit.getCoin(), null, null,
                beforeUsed, afterUsed,
                member.getMemberId(), member.getMemberId());

        //冻结流水 +
        F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.MAKE_UP_PRICE,
                makePrice, deposit.getCoin(), makePrice, deposit.getCoin(), BigDecimal.ZERO, deposit.getCoin(),
                ItemCode.LOCKED, deposit.getCoin(), null, null,
                beforeLock, afterLock,
                member.getMemberId(), member.getMemberId());

        return success();
    }

    public Result backDepositPage(String token){
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Deposit deposit = getDeposit(member);
        if (deposit == null)
            return fail(ApiStatus.BAD_REQUEST);
        Map<String, Object> map = new HashedMap();
        map.put("number", deposit.getNumber());
        map.put("coin", deposit.getCoin());
        map.put("back", 0);
        //是否在退还
        Back back = getCheckBack(member);
        if (back != null)
        {
            map.put("back", 1);
            map.put("number", back.getNumber());
        }

        return success(map);

    }

    public Result backDeposit(String token){
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Deposit deposit = getDeposit(member);
        if (deposit == null)
            return fail(ApiStatus.BAD_REQUEST);
        //是否有审核单
        Back query = getCheckBack(member);
        if (query != null)
            return fail(ApiStatus.BACK_CHECK);
        //：是否存在有挂单
        if (isExistOtcOrder(member))
            return fail(ApiStatus.EXIST_OTC_ORDER);
        Buy queryBuy = new Buy();
        queryBuy.setMemberId(member.getMemberId())
                .setDel("N")
                .setStatus("Y");
        List<Buy> list = buyService.list(new QueryWrapper<>(queryBuy));
        for (Buy buy : list){
            cancelOrder(token, BUY, buy.getOrderNo());
        }
        Sell querySell = new Sell();
        querySell.setMemberId(member.getMemberId())
                .setDel("N")
                .setStatus("Y");
        List<Sell> sellList = sellService.list(new QueryWrapper<>(querySell));
        for (Sell sell : sellList){
            cancelOrder(token, SELL, sell.getOrderNo());
        }

        Back back = new Back();
        back.setCoin(deposit.getCoin())
                .setMemberId(member.getMemberId())
                .setNickName(member.getNickName())
                .setNumber(deposit.getNumber())
                .setStatus(Status.CHECK.code)
                .setCreateUser(member.getMemberId());
        backService.save(back);
        try {
            WebSocketService.sendInfo(BACKDEPOSITSTRING,SYSTEMSOCKETSENDNAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success("退回成功",200);
    }

    //    如挂单中有订单尚在交易过程中（即所有订单未全部处于已完成或已取消状态），则无法退还押金，提示“存在订单尚未处理完毕”，
    private boolean isExistOtcOrder(Member member)
    {
//        Buy query = new Buy();
//        query.setMemberId(member.getMemberId())
//                .setDel("N")
//                .setStatus("Y");
//        if (buyService.count(new QueryWrapper<>(query)) > 0)
//            return true;
//        Sell querySell = new Sell();
//        querySell.setMemberId(member.getMemberId())
//                .setDel("N")
//                .setStatus("Y");
//        if (sellService.count(new QueryWrapper<>(querySell)) > 0)
//            return true;

        int count = billService.getBaseMapper().inOrder(member.getMemberId());
        if (count > 0)
            return true;
        return false;
    }


    /**
     * 获取退还押金审核单
     *
     * @param member
     * @return Back
     */
    public Back getCheckBack(Member member)
    {
        Back query = new Back();
        query.setDel("N")
                .setMemberId(member.getMemberId())
                .setStatus(Status.CHECK.code);
        return backService.getOne(new QueryWrapper<>(query));
    }


    public Result otcBuy(String token, BuyDto dto){
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Deposit deposit = getDeposit(member);
        if (deposit == null || deposit.getStatus().equals("0")){   //状态 全额缴纳-1   待补缴-0
            return Result.fail(ApiStatus.ADD_OTC_MONEY);
        }
        if(!member.getOtc().equals("1")) //非承兑商，不可挂单
            return fail(ApiStatus.BAD_REQUEST);

        Buy buy = new Buy();
        BeanUtil.copyProperties(dto, buy);
        buy.setMemberId(member.getMemberId())
                .setNickName(member.getNickName())
                .setOrderNo(RandomUtil.code("OB"))
                .setStatus("Y") //状态(是否仍在挂单) Y:是 N:否
                .setRestNumber(dto.getNumber()).setRemark(dto.getRemark())
                .setFinishNumber(BigDecimal.ZERO)
                .setTotalPrice(dto.getUnitPrice().multiply(dto.getNumber()))
                .setCreateUser(member.getMemberId());
        buyService.save(buy);
        return success(buy);
    }

    public Result otcSell(String token, SellDto dto){
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Deposit deposit = getDeposit(member);
        if (deposit == null || deposit.getStatus().equals("0")) //状态 全额缴纳-1   待补缴-0
            return Result.fail(ApiStatus.ADD_OTC_MONEY);
        if(!member.getOtc().equals("1")) //非承兑商，不可挂单
            return fail(ApiStatus.BAD_REQUEST);
        Legal legal = F.me().getLegal(member.getMemberId(), dto.getCoin());
        if (legal.getUsedPrice().compareTo(dto.getNumber()) < 0)
            return fail(ApiStatus.WALLET_LESS);

        Payment payment = paymentService.getById(dto.getPaymentId());
        if (payment == null || payment.getMemberId().longValue() != member.getMemberId().longValue())
            return fail(ApiStatus.BAD_REQUEST);

        Sell sell = new Sell();
        BeanUtil.copyProperties(dto, sell);

        sell.setMemberId(member.getMemberId())
                .setNickName(member.getNickName())
                .setOrderNo(RandomUtil.code("OS"))
                .setStatus("Y")
                .setRestNumber(dto.getNumber())
                .setFinishNumber(BigDecimal.ZERO).setRemark(dto.getRemark())
                .setTotalPrice(dto.getUnitPrice().multiply(dto.getNumber()))
                .setPayAccount(payment.getIdcard())
                .setPayName(payment.getName())
                .setPayImg(payment.getImg())
                .setBank(payment.getBank())
                .setBranch(payment.getBranch())
                .setCreateUser(member.getMemberId());
        sellService.save(sell);
        BigDecimal beforeUsed = legal.getUsedPrice();
        BigDecimal afterUsed = legal.getUsedPrice().subtract(dto.getNumber());
        BigDecimal beforeLock = legal.getLockedPrice();
        BigDecimal afterLock = legal.getLockedPrice().add(dto.getNumber());
        legal.setUsedPrice(afterUsed)
                .setLockedPrice(afterLock)
                .setUpdateUser(member.getMemberId());
        if (!legalService.updateById(legal))
            throw new UpdateDataException(100);

        //可用 -
        F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OTC_SELL,
                dto.getNumber(), dto.getCoin(), dto.getNumber(), dto.getCoin(), BigDecimal.ZERO, dto.getCoin(),
                ItemCode.USED, dto.getCoin(), null, null,
                beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
        //冻结 +
        F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OTC_SELL,
                dto.getNumber(), dto.getCoin(), dto.getNumber(), dto.getCoin(), BigDecimal.ZERO, dto.getCoin(),
                ItemCode.LOCKED, dto.getCoin(), null, null,
                beforeLock, afterLock, member.getMemberId(), member.getMemberId());
        return success(sell);
    }


    public Result otcPayMethod(String token){
        Member member = (Member) redisUtil.get(token);
        Payment query = new Payment();
        query.setMemberId(member.getMemberId())
                .setType(PayTypeEnum.ALI_PAY.code)
                .setDel("N");
        List<Payment> aliList = paymentService.list(new QueryWrapper<>(query));
        query.setType(PayTypeEnum.WE_CHAT.code);
        List<Payment> wxList = paymentService.list(new QueryWrapper<>(query));
        query.setType(PayTypeEnum.BANK.code);
        List<Payment> bankList = paymentService.list(new QueryWrapper<>(query));
        query.setType(PayTypeEnum.PAYPAL.code);
        List<Payment> payPalList = paymentService.list(new QueryWrapper<>(query));
        Map<String, Object> map = new HashedMap();
        map.put("ali", aliList);
        map.put("wx", wxList);
        map.put("bank", bankList);
        map.put("payPal",payPalList);
        return success(map);

    }

    public Result otcSellPage(String token, String type)
    {
        Member member = (Member) redisUtil.get(token);
        Map<String, Object> map = new HashedMap();
        map.put("realStatus", member.getRealStatus());//是否实名认证0：否 1：是 2 :审核中
        map.put("price", F.me().getLegal(member.getMemberId(), type).getUsedPrice());//可用
        map.put("type", type);//币种
        return success(map);
    }


    public Result tradeList(String token, TradeDto dto, Page page){
        Member member = (Member) redisUtil.get(token);
        String[] payMethods = null;
        if (StringUtils.isNotBlank(dto.getPayMethod())){
            payMethods = dto.getPayMethod().split(",");
        }
        //0-我要购买 1-我要出售
        if (dto.getType() == 0) {
            //我要购买，拉取出售列表
            Sell query = new Sell();
            if(StringUtils.isNotBlank(dto.getCoin())){
                query.setCoin(dto.getCoin());
            }
            query.setStatus("Y").setDel("N");
            QueryWrapper queryWrapper = new QueryWrapper(query);
            //自己能看见自己发布的广告
            //queryWrapper.ne("member_id", member.getMemberId());
            if (payMethods != null)
                queryWrapper.in("pay_method", payMethods);
            if (dto.getPrice() != null){
                queryWrapper.le("low_price", dto.getPrice());
                queryWrapper.ge("rest_number*unit_price", dto.getPrice());
            }
            if (dto.getNumber() != null){
                queryWrapper.le("low_number", dto.getNumber());
                queryWrapper.ge("rest_number", dto.getNumber());
            }
            queryWrapper.eq("del","N");
            queryWrapper.gt("rest_number", 0.001);
            queryWrapper.orderByAsc("unit_price");
            queryWrapper.orderByAsc("create_time");
            IPage iPage = sellService.page(page, queryWrapper);
            return success(iPage);
        }else{
            //我要出售，拉取购买列表
            Buy query = new Buy();
            if(StringUtils.isNotBlank(dto.getCoin())){
                query.setCoin(dto.getCoin());
            }
            query.setStatus("Y").setDel("N");

            QueryWrapper queryWrapper = new QueryWrapper(query);
            //queryWrapper.ne("member_id", member.getMemberId());
            if (payMethods != null)
                queryWrapper.in("pay_method", payMethods);
            if (dto.getPrice() != null){
                queryWrapper.le("low_price", dto.getPrice());
                queryWrapper.ge("rest_number*unit_price", dto.getPrice());
            }
            if (dto.getNumber() != null){
                queryWrapper.le("low_number", dto.getNumber());
                queryWrapper.ge("rest_number", dto.getNumber());
            }
            queryWrapper.eq("del","N");
            queryWrapper.gt("rest_number", 0.001);
            queryWrapper.orderByDesc("unit_price");
            queryWrapper.orderByAsc("create_time");
            IPage iPage = buyService.page(page, queryWrapper);
            return success(iPage);

        }
    }

    /**
     * 法币交易-下单
     *
     * @param token  token
     * @param sellId 订单id（sellId)
     * @param value  填入值
     * @param type   购买方式（按数量:0 按金额:1）
     * @return Result
     */
    @Transactional(rollbackFor = Exception.class)
    public Result tradeBuy(String token, Long sellId, BigDecimal value, int type,String password)
    {

        Member member = (Member) redisUtil.get(token);

        if (StringUtils.isBlank(password)){
            return Result.fail(ApiStatus.PAY_PWD_EMPTY);
        }
        if (StringUtils.isBlank(member.getPayPassword())){
            return Result.fail(ApiStatus.PAY_PWD_EMPTY);
        }
        String newPwdmd5 = ShiroKit.md5(password, member.getPaySalt());
        if (!member.getPayPassword().equals(newPwdmd5)){
            return Result.fail(ApiStatus.ERROR_PAY_PWD);
        }
        if (StrUtil.isBlank(member.getNickName()))
            return Result.fail(ApiStatus.NOT_NICK_NAME);
        Sell sell = sellService.getById(sellId);
        if(sell.getMemberId().equals(member.getMemberId())){
            return fail(ApiStatus.NOT_MYSELF);
        }
        if (sell == null || "Y".equals(sell.getDel()) || "N".equals(sell.getStatus()))
            return Result.fail(ApiStatus.UPDATE_ORDER);
        //当日取消3次，无法下单
        Long timeLimit = Long.parseLong(F.me().cfg("LIMIT_DONT"));
        if (redisUtil.get(String.format(LIMIT_DONT, member.getMemberId())) != null)
            return fail(ApiStatus.LIMIT_DONT, (timeLimit / 60)+"");
        //订单金额
        BigDecimal price;
        //购买数量
        BigDecimal number = BigDecimal.ZERO;

        if (type == 0 && (sell.getLowNumber().compareTo(value) > 0 || sell.getRestNumber().compareTo(value) < 0))
            return fail(ApiStatus.BAD_REQUEST);
        BigDecimal highPrice = sell.getRestNumber().multiply(sell.getUnitPrice());
        if (type == 1 && (sell.getLowPrice().compareTo(value) > 0 || highPrice.compareTo(value) < 0))
            return fail(ApiStatus.BAD_REQUEST);
        // 购买方式（按数量:0 按金额:1）
        if (type == 0){
            number = value;
            price = number.multiply(sell.getUnitPrice());
        }else{
            number = value.divide(sell.getUnitPrice(), 8, RoundingMode.DOWN);
            price = value;
        }

        //冻结出售用户资金
        //step 1:扣除用户订单数
        sell.setRestNumber(sell.getRestNumber().subtract(number))
                .setUpdateUser(sell.getMemberId());
        if (!sellService.updateById(sell))
            throw new UpdateDataException(100);
        //step 2:生成交易订单
        Bill bill = new Bill();
        String orderNo = RandomUtil.code("OB");
        bill.setOpid(sellId)
                .setType(BillType.BUY.code)
                .setBuyMid(member.getMemberId())
                .setBuyName(member.getNickName())
                .setSellMid(sell.getMemberId())
                .setSellName(sell.getNickName())
                .setOrderNo(orderNo)
                .setMarkNo(cn.hutool.core.util.RandomUtil.randomNumbers(7))
                .setCoin(sell.getCoin())
                .setUnitPrice(sell.getUnitPrice())
                .setNumber(number)
                .setCny(price)
                .setPayMethod(sell.getPayMethod())
                .setPayAccount(sell.getPayAccount())
                .setPayName(sell.getPayName())
                .setPayImg(sell.getPayImg())
                .setBank(sell.getBank())
                .setBranch(sell.getBranch())
                .setRemark(sell.getRemark())
                .setStatus(BillStatus.WAIT.code)//待付款
                .setCreateUser(member.getMemberId());
        billService.save(bill);
        Map<String, Object> map = new HashedMap();
        map.put("orderNo", orderNo);
        String smsWait = F.me().cfg("SMS_WAIT");
        if (StrUtil.isNotBlank(smsWait) && smsIsOpen()){
            String content = String.format(smsWait, orderNo.substring(0,4)+"***");
            homeService.sendMsgWithPhoneOrEmail(member, content);
        }
        return success(map);
    }

    /**
     * 法币交易-下单出售
     *
     * @param token     token
     * @param buyId     订单id（buyId)
     * @param value     填入值
     * @param type      出售方式（按数量:0 按金额:1）
     * @param paymentId 收款id
     * @param payPwd    交易密码
     * @return Result
     */
    @Transactional(rollbackFor = Exception.class)
    public Result tradeSell(String token, Long buyId, BigDecimal value, int type, Long paymentId, String payPwd)
    {
        Member member = (Member) redisUtil.get(token);

        //当日取消3次，无法下单
        Long timeLimit = Long.parseLong(F.me().cfg("LIMIT_DONT"));
        if (redisUtil.get(String.format(LIMIT_DONT, member.getMemberId())) != null)
            return fail(ApiStatus.LIMIT_DONT1,(timeLimit / 60)+"");

        Buy buy = buyService.getById(buyId);
        if(buy.getMemberId().equals(member.getMemberId())){
            return fail(ApiStatus.NOT_MYSELF);
        }
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        if (buy == null || "Y".equals(buy.getDel()) || "N".equals(buy.getStatus()))
            return fail(ApiStatus.UPDATE_ORDER);

        Payment payment = paymentService.getById(paymentId);
//        if(payment.getMemberId().equals(member.getMemberId())){
//            return fail(ApiStatus.NOT_MYSELF);
//        }
        if (payment == null || payment.getDel().equals("Y"))
            return fail(ApiStatus.BAD_REQUEST);
        if (StrUtil.isBlank(member.getPayPassword()))
            return fail(ApiStatus.PAY_PWD_EMPTY);
        if (!buy.getPayMethod().equals(payment.getType()))
            return fail(ApiStatus.PAY_METHOD_ERROR);
        if (!homeService.isTruePayPwd(member, payPwd))
            return fail(ApiStatus.ERROR_PAY_PWD);

        //订单金额
        BigDecimal price;
        //购买数量
        BigDecimal number;

        if (type == 0 && (buy.getLowNumber().compareTo(value) > 0 || buy.getRestNumber().compareTo(value) < 0))
            return fail(ApiStatus.BAD_REQUEST);
        BigDecimal highPrice = buy.getRestNumber().multiply(buy.getUnitPrice());
        if (type == 1 && (buy.getLowPrice().compareTo(value) > 0 || highPrice.compareTo(value) < 0))
            return fail(ApiStatus.BAD_REQUEST);


        if (type == 0)// 出售方式（按数量:0 按金额:1）
        {
            number = value;
            price = number.multiply(buy.getUnitPrice());
        }
        else
        {
            number = value.divide(buy.getUnitPrice(), 8, RoundingMode.DOWN);
            price = value;
        }
        Legal legal = F.me().getLegal(member.getMemberId(), buy.getCoin());

        if (legal.getUsedPrice().compareTo(number) < 0)
            return fail(ApiStatus.WALLET_LESS);

        //
        //step 1:扣除挂单购买用户订单数
        buy.setRestNumber(buy.getRestNumber().subtract(number))
                .setUpdateUser(buy.getMemberId());
        if (!buyService.updateById(buy))
            throw new UpdateDataException(100);
        //step 2:生成交易订单
        Bill bill = new Bill();
        String orderNo = RandomUtil.code("OB");
        bill.setOpid(buyId)
                .setType(BillType.SELL.code)
                .setBuyMid(buy.getMemberId())
                .setBuyName(buy.getNickName())
                .setSellMid(member.getMemberId())
                .setSellName(member.getNickName())
                .setOrderNo(orderNo)
                .setMarkNo(cn.hutool.core.util.RandomUtil.randomNumbers(7))
                .setCoin(buy.getCoin())
                .setUnitPrice(buy.getUnitPrice())
                .setNumber(number)
                .setCny(price)
                .setRemark(buy.getRemark())
                .setPayMethod(payment.getType())
                .setPayAccount(payment.getIdcard())
                .setPayName(payment.getName())
                .setPayImg(payment.getImg())
                .setBank(payment.getBank())
                .setBranch(payment.getBranch())
                .setStatus(BillStatus.WAIT.code)//待付款
                .setCreateUser(member.getMemberId());
        billService.save(bill);

        //step 3:冻结出售用户币


        BigDecimal beforeUsed = legal.getUsedPrice();
        BigDecimal afterUsed = legal.getUsedPrice().subtract(number);

        BigDecimal beforeLock = legal.getLockedPrice();
        BigDecimal afterLock = legal.getLockedPrice().add(number);

        legal.setUsedPrice(afterUsed)
                .setLockedPrice(afterLock)
                .setUpdateUser(member.getMemberId());
        if (!legalService.updateById(legal))
            throw new UpdateDataException(100);
        //可用
        F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.I_SELL,
                number, buy.getCoin(), number, buy.getCoin(), BigDecimal.ZERO, buy.getCoin(),
                ItemCode.USED, buy.getCoin(), null, null,
                beforeUsed, afterUsed,
                member.getMemberId(), member.getMemberId());
        //锁定
        F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.I_SELL,
                number, buy.getCoin(), number, buy.getCoin(), BigDecimal.ZERO, buy.getCoin(),
                ItemCode.LOCKED, buy.getCoin(), null, null,
                beforeLock, afterLock, member.getMemberId(), member.getMemberId());

        String smsWait = F.me().cfg("SMS_WAIT");
        if (StrUtil.isNotBlank(smsWait) && smsIsOpen())
        {
            Member buyMember = F.me().getMember(buy.getMemberId());
            String content = String.format(smsWait, orderNo.substring(0,4)+"**");
            homeService.sendMsgWithPhoneOrEmail(buyMember, content);
        }

        return success(bill);
    }


    public Result tradeBuyWaitPage(String token, String orderNo) {
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Bill query = new Bill();
        query.setDel("N")
                .setOrderNo(orderNo)
//                .setBuyMid(member.getMemberId())
//                .setStatus(BillStatus.WAIT.code)
        ;
        Bill bill = billService.getOne(new QueryWrapper<>(query));
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);
        if (bill.getBuyMid().longValue() != member.getMemberId().longValue() && bill.getSellMid().longValue() != member.getMemberId().longValue())
            return fail(ApiStatus.BAD_REQUEST);
        BillDto billDto = new BillDto();
        BeanUtil.copyProperties(bill, billDto);
        billDto.setCreateTimestamp(billDto.getCreateTime() == null ? null : billDto.getCreateTime().getTime())
                .setPayTimestamp(billDto.getPayTime() == null ? null : billDto.getPayTime().getTime())
                .setNow(System.currentTimeMillis())
        ;
        if (bill.getBuyMid().longValue() == member.getMemberId().longValue() ){
            billDto.setBuyAccount(member.getAccount());
            Member byId = memberService.getById(bill.getSellMid());
            if(byId!=null)
            billDto.setSellAccount(byId.getAccount());
        }else {
            billDto.setSellAccount(member.getAccount());
            Member byId = memberService.getById(bill.getBuyMid());
            if(byId!=null)
                billDto.setBuyAccount(byId.getAccount());


        }
        return success(billDto);
    }


    @Transactional(rollbackFor = Exception.class)
    public Result tradeCancel(String token, String orderNo){
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Bill query = new Bill();
        query.setDel("N")
                .setOrderNo(orderNo)
                .setBuyMid(member.getMemberId())
                .setStatus(BillStatus.WAIT.code)
        ;
        Bill bill = billService.getOne(new QueryWrapper<>(query));
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);

        bill.setStatus(BillStatus.CANCEL.code)
                .setCancelTime(new Date())
                .setUpdateUser(member.getMemberId());

        if (!billService.updateById(bill))
            throw new UpdateDataException(100);

        //今日
        cancelItem(member, bill);
        //【ImBuy】订单号：@，买家已取消订单
        String smsCancel = F.me().cfg("SMS_CANCEL");
        if (StrUtil.isNotBlank(smsCancel) && smsIsOpen()){
            Member sellMember = F.me().getMember(bill.getSellMid());
            if (sellMember == null)
                throw new UpdateDataException(100);
            //取消提醒
            String content = String.format(smsCancel, bill.getOrderNo().substring(0,4)+"**");
            homeService.sendMsgWithPhoneOrEmail(sellMember, content);
        }
        return success();
    }

    /**
     * 取消订单细节及惩罚
     *
     * @param member
     * @param bill
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelItem(Member member, Bill bill){
        int count = Integer.parseInt(F.me().cfg(TIME_COUNT));
        BigDecimal number = bill.getNumber();
        String coin = bill.getCoin();
        //购买单，加还 挂单出售单数量，如用户撤单了，退还到可用资产
        if (bill.getType().equals(BUY)){
            //我是买方
            String key = String.format(CANCEL, member.getMemberId());
            Integer cancel = (Integer) redisUtil.get(key);
            redisUtil.set(key, cancel == null ? 1 : cancel + 1, DateTimeUtil.getRemainSecondsOneDay(new Date()));
            if (cancel != null && cancel.intValue() + 1 >= count){
                redisUtil.set(String.format(LIMIT_DONT, member.getMemberId()), 1, Long.parseLong(F.me().cfg("LIMIT_DONT")));
                redisUtil.del(key);
            }

            Sell sell = sellService.getById(bill.getOpid());

            sell.setRestNumber(sell.getRestNumber().add(bill.getNumber())).setUpdateUser(SYS_PLATFORM);
            if (!sellService.updateById(sell))
                throw new UpdateDataException(100);
            if (sell.getStatus().equals("N"))//已撤单，资产解冻
            {
                Legal legal = F.me().getLegal(sell.getMemberId(), sell.getCoin());
                BigDecimal beforeUsed = legal.getUsedPrice();
                BigDecimal afterUsed = legal.getUsedPrice().add(number);
                BigDecimal beforeLock = legal.getLockedPrice();
                BigDecimal afterLock = legal.getLockedPrice().subtract(number);
                legal.setUsedPrice(afterUsed)
                        .setLockedPrice(afterLock)
                        .setUpdateUser(SYS_PLATFORM);
                if (!legalService.updateById(legal))
                    throw new UpdateDataException(100);
                //可用 +
                F.me().saveCashflow(legal.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OTC_SELL_CANCEL,
                        number, coin, number, coin, BigDecimal.ZERO, coin,
                        ItemCode.USED, coin, null, null,
                        beforeUsed, afterUsed, legal.getMemberId(), legal.getMemberId());
                //冻结 -
                F.me().saveCashflow(legal.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OTC_SELL_CANCEL,
                        number, coin, number, coin, BigDecimal.ZERO, coin,
                        ItemCode.LOCKED, coin, null, null,
                        beforeLock, afterLock, legal.getMemberId(), legal.getMemberId());
            }

        }
        //出售单，加还 挂单购买数量 ，同时解冻 出售用户账户数量（冻结转可用），加惩罚机制
        if (bill.getType().equals(SELL))
        {
            //我是挂单买方
            String key = String.format(OTC_CANCEL, member.getMemberId());

            Integer cancel = (Integer) redisUtil.get(key);
            redisUtil.set(key, cancel == null ? 1 : cancel + 1, DateTimeUtil.getRemainSecondsOneDay(new Date()));
            if (cancel != null && cancel.intValue() + 1 >= count)
            {
                punish(member, bill);

            }

            //f.注（非处罚条例）：解冻卖家对应币数量
            Buy buy = buyService.getById(bill.getOpid());
            buy.setRestNumber(buy.getRestNumber().add(bill.getNumber())).setUpdateUser(SYS_PLATFORM);
            if (!buyService.updateById(buy))
                throw new UpdateDataException(100);

            Legal legal = F.me().getLegal(bill.getSellMid(), bill.getCoin());

            BigDecimal beforeUsed = legal.getUsedPrice();
            BigDecimal afterUsed = legal.getUsedPrice().add(number);
            BigDecimal beforeLock = legal.getLockedPrice();
            BigDecimal afterLock = legal.getLockedPrice().subtract(number);
            legal.setUsedPrice(afterUsed)
                    .setLockedPrice(afterLock)
                    .setUpdateUser(SYS_PLATFORM);
            if (!legalService.updateById(legal))
                throw new UpdateDataException(100);
            //可用 +
            F.me().saveCashflow(legal.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OTC_SELL_CANCEL,
                    number, coin, number, coin, BigDecimal.ZERO, coin,
                    ItemCode.USED, coin, null, null,
                    beforeUsed, afterUsed, legal.getMemberId(), legal.getMemberId());
            //冻结 -
            F.me().saveCashflow(legal.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OTC_SELL_CANCEL,
                    number, coin, number, coin, BigDecimal.ZERO, coin,
                    ItemCode.LOCKED, coin, null, null,
                    beforeLock, afterLock, legal.getMemberId(), legal.getMemberId());

        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void punish(Member member, Bill bill)
    {
        //订单币种
//        String billCoin = bill.getCoin();

        //a.扣除50%保证金
        Deposit deposit = getDeposit(member);

        //押金币种
        String depositCoin = deposit.getCoin();

        if (deposit == null)
        {
            log.info("用户无押金记录");
            throw new UpdateDataException(100);
        }

        BigDecimal subPrice = deposit.getNumber().divide(new BigDecimal(2), 8, RoundingMode.DOWN);
        //
        deposit.setNumber(subPrice)
                .setStatus(NO) //全额缴纳-1   待补缴-0
                .setUpdateUser(SYS_PLATFORM);
        boolean row = depositService.updateById(deposit);
        if (!row)
            throw new UpdateDataException(100);

        Legal depositLegal = F.me().getLegal(member.getMemberId(), depositCoin);


        BigDecimal beforeLock = depositLegal.getLockedPrice();
        BigDecimal afterLock = depositLegal.getLockedPrice().subtract(subPrice);

        depositLegal.setLockedPrice(afterLock)
                .setUpdateUser(SYS_PLATFORM);
        if (!legalService.updateById(depositLegal))
            throw new UpdateDataException(100);

        //冻结流水
        F.me().saveCashflow(member.getMemberId(), ProConst.WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.CANCEL_PUNISH,
                subPrice, depositCoin, subPrice, depositCoin, BigDecimal.ZERO, depositCoin,
                ProConst.ItemCode.LOCKED, depositCoin, null, null,
                beforeLock, afterLock, member.getMemberId(), SYS_PLATFORM);

        //b.关闭挂单买卖功能，补缴后再次开通
        //押金状态已改为待补缴，说明已关闭挂单功能

        //c.所有挂单购买订单剩余未交易部分全部自动撤销
        Buy queryBuy = new Buy();
        queryBuy.setStatus("Y").setMemberId(member.getMemberId()).setDel("N");
        List<Buy> buys = buyService.list(new QueryWrapper<>(queryBuy));
        for (Buy buy : buys)
        {
            buy.setStatus("N").setUpdateUser(SYS_PLATFORM);
            if (!buyService.updateById(buy))
                throw new UpdateDataException(100);
        }
        List<Spot> spotList = F.me().getSpots(null);
        for (Spot spot : spotList)
        {
            String billCoin = spot.getSymbol().split("/")[0];
            if("USDT-ERC20".equals(billCoin)
                    || "USDT-TRC20".equals(billCoin)
                    || "USDT-OMNI".equals(billCoin)){
                billCoin = "USDT";
            }
            //d.所有挂单出售订单剩余为交易部分全部自动撤销，解冻对应剩余数量资产
            Sell querySell = new Sell();
            querySell.setStatus("Y").setCoin(billCoin).setMemberId(member.getMemberId()).setDel("N");
            List<Sell> sells = sellService.list(new QueryWrapper<>(querySell));
            BigDecimal totalNumber = BigDecimal.ZERO;
            for (Sell sell : sells)
            {
                sell.setStatus("N").setUpdateUser(SYS_PLATFORM);
                if (!sellService.updateById(sell))
                    throw new UpdateDataException(100);
                totalNumber = totalNumber.add(sell.getRestNumber());
            }
            if (totalNumber.compareTo(BigDecimal.ZERO) > 0)
            {
                //解冻
                Legal billLegal = F.me().getLegal(member.getMemberId(), billCoin);
                BigDecimal beforeUsed = billLegal.getUsedPrice();
                BigDecimal afterUsed = billLegal.getUsedPrice().add(totalNumber);

                beforeLock = billLegal.getLockedPrice();
                afterLock = billLegal.getLockedPrice().subtract(totalNumber);

                billLegal.setUsedPrice(afterUsed)
                        .setLockedPrice(afterLock)
                        .setUpdateUser(member.getMemberId());
                if (!legalService.updateById(billLegal))
                    throw new UpdateDataException(100);

                //可用 +
                F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OTC_SELL_CANCEL,
                        totalNumber, billCoin, totalNumber, billCoin, BigDecimal.ZERO, billCoin,
                        ItemCode.USED, billCoin, null, null,
                        beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
                //冻结 -
                F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OTC_SELL_CANCEL,
                        totalNumber, billCoin, totalNumber, billCoin, BigDecimal.ZERO, billCoin,
                        ItemCode.LOCKED, billCoin, null, null,
                        beforeLock, afterLock, member.getMemberId(), member.getMemberId());
            }
        }

    }

    /**
     * 短信开关
     *
     * @return true-开 false:关
     */
    public boolean smsIsOpen()
    {
        return F.me().cfg(SMS_OPEN).equals("Y") ? true : false;
    }

    public Result tradeCancelPre(String token, String orderNo)
    {
        Member member = (Member) redisUtil.get(token);
        Long timeLimit = Long.parseLong(F.me().cfg("LIMIT_DONT"));
        Bill bill = getBill(orderNo);
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);
        int count = Integer.parseInt(F.me().cfg(TIME_COUNT));
        //取消时，我是买方
        if (BUY.equals(bill.getType()))
        {

            Integer cancelNumber = (Integer) redisUtil.get(String.format(CANCEL, member.getMemberId()));
            if (cancelNumber != null && cancelNumber == count - 1)
                return success(ApiStatus.MANY_CANCEL,(timeLimit / 60)+"");
        }
        else
        {
            //取消时，我是挂单买方
            Integer cancelNumber = (Integer) redisUtil.get(String.format(OTC_CANCEL, member.getMemberId()));
            if (cancelNumber != null && cancelNumber >= count - 1)
                return success(ApiStatus.MANY_OTC_CANCEL);
        }
        return success();
    }

    /**
     * 根据订单号获取结算单
     *
     * @param orderNo 订单号
     * @return Bill
     */
    private Bill getBill(String orderNo)
    {
        Bill query = new Bill();
        query.setOrderNo(orderNo).setDel("N");
        return billService.getOne(new QueryWrapper<>(query));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result tradePaid(String token, String orderNo)
    {
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Bill query = new Bill();
        query.setDel("N")
                .setOrderNo(orderNo)
                .setBuyMid(member.getMemberId())
                .setStatus(BillStatus.WAIT.code);

        Bill bill = billService.getOne(new QueryWrapper<>(query));
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);

        bill.setStatus(BillStatus.WAIT_COIN.code)
                .setPayTime(new Date())
                .setUpdateUser(member.getMemberId());

        if (!billService.updateById(bill))
            throw new UpdateDataException(100);


        String smsCancel = F.me().cfg("SMS_PAID");

        if (StrUtil.isNotBlank(smsCancel) && smsIsOpen())
        {
            Member sellMember = F.me().getMember(bill.getSellMid());
            if (sellMember == null)
                throw new UpdateDataException(100);

            String content = String.format(smsCancel, sellMember.getAccount());

            homeService.sendMsgWithPhoneOrEmail(sellMember, content);

        }
        return success();
    }


    public Result tradeAppeal(String token, AppealDto appealDto)
    {
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Bill query = new Bill();
        query.setDel("N")
                .setOrderNo(appealDto.getOrderNo())
//                .setStatus(BillStatus.WAIT_COIN.code)
        ;

        Bill bill = billService.getOne(new QueryWrapper<>(query).in("status",BillStatus.FINISH, BillStatus.WAIT_COIN.code, BillStatus.APPEAL.code));
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);
        if (BillStatus.FINISH.code.equals(bill.getStatus())){
            return Result.fail(ApiStatus.EXIT_FINISH_ORDER);
        }
        if (member.getMemberId().longValue() != bill.getBuyMid().longValue() && member.getMemberId().longValue() != bill.getSellMid().longValue())
            return fail(ApiStatus.BAD_REQUEST);
        //申诉类型
        String appealType = "";
        if (member.getMemberId().longValue() == bill.getBuyMid().longValue())
        {
            appealType = BUY; //"BUY";//买家申诉
            bill.setImg(appealDto.getImg())
                    .setAtime(new Date())
                    .setContent(appealDto.getContent())
            ;
        }
        if (member.getMemberId().longValue() == bill.getSellMid().longValue())
        {
            appealType = SELL;//卖家申诉
            bill.setImg1(appealDto.getImg())
                    .setAtime1(new Date())
                    .setContent1(appealDto.getContent())
            ;
        }

        long now = System.currentTimeMillis();
        //倒计时是否到
        long timeOut = Integer.parseInt(F.me().cfg("TIME_OUT")) * 60 * 1000;
        if (bill.getPayTime().getTime() + timeOut > now)
            return fail(ApiStatus.BAD_REQUEST);

        bill.setStatus(BillStatus.APPEAL.code)
                .setAppealType(appealType)
                .setAppealStatus(AppealStatus.CHECK.code)
                .setUpdateUser(member.getMemberId());

        if (!billService.updateById(bill))
            throw new UpdateDataException(100);
        try {
            WebSocketService.sendInfo(TRADEAPPEALSTRING,SYSTEMSOCKETSENDNAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success();
    }


    public Result tradeCancelAppeal(String token, String orderNo)
    {

        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        Bill query = new Bill();
        query.setDel("N")
                .setOrderNo(orderNo)
                .setAppealStatus(AppealStatus.CHECK.code)
                .setStatus(BillStatus.APPEAL.code);

        Bill bill = billService.getOne(new QueryWrapper<>(query));
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);

        if (member.getMemberId().longValue() != bill.getBuyMid().longValue() && member.getMemberId().longValue() != bill.getSellMid().longValue())
            return fail(ApiStatus.BAD_REQUEST);

        bill.setStatus(BillStatus.WAIT_COIN.code)
                .setAppealStatus(AppealStatus.CHECK.code)
                .setUpdateUser(member.getMemberId());

        if (!billService.updateById(bill))
            throw new UpdateDataException(100);
        return success();
    }

    /**
     * 我的订单(挂单购买/出售订单)
     *
     * @param token    token
     * @param pageType BUY:挂单购买订单 SELL:挂单出售订单
     * @param page     page
     */
    public Result otcOrderList(String token, String pageType, Page page){
        Member member = (Member) redisUtil.get(token);
        if (pageType.equals("BUY")){
            Buy query = new Buy();
            query.setMemberId(member.getMemberId())
                    .setDel("N");
            return success(buyService.page(page, new QueryWrapper<>(query).orderByDesc(CREATE_TIME)));
        }else{
            Sell query = new Sell();
            query.setMemberId(member.getMemberId())
                    .setDel("N");
            return success(sellService.page(page, new QueryWrapper<>(query).orderByDesc(CREATE_TIME)));
        }
    }

    /**
     * 我的订单-（ 购买订单 出售订单 ）
     *
     * @param token    token
     * @param pageType BUY:购买订单 SELL:出售订单
     * @param status   ("WAIT", "买家待付款") ("WAIT_COIN", "待放币、已付款") ("CANCEL", "已取消") ("FINISH", "已完成") ("APPEAL", "申述中")
     * @param page     page
     */
    public Result otcBillList(String token, String pageType, String status, Page page){
        Member member = (Member) redisUtil.get(token);

        Bill query = new Bill();
        query.setBuyMid(pageType.equals(BUY) ? member.getMemberId() : null)
                .setSellMid(pageType.equals(BUY) ? null : member.getMemberId())
                .setDel("N")
        ;
        if(StringUtils.isNotBlank(status)){
            query.setStatus(status);
        }
        return success(billService.page(page, new QueryWrapper<>(query).orderByDesc(CREATE_TIME)));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result cancelOrder(String token, String pageType, String orderNo){
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        if (pageType.equals(BUY)){
            Buy query = new Buy();
            query.setMemberId(member.getMemberId())
                    .setOrderNo(orderNo)
                    .setDel("N")
                    .setStatus("Y");

            Buy buy = buyService.getOne(new QueryWrapper<>(query));
            if (buy == null)
                return fail(ApiStatus.BAD_REQUEST);

            buy.setStatus("N").setUpdateUser(member.getMemberId());
            if (!buyService.updateById(buy))
                throw new UpdateDataException(100);

            return success();
        }
        if (pageType.equals(SELL))
        {
            Sell query = new Sell();
            query.setMemberId(member.getMemberId())
                    .setOrderNo(orderNo)
                    .setDel("N")
                    .setStatus("Y");
            Sell sell = sellService.getOne(new QueryWrapper<>(query));

            if (sell == null)
                return fail(ApiStatus.BAD_REQUEST);

            sell.setStatus("N").setUpdateUser(member.getMemberId());
            if (!sellService.updateById(sell))
                throw new UpdateDataException(100);
            Legal legal = F.me().getLegal(member.getMemberId(), sell.getCoin());


            BigDecimal beforeUsed = legal.getUsedPrice();
            BigDecimal afterUsed = legal.getUsedPrice().add(sell.getRestNumber());

            BigDecimal beforeLock = legal.getLockedPrice();
            BigDecimal afterLock = legal.getLockedPrice().subtract(sell.getRestNumber());

            legal.setUsedPrice(afterUsed)
                    .setLockedPrice(afterLock)
                    .setUpdateUser(member.getMemberId());
            if (!legalService.updateById(legal))
                throw new UpdateDataException(100);

            //可用 +
            F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OTC_SELL_CANCEL,
                    sell.getRestNumber(), sell.getCoin(), sell.getRestNumber(), sell.getCoin(), BigDecimal.ZERO, sell.getCoin(),
                    ItemCode.USED, sell.getCoin(), null, null,
                    beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
            //冻结 -
            F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OTC_SELL_CANCEL,
                    sell.getRestNumber(), sell.getCoin(), sell.getRestNumber(), sell.getCoin(), BigDecimal.ZERO, sell.getCoin(),
                    ItemCode.LOCKED, sell.getCoin(), null, null,
                    beforeLock, afterLock, member.getMemberId(), member.getMemberId());

            return success();
        }
        return fail(ApiStatus.BAD_REQUEST);

    }

    @Transactional(rollbackFor = Exception.class)
    public Result otcFinish(String token, String orderNo, String payPwd){
        Member member = (Member) redisUtil.get(token);
        if (StrUtil.isBlank(member.getNickName()))
            return fail(ApiStatus.NOT_NICK_NAME);
        if (!homeService.isTruePayPwd(member, payPwd))
            return fail(ApiStatus.ERROR_PAY_PWD);
        Bill query = new Bill();
        query.setSellMid(member.getMemberId())
                .setOrderNo(orderNo)
                .setStatus(BillStatus.WAIT_COIN.code)
                .setDel("N");

        Bill bill = billService.getOne(new QueryWrapper<>(query));
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);

        bill.setStatus(BillStatus.FINISH.code).setFinishTime(new Date()).setUpdateUser(member.getMemberId());
        if (!billService.updateById(bill))
            throw new UpdateDataException(100);


        // 卖家放币
        Legal legal = F.me().getLegal(member.getMemberId(), bill.getCoin());

        BigDecimal flowPrice = bill.getNumber();
        String coin = bill.getCoin();

        BigDecimal beforeLock = legal.getLockedPrice();
        BigDecimal afterLock = legal.getLockedPrice().subtract(flowPrice);


        legal.setLockedPrice(afterLock)
                .setUpdateUser(member.getMemberId());
        if (!legalService.updateById(legal))
            throw new UpdateDataException(100);

        //买家到账币
        Legal buyLegal = F.me().getLegal(bill.getBuyMid(), bill.getCoin());
        BigDecimal buyBeforeUsed = buyLegal.getUsedPrice();
        BigDecimal buyAfterUsed = buyLegal.getUsedPrice().add(flowPrice);
        buyLegal.setUsedPrice(buyAfterUsed).setUpdateUser(member.getMemberId());
        if (!legalService.updateById(buyLegal))
            throw new UpdateDataException(100);

        //卖家流水
        F.me().saveCashflow(member.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_OUT, CashFlowTypeEnum.OTC_FINISH,
                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
                ItemCode.LOCKED, coin, null, null,
                beforeLock, afterLock, member.getMemberId(), bill.getBuyMid());

        //买家流水
        F.me().saveCashflow(buyLegal.getMemberId(), WalletBigType.LEGAL, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.OTC_FINISH,
                flowPrice, coin, flowPrice, coin, BigDecimal.ZERO, coin,
                ItemCode.USED, coin, null, null,
                buyBeforeUsed, buyAfterUsed, member.getMemberId(), bill.getBuyMid());

        String smsFinish = F.me().cfg("SMS_FINISH");
        if (StrUtil.isNotBlank(smsFinish) && smsIsOpen()) {
            Member sellMember = F.me().getMember(bill.getBuyMid());
            if (sellMember == null)
                throw new UpdateDataException(100);
            String content = String.format(smsFinish, bill.getOrderNo().substring(0,4)+"**", orderNo.substring(0,4)+"**", flowPrice, coin);
            homeService.sendMsgWithPhoneOrEmail(sellMember, content);
        }
        if(!"Y".equals(member.getValidStatus())){
            member.setValidStatus("Y");
            //有效用户之后 进行奖励
            homeService.calculateReferralCommission(member);
            memberService.updateById(member);
            redisUtil.set(token, member, Long.parseLong(F.me().cfg(TOKEN_EXPIRE)));
        }

        //购买者上级返利
//        Member buyer = this.memberService.getById(buyLegal.getMemberId());
//        if (buyer != null && StringUtils.isNotBlank(buyer.getParentRefereeId())){
//          String[] refereeIds =   buyer.getParentRefereeId().split("/");
//          int size = refereeIds.length-1;
//            String oneBonusRatio = null;
//          for (int i = 1;i<size ;i++){
//             if (i==1){
//                oneBonusRatio =   F.me().getSysConfigValueByCode("ONE_BONUS_RATIO");
//             }else if (i==2){
//                  oneBonusRatio =   F.me().getSysConfigValueByCode("TWO_BONUS_RATIO");
//             }else if (i==3){
//                  oneBonusRatio =   F.me().getSysConfigValueByCode("THREE_BONUS_RATIO");
//             }else if (i==4){
//                  oneBonusRatio =   F.me().getSysConfigValueByCode("FOUR_BONUS_RATIO");
//             }else if (i==5){
//                  oneBonusRatio =   F.me().getSysConfigValueByCode("FIVE_BONUS_RATIO");
//             }
//             if (StringUtils.isNotBlank(oneBonusRatio)
//                      && new BigDecimal(oneBonusRatio).compareTo(BigDecimal.ZERO)>0){
//                  BigDecimal returnPrice =flowPrice.multiply(new BigDecimal(oneBonusRatio));
//                  Member other = this.memberService.getById(refereeIds[i]);
//                  if (other != null){
//                      if (StringUtils.isNotBlank(other.getRealStatus()) && "1".equals(other.getRealStatus())){
//                          Bill billEntity = new Bill();
//                          billEntity.setBuyMid(other.getMemberId())
//                                  .setStatus(BillStatus.FINISH.code)
//                                  .setDel("N");
//                         int count = billService.count(new QueryWrapper<>(billEntity));
//                         if (count >0){
//                             Wallet wallet = F.me().getWallet(other.getMemberId(),bill.getCoin());
//                             BigDecimal buyBefore = wallet.getUsedPrice();
//                             wallet.setUsedPrice(wallet.getUsedPrice().add(returnPrice));
//                             wallet.setUpdateUser(1l);
//                             wallet.setUpdateTime(new Date());
//                             walletService.updateById(wallet);
//                             F.me().saveCashflow(wallet.getMemberId(), WalletBigType.WALLET, CashFlowOpEnum.FLOW_IN, CashFlowTypeEnum.BONUS_AWARD,
//                                     returnPrice, coin, returnPrice, coin, BigDecimal.ZERO, coin,
//                                     ItemCode.USED, coin, null, null,
//                                     buyBefore,  wallet.getUsedPrice(), wallet.getMemberId(), wallet.getMemberId());
//                         }
//                      }
//                  }
//              }
//              if (i==5){
//                  break;
//              }
//          }
//        }
        return success();
    }

    public Result getContractOptionConfig() {
        String otc_coin = F.me().getSysConfigValueByCode("OTC_COIN");

        return Result.success(otc_coin.split(","));
    }
    public Result uploadBuyProof(String token, String billNo, String img) {
        Member member =(Member) redisUtil.get(token);
        if(member==null){
            return fail(ApiStatus.ERROR);
        }
        Bill bill = getBill(billNo);
        if(!bill.getBuyMid().equals(member.getMemberId())){
            return fail(ApiStatus.BAD_REQUEST);
        }
        if (bill == null)
            return fail(ApiStatus.BAD_REQUEST);
        bill.setUploadStatus(Constant.Y);
        bill.setUploadImg(img);
        bill.setUpdateTime(new Date());
        bill.setUpdateUser(member.getCreateUser());




        billService.updateById(bill);
        return success(ApiStatus.OK);
    }
}

