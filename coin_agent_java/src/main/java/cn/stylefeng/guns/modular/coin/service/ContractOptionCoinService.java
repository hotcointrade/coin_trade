package cn.stylefeng.guns.modular.coin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionCoin;
import cn.stylefeng.guns.modular.coin.mapper.ContractOptionCoinMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 期权交易对Service
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
@Service
public  class ContractOptionCoinService extends ServiceImpl<ContractOptionCoinMapper, ContractOptionCoin>{

    /**
    * 查询期权交易对
    */
    public Page<Map<String,Object>> selectByCondition(String condition, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition,beginTime,endTime);
    }

    /**
     * 查询期权交易对
     */
    //public ContractOptionCoin selectBySymbol(String symbol) {
    //    return this.baseMapper.selectBySymbol(symbol);
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
    public void addSpot(ContractOptionCoin option) {
        this.baseMapper.insert(option);
    }
}