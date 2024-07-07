package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LockAuto;
/**
 * 锁仓动态收益配置Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-09-25 10:44:59
 */
public interface LockAutoMapper extends BaseMapper<LockAuto> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}