package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.DateTimeUtil;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.chain.entity.Recharge;
import cn.stylefeng.guns.modular.chain.entity.Withdraw;
import cn.stylefeng.guns.modular.chain.service.RechargeService;
import cn.stylefeng.guns.modular.chain.service.WithdrawService;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionOrder;
import cn.stylefeng.guns.modular.coin.service.ContractOptionOrderService;
import cn.stylefeng.guns.modular.e.entity.Compact;
import cn.stylefeng.guns.modular.e.entity.Match;
import cn.stylefeng.guns.modular.e.service.CompactService;
import cn.stylefeng.guns.modular.e.service.MatchService;
import cn.stylefeng.guns.modular.fin.entity.*;
import cn.stylefeng.guns.modular.fin.service.*;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
import cn.stylefeng.guns.modular.mining.service.MiningOrderService;
import cn.stylefeng.guns.modular.otc.entity.*;
import cn.stylefeng.guns.modular.otc.service.*;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 */
@Controller
@RequestMapping("/welcome")
public class WelcomeController extends BaseController {
    @Autowired
    MiningOrderService miningOrderService;
    @Autowired
    ContractOptionOrderService contractOptionOrderService;
    @Resource
    WalletService walletService;
    @Autowired
    CurrencyService currencyService;
    @Autowired
    FinOptionService finOptionService;
    @Autowired
    FinMiningService finMiningService;
    @Autowired
    ContractService contractService;
    @Autowired
    LegalService legalService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private BuyService buyService;

    @Autowired
    private SellService sellService;
    @Autowired
    private CompactService compactService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MemberService memberService;

    @Resource
    RedisUtil redisUtil;

    @Autowired
    DepositService depositService;

    @Autowired
    BackService backService;
    @Autowired
    BillService billService;





    /**
     * 欢迎页
     */

    @ResponseBody
    @RequestMapping("/info")
    public HashMap info(@RequestParam("type") String type) {
        HashMap<Object, Object> map = new HashMap<>();
        //Calendar instance = Calendar.getInstance();
        //根据条件查询
        Long memberId = null;
        String recommendIds = null;
        if (!ShiroKit.isAdmin()){
            memberId=  F.me().getMemberByUser(ShiroKit.getUser().getId());
            recommendIds = F.me().getMemberRecommendIdsByUser(ShiroKit.getUser().getId());
        }
        List<Member> list = F.me().getMemberTeamForPRI(memberId);
        Long[] memberIds = new Long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            memberIds[i] = list.get(i).getMemberId();
        }


        //钱包统计数据
        getWallet(type,map,memberId);

        //今日充币 本周充币
        getRecharge(type,map,memberId,memberIds);

        //今日提币 本周提币
        getWithdraw(type,map,memberId,memberIds);

        //永续 买入卖出
        getCompact(type,map,memberId);
        //法币 买入卖出
        getOtc(type,map,memberId);
        //币币 买入卖出
        getMatch(type,map,memberId);
        //商家数量 一级代理数量
        //用户数量 冻结数量
        getMenber(map,memberId,list);
        //期权
        getOption(map,memberId);

        //矿机
        getMining(map,memberId,memberIds);


        //短信邮箱发送数量
//        map.put("emailCount",redisUtil.get(Constant.EMAIL_COUNT));
//        map.put("smsCount",redisUtil.get(Constant.SMS_COUNT));


        return map;

    }

    private void getMining(HashMap<Object, Object> map, Long memberId,Long[] memberIds) {
        MiningOrder miningOrderQ = new MiningOrder();

        int miningOrderCount = 0;
        int teamMiningOrderCoung = 0;
//        int count = miningOrderService.count(new QueryWrapper<>(miningOrderQ));
//        map.put("miningOrderCount",count);
        if(memberIds.length>0){
            for (Long id : memberIds) {
                miningOrderQ.setMiningId(id);
                int count = miningOrderService.count(new QueryWrapper<>(miningOrderQ));
                if(id.equals(memberId)){
                    miningOrderCount = count;
                }
                teamMiningOrderCoung+=count;

            }

        }
        map.put("miningOrderCount",miningOrderCount);
        map.put("teamMiningOrderCount",teamMiningOrderCoung);


    }

    private void getOption(HashMap<Object, Object> map, Long memberId) {
        BigDecimal optionZ = BigDecimal.ZERO;
        BigDecimal optionD = BigDecimal.ZERO;
        ContractOptionOrder contractOptionOrderQ = new ContractOptionOrder();
        List<ContractOptionOrder> list = contractOptionOrderService.list(new QueryWrapper<>(contractOptionOrderQ));
        if (list!=null)
        for (ContractOptionOrder contractOptionOrder : list) {
            if(contractOptionOrder.getDirection() == 0){
                optionZ = optionZ.add(contractOptionOrder.getBetAmount());
            }else{
                optionD = optionD.add(contractOptionOrder.getBetAmount());
            }
        }
        map.put("optionZ",optionZ);
        map.put("optionD",optionD);



    }

    private void getMenber(HashMap<Object, Object> map,Long memberId,List<Member> list) {
        Member member = new Member();
//        member.setStatus("Y");
//        List<Member> list = memberService.list(new QueryWrapper<>(member));
//        map.put("memberY",list.size());
//        member.setStatus("N");
        int memberY = 0;
        int memberN = 0;
        int memcount = 0;
        int memcount2 = 0;
        for (Member member1 : list) {
            if("Y".equals(member1.getStatus())){
                memberY++;
            }else {
                memberN++;
            }
            String parentRefereeId = member1.getParentRefereeId();
            String[] split = parentRefereeId.split("/");

            if (parentRefereeId.equals("/")) {
                memcount++;
            }
            if (split.length == 2) {
                memcount2++;
            }
        }
        //以及会员
        map.put("memcount",memcount);
        map.put("memcount2",memcount2);
        map.put("memberY",memberY);
        map.put("memberN",memberN);
        map.put("memberCount",memberY+memberN);
        //int count1 = memberService.count(new QueryWrapper<>(member));
        //map.put("memberN",count1);

    }

    private HashMap<Object, Object> getCompact(String type, HashMap<Object, Object> map,Long memberId) {
        Compact compact = new Compact();
        compact.setCoin(type);
        compact.setMemberId(memberId);
        compact.setCompactType(ProConst.CompactType.BUY.code);
        compact.setEnabled("N");
        List<Compact> buylist = compactService.list(new QueryWrapper<>(compact));
        BigDecimal buyCompact= BigDecimal.ZERO;
        for (Compact compact1 : buylist) {
            buyCompact = buyCompact.add(compact1.getNumbers().multiply(compact1.getUnit()));
        }
        map.put("buyCompact",buyCompact);

        compact.setCompactType(ProConst.CompactType.SELL.code);
        BigDecimal sellCompact= BigDecimal.ZERO;
        List<Compact> selllist = compactService.list(new QueryWrapper<>(compact));
        for (Compact compact1 : selllist) {
            sellCompact = sellCompact.add(compact1.getNumbers().multiply(compact1.getUnit()));
        }
        map.put("sellCompact",sellCompact);

        return map;
    }
    private HashMap<Object, Object> getMatch(String type, HashMap<Object, Object> map,Long memberId) {
        Match compact = new Match();
        //compact.set(type);
        compact.setMemberId(memberId);
        compact.setMatchType(ProConst.MatchType.BUY.code);
        compact.setStatus(ProConst.MatchStatus.FINISH.code);
        List<Match> buylist = matchService.list(new QueryWrapper<>(compact));
        BigDecimal buyCompact= BigDecimal.ZERO;
        for (Match compact1 : buylist) {
            buyCompact = buyCompact.add(compact1.getTotalPrice());
        }
        map.put("buyMatch",buyCompact);

        compact.setMatchType(ProConst.MatchType.SELL.code);
        BigDecimal sellCompact= BigDecimal.ZERO;
        List<Match> selllist = matchService.list(new QueryWrapper<>(compact));
        for (Match compact1 : selllist) {
            sellCompact = sellCompact.add(compact1.getTotalPrice());
        }
        map.put("sellMatch",sellCompact);

        return map;
    }

    private HashMap getOtc(String type,HashMap map,Long memberId){
        Bill bill = new Bill();
        bill.setDel("N");
        bill.setStatus(ProConst.BillStatus.FINISH.code);
        //bill.setType(ProConst.BillType.BUY.code);
        bill.setBuyMid(memberId);
        List<Bill> buyList = billService.list(new QueryWrapper<>(bill));
        BigDecimal otcBuy  =BigDecimal.ZERO;
        for (Bill bill1 : buyList) {
            otcBuy =otcBuy.add(bill1.getCny());
        }
        //BigDecimal otcBuy1 = (BigDecimal)map.get("otcBuy");
        map.put("otcBuy",otcBuy.setScale(4, BigDecimal.ROUND_HALF_UP));

        Bill billSell = new Bill();
        billSell.setDel("N");
        billSell.setStatus(ProConst.BillStatus.FINISH.code);
        //billSell.setType(ProConst.BillType.SELL.code);
        billSell.setSellMid(memberId);
        BigDecimal otcSell  =BigDecimal.ZERO;
        List<Bill> sellList = billService.list(new QueryWrapper<>(billSell));
        for (Bill bill1 : sellList) {
            otcSell = otcSell.add(bill1.getCny());
        }
        //BigDecimal otcSell1 = (BigDecimal)map.get("otcSell");
        map.put("otcSell",otcSell.setScale(4, BigDecimal.ROUND_HALF_UP));
//        Deposit deposit = new Deposit();
//        deposit.setDel("N");
//
//        deposit.setStatus("1");
//        int count =depositService.list(new QueryWrapper<>(deposit).groupBy("member_id")).size();
//        //int count = depositService.getBaseMapper().selectCount(new QueryWrapper<>(deposit).groupBy("member_id"));
//
//        Back back = new Back();
//        back.setDel("N");
//        int backCount = backService.list(new QueryWrapper<>(back).groupBy("member_id")).size();
//        map.put("otcCount",count-backCount);

        return map;
    }
    private HashMap getRecharge(String type,HashMap map,Long memberId,Long[] memberIds){
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
        map.put("rechargeSum",sum);

        //团队总充值



        BigDecimal actualPriceSum = BigDecimal.ZERO;
        List<Recharge> recharges1 = rechargeService.selectByMemberId(memberIds);
        if(recharges1!=null){
            for (Recharge recharge1 : recharges1) {
                BigDecimal actualPrice = recharge1.getActualPrice();
                actualPriceSum = actualPriceSum.add(actualPrice);
            }
        }
        map.put("actualPriceSum",actualPriceSum);

//        BigDecimal weekSum = BigDecimal.ZERO;
//        instance.add(Calendar.DAY_OF_YEAR, -7);
//        String s1 = DateTimeUtil.dateToStr(instance.getTime(), DateTimeUtil.ZERO_FORMAT);
//        List<Recharge> recharge2s = rechargeService.list(new QueryWrapper<>(recharge).gt("CREATE_TIME", s1));
//        for (Recharge recharge2 : recharge2s) {
//            weekSum = weekSum.add(recharge2.getPrice());
//        }
//        map.put("weekRechargeSum",weekSum);
        return map;
    }
    private HashMap getWithdraw(String type,HashMap map,Long memberId,Long[] memberIds){
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
        map.put("withdrawSum",sum);


        BigDecimal withdrawPriceSum = BigDecimal.ZERO;
        List<Withdraw> withdraws1 = withdrawService.selectByMemberId(memberIds);
        if(withdraws1!=null){
            for (Withdraw withdraw : withdraws1) {
                BigDecimal actualPrice = withdraw.getActualPrice();
                withdrawPriceSum = withdrawPriceSum.add(actualPrice);
            }
        }
        map.put("withdrawPriceSum",withdrawPriceSum);

//        BigDecimal weekSum = BigDecimal.ZERO;
//        instance.add(Calendar.DAY_OF_YEAR, -7);
//        String s1 = DateTimeUtil.dateToStr(instance.getTime(), DateTimeUtil.ZERO_FORMAT);
//        List<Withdraw> recharge2s = withdrawService.list(new QueryWrapper<>(recharge).gt("CREATE_TIME", s1));
//        for (Withdraw recharge2 : recharge2s) {
//            weekSum = weekSum.add(recharge2.getPrice());
//        }
//        map.put("weekWithdrawSum",weekSum);

        return map;
    }

    private HashMap getWallet(String type,HashMap map,Long memberId){
        //钱包
        Wallet walletQ = new Wallet();
        walletQ.setType(type);
        walletQ.setMemberId(memberId);
        //钱包折合
        BigDecimal walletConvert = BigDecimal.ZERO;
        //币币折合
        BigDecimal currencyConvert = BigDecimal.ZERO;
        //期权折合
        BigDecimal optionConvert = BigDecimal.ZERO;
        //合约折合
        BigDecimal contractConvert = BigDecimal.ZERO;
        //法币折合
        BigDecimal legalConvert = BigDecimal.ZERO;

        BigDecimal miningConvert = BigDecimal.ZERO;

        List<Wallet> walletList = walletService.list(new QueryWrapper<>(walletQ));
        for (Wallet entity : walletList){
            BigDecimal total = entity.getUsedPrice()
                    .add(entity.getMortgagePrice()== null ? BigDecimal.ZERO :entity.getMortgagePrice())
                    .add(entity.getFinancesPrice()== null ? BigDecimal.ZERO :entity.getFinancesPrice())
                    .add(entity.getLockedPrice() == null ? BigDecimal.ZERO :entity.getLockedPrice());
            walletConvert = walletConvert.add(total);
        }
        map.put("walletConvert",walletConvert);

        Currency currencyQ = new Currency();
        currencyQ.setType(type);
        currencyQ.setMemberId(memberId);
        List<Currency> currencies = currencyService.list(new QueryWrapper<>(currencyQ));
        for (Currency entity : currencies) {
            BigDecimal usedprice = entity.getUsedPrice() == null ? BigDecimal.ZERO:entity.getUsedPrice();
            BigDecimal total = usedprice.add(entity.getLockedPrice() == null ? BigDecimal.ZERO :entity.getLockedPrice());
            currencyConvert = currencyConvert.add(total);
        }
        map.put("currencyConvert",currencyConvert);



        FinOption finOptionQ = new FinOption();
        finOptionQ.setType(type);
        finOptionQ.setMemberId(memberId);
        //期权账户
        List<FinOption> optionList = finOptionService.list(new QueryWrapper<>(finOptionQ));
        for (FinOption entity : optionList){
            BigDecimal total = entity.getUsedPrice().add(entity.getLockedPrice());
            optionConvert = optionConvert.add(total);
        }
        map.put("optionConvert",optionConvert);

        Contract contractQ = new Contract();
        contractQ.setType(type);
        contractQ.setMemberId(memberId);
        //合约账户
        List<Contract> contractList = contractService.list(new QueryWrapper<>(contractQ));
        for (Contract entity : contractList){
            BigDecimal total = entity.getUsedPrice();
            contractConvert = contractConvert.add(total);
        }
        map.put("contractConvert",contractConvert);

        Legal legalQ = new Legal();
        legalQ.setType(type);
        legalQ.setMemberId(memberId);
        List<Legal> legalList = legalService.list(new QueryWrapper<>(legalQ));
        for (Legal entity : legalList){
            BigDecimal total = entity.getUsedPrice().add(entity.getLockedPrice());
            legalConvert = legalConvert.add(total);
        }
        map.put("legalConvert",legalConvert);

        FinMining miningQ = new FinMining();
        miningQ.setType(type).setMemberId(memberId);

        List<FinMining> miningList = finMiningService.list(new QueryWrapper<>(miningQ));
        for (FinMining entity : miningList){
            BigDecimal total = entity.getUsedPrice().add(entity.getLockedPrice());
            miningConvert = miningConvert.add(total);
        }
        map.put("miningConvert",miningConvert);

        return map;
    }



}
