package cn.stylefeng.guns.modular.coin.service;

import cn.stylefeng.guns.modular.coin.entity.ContractGoldOrder;
import cn.stylefeng.guns.modular.coin.mapper.ContractGoldOrderMapper;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;
import java.util.List;
/**
 * 黄金交易对订单Service
 */
@Service
public class ContractGoldOrderService extends ServiceImpl<ContractGoldOrderMapper, ContractGoldOrder>{

   
	public Page<Map<String, Object>> selectByCondition(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectByCondition(page,condition);
    }
   
	
}