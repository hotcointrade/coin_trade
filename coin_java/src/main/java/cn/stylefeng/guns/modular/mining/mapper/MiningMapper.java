package cn.stylefeng.guns.modular.mining.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.mining.entity.Mining;
/**
 * 矿机Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-06-07 13:24:41
 */
public interface MiningMapper extends BaseMapper<Mining> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}