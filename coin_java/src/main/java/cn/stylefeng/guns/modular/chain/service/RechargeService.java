package cn.stylefeng.guns.modular.chain.service;

import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.app.service.HomeService;
import cn.stylefeng.guns.modular.app.service.MemberService;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.fin.entity.Contract;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.service.ContractService;
import cn.stylefeng.guns.modular.fin.service.WalletService;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.stylefeng.guns.modular.chain.entity.Recharge;
import cn.stylefeng.guns.modular.chain.mapper.RechargeMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 充币记录Service
 *
 * @author yaying.liu
 * @Date 2020-01-14 16:00:54
 */
@Service
public class RechargeService extends ServiceImpl<RechargeMapper, Recharge>
{

    @Autowired
    WalletService walletService;
    @Autowired
    ContractService contractService;
    @Autowired
    MemberService memberService;
    @Autowired
    HomeService homeService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 查询充币记录
     */
    public Page<Map<String, Object>> selectByCondition(Map param)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, param);
    }
    public List<Recharge> selectByMemberId(Long... memberIds ){

        return this.baseMapper.selectByMemberIds(memberIds);
    };

    /**
     * 删除充币记录
     */
    public void deleteRecharge(Long rechargeId)
    {
        Recharge entity = this.baseMapper.selectById(rechargeId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加充币记录
     */
    public void addRecharge(Recharge recharge)
    {
        this.baseMapper.insert(recharge);
    }
    @Transactional
    public ResponseData pass(Recharge entity){
        String coin = entity.getType();
        if ("USDT-ERC20".equals(entity.getType()) || "USDT-TRC20".equals(entity.getType()) || "USDT-OMNI".equals(entity.getType())){
            entity.setType("USDT");
        }

//        Wallet wallet = F.me().getWallet(entity.getMemberId(), entity.getType());
        Contract contract = F.me().getContract(entity.getMemberId(), entity.getType());
        F.me().saveCashflow(contract.getMemberId(), ProConst.WalletBigType.CONTRACT, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.RECHARGE,
                entity.getActualPrice(), entity.getType(), entity.getActualPrice(), entity.getType(), BigDecimal.ZERO, entity.getType(),
                ProConst.ItemCode.USED, entity.getType(), null, null,
                contract.getUsedPrice(), contract.getUsedPrice().add(entity.getActualPrice()), contract.getMemberId(), contract.getMemberId());
        contract.setUsedPrice(contract.getUsedPrice().add(entity.getActualPrice()));
        contractService.updateById(contract);
        entity.setType(coin);
        entity.setStatus(ProConst.WithdrawStatusEnum.PASS.getCode());
        this.updateById(entity);
        Member member = memberService.getById(entity.getMemberId());
        if(!"Y".equals(member.getValidStatus())){
            member.setValidStatus("Y");
            //有效用户之后 进行奖励
            homeService.calculateReferralCommission(member);
            memberService.updateById(member);
            Object o = redisUtil.get(Constant.SINGLE_ACCOUNT + member.getAccount());
            if(o!=null){
                String token = (String)o;
                redisUtil.set(token, member, Long.parseLong(F.me().cfg(Constant.TOKEN_EXPIRE)));
            }
        }
        return ResponseData.success();
    }

    @Transactional
    public ResponseData fail(Recharge entity)
    {
        entity.setStatus(ProConst.WithdrawStatusEnum.REJECT.getCode());

        this.updateById(entity);
        return ResponseData.success();
    }



}
