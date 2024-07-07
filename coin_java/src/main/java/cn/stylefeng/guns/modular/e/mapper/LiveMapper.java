package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Live;
/**
 * 生活支付开通记录Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-06-28 16:17:00
 */
public interface LiveMapper extends BaseMapper<Live> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}