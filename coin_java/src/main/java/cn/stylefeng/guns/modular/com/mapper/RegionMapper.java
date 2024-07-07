package cn.stylefeng.guns.modular.com.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Region;
/**
 * 地区Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-06 11:32:41
 */
public interface RegionMapper extends BaseMapper<Region> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);


}