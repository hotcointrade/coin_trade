package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Bought;
/**
 * 法币交易Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-04-03 09:48:13
 */
public interface BoughtMapper extends BaseMapper<Bought> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

    Bought getBought(@Param("orderNo") String orderNo);
}