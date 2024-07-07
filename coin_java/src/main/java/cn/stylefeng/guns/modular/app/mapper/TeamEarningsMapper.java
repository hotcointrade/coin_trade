package cn.stylefeng.guns.modular.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.TeamEarnings;
/**
 * 团队收益Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-02-18 20:22:22
 */
public interface TeamEarningsMapper extends BaseMapper<TeamEarnings> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}