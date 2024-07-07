package cn.stylefeng.guns.modular.fin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.fin.entity.PlatformCashflow;
/**
 * 平台流水Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-01-03 18:06:11
 */
public interface PlatformCashflowMapper extends BaseMapper<PlatformCashflow> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}