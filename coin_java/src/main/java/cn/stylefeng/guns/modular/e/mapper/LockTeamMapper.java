package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.LockTeam;
/**
 * 锁仓团队奖配置Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-09-25 10:45:37
 */
public interface LockTeamMapper extends BaseMapper<LockTeam> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}