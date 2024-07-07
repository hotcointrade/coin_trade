package cn.stylefeng.guns.modular.chain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.chain.entity.Convert;
/**
 * 豆兑换信息Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-01-17 14:46:22
 */
public interface ConvertMapper extends BaseMapper<Convert> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}