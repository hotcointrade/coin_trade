package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Public;
/**
 * 公账提币Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-01-17 18:55:07
 */
public interface PublicMapper extends BaseMapper<Public> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("param") Map param);

    Map<String,Object> total(@Param("param") Map param);
}