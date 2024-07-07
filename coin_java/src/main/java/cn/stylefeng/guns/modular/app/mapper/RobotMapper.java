package cn.stylefeng.guns.modular.app.mapper;

import cn.stylefeng.guns.modular.app.entity.RobotConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface RobotMapper  extends BaseMapper<RobotConfig> {
    Page<Map<String, Object>> selectByCondition(@Param("page")Page page,@Param("condition") String condition);
}
