package cn.stylefeng.guns.modular.com.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Symbol;
/**
 * 币种Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-09 11:03:53
 */
public interface SymbolMapper extends BaseMapper<Symbol> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}