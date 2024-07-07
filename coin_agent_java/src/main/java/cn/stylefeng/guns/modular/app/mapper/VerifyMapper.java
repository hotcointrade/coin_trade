package cn.stylefeng.guns.modular.app.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.app.entity.Verify;
/**
 * 实名认证Mapper 接口
 *
 * @author yaying.liu
 * @Date 2020-03-11 16:31:58
 */
public interface VerifyMapper extends BaseMapper<Verify> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}