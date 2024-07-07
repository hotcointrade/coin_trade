package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.FinMining;
import cn.stylefeng.guns.modular.fin.mapper.FinMiningMapper;
/**
 * 矿机账户Service
 *
 * @author yaying.liu
 * @since 2022-06-07 13:37:29
 */
@Service
public  class FinMiningService extends ServiceImpl<FinMiningMapper,FinMining>{

    /**
    * 查询矿机账户
    */
    public Page<Map<String,Object>> selectByCondition(Double minPrice,Double maxPrice,String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,minPrice,maxPrice,condition,beginTime,endTime);
    }

    /**
    * 删除矿机账户
    */
    public void deleteFinMining(Long finMiningId) {
        FinMining entity=this.baseMapper.selectById(finMiningId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加矿机账户
    */
    public void addFinMining(FinMining finMining) {
        this.baseMapper.insert(finMining);
    }

    public ResponseData edit(FinMining finMining) {
        {
            FinMining walletQ = this.getById(finMining.getMiningId());
            if (walletQ != null)
            {
                if (!this.updateById(finMining))
                    throw new UpdateDataException(100);
                String coin = walletQ.getType();

                if (finMining.getUsedPrice().compareTo(walletQ.getUsedPrice()) > 0)
                {

                    //后台增加
                    F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.MINING, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.PLATFORM_ADD,
                            finMining.getUsedPrice().subtract(walletQ.getUsedPrice()), coin,
                            finMining.getUsedPrice().subtract(walletQ.getUsedPrice()), coin,
                            BigDecimal.ZERO, coin,
                            ProConst.ItemCode.USED, coin, null, null,
                            walletQ.getUsedPrice(), finMining.getUsedPrice(), Constant.SYS_PLATFORM, walletQ.getMemberId());
                }
                if (finMining.getUsedPrice().compareTo(walletQ.getUsedPrice()) < 0)
                {
                    //后台减少
                    F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.MINING, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.PLATFORM_SUB,

                            walletQ.getUsedPrice().subtract(finMining.getUsedPrice()), coin,
                            walletQ.getUsedPrice().subtract(finMining.getUsedPrice()), coin,
                            BigDecimal.ZERO, coin,
                            ProConst.ItemCode.USED, coin, null, null,
                            walletQ.getUsedPrice(), finMining.getUsedPrice(), walletQ.getMemberId(), Constant.SYS_PLATFORM);
                }
            }
            return new SuccessResponseData();
        }
    }
}
