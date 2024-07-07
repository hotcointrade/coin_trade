package cn.stylefeng.guns.modular.coin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.coin.entity.Spot;
/**
 * 现货交易对Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-08-25 10:48:06
 */
public interface SpotMapper extends BaseMapper<Spot> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}