package cn.stylefeng.guns.modular.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.Community;
/**
 * 官方社区Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-10-18 18:24:14
 */
public interface CommunityMapper extends BaseMapper<Community> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}