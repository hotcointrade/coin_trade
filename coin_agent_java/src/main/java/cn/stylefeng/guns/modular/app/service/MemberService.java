package cn.stylefeng.guns.modular.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.mapper.MemberMapper;
import cn.stylefeng.guns.modular.app.vo.RegVo;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.base.util.Result;
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
import cn.stylefeng.guns.modular.fin.service.ContractService;
import cn.stylefeng.guns.modular.fin.service.CurrencyService;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import cn.stylefeng.guns.modular.otc.entity.Bill;
import cn.stylefeng.guns.modular.otc.entity.Buy;
import cn.stylefeng.guns.modular.otc.entity.Sell;
import cn.stylefeng.guns.modular.otc.service.BillService;
import cn.stylefeng.guns.modular.otc.service.BuyService;
import cn.stylefeng.guns.modular.otc.service.SellService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息Service
 *
 * @author yaying.liu
 * @Date 2019-12-06 09:50:49
 */
@Service
public  class MemberService extends ServiceImpl<MemberMapper,Member>{

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private DeleteService deleteService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ContractService contractService;

    //币币订单
    @Autowired
    private MatchService matchService;

    //永续
    @Autowired
    CompactService compactService;
    //法币
    @Autowired
    BuyService buyService;
    @Autowired
    private SellService sellService;
    @Autowired
    private ContractOptionOrderService contractOptionOrderService;





    @Autowired
    HomeService homeService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    BillService billService;



    //团队总人数，团队的总充值总提现
    public HashMap myTeamInfo(Long memberId){
        HashMap<String, Object> map = new HashMap<>();
        Member member = getById(memberId);
        List<Member> list = selectTeamForPRI(memberId);
        int count = 0;
        //充值总额
        BigDecimal  rechargeTotal = BigDecimal.ZERO;
        //提现总额
        BigDecimal  withdrawalTotal = BigDecimal.ZERO;
        String type = "USDT";
        BigDecimal sellMatch=BigDecimal.ZERO;
        BigDecimal buyMatch=BigDecimal.ZERO;
        BigDecimal buyCompact=BigDecimal.ZERO;
        BigDecimal sellCompact=BigDecimal.ZERO;
        BigDecimal otcSell=BigDecimal.ZERO;
        BigDecimal otcBuy=BigDecimal.ZERO;
        BigDecimal optionZ = BigDecimal.ZERO;
        BigDecimal optionD = BigDecimal.ZERO;
        //币币 永续 法币 期权 交易买卖总额
        //永续卖出买入
        map.put("buyCompact",buyCompact);
        map.put("sellCompact",sellCompact);

        //币币
        map.put("sellMatch",sellMatch);
        map.put("buyMatch",buyMatch);

        //法币
        map.put("otcSell",otcSell);
        map.put("otcBuy",otcBuy);
        //期权
        map.put("optionZ",optionZ);
        map.put("optionD",optionD);
        //永续 买入卖出
        getCompact(type,map,memberId);
        //法币 买入卖出
        getOtc(type,map,memberId);
        //bibi
        getMatch(type,map,memberId);
        getOption(map,memberId);
        if(list!=null && list.size()>0){
            count = list.size();
            Long[] memberIds = new Long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                memberIds[i] = list.get(i).getMemberId();
                //永续 买入卖出
                getCompact(type,map,memberIds[i]);
                //法币 买入卖出
                getOtc(type,map,memberIds[i]);
                //bibi
                getMatch(type,map,memberIds[i]);
                getOption(map,memberIds[i]);
            }
            List<Recharge> recharges = rechargeService.selectByMemberId(memberIds);
            if(recharges!=null && recharges.size()>0){
                for (Recharge recharge : recharges) {
                    rechargeTotal = rechargeTotal.add(recharge.getActualPrice());
                }
            }
            List<Withdraw> withdraws = withdrawService.selectByMemberId(memberIds);
            if(withdraws!=null && withdraws.size()>0){
                for (Withdraw withdraw : withdraws) {
                    withdrawalTotal=withdrawalTotal.add(withdraw.getActualPrice());
                }
            }
        }
        map.put("memberId",memberId);
        map.put("account",member.getAccount());
        map.put("teamCount",count);
        map.put("rechargeTotal",rechargeTotal.setScale(4, BigDecimal.ROUND_HALF_UP));
        map.put("withdrawalTotal",withdrawalTotal.setScale(4, BigDecimal.ROUND_HALF_UP));

        return map;
    }

    private void getOption(HashMap<String, Object> map, Long memberId) {
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

        BigDecimal optionZ1 = (BigDecimal)map.get("optionZ");
        BigDecimal optionD1 = (BigDecimal)map.get("optionD");

        map.put("optionZ",optionZ.add(optionZ1).setScale(4, BigDecimal.ROUND_HALF_UP));
        map.put("optionD",optionD.add(optionD1).setScale(4, BigDecimal.ROUND_HALF_UP));



    }
    private HashMap<String, Object> getCompact(String type, HashMap<String, Object> map,Long memberId) {
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
        BigDecimal buyCompact1 =(BigDecimal) map.get("buyCompact");
        map.put("buyCompact",buyCompact1.add(buyCompact).setScale(4, BigDecimal.ROUND_HALF_UP));

        compact.setCompactType(ProConst.CompactType.SELL.code);
        BigDecimal sellCompact= BigDecimal.ZERO;
        List<Compact> selllist = compactService.list(new QueryWrapper<>(compact));
        for (Compact compact1 : selllist) {
            sellCompact = sellCompact.add(compact1.getNumbers().multiply(compact1.getUnit()));
        }
        BigDecimal sellCompact1 =(BigDecimal) map.get("sellCompact");
        map.put("sellCompact",sellCompact.add(sellCompact1).setScale(4, BigDecimal.ROUND_HALF_UP));

        return map;
    }

    private HashMap<String, Object> getMatch(String type, HashMap<String, Object> map,Long memberId) {
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
        BigDecimal buyMatch1 = (BigDecimal) map.get("buyMatch");
        map.put("buyMatch",buyCompact.add(buyMatch1).setScale(4, BigDecimal.ROUND_HALF_UP));

        compact.setMatchType(ProConst.MatchType.SELL.code);
        BigDecimal sellCompact= BigDecimal.ZERO;
        List<Match> selllist = matchService.list(new QueryWrapper<>(compact));
        for (Match compact1 : selllist) {
            sellCompact = sellCompact.add(compact1.getTotalPrice());
        }
        BigDecimal sellMatch1=(BigDecimal)map.get("sellMatch");
        map.put("sellMatch",sellCompact.add(sellMatch1).setScale(4, BigDecimal.ROUND_HALF_UP));

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
        BigDecimal otcBuy1 = (BigDecimal)map.get("otcBuy");
        map.put("otcBuy",otcBuy.add(otcBuy1).setScale(4, BigDecimal.ROUND_HALF_UP));

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
        BigDecimal otcSell1 = (BigDecimal)map.get("otcSell");
        map.put("otcSell",otcSell.add(otcSell1).setScale(4, BigDecimal.ROUND_HALF_UP));
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
    public List<Member> selectTeamForPRI(Long memberId){
        return  this.baseMapper.selectTeamForPRI(memberId);
    }

    //三级
    public Page<Map<String,Object>> selectByLevel3(Long memberId){
        Page page = LayuiPageFactory.defaultPage();
        return  this.baseMapper.selectByLevel3(page,memberId);
    }
    //其他
    public Page<Map<String,Object>> selectByLevel4(Long memberId,String recommendIds){
        Page page = LayuiPageFactory.defaultPage();
        return  this.baseMapper.selectByLevel4(page,memberId,recommendIds);
    }
    public Map<String, Object> selectById(Long refereeId) {
        return this.baseMapper.getById(refereeId);
    }
    /**
     * 查询用户信息
     */
    public Page<Map<String,Object>> selectByCondition(String condition,Long refereeId,Long memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,refereeId,memberId,recommendIds);
    }
    /**
     * 查询用户信息
     */
    public Page<Map<String,Object>> selectByConditionForTeam(String condition,Long refereeId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        Page<Map<String, Object>> result = this.baseMapper.selectByCondition(page,condition,refereeId,null,null);
        List<Map<String, Object>> records = result.getRecords();
        for (Map<String, Object> record : records) {
            String parentRefereeId = (String)record.get("parentRefereeId");
            String[] split = parentRefereeId.split("/");
//            split = reverse(split);
//            //先定位查询账户的位置
//            int selectAccount=0;
//            for (int i = 0; i < split.length; i++) {
//                Long memberId = Long.parseLong(split[i]);
//                if(refereeId.equals(memberId)){
//                    selectAccount = i;
//                    break;
//                }
//            }

//            record.put("class", split.length-selectAccount-1);
            record.put("class", split.length);
        }
        return result;
    }
    /**
     * 查询用户信息
     */
    public Page<Map<String,Object>> selectAllForTeam(String condition,Long refereeId,Long userId,Page page) {

        String recommendIds = F.me().getMemberRecommendIdsByUser(userId);

        Page<Map<String, Object>> result = this.baseMapper.selectByConditionForTeam(page,condition,refereeId,recommendIds);
        List<Map<String, Object>> records = result.getRecords();
        for (Map<String, Object> record : records) {
            String parentRefereeId = (String)record.get("parentRefereeId");
            String account = (String)record.get("account");
            if (account.contains("@")){
                record.put("account",account.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4"))  ;
            }else{
                //第一个参数是正则表达式，$1匹配第一个括号，$2匹配第二个
                record.put("account",account.substring(0,3)+"****"+account.substring(account.length()-3,account.length()-1));
                //record.put("account",account.replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1******$2"));
            }
            if(parentRefereeId.length()<2) break;
            String[] split = parentRefereeId.split("/");
            split = reverse(split);
            //先定位查询账户的位置
            int selectAccount=0;
            for (int i = 0; i < split.length; i++) {
                if(StringUtils.isBlank(split[i])) break;
                Long memberId = Long.parseLong(split[i]);
                if(memberId.equals(refereeId)){
                    selectAccount = i;
                    break;
                }
            }

            record.put("class", split.length-selectAccount-1);
//            record.put("class", split.length);
        }
        return result;
    }

    private String[] reverse(String[] arr){
        //遍历数组
        for(int i = 0;i < arr.length / 2;i++){
            //交换元素 因为i从0开始所以这里一定要再减去1
            String temp = arr[arr.length -i - 1];
            arr[arr.length -i - 1] = arr[i];
            arr[i] = temp;
        }//返回反转后的结果
        return arr;
    }
    /**
    * 删除用户信息
    */
    @Transactional
    public void deleteMember(Long memberId) {
       // Member entity=this.baseMapper.selectById(memberId);
        //将删除标志设置为Y，表示删除
//        entity.setAccount(entity.getAccount()+"_"+RandomUtil.randomString(5));
//        entity.setDel("Y");
        this.removeById(memberId);
        deleteService.delete(memberId);
    }

    /**
    * 添加用户信息
    */
    public Result addMember(Member member) {
        RegVo regVo=new RegVo();
        BeanUtil.copyProperties(member,regVo);
        if (!member.getAccount().contains("@")){
            regVo.setCode("86");
        }
        String number="123456";
        redisUtil.set(Constant.SMS+regVo.getAccount(),number,60);
        regVo.setMsg(number);

        return homeService.register(regVo);

    }



    public List<ZTreeNode> tree(Long memberId, String recommendIds) {
        return this.baseMapper.tree(memberId,recommendIds);
    }

    public HashMap memberAllWallet(Long memberId) {


        return null;
    }
}
