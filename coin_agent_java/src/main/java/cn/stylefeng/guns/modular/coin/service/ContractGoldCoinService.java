package cn.stylefeng.guns.modular.coin.service;

import cn.stylefeng.guns.modular.coin.entity.ContractGoldCoin;
import cn.stylefeng.guns.modular.coin.mapper.ContractGoldCoinMapper;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;
import java.util.List;
/**
 * 黄金交易对Service
 */
@Service
public class ContractGoldCoinService extends ServiceImpl<ContractGoldCoinMapper, ContractGoldCoin>{

   
	public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }
   
	
}