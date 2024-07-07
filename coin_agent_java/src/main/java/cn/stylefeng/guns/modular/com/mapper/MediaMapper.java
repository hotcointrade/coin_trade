package cn.stylefeng.guns.modular.com.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import cn.stylefeng.guns.modular.com.entity.Media;
/**
 * 视频和音频Mapper 接口
 *
 * @author yaying.liu
 * @Date 2019-12-10 14:44:00
 */
public interface MediaMapper extends BaseMapper<Media> {

    Page<Map<String,Object>> selectByCondition(@Param("page") Page page, @Param("condition") String condition);

}