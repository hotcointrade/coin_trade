package cn.stylefeng.guns.modular.fin.service;

import cn.stylefeng.guns.core.exceptions.UpdateDataException;
import cn.stylefeng.guns.modular.base.state.Constant;
import cn.stylefeng.guns.modular.base.state.ProConst;
import cn.stylefeng.guns.modular.base.state.F;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.Legal;
import cn.stylefeng.guns.modular.fin.mapper.LegalMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 法币账户Service
 *
 * @author yaying.liu
 * @Date 2020-03-12 19:11:25
 */
@Service
public  class LegalService extends ServiceImpl<LegalMapper,Legal>{

    /**
    * 查询法币账户
    */
    public Page<Map<String,Object>> selectByCondition(Double minPrice,Double maxPrice,String condition,Long  memberId,String recommendIds) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,minPrice,maxPrice,condition,memberId,recommendIds);
    }

    /**
    * 删除法币账户
    */
    public void deleteLegal(Long legalId) {
        Legal entity=this.baseMapper.selectById(legalId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加法币账户
    */
    public void addLegal(Legal legal) {
        this.baseMapper.insert(legal);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseData edit(Legal wallet)
    {
        Legal walletQ = this.getById(wallet.getLegalId());
        if(walletQ!=null)
        {
            if(!this.updateById(wallet))
                throw new UpdateDataException(100);
            String coin=walletQ.getType();
            if (wallet.getUsedPrice().compareTo(walletQ.getUsedPrice()) > 0)
            {

                //后台增加
                F.me().saveCashflow(walletQ.getMemberId(),ProConst.WalletBigType.LEGAL,ProConst.CashFlowOpEnum.FLOW_IN,ProConst.CashFlowTypeEnum.PLATFORM_ADD,
                        wallet.getUsedPrice().subtract(walletQ.getUsedPrice()),coin,
                        wallet.getUsedPrice().subtract(walletQ.getUsedPrice()),coin,
                        BigDecimal.ZERO,coin,
                        ProConst.ItemCode.USED,coin,null,null,
                        walletQ.getUsedPrice(),wallet.getUsedPrice(),Constant.SYS_PLATFORM,walletQ.getMemberId());
            }
            if (wallet.getUsedPrice().compareTo(walletQ.getUsedPrice()) < 0)
            {
                //后台减少
                F.me().saveCashflow(walletQ.getMemberId(),ProConst.WalletBigType.LEGAL,ProConst.CashFlowOpEnum.FLOW_OUT,ProConst.CashFlowTypeEnum.PLATFORM_SUB,
                        walletQ.getUsedPrice().subtract(wallet.getUsedPrice()),coin,
                        walletQ.getUsedPrice().subtract(wallet.getUsedPrice()),coin,
                        BigDecimal.ZERO,coin,
                        ProConst.ItemCode.USED,coin,null,null,
                        walletQ.getUsedPrice(),wallet.getUsedPrice(),walletQ.getMemberId(),Constant.SYS_PLATFORM);
            }
        }
        return new SuccessResponseData();
    }
}
