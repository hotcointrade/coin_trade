package cn.stylefeng.guns.modular.fin.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.WalletHistory;
import cn.stylefeng.guns.modular.fin.mapper.WalletHistoryMapper;
/**
 * 钱包历史变更Service
 *
 * @author yaying.liu
 * @Date 2019-12-09 17:06:57
 */
@Service
public  class WalletHistoryService extends ServiceImpl<WalletHistoryMapper,WalletHistory>{

    /**
    * 查询钱包历史变更
    */
    public Page<Map<String,Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }

    /**
    * 删除钱包历史变更
    */
    public void deleteWalletHistory(Long walletHistoryId) {
        WalletHistory entity=this.baseMapper.selectById(walletHistoryId);
        //将删除标志设置为Y，表示删除
        entity.setDel("Y");
        this.baseMapper.updateById(entity);
    }

    /**
    * 添加钱包历史变更
    */
    public void addWalletHistory(WalletHistory walletHistory) {
        this.baseMapper.insert(walletHistory);
    }
}