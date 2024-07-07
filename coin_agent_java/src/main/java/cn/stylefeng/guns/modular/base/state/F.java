package cn.stylefeng.guns.modular.base.state;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.mapper.MemberMapper;
import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.chain.entity.Dapp;
import cn.stylefeng.guns.modular.chain.entity.Recharge;
import cn.stylefeng.guns.modular.chain.entity.Withdraw;
import cn.stylefeng.guns.modular.chain.mapper.DappMapper;
import cn.stylefeng.guns.modular.chain.service.RechargeService;
import cn.stylefeng.guns.modular.chain.service.WithdrawService;
import cn.stylefeng.guns.modular.coin.entity.*;
import cn.stylefeng.guns.modular.coin.mapper.*;
import cn.stylefeng.guns.modular.com.entity.Region;
import cn.stylefeng.guns.modular.com.entity.SvcException;
import cn.stylefeng.guns.modular.com.mapper.RegionMapper;
import cn.stylefeng.guns.modular.com.mapper.SvcExceptionMapper;
import cn.stylefeng.guns.modular.fin.constant.FinMiningMap;
import cn.stylefeng.guns.modular.fin.constant.MiningMap;
import cn.stylefeng.guns.modular.fin.entity.*;
import cn.stylefeng.guns.modular.fin.mapper.*;
import cn.stylefeng.guns.modular.meta_data.entity.Config;
import cn.stylefeng.guns.modular.meta_data.mapper.ConfigMapper;
import cn.stylefeng.guns.modular.mining.entity.Mining;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.mapper.MiningMapper;
import cn.stylefeng.guns.modular.mining.mapper.MiningOrderMapper;
import cn.stylefeng.guns.modular.promotion.entity.GatewayDefine;
import cn.stylefeng.guns.modular.promotion.entity.GatewayRecord;
import cn.stylefeng.guns.modular.promotion.mapper.GatewayDefineMapper;
import cn.stylefeng.guns.modular.promotion.mapper.GatewayRecordMapper;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.mapper.UserMapper;
import cn.stylefeng.roses.core.util.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@DependsOn("springContextHolder")
@Slf4j
public class F implements IPromotionFactory
{


    public static F me()
    {
        return SpringContextHolder.getBean("f");
    }

    private WithdrawService withdrawService =  SpringContextHolder.getBean(WithdrawService.class);
    private RechargeService rechargeService =  SpringContextHolder.getBean(RechargeService.class);


    private MemberMapper appMemberMapper = SpringContextHolder.getBean(MemberMapper.class);


    private ConfigMapper configMapper = SpringContextHolder.getBean(ConfigMapper.class);

    private GatewayRecordMapper gatewayRecordMapper = SpringContextHolder.getBean(GatewayRecordMapper.class);

    private GatewayDefineMapper gatewayDefineMapper = SpringContextHolder.getBean(GatewayDefineMapper.class);


    private WalletMapper walletMapper = SpringContextHolder.getBean(WalletMapper.class);
    private RegionMapper regionMapper = SpringContextHolder.getBean(RegionMapper.class);

    private WalletHistoryMapper walletHistoryMapper = SpringContextHolder.getBean(WalletHistoryMapper.class);

    private CashflowMapper cashflowMapper = SpringContextHolder.getBean(CashflowMapper.class);

    private SvcExceptionMapper svcExceptionMapper = SpringContextHolder.getBean(SvcExceptionMapper.class);


    private ContractMapper contractMapper = SpringContextHolder.getBean(ContractMapper.class);
    private CurrencyMapper currencyMapper = SpringContextHolder.getBean(CurrencyMapper.class);
    private FinMiningMapper finMiningMapper = SpringContextHolder.getBean(FinMiningMapper.class);
    private FinOptionMapper finOptionMapper = SpringContextHolder.getBean(FinOptionMapper.class);
    private FinFuturesMapper finFuturesMapper = SpringContextHolder.getBean(FinFuturesMapper.class);
    private LegalMapper legalMapper = SpringContextHolder.getBean(LegalMapper.class);


    private DappMapper dappMapper = SpringContextHolder.getBean(DappMapper.class);

    public RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);

    private SpotMapper spotMapper = SpringContextHolder.getBean(SpotMapper.class);
    private SwapMapper swapMapper = SpringContextHolder.getBean(SwapMapper.class);
    private FuturesMapper futuresMapper = SpringContextHolder.getBean(FuturesMapper.class);
    //private OptionMapper optionMapper = SpringContextHolder.getBean(OptionMapper.class);
    private ContractOptionCoinMapper optionCoinMapper = SpringContextHolder.getBean(ContractOptionCoinMapper.class);
    private UserMapper userMapper =  SpringContextHolder.getBean(UserMapper.class);
    private MiningOrderMapper miningOrderMapper =  SpringContextHolder.getBean(MiningOrderMapper.class);
    private MiningMapper miningMapper =  SpringContextHolder.getBean(MiningMapper.class);


    /***
     * 保存流水记录
     * @param memberId 用户id
     * @param walletType 账户类型
     * @param flowOp 流水方向（1：流入(+)      0 :流出(-) ）
     * @param flowType 流水类型
     *
     * @param flowPrice 流水金额
     * @param flowCoin 流水币种
     * @param actualPrice 实际金额
     * @param actualCoin 实际币种
     * @param fee 手续费
     * @param feeCoin 手续费币种
     *
     * @param itemCode 具体参数
     * @param coinType 钱包类型
     * @param status 状态
     * @param remark 备注
     *
     * @param beforePrice 变更前额度
     * @param afterPrice 变更后额度
     * @param fromId 来处
     * @param toId 去向
     */
    public void saveCashflow(Long memberId,
                             ProConst.WalletBigType walletType,
                             ProConst.CashFlowOpEnum flowOp,
                             ProConst.CashFlowTypeEnum flowType,
                             BigDecimal flowPrice,
                             String flowCoin,
                             BigDecimal actualPrice,
                             String actualCoin,
                             BigDecimal fee,
                             String feeCoin,
                             ProConst.ItemCode itemCode,
                             String coinType,
                             String status,
                             String remark,
                             BigDecimal beforePrice, BigDecimal afterPrice, Long fromId, Long toId
    )
    {
        Cashflow cashflow = new Cashflow();
        cashflow.setMemberId(memberId)
                .setWalletType(walletType.code)
                .setFlowOp(flowOp.code)
                .setFlowType(flowType.getCode())
                .setFlowPrice(flowPrice)
                .setFlowCoin(flowCoin)
                .setActualPrice(actualPrice)
                .setActualCoin(actualCoin)
                .setFee(fee).setFeeCoin(feeCoin)
                .setItemCode(itemCode.code)
                .setItemName(coinType)
                .setStatus(status)
                .setRemark(remark)
                .setBeforePrice(beforePrice).setAfterPrice(afterPrice)
                .setFromId(fromId)
                .setToId(toId)
                .setCreateUser(memberId);
        cashflowMapper.insert(cashflow);
    }


    /**
     * 保存钱包存储记录
     *
     * @param wallet wallet
     */
    public void saveWalletHistory(Wallet wallet)
    {
        WalletHistory history = new WalletHistory();
        wallet.setCreateTime(new Date());
        wallet.setUpdateTime(new Date());
        BeanUtil.copyProperties(wallet, history);
        walletHistoryMapper.insert(history);
    }


    /**
     * 获取地区数据
     *
     * @param regionId 地区id
     * @param level    层级
     * @return Region
     */
    public Region getRegion(Long regionId, Long level)
    {
        Region region = new Region();
        region.setRegionId(regionId).setLevelType(level).setDel("N");
        return regionMapper.selectOne(new QueryWrapper<>(region));
    }

    /**
     * 获取地区数据
     *
     * @param regionId 地区id
     * @return Region
     */
    public Region getRegion(Long regionId){
        Region region = new Region();
        region.setRegionId(regionId).setDel("N");
        return regionMapper.selectOne(new QueryWrapper<>(region));
    }





    /**
     * 获取钱包信息
     * @param member 用户
     * @param type   钱包类型 ProConst.WalletType
     * @return Wallet
     */
    public Wallet getWallet(Member member, String type){
        return getWallet(member.getMemberId(), type);
    }


    /**
     * 获取钱包信息
     *
     * @param memberId 用户id
     * @param type     钱包类型 ProConst.WalletType
     * @return Wallet
     */
    public Wallet getWallet(Long memberId, String type)
    {
        Wallet walletQ = new Wallet();
        walletQ.setMemberId(memberId).setDel("N").setType(type);
        Wallet walletR = walletMapper.selectOne(new QueryWrapper<>(walletQ));
        return walletR;
    }


    public Wallet getWalletByNoException(Long memberId, String type)
    {
        Wallet walletQ = new Wallet();
        walletQ.setMemberId(memberId).setDel("N").setType(type);
        Wallet walletR = walletMapper.selectOne(new QueryWrapper<>(walletQ));
        return walletR;
    }

    /**
     * 网关记录
     */
    public void gatewayRecord(GatewayRecord gatewayRecord)
    {
        gatewayRecord.setCreateUser(-1L);
        gatewayRecordMapper.insert(gatewayRecord);
    }


    /**
     * 获取系统参数配置信息
     *
     * @param code code
     * @return String
     */
    public String getSysConfigValueByCode(String code)
    {
        Config config = new Config();
        config.setCode(code);

        Config result = configMapper.selectOne(new QueryWrapper<>(config));
        if (result != null)
        {
            return result.getValue();
        }
        return "";
    }

    /**
     * 获取系统参数配置信息
     *
     * @param code code
     * @return String
     */
    public String cfg(String code)
    {
        return getSysConfigValueByCode(code);
    }




    /**
     * 获取用户信息
     *
     * @param memberId   用户id
     * @param inviteCode 邀请码
     * @return Member
     */
    public Member getMember(Long memberId, String inviteCode){
        Member member = new Member();
        member.setMemberId(memberId).setInviteCode(inviteCode);
        return appMemberMapper.selectOne(new QueryWrapper<>(member));
    }


    /**
     * 根据id获取用户信息
     *
     * @param memberId
     * @return Member
     */
    public Member getMember(Long memberId)
    {
        return getMember(memberId, null);
    }
    /**
     * 根据id获取用户下级用户
     *
     * @param memberId
     * @return Member
     */
    public List<Member> getMemberTeamForPRI(Long parentRefereeId)
    {
        return appMemberMapper.selectTeamForPRI(parentRefereeId);
    }


    /**
     * 是否开放日志
     *
     * @param apiGatewayCode 网关编码
     * @return boolean
     */
    public boolean isOpenLog(String apiGatewayCode)
    {
        if (!existGateWay(apiGatewayCode))//网关是否存在
        {
            return false;
        }
        GatewayDefine define = new GatewayDefine();
        define.setDel("N").setCode(apiGatewayCode).setStatus(ProConst.GatewayStatusEnum.OPEN_LOG.getCode());
        if (gatewayDefineMapper.selectOne(new QueryWrapper<>(define)) == null)
        {
            return false;
        }
        return true;
    }


    /**
     * 是否打开网关
     *
     * @param apiGatewayCode
     * @return boolean
     */
    public boolean isEnableGateway(String apiGatewayCode)
    {
        if (!existGateWay(apiGatewayCode))
        {
            return false;
        }
        GatewayDefine define = new GatewayDefine();
        define.setDel("N").setCode(apiGatewayCode);
        if (gatewayDefineMapper.selectOne(new QueryWrapper<>(define).in("status", ProConst.GatewayStatusEnum.OPEN.getCode(), ProConst.GatewayStatusEnum.OPEN_LOG.getCode())) == null)
        {
            return false;
        }
        return true;
    }

    /**
     * 网关是否存在
     *
     * @param apiGatewayCode
     * @return boolean
     */
    public boolean existGateWay(String apiGatewayCode)
    {
        GatewayDefine define = new GatewayDefine();
        define.setDel("N").setCode(apiGatewayCode);
        if (gatewayDefineMapper.selectList(new QueryWrapper<>(define)).size() > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 将异常保存
     *
     * @param jsonObject
     */
    public void saveWalletException(String method, String msg, JSONObject jsonObject)
    {
        SvcException svcException = new SvcException();
        svcException.setMethod(method).setMsg(msg).setContent(jsonObject.toJSONString())
                .setCreateTime(new Date());
        svcExceptionMapper.insert(svcException);
    }

    /**
     * 获取BTC已提数量
     *
     * @param memberId
     * @return BigDecimal
     */
    public BigDecimal getBtcWithdrawalPrice(Long memberId)
    {
        return cashflowMapper.getBtcWithdrawalPrice(memberId);
    }


    /**
     * 合约账户
     *
     * @param memberId id
     * @param type     类型
     * @return Contract
     */
    public Contract getContract(Long memberId, String type)
    {
        Contract walletQ = new Contract();
        walletQ.setMemberId(memberId).setDel("N").setType(type);
        Contract walletR = contractMapper.selectOne(new QueryWrapper<>(walletQ));
        return walletR;
    }


    /**
     * 币币账户
     *
     * @param memberId
     * @param type
     * @return Currency
     */
    public Currency getCurrency(Long memberId, String type)
    {
        Currency walletQ = new Currency();
        walletQ.setMemberId(memberId).setDel("N").setType(type);
        Currency walletR = currencyMapper.selectOne(new QueryWrapper<>(walletQ));
        if(walletR!=null){
            if(walletR.getUsedPrice()==null){
                walletR.setUsedPrice(BigDecimal.ZERO);
            }
            if(walletR.getLockedPrice()==null){
                walletR.setLockedPrice(BigDecimal.ZERO);
            }
        }

        return walletR;
    }
    /**
     * 矿机账户
     *
     * @param memberId
     * @param type
     * @return Currency
     */
    public FinMining getMining(Long memberId, String type)
    {
        FinMining walletQ = new FinMining();
        walletQ.setMemberId(memberId).setDel("N").setType(type);
        FinMining walletR = finMiningMapper.selectOne(new QueryWrapper<>(walletQ));
        if(walletR!=null){
            if(walletR.getUsedPrice()==null){
                walletR.setUsedPrice(BigDecimal.ZERO);
            }
            if(walletR.getLockedPrice()==null){
                walletR.setLockedPrice(BigDecimal.ZERO);
            }
        }

        return walletR;
    }

    /**
     * 期权账户
     *
     * @param memberId
     * @param type
     * @return Currency
     */
    public FinOption getFinOption(Long memberId, String type)
    {
        FinOption walletQ = new FinOption();
        walletQ.setMemberId(memberId).setDel("N").setType(type);
        FinOption walletR = finOptionMapper.selectOne(new QueryWrapper<>(walletQ));
        return walletR;
    }
    /**
     * 期权账户s
     */
    public List<FinOption> getFinOptions(Long memberId)
    {
        FinOption walletQ = new FinOption();
        walletQ.setMemberId(memberId).setDel("N");
        List<FinOption> walletR = finOptionMapper.selectList(new QueryWrapper<>(walletQ));
        return walletR;
    }
    /**
     * 黄金账户
     *
     * @param memberId
     * @param type
     * @return Currency
     */
    public FinFutures getFinFutures(Long memberId, String type)
    {
        FinFutures walletQ = new FinFutures();
        walletQ.setMemberId(memberId).setDel("N").setType(type);
        FinFutures walletR = finFuturesMapper.selectOne(new QueryWrapper<>(walletQ));
        return walletR;
    }
    /**
     * 黄金账户s
     */
    public List<FinFutures> getFinFuturess(Long memberId)
    {
        FinFutures walletQ = new FinFutures();
        walletQ.setMemberId(memberId).setDel("N");
        List<FinFutures> walletR = finFuturesMapper.selectList(new QueryWrapper<>(walletQ));
        return walletR;
    }




//    /**
//     * 法币账户
//     * @param memberId
//     * @return
//     */
//    public Legal getLegal(Long memberId)
//    {
//        Legal walletQ = new Legal();
//        walletQ.setMemberId(memberId).setDel("N");
//        Legal walletR = legalMapper.selectOne(new QueryWrapper<>(walletQ));
//        return walletR;
//    }


    /**
     * 法币账户
     *
     * @param memberId
     * @param type     类型
     * @return Legal
     */
    public Legal getLegal(Long memberId, String type)
    {
        Legal walletQ = new Legal();
        walletQ.setMemberId(memberId)
                .setType(type)
                .setDel("N");
        return legalMapper.selectOne(new QueryWrapper<>(walletQ));
    }


    /**
     * 修改平台密码
     *
     * @param platPwd
     */
    public void updateFlatPwd(String platPwd)
    {
        configMapper.updateFlatPwd(platPwd);
    }


    //钱包接口记录 日志
    public void wlog(Dapp dapp)
    {
        dapp.setCreateUser(-1L);
        dappMapper.insert(dapp);
    }

    //钱包接口记录 日志
    public void wlog(String name, String request, String response)
    {
        Dapp dapp = new Dapp();
        dapp.setName(name)
                .setRequest(request)
                .setResponse(response);
        wlog(dapp);
    }

    /**
     * 获取现货交易对列表
     *
     * @param status Y-启用 N-禁用 NULL-全部
     * @return List
     */
    public List<Spot> getSpots(String status){
        Spot quer = new Spot();
        quer.setDel("N")
                .setStatus(status);
        return spotMapper.selectList(new QueryWrapper<>(quer));
    }

    /**
     * 获取现货交易对 对象
     *
     * @param symbol 现货交易对
     * @return Spot
     */
    public Spot getSpot(String symbol)
    {
        Spot query=new Spot();
        query.setSymbol(symbol);

        return spotMapper.selectOne(new QueryWrapper<>(query));
    }


    /**
     * 获取合约交易对列表
     *
     * @param status Y-启用 N-禁用 NULL-全部
     * @return List
     */
    public List<Swap> getSwaps(String status)
    {
        Swap query = new Swap();
        query.setDel("N")
                .setStatus(status);
        return swapMapper.selectList(new QueryWrapper<>(query));
    }

    public List<Futures> getFuturess(String status)
    {
        Futures query = new Futures();
        query.setDel("N");
        query.setStatus(status);
        return futuresMapper.selectList(new QueryWrapper<>(query));
    }

    public Futures getFutures(String symbol)
    {
        Futures query = new Futures();
        query.setSymbol(symbol);
        return futuresMapper.selectOne(new QueryWrapper<>(query));
    }


 /**
     * 获取合约交易对列表
     *
     * @param symbol 交易对
     * @return List
     */
    public Swap getSwap(String symbol)
    {
        Swap query = new Swap();
        query.setDel("N")
                .setSymbol(symbol)
                ;
        return swapMapper.selectOne(new QueryWrapper<>(query));
    }
    /**
     * 获取一个期权交易对列表
     *
     * @param symbol 交易对
     * @return List
     */
    public ContractOptionCoin getOption(String symbol)
    {
        ContractOptionCoin query = new ContractOptionCoin();
//        query.setDel("N")
//                .setSymbol(symbol)
        ;
        return optionCoinMapper.selectOne(new QueryWrapper<>(query));
    }
    /**
     * 获取期权交易对列表
     *
     * @param status Y-启用 N-禁用 NULL-全部
     * @return List
     */
    public List<ContractOptionCoin> getOptions(String status)
    {
        ContractOptionCoin query = new ContractOptionCoin();
        //query.setDel("N")
        //        .setStatus(status);
        return optionCoinMapper.selectList(new QueryWrapper<>(query));
    }



    public Long getMemberByUser(Long id) {
        if (id != null && id >0){
          User user =   userMapper.selectById(id);
          if(user != null && user.getMemberId() != null){
              Member member = this.appMemberMapper.selectById(user.getMemberId());
              if(member != null){
                  return member.getMemberId();
              }
          }
        }
        return  null;
    }

    public String getMemberRecommendIdsByUser(Long id) {
        if (id != null && id >0){
            User user =   userMapper.selectById(id);
            if(user != null && user.getMemberId() != null){
                Member member = this.appMemberMapper.selectById(user.getMemberId());
                if(member != null){
                   if (StringUtils.isNotBlank(member.getParentRefereeId())){
                       return "/"+member.getMemberId()+member.getParentRefereeId();
                   }else{
                       return "/"+member.getMemberId();
                   }
                }
            }
        }
        return  null;
    }
    public String getMemberRecommendIdsByMember(Long memberId) {
        if (memberId != null && memberId >0){
            Member member = this.appMemberMapper.selectById(memberId);
            if(member != null){
                if (StringUtils.isNotBlank(member.getParentRefereeId())){
                    return "/"+member.getMemberId()+member.getParentRefereeId();
                }else {
                    return "/" + member.getMemberId();
                }
            }
        }
        return  null;
    }

    public MiningOrder getMiningOrder(Long id) {
        return  miningOrderMapper.selectById(id);
    }
    public Mining getMining(Long id) {
        return  miningMapper.selectById(id);
    }

    /**
     * 获取用户今日提现
     * @param memberId
     * @return
     */
    public BigDecimal getTodayWithdraw(Long memberId){
        Calendar instance = Calendar.getInstance();
        BigDecimal sum = BigDecimal.ZERO;
        Withdraw recharge = new Withdraw();
        //recharge.setType(type);
        recharge.setMemberId(memberId);
        recharge.setStatus("PASS");
        String s = DateTimeUtil.dateToStr(instance.getTime(), DateTimeUtil.ZERO_FORMAT);
        List<Withdraw> recharges = withdrawService.list(new QueryWrapper<>(recharge).gt("CREATE_TIME", s));
        for (Withdraw entity : recharges) {
            sum = sum.add(entity.getPrice());
        }
        return sum;
    }
    /**
     * 获取用户总提现
     * @param memberId
     * @return
     */
    public BigDecimal getAllWithdraw(Long memberId){
        BigDecimal withdrawPriceSum = BigDecimal.ZERO;
        List<Withdraw> withdraws1 = withdrawService.selectByMemberId(memberId);
        if(withdraws1!=null){
            for (Withdraw withdraw : withdraws1) {
                BigDecimal actualPrice = withdraw.getActualPrice();
                withdrawPriceSum = withdrawPriceSum.add(actualPrice);
            }
        }
        return withdrawPriceSum;
    }
    /**
     * 获取用户今日充值
     * @param memberId
     * @return
     */
    public BigDecimal getTodayRecharge(Long memberId){
        Calendar instance = Calendar.getInstance();
        BigDecimal sum = BigDecimal.ZERO;
        Recharge recharge = new Recharge();
        //recharge.setType(type);
        recharge.setStatus("PASS");
        recharge.setMemberId(memberId);
        String s = DateTimeUtil.dateToStr(instance.getTime(), DateTimeUtil.ZERO_FORMAT);
        List<Recharge> recharges = rechargeService.list(new QueryWrapper<>(recharge).gt("CREATE_TIME", s));
        for (Recharge entity : recharges) {
            sum = sum.add(entity.getPrice());
        }
        return sum;
    }
    /**
     * 获取用户今日充值
     * @param memberId
     * @return
     */
    public BigDecimal getAllRecharge(Long memberId){
        BigDecimal actualPriceSum = BigDecimal.ZERO;
        List<Recharge> recharges1 = rechargeService.selectByMemberId(memberId);
        if(recharges1!=null){
            for (Recharge recharge1 : recharges1) {
                BigDecimal actualPrice = recharge1.getActualPrice();
                actualPriceSum = actualPriceSum.add(actualPrice);
            }
        }
        return actualPriceSum;
    }
    /**
     * 获取用户团队人数
     * @param memberId
     * @return
     */
    public int getTeamNumbers(Long memberId){
        return appMemberMapper.getTeamNumbers(memberId);
    }
    public BigDecimal getUserNoPrice(Long memberId){
        return appMemberMapper.getAllByLockedPrice(memberId);

    };
}
