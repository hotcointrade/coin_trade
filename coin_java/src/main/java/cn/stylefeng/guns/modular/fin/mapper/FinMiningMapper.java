package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.FinMining;
/**
 * 矿机账户Mapper 接口
 *
 * @author yaying.liu
 * @since 2022-06-07 13:37:29
 */
public interface FinMiningMapper extends BaseMapper<FinMining> {

    Page<Map<String,Object>> selectByCondition(
                                               @Param("page") Page page,
                                               @Param("minPrice")  Double minPrice,
                                               @Param("maxPrice") Double maxPrice,@Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}
