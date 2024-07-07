package cn.stylefeng.guns.modular.otc.service;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.service.LegalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Sell;
import cn.stylefeng.guns.modular.otc.mapper.SellMapper;
/**
 * 出售挂单订单Service
 *
 * @author yaying.liu
 * @since 2020-07-13 14:53:27
 */
@Service
public  class SellService extends ServiceImpl<SellMapper,Sell>{


    @Autowired
    LegalService legalService;

    /**
    * 查询出售挂单订单
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime,
                                                      String nickName, String orderNo,
                                                      String coin, String payMethod, String status,Long memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime,nickName,orderNo,coin,payMethod,status,memberId,recommendIds);
    }

    /**
    * 删除出售挂单订单
    */
    public void deleteSell(Long sellId) {
        Sell entity=this.baseMapper.selectById(sellId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加出售挂单订单
    */
    public void addSell(Sell sell) {
        this.baseMapper.insert(sell);
    }

    public void cancel(Long buyId)
    {
        Sell sell=this.baseMapper.selectById(buyId);
        if(!"Y".equals(sell.getStatus()))
            return;

        sell.setStatus("N").setUpdateUser(sell.getMemberId());
        if (!updateById(sell))
            throw new UpdateDataException(100);

        Member member= F.me().getMember(sell.getMemberId());
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
        F.me().saveCashflow(member.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.OTC_SELL_CANCEL,
                sell.getRestNumber(), sell.getCoin(), sell.getRestNumber(), sell.getCoin(), BigDecimal.ZERO, sell.getCoin(),
                ProConst.ItemCode.USED, sell.getCoin(), null, null,
                beforeUsed, afterUsed, member.getMemberId(), member.getMemberId());
        //冻结 -
        F.me().saveCashflow(member.getMemberId(), ProConst.WalletBigType.LEGAL, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.OTC_SELL_CANCEL,
                sell.getRestNumber(), sell.getCoin(), sell.getRestNumber(), sell.getCoin(), BigDecimal.ZERO, sell.getCoin(),
                ProConst.ItemCode.LOCKED, sell.getCoin(), null, null,
                beforeLock, afterLock, member.getMemberId(), member.getMemberId());

    }
}