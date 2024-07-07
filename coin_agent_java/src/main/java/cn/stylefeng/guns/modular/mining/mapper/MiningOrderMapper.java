package cn.stylefeng.guns.modular.mining.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.mining.entity.MiningOrder;
/**
 * 矿机订单Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-06-08 21:02:12
 */
public interface MiningOrderMapper extends BaseMapper<MiningOrder> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition,
                                               @Param("beginTime") String beginTime, @Param("endTime") String endTime,
            @Param("memberId")Long memberId,@Param("recommendIds")String recommendIds);

}
