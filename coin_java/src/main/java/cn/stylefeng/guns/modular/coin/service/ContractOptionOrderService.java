package cn.stylefeng.guns.modular.coin.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.coin.entity.ContractOptionOrder;
import cn.stylefeng.guns.modular.coin.mapper.ContractOptionOrderMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 期权交易对Service
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
@Service
public  class ContractOptionOrderService extends ServiceImpl<ContractOptionOrderMapper, ContractOptionOrder>{

    /**
    * 查询期权交易对
    */
    public Page<Map<String,Object>> selectByCondition( String symbol, Long memberId, BigDecimal betAmount, BigDecimal rewardAmount) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page, symbol, memberId, betAmount, rewardAmount);
    }

    /**
    * 删除期权交易对
    */
    public void deleteSpot(Long spotId) {

        removeById(spotId);
    }

    /**
    * 添加期权交易对
    */
    public void addSpot(ContractOptionOrder option) {
        this.baseMapper.insert(option);
    }
}
