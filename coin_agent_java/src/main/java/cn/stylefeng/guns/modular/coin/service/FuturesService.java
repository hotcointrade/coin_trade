package cn.stylefeng.guns.modular.coin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.coin.entity.Futures;
import cn.stylefeng.guns.modular.coin.entity.Swap;
import cn.stylefeng.guns.modular.coin.mapper.FuturesMapper;
import cn.stylefeng.guns.modular.coin.mapper.SwapMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 合约交易对Service
 *
 * @author yaying.liu
 * @since 2020-08-25 10:49:35
 */
@Service
public  class FuturesService extends ServiceImpl<FuturesMapper, Futures>{

    /**
    * 查询合约交易对
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
    * 删除合约交易对
    */
    public void deleteSwap(Long swapId) {
//        Swap entity=this.baseMapper.selectById(swapId);
//        //将删除标志设置为Y，表示删除
//        entity.setDel("Y");
//        this.baseMapper.updateById(entity);
        removeById(swapId);
    }

    /**
    * 添加合约交易对
    */
    public void addSwap(Futures swap) {
        this.baseMapper.insert(swap);
    }
}