package cn.stylefeng.guns.modular.otc.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Appeal;
/**
 * 申诉订单Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-07-15 17:16:25
 */
public interface AppealMapper extends BaseMapper<Appeal> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}