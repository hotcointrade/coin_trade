package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.util.RedisUtil;
import cn.stylefeng.guns.modular.fin.entity.FinFutures;
import cn.stylefeng.guns.modular.fin.mapper.ContractMapper;
import cn.stylefeng.guns.modular.fin.mapper.FinFuturesMapper;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 合约账户Service
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:09:49
 */
@Service
public class FinFuturesService extends ServiceImpl<FinFuturesMapper, FinFutures>
{

    @Autowired
    RedisUtil redisUtil;

    /**
     * 查询合约账户
     */
    public Page<Map<String, Object>> selectByCondition(String condition,Long memberId,String recommendIds)
    {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, condition,memberId,recommendIds);
    }

    /**
     * 删除合约账户
     */
    public void deleteContract(Long contractId)
    {
        FinFutures entity = this.baseMapper.selectById(contractId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
     * 添加合约账户
     */
    public void addContract(FinFutures contract)
    {
        this.baseMapper.insert(contract);
    }

    public int updateWallet(FinFutures contract)
    {
        if (contract.getUpdateUser() == null)
            contract.setUpdateUser(1L);
        return this.baseMapper.updateWallet(contract);
    }

    public ResponseData edit(FinFutures wallet)
    {
        FinFutures walletQ = this.getById(wallet.getContractId());
        if (walletQ != null)
        {
            String coin =walletQ.getType();
            redisUtil.set(Constant.CONTRACT_TRANSFER_LOCK+walletQ.getMemberId(),1,60);
            BigDecimal before=walletQ.getEntrustPrice();
            BigDecimal after=wallet.getEntrustPrice();
            int rows = this.getBaseMapper().updateWallet(wallet);
            while (rows<=0)
            {
                 walletQ = this.getById(wallet.getContractId());
                 walletQ.setVersion(walletQ.getVersion()).setEntrustPrice(after);
                 rows = updateWallet(walletQ);
                 if(rows>0)
                 {
                     redisUtil.del(Constant.CONTRACT_TRANSFER_LOCK+walletQ.getMemberId());
                     if (wallet.getEntrustPrice().compareTo(walletQ.getEntrustPrice()) > 0)
                     {

                         //后台增加
                         F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.CONTRACT, ProConst.CashFlowOpEnum.FLOW_IN, ProConst.CashFlowTypeEnum.PLATFORM_ADD,
                                 wallet.getEntrustPrice().subtract(walletQ.getEntrustPrice()), coin,
                                 wallet.getEntrustPrice().subtract(walletQ.getEntrustPrice()), coin,
                                 BigDecimal.ZERO, coin,
                                 ProConst.ItemCode.ENTRUST, coin, null, null,
                                 before, after, Constant.SYS_PLATFORM, walletQ.getMemberId());
                     }
                     if (wallet.getEntrustPrice().compareTo(walletQ.getEntrustPrice()) < 0)
                     {
                         //后台减少
                         F.me().saveCashflow(walletQ.getMemberId(), ProConst.WalletBigType.CONTRACT, ProConst.CashFlowOpEnum.FLOW_OUT, ProConst.CashFlowTypeEnum.PLATFORM_SUB,
                                 walletQ.getEntrustPrice().subtract(wallet.getEntrustPrice()), coin,
                                 walletQ.getEntrustPrice().subtract(wallet.getEntrustPrice()), coin,
                                 BigDecimal.ZERO, coin,
                                 ProConst.ItemCode.ENTRUST, coin, null, null,
                                 before,after, walletQ.getMemberId(), Constant.SYS_PLATFORM);
                     }
                 }

                try
                {
                    Thread.sleep(100);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }







        }




        return new SuccessResponseData();
    }

}
