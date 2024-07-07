package cn.stylefeng.guns.modular.coin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.coin.entity.ContractOption;
import cn.stylefeng.guns.modular.coin.mapper.ContractOptionMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 期权交易对Service
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
@Service
public  class ContractOptionService extends ServiceImpl<ContractOptionMapper, ContractOption>{

    /**
    * 查询期权交易对
    */
    //public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
    //    Page page = LayuiPageFactory.defaultPage();
    //    return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    //}

    /**
    * 删除期权交易对
    */
    public void deleteSpot(Long spotId) {

        removeById(spotId);
    }

    /**
    * 添加期权交易对
    */
    public void addSpot(ContractOption option) {
        this.baseMapper.insert(option);
    }

    public Page<Map<String, Object>> selectByCondition(String optionNo, String symbol, Integer totalBuyCount, Integer totalSellCount, Integer totalPl) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,optionNo,symbol,totalBuyCount,totalSellCount,totalPl);
    }

    public Page selectByCount(String symbol, int count) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,null,symbol,null,null,null);
    }
}