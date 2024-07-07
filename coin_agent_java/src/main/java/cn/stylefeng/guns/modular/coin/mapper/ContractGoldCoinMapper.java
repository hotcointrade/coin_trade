package cn.stylefeng.guns.modular.coin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import java.util.List;
import cn.stylefeng.guns.modular.coin.entity.ContractGoldCoin;

public interface ContractGoldCoinMapper extends BaseMapper<ContractGoldCoin>{

	/**
	* 分页
	*/
	Page<Map<String, Object>> selectByCondition(Page page, String condition);

	
	
}