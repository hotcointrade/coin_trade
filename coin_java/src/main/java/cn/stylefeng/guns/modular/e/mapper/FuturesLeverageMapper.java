package cn.stylefeng.guns.modular.e.mapper;

import cn.stylefeng.guns.modular.e.entity.FuturesLeverage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 *
 *
 * @author yaying.liu
 * @Date 2020-03-23 09:19:33
 */
public interface FuturesLeverageMapper extends BaseMapper<FuturesLeverage> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}