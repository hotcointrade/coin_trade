package cn.stylefeng.guns.modular.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.CoinApply;
/**
 * 上币申请Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-02-20 20:37:01
 */
public interface CoinApplyMapper extends BaseMapper<CoinApply> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}