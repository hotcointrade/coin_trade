package cn.stylefeng.guns.modular.otc.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.otc.entity.Back;
/**
 * 退还押金Mapper 接口
 *
 * @author yaying.liu
 * @since 2020-07-09 14:33:04
 */
public interface BackMapper extends BaseMapper<Back> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition,
                                               @Param("beginTime") String beginTime,
                                               @Param("endTime") String endTime,
                                               @Param("memberId") Long memberId,
                                               @Param("recommendIds") String recommendIds);

}