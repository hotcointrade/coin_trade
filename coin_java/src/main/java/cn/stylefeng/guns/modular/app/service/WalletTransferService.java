package cn.stylefeng.guns.modular.app.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.WalletTransfer;
import cn.stylefeng.guns.modular.app.mapper.WalletTransferMapper;
/**
 * 佣金划转Service
 *
 * @author yaying.liu
 * @since 2022-02-18 20:54:40
 */
@Service
public  class WalletTransferService extends ServiceImpl<WalletTransferMapper,WalletTransfer>{

    /**
    * 查询佣金划转
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除佣金划转
    */
    public void deleteWalletTransfer(Long walletTransferId) {
        WalletTransfer entity=this.baseMapper.selectById(walletTransferId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加佣金划转
    */
    public void addWalletTransfer(WalletTransfer walletTransfer) {
        this.baseMapper.insert(walletTransfer);
    }
}