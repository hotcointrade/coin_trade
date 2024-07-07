package cn.stylefeng.guns.modular.coin.mapper;

import cn.stylefeng.guns.modular.coin.entity.SymbolsSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;


public interface SymbolsSettingMapper extends BaseMapper<SymbolsSetting> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}