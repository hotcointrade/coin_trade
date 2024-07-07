package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.TeamEarning;
/**
 * 团队佣金Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-02-18 18:53:54
 */
public interface TeamEarningMapper extends BaseMapper<TeamEarning> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}