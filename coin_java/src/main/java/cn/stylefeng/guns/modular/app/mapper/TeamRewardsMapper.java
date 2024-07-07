package cn.stylefeng.guns.modular.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.TeamRewards;
/**
 * 社区奖励Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-07-07 17:45:57
 */
public interface TeamRewardsMapper extends BaseMapper<TeamRewards> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}