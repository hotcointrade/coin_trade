package cn.stylefeng.guns.modular.app.service;


import cn.stylefeng.guns.modular.app.entity.*;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.chain.entity.Recharge;
import cn.stylefeng.guns.modular.chain.entity.Withdraw;
import cn.stylefeng.guns.modular.chain.service.RechargeService;
import cn.stylefeng.guns.modular.chain.service.WithdrawService;
import cn.stylefeng.guns.modular.coin.entity.ContractOption;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionOrder;
import cn.stylefeng.guns.modular.coin.service.ContractOptionOrderService;
import cn.stylefeng.guns.modular.coin.service.ContractOptionService;
import cn.stylefeng.guns.modular.com.entity.CheckIn;
import cn.stylefeng.guns.modular.com.service.CheckInService;
import cn.stylefeng.guns.modular.e.entity.Compact;
import cn.stylefeng.guns.modular.e.entity.Match;
import cn.stylefeng.guns.modular.e.service.CompactService;
import cn.stylefeng.guns.modular.e.service.MatchService;
import cn.stylefeng.guns.modular.fin.entity.*;
import cn.stylefeng.guns.modular.fin.service.*;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.entity.MiningOrderDetail;
import cn.stylefeng.guns.modular.mining.service.MiningOrderDetailService;
import cn.stylefeng.guns.modular.mining.service.MiningOrderService;
import cn.stylefeng.guns.modular.otc.entity.*;
import cn.stylefeng.guns.modular.otc.service.*;
import cn.stylefeng.guns.modular.system.service.LoginLogService;
import cn.stylefeng.guns.modular.system.service.OperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class DeleteService extends Constant {

    //团队佣金
    @Autowired
    private TeamEarningService teamEarningService;
    //团队收益
    @Autowired
    private TeamEarningsService teamEarningsService;
    //团队收益划转记录
    @Autowired
    private WalletTransferService walletTransferService;
    //实名认证
    @Autowired
    private VerifyService verifyService;
    //收款方式
    @Autowired
    private PaymentService paymentService;
    //流水记录
    @Autowired
    private CashflowService cashflowService;
    //充币信息
    @Autowired
    private RechargeService rechargeService;
    //提币信息
    @Autowired
    private WithdrawService withdrawService;



    //一件清理无用数据

    //删除用户 -- 团队 --团队收益 -- 团队收益划转记录 --实名认证 -- 收款方式 --流水记录 --充币信息 --提笔信息

    // --钱包[钱包，合约，币币，法币，期权]
    @Autowired
    private WalletService walletService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private LegalService legalService;
    @Autowired
    private FinOptionService finOptionService;
    //理财生息 -- 理财订单
    @Autowired
    private FinPeriodicOrderService finPeriodicOrderService;
    //--上臂申请
    @Autowired
    private CoinApplyService coinApplyService;

    //法币  -- 用户押金 -- 退还押金 --购买挂单订单 --出售挂单订单，交易结算订单,申述订单
    @Autowired
    private DepositService depositService;
    @Autowired
    private BackService backService;

    @Autowired
    private BuyService buyService;
    @Autowired
    private SellService sellService;
    //交易结算订单,申述订单
    @Autowired
    private BillService billService;


    //新币 --认购记录 --释放记录
    @Autowired
    private TeManagementLogService teManagementLogService;
    @Autowired
    private TeManagementReleaseService teManagementReleaseService;
    //币币 --委托单信息
    @Autowired
    private MatchService matchService;
    //期权 --合约管理 --订单管理
    @Autowired
    private ContractOptionService contractOptionService;
    @Autowired
    private ContractOptionOrderService contractOptionOrderService;

    //合约 --合约订单
    @Autowired
    private CompactService compactService;

    //登录日志
    @Autowired
    private LoginLogService loginLogService;
    //业务日志
    @Autowired
    private OperationLogService operationLogService;


    @Autowired
    private MiningOrderService miningOrderService;
    @Autowired
    private MiningOrderDetailService miningOrderDetailService;
    @Autowired
    private CheckInService checkInService;

    public void delete(Long memberId){
        int delete0 = teamEarningService.getBaseMapper().delete(new LambdaQueryWrapper<TeamEarning>().eq(TeamEarning::getMemberId,memberId));
        int delete1 = teamEarningsService.getBaseMapper().delete((new LambdaQueryWrapper<TeamEarnings>()).eq(TeamEarnings::getEarningsId, memberId));
        int delete2 = walletTransferService.getBaseMapper().delete((new LambdaQueryWrapper<WalletTransfer>()).eq(WalletTransfer::getMemberId, memberId));


        int delete4 = verifyService.getBaseMapper().delete((new LambdaQueryWrapper<Verify>()).eq(Verify::getMemberId, memberId));
        int delete5 = paymentService.getBaseMapper().delete((new LambdaQueryWrapper<Payment>()).eq(Payment::getMemberId, memberId));
        int delete6 = cashflowService.getBaseMapper().delete((new LambdaQueryWrapper<Cashflow>()).eq(Cashflow::getMemberId, memberId));
        int delete7 = rechargeService.getBaseMapper().delete((new LambdaQueryWrapper<Recharge>()).eq(Recharge::getMemberId, memberId));
        int delete8 = withdrawService.getBaseMapper().delete((new LambdaQueryWrapper<Withdraw>()).eq(Withdraw::getMemberId, memberId));

        int delete9 = walletService.getBaseMapper().delete((new LambdaQueryWrapper<Wallet>()).eq(Wallet::getMemberId, memberId));
        int delete10 = contractService.getBaseMapper().delete((new LambdaQueryWrapper<Contract>()).eq(Contract::getMemberId, memberId));
        int delete11 = currencyService.getBaseMapper().delete((new LambdaQueryWrapper<Currency>()).eq(Currency::getMemberId, memberId));
        int delete12 = legalService.getBaseMapper().delete((new LambdaQueryWrapper<Legal>()).eq(Legal::getMemberId, memberId));
        int delete13 = finOptionService.getBaseMapper().delete((new LambdaQueryWrapper<FinOption>()).eq(FinOption::getMemberId, memberId));
        int delete14 = finPeriodicOrderService.getBaseMapper().delete((new LambdaQueryWrapper<PeriodicOrder>()).eq(PeriodicOrder::getMemberId, memberId));
        int delete15 = coinApplyService.getBaseMapper().delete((new LambdaQueryWrapper<CoinApply>()).eq(CoinApply::getMemberId, memberId));
        int delete16 = depositService.getBaseMapper().delete((new LambdaQueryWrapper<Deposit>()).eq(Deposit::getMemberId, memberId));
        int delete17 = backService.getBaseMapper().delete((new LambdaQueryWrapper<Back>()).eq(Back::getMemberId, memberId));
        int delete18 = buyService.getBaseMapper().delete((new LambdaQueryWrapper<Buy>()).eq(Buy::getMemberId, memberId));
        int delete19 = sellService.getBaseMapper().delete((new LambdaQueryWrapper<Sell>()).eq(Sell::getMemberId, memberId));

        int delete20 = billService.getBaseMapper().delete((new LambdaQueryWrapper<Bill>()).eq(Bill::getBuyMid, memberId));
        int delete21 = billService.getBaseMapper().delete((new LambdaQueryWrapper<Bill>()).eq(Bill::getSellMid, memberId));
        int delete22 = teManagementLogService.getBaseMapper().delete((new LambdaQueryWrapper<TeManagementLog>()).eq(TeManagementLog::getUid, memberId));
        int delete23 = teManagementReleaseService.getBaseMapper().delete((new LambdaQueryWrapper<TeManagementRelease>()).eq(TeManagementRelease::getUid, memberId));
        int delete24 = matchService.getBaseMapper().delete((new LambdaQueryWrapper<Match>()).eq(Match::getMemberId, memberId));
        int delete25 = contractOptionOrderService.getBaseMapper().delete((new LambdaQueryWrapper<ContractOptionOrder>()).eq(ContractOptionOrder::getMemberId, memberId));
        //int delete3 = contractOptionService.getBaseMapper().delete((new LambdaQueryWrapper<ContractOption>()));
        int delete = compactService.getBaseMapper().delete((new LambdaQueryWrapper<Compact>()).eq(Compact::getMemberId, memberId));
        int delete26 =teamEarningsService.getBaseMapper().delete(new LambdaQueryWrapper<TeamEarnings>().eq(TeamEarnings::getEarningsId,memberId));
        int delete27 = walletTransferService.getBaseMapper().delete(new LambdaQueryWrapper<WalletTransfer>().eq(WalletTransfer::getMemberId,memberId));
        int delete28 = miningOrderService.getBaseMapper().delete(new LambdaQueryWrapper<MiningOrder>().eq(MiningOrder::getMemberId,memberId));
        int delete29 = miningOrderDetailService.getBaseMapper().delete(new LambdaQueryWrapper<MiningOrderDetail>().eq(MiningOrderDetail::getMemberId,memberId));
        int delete30 = checkInService.getBaseMapper().delete(new LambdaQueryWrapper<CheckIn>().eq(CheckIn::getMemberId,memberId));


    }

    /**
     * delete from fin_cashflow
     * where not exists
     * (select 1 from app_member
     * where fin_cashflow.member_id =app_member.member_id )
     */
    public void deleteNotExists(String tableName , String toTableName,String tableField,String toTableField ){
        String sql = tableName;
        if(toTableName !=null){
            sql+=" where not exists (select 1 from "
                    +toTableName
                    +" where "+tableName+"."+tableField+" = "+toTableName+"."+toTableField+")";
        }
        SqlRunner.db().delete("delete from "+sql);
    }
    //删除所有脏数据
    public void deleteAll(){
        deleteNotExists("app_team_earnings","app_member","earnings_id","member_id");
        deleteNotExists("fin_invite_wallet","app_member","member_id","member_id");

        deleteNotExists("app_wallet_transfer","app_member","member_id","member_id");
        deleteNotExists("app_verify","app_member","member_id","member_id");
        deleteNotExists("app_payment","app_member","member_id","member_id");
        deleteNotExists("fin_cashflow","app_member","member_id","member_id");
        deleteNotExists("chain_recharge","app_member","member_id","member_id");
        deleteNotExists("chain_withdraw","app_member","member_id","member_id");
        deleteNotExists("fin_wallet","app_member","member_id","member_id");
        deleteNotExists("fin_contract","app_member","member_id","member_id");
        deleteNotExists("fin_currency","app_member","member_id","member_id");
        deleteNotExists("fin_legal","app_member","member_id","member_id");
        deleteNotExists("fin_option","app_member","member_id","member_id");
        deleteNotExists("fin_periodic_order","app_member","member_id","member_id");
        deleteNotExists("app_coin_apply","app_member","member_id","member_id");
        deleteNotExists("otc_deposit","app_member","member_id","member_id");
        deleteNotExists("otc_back","app_member","member_id","member_id");
        deleteNotExists("otc_buy","app_member","member_id","member_id");
        deleteNotExists("otc_sell","app_member","member_id","member_id");
        deleteNotExists("otc_bill","app_member","buy_mid","member_id");
        deleteNotExists("otc_bill","app_member","sell_mid","member_id");
        deleteNotExists("te_management_log","app_member","uid","member_id");
        deleteNotExists("e_match","app_member","member_id","member_id");
        deleteNotExists("contract_option_order","app_member","member_id","member_id");
        deleteNotExists("mining_order","app_member","member_id","member_id");
        deleteNotExists("mining_order_detail","app_member","member_id","member_id");
        deleteNotExists("com_check_in","app_member","member_id","member_id");

    }
    //删除合约期数
    public void deleteContractOption(){

        deleteNotExists("contract_option",null,"member_id","member_id");
        SqlRunner.db().delete("update  contract_option_coin set max_Option_No= 0");
    }


}







