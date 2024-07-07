package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Quotes;
/**
 * 行情涨跌控制Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-23 11:44:38
 */
public interface QuotesMapper extends BaseMapper<Quotes> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}