package cn.stylefeng.guns.modular.coin.service;

import cn.stylefeng.guns.modular.coin.entity.ContractGold;
import cn.stylefeng.guns.modular.coin.mapper.ContractGoldMapper;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;
import java.util.List;
/**
 * 黄金交易每一期Service
 */
@Service
public class ContractGoldService extends ServiceImpl<ContractGoldMapper, ContractGold>{

   
	public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }
   
	
}