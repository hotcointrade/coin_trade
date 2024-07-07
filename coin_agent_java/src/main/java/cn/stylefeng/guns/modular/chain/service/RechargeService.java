package cn.stylefeng.guns.modular.chain.service;

import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
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
        Wallet wallet = F.me().getWallet(entity.getMemberId(), entity.getType());
        F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.RECHARGE,
                entity.getActualPrice(), entity.getType(), entity.getActualPrice(), entity.getType(), BigDecimal.ZERO, entity.getType(),
                ProConst.ItemCode.USED, entity.getType(), null, null,
                wallet.getUsedPrice(), wallet.getUsedPrice().add(entity.getActualPrice()), wallet.getMemberId(), wallet.getMemberId());
        wallet.setUsedPrice(wallet.getUsedPrice().add(entity.getActualPrice()));
        walletService.updateById(wallet);
        entity.setType(coin);
        entity.setStatus(ProConst.WithdrawStatusEnum.PASS.getCode());
        this.updateById(entity);
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
