package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import cn.stylefeng.guns.core.util.TimeCompare;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.fin.entity.Periodic;
import cn.stylefeng.guns.modular.fin.entity.PeriodicOrder;
import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.mapper.FinPeriodicMapper;
import cn.stylefeng.guns.modular.fin.mapper.FinPeriodicOrderMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 理财生息配置Service
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:10:50
 */
@Service
public class FinPeriodicOrderService extends ServiceImpl<FinPeriodicOrderMapper, PeriodicOrder>
{
    private Logger log = LoggerFactory.getLogger(FinPeriodicOrderService.class);
    @Autowired
    FinPeriodicService finPeriodicService;
    @Resource
    WalletService walletService;
    /**
     * 查询
     */
    public Page<Map<String, Object>> selectByCondition(String condition)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition);
    }
    /**
     * 查询总投资额 未结束 已结算的
     */
    public Map selectTotal(Long memberId)
    {
        Map<String, Object> map = this.baseMapper.selectWinTotal(memberId);
        Object principal = map.get("principal");
        BigDecimal p1 = new BigDecimal(principal.toString());
        Map<String, Object> map1 = this.baseMapper.selectNoWinTotal(memberId);
        Object principal2 = map1.get("principal");
        BigDecimal p2 = new BigDecimal(principal2.toString());
        BigDecimal p = p1.add(p2);
        map.putAll(map1);
        map.put("principal",p);
        return map;
    }

    /**
     * 删除
     */
    public void deletePeriodic(Long currencyId)
    {
        PeriodicOrder entity = this.baseMapper.selectById(currencyId);
        //将删除标志设置为Y，表示删除
        entity.setIsDelete("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加
     */
    public void addPeriodicOrder(PeriodicOrder periodicOrder)
    {
        this.baseMapper.insert(periodicOrder);
    }

    public List selectByList(Long memberId){
       return this.baseMapper.selectByList(memberId);
    }

    /**
     * 撤销
     * @param currencyIds
     */
    @Transactional
    public void revoke(Long... currencyIds) {
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        for (Long currencyId : currencyIds) {
            PeriodicOrder order = getById(currencyId);

            if(order.getResult().equals("1")){
                revoke(order);
            }

        }
    }
    private void revoke(PeriodicOrder order){
        order.setResult("4");
        BigDecimal amount = order.getAmount();
        BigDecimal addManey = BigDecimal.ZERO;
        //钱包账户
        Wallet usdt = F.me().getWallet(order.getMemberId(), order.getSymbol());
        Assert.notNull(usdt, "钱包没钱");
        if(amount.compareTo(usdt.getLockedPrice()) > 0) {
            log.error(order.getMemberId()+"{}用户钱包冻结金额不足");
            return ;
        }
        usdt.setUsedPrice(usdt.getUsedPrice().add(amount));
        usdt.setLockedPrice(usdt.getLockedPrice().subtract(amount));
        order.setWinN(addManey);
        order.setUpdateTime(new Date());
        order.setUpdateUser(1L);
        order.setRemark("已撤销");
        updateById(order);
        walletService.updateById(usdt);
        F.me().saveCashflow(order.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN,  ProConst.CashFlowTypeEnum.SAVECOINDO,
                amount, order.getSymbol(),amount, order.getSymbol(),
                BigDecimal.ZERO, order.getSymbol(),
                ProConst.ItemCode.USED,order.getSymbol(), null, ProConst.CashFlowTypeEnum.SAVECOINDO.getValue(),
                usdt.getUsedPrice().subtract(amount), usdt.getUsedPrice(),
                order.getMemberId(), order.getMemberId());
    }
    private void settlement(PeriodicOrder order){
        order.setResult("3");

        BigDecimal rate = order.getRate();
        BigDecimal amount = order.getAmount();
        BigDecimal addManey = rate.multiply(amount.divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP));

        //钱包账户
        Wallet usdt = F.me().getWallet(order.getMemberId(), order.getSymbol());
        Assert.notNull(usdt, "钱包没钱");
        Assert.isTrue(amount.compareTo(usdt.getLockedPrice()) <= 0, order.getMemberId()+"{}用户钱包冻结金额不足");

        usdt.setUsedPrice(usdt.getUsedPrice().add(amount).add(addManey));
        usdt.setLockedPrice(usdt.getLockedPrice().subtract(amount));
        order.setWinN(addManey);
        order.setUpdateTime(new Date());
        order.setUpdateUser(1L);
        order.setRemark("已结算");
        updateById(order);
        walletService.updateById(usdt);
        F.me().saveCashflow(order.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN,  ProConst.CashFlowTypeEnum.SAVECOINDO,
                amount, order.getSymbol(),amount, order.getSymbol(),
                BigDecimal.ZERO, order.getSymbol(),
                ProConst.ItemCode.USED,order.getSymbol(), null, ProConst.CashFlowTypeEnum.SAVECOINDO.getValue(),
                usdt.getUsedPrice().subtract(amount), usdt.getUsedPrice(),
                order.getMemberId(), order.getMemberId());
        F.me().saveCashflow(order.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN,  ProConst.CashFlowTypeEnum.SAVECOIN,
                addManey, order.getSymbol(),addManey, order.getSymbol(),
                BigDecimal.ZERO, order.getSymbol(),
                ProConst.ItemCode.USED,order.getSymbol(), null, "存币收益",
                usdt.getUsedPrice().subtract(addManey), usdt.getUsedPrice(),
                order.getMemberId(), order.getMemberId());
    }

    /**
     * 提前结算
     * @param currencyIds
     */
    @Transactional
    public void advanceSettlement(Long... currencyIds) {
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        for (Long currencyId : currencyIds) {
            PeriodicOrder order = getById(currencyId);
            if(order.getResult().equals("1")){
                settlement(order);
            }
        }
    }
}
