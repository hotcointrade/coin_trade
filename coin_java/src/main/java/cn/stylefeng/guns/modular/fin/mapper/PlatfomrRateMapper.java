package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PlatfomrRate;
/**
 * 系统汇率Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-10-18 13:18:11
 */
public interface PlatfomrRateMapper extends BaseMapper<PlatfomrRate> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}
