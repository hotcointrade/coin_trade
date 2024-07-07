package cn.stylefeng.guns.modular.fin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;
import java.util.List;
import cn.stylefeng.guns.modular.fin.entity.TeManagementRelease;
import org.apache.ibatis.annotations.Param;

public interface TeManagementReleaseMapper extends BaseMapper<TeManagementRelease>{

	/**
	* 分页
	*/
	Page<Map<String, Object>> selectByCondition(@Param("page")Page page, @Param("condition")String condition);

	
	
}