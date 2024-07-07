package cn.stylefeng.guns.modular.e.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.e.entity.Leverage;
/**
 * 杠杆倍率Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-23 09:19:33
 */
public interface LeverageMapper extends BaseMapper<Leverage> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}