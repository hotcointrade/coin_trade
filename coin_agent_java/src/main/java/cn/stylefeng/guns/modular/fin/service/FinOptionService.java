package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.coin.entity.Option;
import cn.stylefeng.guns.modular.fin.entity.Currency;
import cn.stylefeng.guns.modular.fin.entity.FinOption;
import cn.stylefeng.guns.modular.fin.mapper.CurrencyMapper;
import cn.stylefeng.guns.modular.fin.mapper.FinOptionMapper;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 币币账户Service
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:10:50
 */
@Service
public class FinOptionService extends ServiceImpl<FinOptionMapper, FinOption>
{

    /**
     * 查询币币账户
     */
    public Page<Map<String, Object>> selectByCondition(String condition,Long memberId,String recommendIds)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition,memberId,recommendIds);
    }

    /**
     * 删除币币账户
     */
    public void deleteCurrency(Long currencyId)
    {
        FinOption entity = this.baseMapper.selectById(currencyId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加币币账户
     */
    public void addCurrency(FinOption currency)
    {
        this.baseMapper.insert(currency);
    }

    public int updateWallet(FinOption entity)
    {
        if (entity.getUpdateUser() == null)
            entity.setUpdateUser(1L);
        return this.baseMapper.updateWallet(entity);
    }

    public ResponseData edit(FinOption wallet)
    {
        FinOption walletQ = this.getById(wallet.getCurrencyId());
        if (walletQ != null)
        {
            if (!this.updateById(wallet))
                throw new UpdateDataException(100);
            String coin = walletQ.getType();

            if (wallet.getUsedPrice().compareTo(walletQ.getUsedPrice()) > 0)
            {

                //后台增加
                F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.OPTION, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.PLATFORM_ADD,
                        wallet.getUsedPrice().subtract(walletQ.getUsedPrice()), coin,
                        wallet.getUsedPrice().subtract(walletQ.getUsedPrice()), coin,
                        BigDecimal.ZERO, coin,
                        ProConst.ItemCode.USED, coin, null, null,
                        walletQ.getUsedPrice(), wallet.getUsedPrice(), Constant.SYS_PLATFORM, walletQ.getMemberId());
            }
            if (wallet.getUsedPrice().compareTo(walletQ.getUsedPrice()) < 0)
            {
                //后台减少
                F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.OPTION, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.PLATFORM_SUB,
                        walletQ.getUsedPrice().subtract(wallet.getUsedPrice()), coin,
                        walletQ.getUsedPrice().subtract(wallet.getUsedPrice()), coin,
                        BigDecimal.ZERO, coin,
                        ProConst.ItemCode.USED, coin, null, null,
                        walletQ.getUsedPrice(), wallet.getUsedPrice(), walletQ.getMemberId(), Constant.SYS_PLATFORM);
            }
        }
        return new SuccessResponseData();
    }
}
