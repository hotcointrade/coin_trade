package cn.stylefeng.guns.modular.promotion.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.promotion.entity.GatewayRecord;
/**
 * 网关记录Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-10-11 10:44:44
 */
public interface GatewayRecordMapper extends BaseMapper<GatewayRecord> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}