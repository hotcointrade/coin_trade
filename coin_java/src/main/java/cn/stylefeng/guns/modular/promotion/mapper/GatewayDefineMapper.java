package cn.stylefeng.guns.modular.promotion.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.promotion.entity.GatewayDefine;
/**
 * 网关定义Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-10-11 11:20:42
 */
public interface GatewayDefineMapper extends BaseMapper<GatewayDefine> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}