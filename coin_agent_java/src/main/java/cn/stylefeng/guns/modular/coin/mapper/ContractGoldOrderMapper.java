package cn.stylefeng.guns.modular.coin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import java.util.List;
import cn.stylefeng.guns.modular.coin.entity.ContractGoldOrder;

public interface ContractGoldOrderMapper extends BaseMapper<ContractGoldOrder>{

	/**
	* 分页
	*/
	Page<Map<String, Object>> selectByCondition(Page page, String condition);

	
	
}