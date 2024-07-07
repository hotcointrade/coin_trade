package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.PlatformAddress;
/**
 * 平台地址Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-16 09:31:12
 */
public interface PlatformAddressMapper extends BaseMapper<PlatformAddress> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}