package cn.stylefeng.guns.modular.com.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.SvcException;
/**
 * 异常信息Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-24 10:20:02
 */
public interface SvcExceptionMapper extends BaseMapper<SvcException> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}