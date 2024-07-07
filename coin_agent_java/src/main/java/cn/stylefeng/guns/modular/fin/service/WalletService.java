package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.app.entity.Member;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.stylefeng.guns.modular.fin.entity.Wallet;
import cn.stylefeng.guns.modular.fin.mapper.WalletMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户信息Service
 *
 * @author yaying.liu
 * @Date 2020-03-12 17:05:14
 */
@Service
public class WalletService extends ServiceImpl<WalletMapper, Wallet> {

    @Autowired
    RedisUtil redisUtil;

    /**
     * 查询账户信息
     */
    public Page<Map<String, Object>> selectByCondition(String condition, Long memberId, String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition,memberId,recommendIds);
    }

    /**
     * 删除账户信息
     */
    public void deleteWallet(Long walletId) {
        Wallet entity = this.baseMapper.selectById(walletId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加账户信息
     */
    public void addWallet(Wallet wallet) {
        this.baseMapper.insert(wallet);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseData edit(Wallet wallet) {
        Wallet walletQ = this.getById(wallet.getWalletId());
        if (walletQ != null) {

            if (!this.updateById(wallet))
                throw new UpdateDataException(100);

            String coin = walletQ.getType();
            if (wallet.getUsedPrice().compareTo(walletQ.getUsedPrice()) > 0) {

                //后台增加
                F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.PLATFORM_ADD,
                        wallet.getUsedPrice().subtract(walletQ.getUsedPrice()), coin,
                        wallet.getUsedPrice().subtract(walletQ.getUsedPrice()), coin,
                        BigDecimal.ZERO, coin,
                        ProConst.ItemCode.USED, coin, null, null,
                        walletQ.getUsedPrice(), wallet.getUsedPrice(), Constant.SYS_PLATFORM, walletQ.getMemberId());
            }
            if (wallet.getUsedPrice().compareTo(walletQ.getUsedPrice()) < 0) {
                //后台减少
                F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.PLATFORM_SUB,
                        walletQ.getUsedPrice().subtract(wallet.getUsedPrice()), coin,
                        walletQ.getUsedPrice().subtract(wallet.getUsedPrice()), coin,
                        BigDecimal.ZERO, coin,
                        ProConst.ItemCode.USED, coin, null, null,
                        walletQ.getUsedPrice(), wallet.getUsedPrice(), walletQ.getMemberId(), Constant.SYS_PLATFORM);
            }
        }

        return new SuccessResponseData();
    }

    public void releaseFot() {
        synchronized (this) {
            if (redisUtil.get("releaseFot") != null)
                throw new ServiceException(400, "等待之前释放完成后再操作");

            redisUtil.publish("releaseFot", "释放冻结fot");

        }
    }

    /**
     * 释放fot冻结启动
     */
    public void releaseFotRun() {
        redisUtil.lock("releaseFot", 1, 5 * 60);

        Wallet query = new Wallet();
        query.setType("FOT").setDel("N");

        QueryWrapper queryWrapper = new QueryWrapper(query);
        queryWrapper.gt("gaslock", 0);
        List<Wallet> walletList = list(queryWrapper);

        for (Wallet wallet : walletList) {
            BigDecimal flowPrice=wallet.getGaslock();
            BigDecimal beforeLock=wallet.getLockedPrice();
            BigDecimal afterLock = beforeLock.subtract(flowPrice);
            BigDecimal beforeUsed=wallet.getUsedPrice();
            BigDecimal afterUsed=beforeUsed.add(flowPrice);

            wallet.setGaslock(wallet.getGaslock().subtract(flowPrice))
                    .setUsedPrice(afterUsed)
                    .setLockedPrice(afterLock)
                    .setUpdateUser(Constant.SYS_PLATFORM);

            if(!updateById(wallet))
            {
                throw new UpdateDataException(100);
            }
            //冻结流水 -
//            F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.GAS_RELEASE,
//                    flowPrice,wallet.getType(),flowPrice,wallet.getType(),BigDecimal.ZERO,wallet.getType(),
//                    ProConst.ItemCode.LOCKED,wallet.getType(),null,null,
//                    beforeLock,afterLock,wallet.getMemberId(),wallet.getMemberId());
//
//            //可用流水 +
//            F.me().saveCashflow(wallet.getMemberId(), ProConst.WalletBigType.WALLET, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.GAS_RELEASE,
//                    flowPrice,wallet.getType(),flowPrice,wallet.getType(),BigDecimal.ZERO,wallet.getType(),
//                    ProConst.ItemCode.USED,wallet.getType(),null,null,
//                    beforeUsed,afterUsed,wallet.getMemberId(),wallet.getMemberId());

        }


        redisUtil.unLock("releaseFot");
    }
}
