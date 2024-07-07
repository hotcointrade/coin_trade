package cn.stylefeng.guns.modular.com.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Business;
/**
 * 行业资讯Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:09:15
 */
public interface BusinessMapper extends BaseMapper<Business> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}